@echo off
echo ========================================
echo noTrackers App - Quick Test URLs
echo ========================================
echo.
echo Copy these URLs to test your app:
echo.
echo 1. YouTube URL (should remove si parameter):
echo https://youtu.be/cjeIz-n-hrE?si=ayWBOz-3ZKS3c_E7
echo.
echo 2. Spotify URL (should remove si and utm):
echo https://open.spotify.com/track/abc123?si=xyz789^&utm_source=twitter
echo.
echo 3. Facebook URL (should remove fbclid):
echo https://www.facebook.com/post/123?fbclid=123456^&utm_medium=social
echo.
echo 4. Complex URL (should remove multiple trackers):
echo https://example.com/page?utm_source=facebook^&utm_medium=social^&utm_campaign=summer^&fbclid=123456^&gclid=789012^&si=xyz789
echo.
echo 5. Legitimate URL (should NOT be modified):
echo https://example.com/search?q=privacy^&page=2^&lang=en^&id=123
echo.
echo ========================================
echo Testing Instructions:
echo 1. Copy each URL above
echo 2. Paste in your browser
echo 3. Share the URL
echo 4. Select "noTrackers" from share menu
echo 5. Check if tracking parameters are removed
echo ========================================
echo.
pause

