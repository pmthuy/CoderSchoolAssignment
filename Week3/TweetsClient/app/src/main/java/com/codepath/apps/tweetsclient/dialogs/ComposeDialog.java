package com.codepath.apps.tweetsclient.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tweetsclient.R;
import com.codepath.apps.tweetsclient.models.User;
import com.codepath.apps.tweetsclient.services.TweetsApplication;
import com.codepath.apps.tweetsclient.services.TweetsClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;


public class ComposeDialog extends DialogFragment {
    private TweetsClient client = TweetsApplication.getRestClient();
    private TextView tvName;
    private TextView tvUserName;
    private TextView tvTextCount;
    private EditText etBody;
    private ImageView ivProfileImage;
    private Button btnTweet;
    private ImageView ivBtnClose;
    private User user;
    private String retweetName;
    private long statusId = 1;

    public ComposeDialog() {
    }

    public static ComposeDialog newInstance(User user, String retweetName, long statusId)  {
        ComposeDialog frag = new ComposeDialog();
        frag.user = user;
        frag.retweetName = retweetName;
        frag.statusId = statusId;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container);
    }

    private void findViewById(final View view){
        tvName = (TextView) view.findViewById(R.id.tvName_compose);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName_compose);
        etBody = (EditText) view.findViewById(R.id.etBody_compose);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage_compose);
        ivProfileImage.setImageResource(0);
        btnTweet = (Button) view.findViewById(R.id.btnTweet_compose);
        tvTextCount = (TextView) view.findViewById(R.id.tvTextCount);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViewById(view);
        tvName.setText(user.getName());
        tvUserName.setText(user.getScreenName());
        Picasso.with(view.getContext()).load(user.getProfileImageUrl()).into(ivProfileImage);
        if(!retweetName.isEmpty()){
            etBody.setText("@"+retweetName);
        }
        etBody.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                tvTextCount.setText(String.valueOf(140 - etBody.getText().length()));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        btnTweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (etBody.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "Can not tweet empty Body", Toast.LENGTH_SHORT).show();
                } else {
                    if (!retweetName.isEmpty()) {
                        client.reTweet(etBody.getText().toString(), statusId,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                                Log.d("DEBUG", "tweetting onSuccess" + json.toString());
                                Toast.makeText(view.getContext(), "Tweetting Success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.d("DEBUG", "tweetting onFailure" + errorResponse.toString());
                                Toast.makeText(view.getContext(), "Tweetting Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        client.tweetting(etBody.getText().toString(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                                Log.d("DEBUG", "tweetting onSuccess" + json.toString());
                                Toast.makeText(view.getContext(), "Tweetting Success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.d("DEBUG", "tweetting onFailure" + errorResponse.toString());
                                Toast.makeText(view.getContext(), "Tweetting Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    dismiss();
                }

            }
        });
        ivBtnClose = (ImageView) view.findViewById(R.id.ivBtnClose);
        ivBtnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}