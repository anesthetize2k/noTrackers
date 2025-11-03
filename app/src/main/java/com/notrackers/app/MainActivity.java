package com.notrackers.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Html;
import androidx.core.text.HtmlCompat;

public class MainActivity extends Activity {
    private EditText urlEditText;
    private Button copyButton;
    private Button shareButton;
    private Button reportButton;
    private Button securePrivacyButton;
    private ImageButton refreshButton;
    private CheckBox spreadWordCheckBox;
    private TextView celebrationMessage;
    private TextView cleanUrlLabel;
    private TextView infoMessage;
    private String cleanedUrl;
    private String originalUrl;
    private boolean hasCleanedUrl = false;
    private boolean openedViaShare = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlEditText = findViewById(R.id.urlEditText);
        copyButton = findViewById(R.id.copyButton);
        shareButton = findViewById(R.id.shareButton);
        reportButton = findViewById(R.id.reportButton);
        securePrivacyButton = findViewById(R.id.securePrivacyButton);
        refreshButton = findViewById(R.id.refreshButton);
        spreadWordCheckBox = findViewById(R.id.spreadWordCheckBox);
        celebrationMessage = findViewById(R.id.celebrationMessage);
        cleanUrlLabel = findViewById(R.id.cleanUrlLabel);
        infoMessage = findViewById(R.id.infoMessage);

        String infoText = "Big Tech tracks what you share — and with whom — quietly building profiles that invade your privacy and that of those you share content with.<br/><br/>" +
                "<b>noTrackers</b> helps you stop that.<br/><br/>" +
                "When you share a link, choose <b>noTrackers</b> — we remove hidden tracking codes and give you a clean, private URL.<br/><br/>" +
                "Share content, not your digital footprint.";
        infoMessage.setText(HtmlCompat.fromHtml(infoText, HtmlCompat.FROM_HTML_MODE_LEGACY));

        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_SEND.equals(intent.getAction())) {
            openedViaShare = true;
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null && isUrl(sharedText)) {
                originalUrl = sharedText;
                cleanedUrl = removeTrackingParameters(sharedText);
                urlEditText.setText(cleanedUrl);
                hasCleanedUrl = true;
                updateUIState();
            }
        } else {
            openedViaShare = false;
            updateUIState();
        }

        securePrivacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUrl = urlEditText.getText().toString().trim();
                if (!inputUrl.isEmpty() && isUrl(inputUrl)) {
                    originalUrl = inputUrl;
                    cleanedUrl = removeTrackingParameters(inputUrl);
                    urlEditText.setText(cleanedUrl);
                    hasCleanedUrl = true;
                    updateUIState();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a valid URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlEditText.setText("");
                cleanedUrl = null;
                originalUrl = null;
                hasCleanedUrl = false;
                updateUIState();
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUrl();
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportDialog();
            }
        });
    }

    private void updateUIState() {
        if (hasCleanedUrl) {
            cleanUrlLabel.setVisibility(View.VISIBLE);
            celebrationMessage.setVisibility(View.VISIBLE);
            securePrivacyButton.setVisibility(View.GONE);
            refreshButton.setVisibility(View.VISIBLE);
            spreadWordCheckBox.setVisibility(View.VISIBLE);
            copyButton.setVisibility(View.VISIBLE);
            shareButton.setVisibility(View.VISIBLE);
            reportButton.setVisibility(View.VISIBLE);
            infoMessage.setVisibility(View.GONE);
            urlEditText.setHint(null);
        } else {
            cleanUrlLabel.setVisibility(View.GONE);
            celebrationMessage.setVisibility(View.GONE);
            securePrivacyButton.setVisibility(View.VISIBLE);
            refreshButton.setVisibility(View.GONE);
            spreadWordCheckBox.setVisibility(View.GONE);
            copyButton.setVisibility(View.GONE);
            shareButton.setVisibility(View.GONE);
            reportButton.setVisibility(View.GONE);
            urlEditText.setHint("Enter or paste URL here...");
            
            if (!openedViaShare) {
                infoMessage.setVisibility(View.VISIBLE);
            } else {
                infoMessage.setVisibility(View.GONE);
            }
        }
    }

    private boolean isUrl(String text) {
        try {
            new java.net.URL(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String removeTrackingParameters(String url) {
        return UrlCleaner.cleanUrl(url);
    }

    private void copyToClipboard() {
        String url = urlEditText.getText().toString().trim();
        if (!url.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Cleaned URL", url);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "URL copied to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No URL to copy", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareUrl() {
        String url = urlEditText.getText().toString().trim();
        if (!url.isEmpty()) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            
            String shareText = url;
            if (spreadWordCheckBox.isChecked()) {
                shareText = url + "\n\nI removed tracking urls by using the noTrackers app.";
            }
            
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share URL"));
        } else {
            Toast.makeText(this, "No URL to share", Toast.LENGTH_SHORT).show();
        }
    }

    private void showReportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Report Error");
        builder.setMessage("This will send the original URL and cleaned URL to the developer (skfrin@proton.me) to help improve the app. You can edit the message to hide any information you don't want to share.\n\nDo you want to continue?");
        builder.setPositiveButton("Send Report", (dialog, which) -> {
            sendReportEmail();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    private void sendReportEmail() {
        String originalUrl = this.originalUrl != null ? this.originalUrl : "No original URL";
        String cleanedUrl = urlEditText.getText().toString().trim();
        if (cleanedUrl.isEmpty()) {
            cleanedUrl = "No cleaned URL";
        }

        String subject = "noTrackers Feedback";
        String body = "Hello,\n\n" +
                "I'm reporting an issue with the noTrackers app:\n\n" +
                "Original URL:\n" + originalUrl + "\n\n" +
                "Cleaned URL:\n" + cleanedUrl + "\n\n" +
                "Issue description:\n" +
                "[Please describe what went wrong or what you expected to happen]\n\n" +
                "Thank you for your feedback!\n\n" +
                "---\n" +
                "Sent from noTrackers app";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"skfrin@proton.me"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send feedback via email"));
        } catch (Exception e) {
            Toast.makeText(this, "No email app found. Please install an email app.", Toast.LENGTH_LONG).show();
        }
    }
}
