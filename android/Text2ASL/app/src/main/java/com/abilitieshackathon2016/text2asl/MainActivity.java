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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

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

            webview.loadUrl(urlWithParams);
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



    private String postRequest(String text) {
        String response = "";
        try {
            URL url = new URL("http://899eca95.ngrok.io/?words=" + text);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(text);

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
