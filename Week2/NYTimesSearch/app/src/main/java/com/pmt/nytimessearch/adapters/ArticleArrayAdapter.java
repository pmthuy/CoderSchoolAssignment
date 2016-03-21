package com.pmt.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pmt.nytimessearch.R;
import com.pmt.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by thuypm on 20/03/2016.
 */
public class ArticleArrayAdapter extends  RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder>{

    private ArrayList<Article> articles;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }

    // Pass in the contact array into the constructor
    public ArticleArrayAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }
    private Context context;
    @Override
    public ArticleArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleArrayAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = articles.get(position);

        // Set item views based on the data model
        TextView textView = viewHolder.tvTitle;
        textView.setText(article.getHeadline());
        ImageView imageView = viewHolder.ivImage;
        imageView.setImageResource(0);

        String thumbNail = article.getThumbNail();
        if(!TextUtils.isEmpty(thumbNail)){
            Picasso.with(context).load(thumbNail).into(imageView);
        }
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return articles.size();
    }

}
