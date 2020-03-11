package com.example.artikelapp.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artikelapp.R;
import com.example.artikelapp.activities.DetailActivity;
import com.example.artikelapp.activities.EditArticleActivity;
import com.example.artikelapp.api.RetrofitClient;
import com.example.artikelapp.models.Post;
import com.example.artikelapp.storage.SharedPrefManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Post> listPosts;

    public PostsAdapter(Context context, ArrayList<Post> listPosts) {
        this.context = context;
        this.listPosts = listPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_post, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final String Token = "Bearer " + SharedPrefManager.getInstance(context).getToken();

        holder.tvTitle.setText(listPosts.get(position).getTitle());
        holder.tvDescription.setText(Html.fromHtml(listPosts.get(position).getExcerpt()));
        holder.tvTags.setText(listPosts.get(position).getTag());
        holder.tvCreated.setText(listPosts.get(position).getCreated());
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, holder.option);
                popupMenu.inflate(R.menu.menu_card);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.item_edit:

                                Intent toEditActivity = new Intent(context, EditArticleActivity.class);

                                toEditActivity.putExtra("id", listPosts.get(position).getId());

                                context.startActivity(toEditActivity);

                                break;

                            case R.id.item_delete:

                                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
                                materialAlertDialogBuilder.setMessage("Delete Article?");
                                materialAlertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Call<Post> call = RetrofitClient.getInstance().getApi().delPost(Token, listPosts.get(position).getId());
                                        call.enqueue(new Callback<Post>() {
                                            @Override
                                            public void onResponse(Call<Post> call, Response<Post> response) {
                                                listPosts.remove(position);
                                                notifyDataSetChanged();
                                                Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<Post> call, Throwable t) {
                                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });

                                materialAlertDialogBuilder.setNegativeButton("Cancel", null);
                                materialAlertDialogBuilder.show();

                                break;

                        }

                        return false;
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {

        return listPosts.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle, tvDescription, tvTags, tvCreated;
        MaterialCardView materialCardView;
        ImageButton option;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.item_title_post);
            tvDescription = itemView.findViewById(R.id.item_desc_post);
            tvTags = itemView.findViewById(R.id.item_tag_post);
            tvCreated = itemView.findViewById(R.id.item_created_post);
            materialCardView = itemView.findViewById(R.id.card_view_post);
            option = itemView.findViewById(R.id.option_post);

            materialCardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent toDetailActivity = new Intent(v.getContext(), DetailActivity.class);

            toDetailActivity.putExtra("id", listPosts.get(getAdapterPosition()).getId());

            v.getContext().startActivity(toDetailActivity);
        }
    }

}
