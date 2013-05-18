package edu.monash.controlnet.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import edu.monash.controlnet.R;

/**
 * Created by Moses Wan on 18/05/13.
 */
public class NodeSite extends Activity {

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);


        extras = getIntent().getExtras();
        this.setTitle(extras.getString("IP Address"));

        WebView wvNode = (WebView) findViewById(R.id.wvNode);
        wvNode.loadUrl("http://" + extras.getString("IP Address"));
        WebSettings webSettings = wvNode.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvNode.setWebViewClient(new MyWebViewClient());

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("http://" + extras.getString("IP Address"))) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
