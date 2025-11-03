package com.notrackers.app;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

public class UrlCleaner {
    
    private static final String[] GLOBAL_DROP_PREFIXES = {
        "utm_", "vero_", "oly_", "icn_", "pk_", "mtm_", "ga_", "_ga", 
        "fb_", "_fb", "mc_", "hs_", "_hs"
    };
    
    private static final Pattern WT_PREFIX = Pattern.compile("^wt\\.", Pattern.CASE_INSENSITIVE);
    
    private static final Set<String> GLOBAL_DROP_EXACT = new HashSet<>(Arrays.asList(
        "gclid", "gclsrc", "dclid", "wbraid", "gbraid", "msclkid", "yclid", "ttclid",
        "fbclid", "twclid", "igshid", "ig_rid", "mkt_tok", "_hsenc", "_hsmi",
        "referrer", "refsrc", "src", "source", "campaign", "campaignid", "cid", "cjid", "cjdata",
        "adid", "adgroup", "adset", "ad_name", "adgroupid", "adposition",
        "spm", "scid", "s_cid", "sc_cid", "icid", "mbid", "rb_clickid", "clickid", "clid", "c_id",
        "trk", "track", "tracking_id", "trackingid", "trgid",
        "affiliate", "affiliate_id", "aff_id", "aff_sub", "aff_sub2", "aff_sub3", "aff_sub4", "aff_sub5",
        "afftrack", "affname", "affkey",
        "partner", "partnerid", "partner_id", "utm_email", "utm_reader", "utm_brand", "utm_social",
        "vero_conv", "vero_id", "_ig", "gi", "epik", "pincode", "psc",
        "mc_eid", "mc_cid", "xtor", "xtsrc", "xtor=", "xt", "at", "itscg", "itsct", "ct",
        "oly_enc_id", "oly_anon_id", "s_kwcid"
    ));
    
    private static final Set<String> REDIRECT_PARAMS = new HashSet<>(Arrays.asList(
        "redirect", "redirect_uri", "redirect_url", "destination", "dest", "to", "r", "next", 
        "continue", "return", "returl"
    ));
    
    private static final Map<String, String[]> REDIRECTOR_RULES = new HashMap<>();
    static {
        REDIRECTOR_RULES.put("l.facebook.com", new String[]{"u", "url"});
        REDIRECTOR_RULES.put("lm.facebook.com", new String[]{"u", "url"});
        REDIRECTOR_RULES.put("facebook.com/l.php", new String[]{"u"});
        REDIRECTOR_RULES.put("out.reddit.com", new String[]{"url"});
        REDIRECTOR_RULES.put("news.ycombinator.com", new String[]{"u"});
        REDIRECTOR_RULES.put("lnkd.in", new String[]{"url", "dest"});
        REDIRECTOR_RULES.put("medium.com", new String[]{"url"});
        REDIRECTOR_RULES.put("duckduckgo.com", new String[]{"uddg"});
        REDIRECTOR_RULES.put("slack-redir.net", new String[]{"url"});
        REDIRECTOR_RULES.put("www.linkedin.com/safety/go", new String[]{"url", "dest"});
    }
    
