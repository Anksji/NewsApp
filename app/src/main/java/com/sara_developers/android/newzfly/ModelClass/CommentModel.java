package com.sara_developers.android.newzfly.ModelClass;

public class CommentModel{

    public String userName;
    public String userImg;
    public String userCmt;
    public String user_Id_commented;
    public String cmtDate;
    public String  cmtTimeStamp;

    public CommentModel(String userName, String userImg,
                        String userCmt, String user_Id_commented, String cmtDate, String cmtTimeStamp) {
        this.userName = userName;
        this.userImg = userImg;
        this.userCmt = userCmt;
        this.user_Id_commented = user_Id_commented;
        this.cmtDate = cmtDate;
        this.cmtTimeStamp = cmtTimeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public String getUserCmt() {
        return userCmt;
    }

    public String getUser_Id_commented() {
        return user_Id_commented;
    }

    public String getCmtDate() {
        return cmtDate;
    }

    public String getCmtTimeStamp() {
        return cmtTimeStamp;
    }
}
