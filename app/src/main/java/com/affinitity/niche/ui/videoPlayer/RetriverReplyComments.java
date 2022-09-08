package com.affinitity.niche.ui.videoPlayer;

public class RetriverReplyComments  {
    public String user_img, comment,user,id;

        public RetriverReplyComments() {}

        public RetriverReplyComments(String user_img, String comment,String user) {
            this.user_img = user_img;
            this.comment = comment;
            this.user = user;
        }

        public String getUser_img() {
            return user_img;
        }

        public String getComment() {
            return comment;
        }


        public String getUser() {
            return user;
        }

        public String getId() {
            return id;
        }

}