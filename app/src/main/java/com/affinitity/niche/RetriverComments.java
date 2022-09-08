package com.affinitity.niche;

import com.affinitity.niche.ui.videoPlayer.RetriverReplyComments;

public class RetriverComments  {

    public String user_img, comment,like,comment_ids,user,id,replie_coments;

    public RetriverComments() {}

    public RetriverComments(String user_img, String comment, String like, String comment_ids, String user, String id, String replie_coments) {
        this.user_img = user_img;
        this.comment = comment;
        this.like = like;
        this.comment_ids = comment_ids;
        this.user = user;
        this.id = id;
        this.replie_coments = replie_coments;
    }

    public String getUser_img() {
        return user_img;
    }

    public String getComment() {
        return comment;
    }

    public String getLike() {
        return like;
    }

    public String getComment_ids() {
        return comment_ids;
    }

    public String getUser() {
        return user;
    }

    public String getId() {
        return id;
    }

    public String getReplie_coments() {
        return replie_coments;
    }
}
