package com.abilitieshackathon2016.text2asl;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.graphics.Bitmap;

public class MainActivity extends AppCompatActivity {

    private boolean isRedirected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
    }

    private void handleSendText(Intent intent) {
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (!TextUtils.isEmpty(text)) {
            String url = "http://a88ba2de.ngrok.io/translate?words=";
            WebView webview = new WebView(this);
            setContentView(webview);
            String urlWithParams = url + "\'" + text + "\'";
            Log.d("OOPS", urlWithParams);
            WebSettings settings = webview.getSettings();
            settings.setDefaultTextEncodingName("utf-8");
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url){
                    // do your handling codes here, which url is the requested url
                    // probably you need to open that url rather than redirect:
                    //view.loadUrl(url);
                    return false; // then it is not handled by default action
                }
            });

            startWebView(webview, urlWithParams);
        }
    }

    private void showWebPage(String response) {
        if (!TextUtils.isEmpty(response)) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(response));
            startActivity(i);

        }
    }

    public static final MediaType JSON
        = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(String url, String json, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private void startWebView(WebView webView,String url) {

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                isRedirected = true;
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isRedirected = false;
            }

            public void onLoadResource(WebView view, String url) {
                if (!isRedirected) {
                    if (progressDialog == null) {
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    }
                }

            }

            public void onPageFinished(WebView view, String url) {
                try {
                    isRedirected = true;

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }


                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
