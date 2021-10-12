package com.example.meme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    ImageView image;
    Button next,share;
    String link;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        update();
        image=findViewById(R.id.imageView2);
        next=findViewById(R.id.button2);
        share=findViewById(R.id.button);
        progressBar=findViewById(R.id.progressBar);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                update();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_SEND);

                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, link);
                startActivity(i);



            }
        });


    }
    public  void update(){
        try {
            makecall("https://meme-api.herokuapp.com/gimme");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void makecall(String url) throws IOException {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(res);
                     link= jsonObject.getString("url");

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.get().load(link).into(image, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onError(Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}