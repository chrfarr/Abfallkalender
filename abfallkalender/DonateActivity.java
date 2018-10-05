package de.farr_net.abfallkalender;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;

public class DonateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        boolean connectedToInternet = new Connectivity().isConnected(this);
        if (!connectedToInternet){
            Snackbar.make(findViewById(android.R.id.content), "Bitte stelle eine Internetverbindung her!", Snackbar.LENGTH_LONG).show();
        }else {
            setContentView(R.layout.activity_donate);
            WebView myWebView = (WebView) findViewById(R.id.webview_donate);
            myWebView.loadUrl("http://apps.farr-net.de/donate.html");
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }
}
