package com.example.artikelapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.artikelapp.R;
import com.example.artikelapp.api.RetrofitClient;
import com.example.artikelapp.models.Post;
import com.example.artikelapp.storage.SharedPrefManager;
import com.example.artikelapp.utility.IMM;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddArticleActivity extends AppCompatActivity implements View.OnClickListener {

    EditText AddTitle, AddContent, AddTags;
    MaterialButton SaveButton;
    String title, content, tag, created;
    private String Token = "Bearer " + SharedPrefManager.getInstance(this).getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        AddTitle = findViewById(R.id.AddTitle);
        AddContent = findViewById(R.id.AddContent);
        AddTags = findViewById(R.id.AddTags);
        SaveButton = findViewById(R.id.AddCreateButton);
        SaveButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddCreateButton:
                IMM.hideKeyboard(this);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");

                title = AddTitle.getText().toString().trim();
                content = AddContent.getText().toString().trim();
                tag = AddTags.getText().toString().trim();
                created = simpleDateFormat.format(calendar.getTime());

                boolean isEmpty = false;

                if (TextUtils.isEmpty(title)) {
                    isEmpty = true;
                    AddTitle.setError("Fill the title");
                }

                if (TextUtils.isEmpty(content)) {
                    isEmpty = true;
                    AddContent.setError("Fill the content");
                }

                if (TextUtils.isEmpty(tag)) {
                    isEmpty = true;
                    AddTags.setError("Fill the tags");
                }

                if (!isEmpty) {
                    Call<Post> call = RetrofitClient.getInstance().getApi().setPost(Token, title, content, tag, created);

                    call.enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {
                            toArticleActivity();
                            Toast.makeText(AddArticleActivity.this, "Article Created", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {
                            toArticleActivity();
                            Toast.makeText(AddArticleActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                break;

        }
    }

    public void toArticleActivity() {

        Intent toArticleActivity = new Intent(this, ArticleActivity.class);
        startActivity(toArticleActivity);

    }

}
