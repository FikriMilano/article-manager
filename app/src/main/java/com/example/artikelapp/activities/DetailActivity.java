package com.example.artikelapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artikelapp.R;
import com.example.artikelapp.api.RetrofitClient;
import com.example.artikelapp.models.Post;
import com.example.artikelapp.storage.SharedPrefManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvCreatedDate, tvAuthor, tvContent, tvTags;
    private String title, createdDate, author, content, tags;
    private String Token = "Bearer " + SharedPrefManager.getInstance(this).getToken();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvCreatedDate = findViewById(R.id.tvCreatedDate);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvContent = findViewById(R.id.tvContent);
        tvTags = findViewById(R.id.tvTags);


        Intent receiveExtra = getIntent();
        id = receiveExtra.getIntExtra("id", 0);

        getDetailPosts();

    }

    public void getDetailPosts() {

        Call<Post> call = RetrofitClient.getInstance().getApi().getDetailPost(Token, id);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (response.errorBody() == null) {

                    Post post = response.body();

                    title = post.getTitle();
                    content = post.getContent();
                    author = post.getAuthor();
                    tags = post.getTag();
                    createdDate = post.getCreated();

                    tvTitle.setText(title);
                    tvContent.setText(Html.fromHtml(content));
                    tvAuthor.setText(author);
                    tvTags.setText(tags);
                    tvCreatedDate.setText(createdDate);

                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
