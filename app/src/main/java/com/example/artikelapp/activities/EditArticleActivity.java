package com.example.artikelapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.artikelapp.R;
import com.example.artikelapp.api.RetrofitClient;
import com.example.artikelapp.models.Post;
import com.example.artikelapp.storage.SharedPrefManager;
import com.example.artikelapp.utility.IMM;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditArticleActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTitle, editContent, editTags;
    private Button saveButton;

    private String Token = "Bearer " + SharedPrefManager.getInstance(this).getToken();

    private  String titleOld, contentOld, tagsOld;
    private int id;

    private String titleNew, contentNew, tagsNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        Initiation();
        getExtras();
        getDetailEdit();
        saveButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.EditSaveButton:

                IMM.hideKeyboard(this);
                saveEdit();

                break;
        }
    }

    public void Initiation() {

        editTitle = findViewById(R.id.EditTitle);
        editContent = findViewById(R.id.EditContent);
        editTags = findViewById(R.id.EditTags);
        saveButton = findViewById(R.id.EditSaveButton);

    }

    public void getExtras() {

        Intent receiveIntent = getIntent();
        id = receiveIntent.getIntExtra("id", 0);

    }

    public void saveEdit() {

        titleNew = editTitle.getText().toString().trim();
        contentNew = editContent.getText().toString().trim();
        tagsNew = editTags.getText().toString().trim();

        Call<Post> call = RetrofitClient.getInstance().getApi().editPost(Token, id, titleNew, contentNew, contentNew, tagsNew);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                response.message();
                toArticleActivity();
                Toast.makeText(EditArticleActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                toArticleActivity();
                Toast.makeText(EditArticleActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void toArticleActivity() {

        Intent toArticleActivity = new Intent(this, ArticleActivity.class);
        startActivity(toArticleActivity);

    }

    public  void getDetailEdit() {

        Call<Post> call = RetrofitClient.getInstance().getApi().getDetailPost(Token, id);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (response.errorBody() == null) {

                    Post post = response.body();

                    titleOld = post.getTitle();
                    contentOld = post.getContent();
                    tagsOld = post.getTag();

                    editTitle.setText(titleOld);
                    editContent.setText(contentOld);
                    editTags.setText(tagsOld);

                }

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(EditArticleActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
