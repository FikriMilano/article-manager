package com.example.artikelapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.artikelapp.R;

import com.example.artikelapp.adapter.PostsAdapter;
import com.example.artikelapp.api.RetrofitClient;
import com.example.artikelapp.models.Post;
import com.example.artikelapp.storage.SharedPrefManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleActivity extends AppCompatActivity implements View.OnClickListener {

    private String Token = "Bearer " + SharedPrefManager.getInstance(this).getToken();
    private RecyclerView recyclerView;
    private ArrayList<Post> postArrayList;
    private PostsAdapter postsAdapter;
    private FloatingActionButton floatingActionButton;
    private MaterialToolbar materialToolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialCardView materialCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        materialCardView = findViewById(R.id.card_view_post);

        floatingActionButton = findViewById(R.id.fab_add);
        floatingActionButton.setOnClickListener(this);

        materialToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(materialToolbar);

        swipeRefreshLayout = findViewById(R.id.refresh);

        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getPosts();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getPosts();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Token == null) {
            toLoginActivity();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add:

                Intent toAddArticleActivity = new Intent(this, AddArticleActivity.class);
                startActivity(toAddArticleActivity);

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPrefManager.getInstance(ArticleActivity.this).clearToken();
                toLoginActivity();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void toLoginActivity(){
        Intent intent = new Intent(ArticleActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void showRecyclerList() {
        postsAdapter = new PostsAdapter(this, postArrayList);
        recyclerView.setAdapter(postsAdapter);
    }

    public void getPosts() {

        // getPosts
        Call<List<Post>> callPosts = RetrofitClient.getInstance().getApi().getPosts(Token);
        callPosts.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (response.errorBody() != null) {
                    Toast.makeText(ArticleActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }

                if (response.errorBody() == null) {
                    Toast.makeText(ArticleActivity.this, "No Error Detected", Toast.LENGTH_SHORT).show();

                }
                if (response.body() != null) {
                    postArrayList = new ArrayList<>(response.body());
                    Collections.reverse(postArrayList);
                    showRecyclerList();
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(ArticleActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