    public static String cleanUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return url;
        }
        
        String cleaned = url.trim();
        int maxUnwrapDepth = 5;
        int unwrapCount = 0;
        
        while (unwrapCount < maxUnwrapDepth) {
            String unwrapped = unwrapRedirector(cleaned);
            if (unwrapped == null || unwrapped.equals(cleaned)) {
                break;
            }
            cleaned = unwrapped;
            unwrapCount++;
        }
        
        return cleanUrlInternal(cleaned);
    }
    
    private static String cleanUrlInternal(String url) {
        try {
            UriComponents components = parseUri(url);
            if (components == null) {
                return url;
            }
            
            components = normalizeUrl(components);
            components.queryParams = cleanQueryParams(components.queryParams, components.host);
            
            if (components.fragment != null && components.fragment.contains("=")) {
                List<Param> fragmentParams = parseQueryString(components.fragment);
                List<Param> cleanedFragment = cleanQueryParams(fragmentParams, components.host);
                if (cleanedFragment.isEmpty()) {
                    components.fragment = null;
                } else {
                    components.fragment = buildQueryString(cleanedFragment);
                }
            }
            
            return buildUri(components);
        } catch (Exception e) {
            return url;
        }
    }
    
    private static String unwrapRedirector(String url) {
        try {
            UriComponents components = parseUri(url);
            if (components == null) {
                return null;
            }
            
            String host = components.host != null ? components.host.toLowerCase() : "";
            String path = components.path != null ? components.path.toLowerCase() : "";
            
            if (host.contains("google.com") || host.contains("google.")) {
                if (path.contains("/url") || path.contains("/imgres")) {
                    for (Param param : components.queryParams) {
                        String keyLower = param.key.toLowerCase();
                        if (keyLower.equals("url") || keyLower.equals("q")) {
                            try {
                                String destination = URLDecoder.decode(param.value, "UTF-8");
                                if (destination.startsWith("http://") || destination.startsWith("https://")) {
                                    return destination;
                                }
                            } catch (Exception e) {
                                // ignore
                            }
                        }
                    }
                }
            }
            
            if (host.contains("mail.") || host.contains("newsletter.")) {
                for (Param param : components.queryParams) {
                    String keyLower = param.key.toLowerCase();
                    if (keyLower.equals("url") || keyLower.equals("u") || 
                        keyLower.equals("redirect") || keyLower.equals("destination")) {
                        try {
                            String destination = URLDecoder.decode(param.value, "UTF-8");
                            if (destination.startsWith("http://") || destination.startsWith("https://")) {
                                return destination;
                            }
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                }
            }
            
            for (Map.Entry<String, String[]> entry : REDIRECTOR_RULES.entrySet()) {
                String redirectorPattern = entry.getKey().toLowerCase();
                String[] extractParams = entry.getValue();
                
                boolean matches = false;
                
                if (redirectorPattern.equals("facebook.com/l.php")) {
                    if (host.contains("facebook.com") && path.contains("/l.php")) {
                        matches = true;
                    }
                } else if (redirectorPattern.equals("www.linkedin.com/safety/go")) {
                    if (host.contains("linkedin.com") && path.contains("/safety/go")) {
                        matches = true;
                    }
                } else if (redirectorPattern.equals("news.ycombinator.com")) {
                    if (host.contains("ycombinator.com") && path.contains("/link")) {
                        matches = true;
                    }
                } else if (redirectorPattern.equals("medium.com")) {
                    if (host.contains("medium.com") && path.contains("/r/")) {
                        matches = true;
                    }
                } else if (redirectorPattern.equals("duckduckgo.com")) {
                    if (host.contains("duckduckgo.com") && path.contains("/l/")) {
                        matches = true;
                    }
                } else if (host.contains(redirectorPattern) || path.contains(redirectorPattern)) {
                    matches = true;
                }
                
                if (matches) {
                    for (String paramName : extractParams) {
                        for (Param param : components.queryParams) {
                            if (param.key.toLowerCase().equals(paramName.toLowerCase())) {
                                try {
                                    String destination = URLDecoder.decode(param.value, "UTF-8");
                                    if (destination.startsWith("http://") || destination.startsWith("https://")) {
                                        return destination;
                                    }
                                } catch (Exception e) {
                                    // ignore
                                }
                            }
                        }
                    }
                }
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private static List<Param> cleanQueryParams(List<Param> params, String host) {
        List<Param> cleaned = new ArrayList<>();
        String hostLower = host != null ? host.toLowerCase() : "";
        
        for (Param param : params) {
            String keyLower = param.key.toLowerCase();
            boolean shouldDrop = false;
            
            if (GLOBAL_DROP_EXACT.contains(keyLower)) {
                shouldDrop = true;
            }
            
            if (!shouldDrop) {
                for (String prefix : GLOBAL_DROP_PREFIXES) {
                    if (keyLower.startsWith(prefix.toLowerCase())) {
                        shouldDrop = true;
                        break;
                    }
                }
            }
            
            if (!shouldDrop && WT_PREFIX.matcher(keyLower).find()) {
                shouldDrop = true;
            }
            
            if (!shouldDrop && keyLower.startsWith("s_") && hostLower.contains("adobe")) {
                if (param.value != null && param.value.length() > 10) {
                    shouldDrop = true;
                }
            }
            
            if (!shouldDrop) {
                shouldDrop = shouldDropParam(hostLower, keyLower, param.value);
            }
            
            if (!shouldDrop && REDIRECT_PARAMS.contains(keyLower)) {
                shouldDrop = true;
            }
            
            if (!shouldDrop) {
                cleaned.add(param);
            }
        }
        
        return cleaned;
    }
    
    private static boolean shouldDropParam(String hostLower, String keyLower, String value) {
        if (hostLower.contains("youtube.com") || hostLower.contains("youtu.be")) {
            Set<String> keep = new HashSet<>(Arrays.asList("v", "t", "time_continue", "list", "index"));
            if (keyLower.equals("pp") && value != null && value.contains("shareable_link")) {
                return false;
            }
            if (keyLower.equals("feature") && value != null && value.contains("shareable_link")) {
                return false;
            }
            if (keep.contains(keyLower)) {
                return false;
            }
            Set<String> drop = new HashSet<>(Arrays.asList("si", "feature", "app", "bp", "pp", 
                "has_verified", "embeds_referring_euri", "ppurl", "ab_channel", "sp"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("instagram.com") || hostLower.contains("instagr.am")) {
            Set<String> drop = new HashSet<>(Arrays.asList("igshid", "ig_rid"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || 
                   keyLower.equals("fbclid") || keyLower.equals("__a") || 
                   keyLower.equals("__coig_restricted_ia");
        }
        
        if (hostLower.contains("twitter.com") || hostLower.contains("x.com")) {
            Set<String> drop = new HashSet<>(Arrays.asList("s", "t", "cn", "ref_src", "ref_url", "twclid"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("facebook.com") || hostLower.contains("fb.watch") || 
            hostLower.contains("threads.net")) {
            Set<String> drop = new HashSet<>(Arrays.asList("fbclid", "mibextid", "refsrc", "ref", 
                "__tn__", "eid", "stype"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("paipv");
        }
        
        if (hostLower.contains("tiktok.com")) {
            Set<String> drop = new HashSet<>(Arrays.asList("tt_from", "source", "u_code", "share_app_id", 
                "share_link_id", "ttclid", "sender_device", "sec_uid", "referer_url"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_");
        }
        
        if (hostLower.contains("linkedin.com") || hostLower.contains("lnkd.in")) {
            Set<String> drop = new HashSet<>(Arrays.asList("trk", "lipi", "li_fat_id", "refId"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("reddit.com")) {
            Set<String> drop = new HashSet<>(Arrays.asList("rdt_cid", "rdt", "share_id", "context", 
                "ref_campaign", "ref_source"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("medium.com")) {
            Set<String> drop = new HashSet<>(Arrays.asList("source", "sk", "recommendations", "ref"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("substack.com")) {
            Set<String> drop = new HashSet<>(Arrays.asList("r", "share_type", "sd", "s"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("amazon.")) {
            Set<String> keep = new HashSet<>(Arrays.asList("p"));
            if (keep.contains(keyLower)) {
                return false;
            }
            Set<String> drop = new HashSet<>(Arrays.asList("tag", "ascsubtag", "linkCode", "creative", 
                "campaign", "camp", "creativeASIN", "th", "smid", "ref", "qid", "sr", "sprefix", "psc"));
            if (keyLower.startsWith("pf_rd_")) {
                return true;
            }
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("play.google.com")) {
            Set<String> drop = new HashSet<>(Arrays.asList("referrer", "pcampaignid"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("apps.apple.com") || hostLower.contains("music.apple.com") || 
            hostLower.contains("itunes.apple.com")) {
            Set<String> drop = new HashSet<>(Arrays.asList("ct", "itscg", "itsct", "at", "app", "ls", 
                "uo", "pt", "ign-mpt"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("spotify.com")) {
            Set<String> drop = new HashSet<>(Arrays.asList("si", "nd", "context"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("music.youtube.com")) {
            Set<String> keep = new HashSet<>(Arrays.asList("v", "t", "time_continue", "list", "index"));
            if (keep.contains(keyLower)) {
                return false;
            }
            Set<String> drop = new HashSet<>(Arrays.asList("si", "feature", "app", "bp", "pp"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("github.com") || hostLower.contains("gitlab.com")) {
            Set<String> keep = new HashSet<>(Arrays.asList("ref", "at", "tab", "plain"));
            if (keep.contains(keyLower)) {
                return false;
            }
            return keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("stackoverflow.com") || hostLower.contains("stackexchange.com") || 
            hostLower.contains("superuser.com") || hostLower.contains("serverfault.com")) {
            Set<String> drop = new HashSet<>(Arrays.asList("s"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("pinterest.")) {
            Set<String> drop = new HashSet<>(Arrays.asList("epik", "p_tap", "mt", "cid"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("alibaba.com") || hostLower.contains("aliexpress.com")) {
            Set<String> keep = new HashSet<>(Arrays.asList("item", "sku_id"));
            if (keep.contains(keyLower)) {
                return false;
            }
            Set<String> drop = new HashSet<>(Arrays.asList("spm", "aff_platform", "sk", "scm", 
                "algo_expid", "algo_pvid", "ws_ab_test", "btsid", "utparam"));
            if (keyLower.startsWith("aff_")) {
                return true;
            }
            return drop.contains(keyLower) || keyLower.startsWith("utm_");
        }
        
        if (hostLower.contains("vimeo.com")) {
            Set<String> drop = new HashSet<>(Arrays.asList("share", "ref", "referrer"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("producthunt.com")) {
            Set<String> drop = new HashSet<>(Arrays.asList("ref"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("twitch.tv")) {
            Set<String> drop = new HashSet<>(Arrays.asList("tt_medium", "tt_content"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        if (hostLower.contains("nytimes.com") || hostLower.contains("washingtonpost.com") || 
            hostLower.contains("theguardian.com") || hostLower.contains("wsj.com") ||
            hostLower.contains("reuters.com") || hostLower.contains("cnn.com") ||
            hostLower.contains("bbc.com") || hostLower.contains("bbc.co.uk")) {
            Set<String> drop = new HashSet<>(Arrays.asList("smid", "partner", "cmp", "CMP", "spm", 
                "icid", "mbid", "ref", "sharetype", "outputType"));
            return drop.contains(keyLower) || keyLower.startsWith("utm_") || keyLower.equals("fbclid");
        }
        
        return false;
    }
    
    private static UriComponents normalizeUrl(UriComponents components) {
        String host = components.host != null ? components.host.toLowerCase() : "";
        String path = components.path != null ? components.path : "";
        
        if ((host.contains("youtube.com") || host.contains("youtu.be")) && path.contains("/shorts/")) {
            String[] parts = path.split("/shorts/");
            if (parts.length > 1) {
                String videoId = parts[1].split("\\?")[0].split("#")[0];
                components.path = "/watch";
                String timestamp = null;
                if (components.fragment != null && components.fragment.startsWith("t=")) {
                    timestamp = components.fragment.substring(2);
                }
                boolean hasV = false;
                for (Param p : components.queryParams) {
                    if (p.key.equalsIgnoreCase("v")) {
                        hasV = true;
                        break;
                    }
                }
                if (!hasV) {
                    components.queryParams.add(0, new Param("v", videoId));
                }
                if (timestamp != null) {
                    boolean hasT = false;
                    for (Param p : components.queryParams) {
                        if (p.key.equalsIgnoreCase("t")) {
                            hasT = true;
                            break;
                        }
                    }
                    if (!hasT) {
                        components.queryParams.add(new Param("t", timestamp));
                    }
                    components.fragment = null;
                }
            }
        }
        
        if ((host.contains("youtube.com") || host.contains("youtu.be")) && 
            components.fragment != null && components.fragment.startsWith("t=")) {
            String timestamp = components.fragment.substring(2);
            boolean hasT = false;
            for (Param p : components.queryParams) {
                if (p.key.equalsIgnoreCase("t")) {
                    hasT = true;
                    p.value = timestamp;
                    break;
                }
            }
            if (!hasT) {
                components.queryParams.add(new Param("t", timestamp));
            }
            components.fragment = null;
        }
        
        if (host.contains("amazon.") && path != null && path.contains("/gp/product/")) {
            String[] parts = path.split("/gp/product/");
            if (parts.length > 1) {
                String asin = parts[1].split("/")[0].split("\\?")[0];
                if (!path.contains("/dp/")) {
                    components.path = "/dp/" + asin;
                }
            }
        }
        
        if (host.contains("youtu.be") && path != null && path.startsWith("/")) {
            String videoId = path.substring(1).split("\\?")[0].split("#")[0];
            components.host = "www.youtube.com";
            components.path = "/watch";
            boolean hasV = false;
            for (Param p : components.queryParams) {
                if (p.key.equalsIgnoreCase("v")) {
                    hasV = true;
                    break;
                }
            }
            if (!hasV) {
                components.queryParams.add(0, new Param("v", videoId));
            }
        }
        
        if (host.contains("instagram.com") || host.contains("instagr.am")) {
            if (path != null && path.endsWith("/")) {
                components.path = path.substring(0, path.length() - 1);
            }
        }
        
        if (path != null && path.contains("/amp")) {
            String canonicalPath = path.replace("/amp", "").replace("amp/", "");
            if (path.endsWith("/amp") || path.contains("/amp/")) {
                components.path = canonicalPath;
            }
        }
        if (components.queryParams != null) {
            List<Param> paramsToRemove = new ArrayList<>();
            for (Param p : components.queryParams) {
                if (p.key.equalsIgnoreCase("outputType") && p.value != null && 
                    p.value.equalsIgnoreCase("amp")) {
                    paramsToRemove.add(p);
                }
            }
            components.queryParams.removeAll(paramsToRemove);
        }
        
        return components;
    }
    
    private static UriComponents parseUri(String url) {
        try {
            UriComponents components = new UriComponents();
            
            int schemeEnd = url.indexOf("://");
            if (schemeEnd == -1) {
                return null;
            }
            
            components.scheme = url.substring(0, schemeEnd);
            String remaining = url.substring(schemeEnd + 3);
            
            int pathStart = remaining.indexOf("/");
            int queryStart = remaining.indexOf("?");
            int fragmentStart = remaining.indexOf("#");
            
            String authority;
            if (pathStart != -1) {
                authority = remaining.substring(0, pathStart);
            } else if (queryStart != -1) {
                authority = remaining.substring(0, queryStart);
            } else if (fragmentStart != -1) {
                authority = remaining.substring(0, fragmentStart);
            } else {
                authority = remaining;
            }
            
            components.host = authority.split(":")[0];
            if (authority.contains(":")) {
                components.port = authority.substring(authority.indexOf(":") + 1);
            }
            
            int actualPathStart = (pathStart != -1) ? pathStart : 
                                 (queryStart != -1) ? queryStart : 
                                 (fragmentStart != -1) ? fragmentStart : remaining.length();
            
            if (pathStart != -1) {
                int pathEnd = (queryStart != -1) ? queryStart : 
                             (fragmentStart != -1) ? fragmentStart : remaining.length();
                components.path = remaining.substring(pathStart, pathEnd);
            } else {
                components.path = "/";
            }
            
            if (queryStart != -1) {
                int queryEnd = (fragmentStart != -1) ? fragmentStart : remaining.length();
                String queryStr = remaining.substring(queryStart + 1, queryEnd);
                components.queryParams = parseQueryString(queryStr);
            } else {
                components.queryParams = new ArrayList<>();
            }
            
            if (fragmentStart != -1) {
                components.fragment = remaining.substring(fragmentStart + 1);
            }
            
            return components;
        } catch (Exception e) {
            return null;
        }
    }
    
    private static List<Param> parseQueryString(String query) {
        List<Param> params = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            return params;
        }
        
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            if (pair.isEmpty()) continue;
            String[] keyValue = pair.split("=", 2);
            try {
                String key = keyValue.length > 0 ? URLDecoder.decode(keyValue[0], "UTF-8") : "";
                String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], "UTF-8") : "";
                params.add(new Param(key, value));
            } catch (Exception e) {
                // ignore
            }
        }
        return params;
    }
    
    private static String buildQueryString(List<Param> params) {
        if (params.isEmpty()) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Param param : params) {
            if (!first) {
                sb.append("&");
            }
            try {
                sb.append(URLEncoder.encode(param.key, "UTF-8"))
                  .append("=")
                  .append(URLEncoder.encode(param.value != null ? param.value : "", "UTF-8"));
            } catch (Exception e) {
                sb.append(param.key).append("=").append(param.value != null ? param.value : "");
            }
            first = false;
        }
        return sb.toString();
    }
    
    private static String buildUri(UriComponents components) {
        StringBuilder sb = new StringBuilder();
        
        if (components.scheme != null) {
            sb.append(components.scheme).append("://");
        }
        
        if (components.host != null) {
            sb.append(components.host);
        }
        
        if (components.port != null && !components.port.isEmpty()) {
            sb.append(":").append(components.port);
        }
        
        if (components.path != null) {
            sb.append(components.path);
        }
        
        String queryStr = buildQueryString(components.queryParams);
        if (queryStr != null && !queryStr.isEmpty()) {
            sb.append("?").append(queryStr);
        }
        
        if (components.fragment != null && !components.fragment.isEmpty()) {
            sb.append("#").append(components.fragment);
        }
        
        return sb.toString();
    }
    
    private static class UriComponents {
        String scheme;
        String host;
        String port;
        String path;
        List<Param> queryParams;
        String fragment;
        
        UriComponents() {
            queryParams = new ArrayList<>();
        }
    }
    
    private static class Param {
        String key;
        String value;
        
        Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
