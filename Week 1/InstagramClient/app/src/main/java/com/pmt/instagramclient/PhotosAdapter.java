package com.pmt.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pmt.utils.CircleTransform;
import com.pmt.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by thuypm on 09/03/2016.
 */
public class PhotosAdapter extends ArrayAdapter<Photo> {

    private static class ViewHolder {
        TextView tvCaption;
        TextView tvUserName;
        TextView tvLikes;
        TextView tvCreateTimes;
        ImageView ivPhoto;
        ImageView ivAvatar;
    }


    public PhotosAdapter(Context context, List<Photo> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Photo photo = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
            viewHolder.tvCreateTimes = (TextView) convertView.findViewById(R.id.tvCreateTimes);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.ivAvatar = (ImageView) convertView.findViewById(R.id.ivUserAvatar);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvCaption.setText(photo.caption);
        viewHolder.tvUserName.setText(photo.userName);
        viewHolder.tvLikes.setText(Utils.getLikesString(photo.likesCount));
        viewHolder.tvCreateTimes.setText(Utils.getTimeString(photo.createTime));
        viewHolder.ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.loading).error(R.drawable.error).into(viewHolder.ivPhoto);
        viewHolder.ivAvatar.setImageResource(0);
        Picasso.with(getContext()).load(photo.avatar).placeholder(R.drawable.error).error(R.drawable.error).transform(new CircleTransform()).into(viewHolder.ivAvatar);
        return convertView;
    }
}
