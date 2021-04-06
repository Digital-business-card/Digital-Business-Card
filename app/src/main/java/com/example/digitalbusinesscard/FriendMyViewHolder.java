package com.example.digitalbusinesscard;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendMyViewHolder extends RecyclerView.ViewHolder {

    TextView username;
    CircleImageView profile_image;
    TextView UserId;

    public FriendMyViewHolder(@NonNull View itemView) {
        super(itemView);
        username=itemView.findViewById(R.id.Fusername);
        profile_image=itemView.findViewById(R.id.profile_image);
        UserId=itemView.findViewById(R.id.UserIDMain);
    }
}
