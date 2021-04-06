package com.example.digitalbusinesscard;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersViewHolder extends RecyclerView.ViewHolder {

    CircleImageView profile_Image;
    TextView  username,UserId;
    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);

        profile_Image=itemView.findViewById(R.id.profile_image);
        username=itemView.findViewById(R.id.RecUsername);
        UserId=itemView.findViewById(R.id.UidUser);
    }
}
