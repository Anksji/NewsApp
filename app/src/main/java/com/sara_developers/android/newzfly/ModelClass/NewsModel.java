package com.sara_developers.android.newzfly.ModelClass;

public class NewsModel {

    String HeadLine;
    String PostingDate;
    String NewsContent;
    String NewsCoverImg;
    String NewsLink;
    String NewsId;

    public String getNewsId() {
        return NewsId;
    }

    public String getHeadLine() {
        return HeadLine;
    }

    public String getPostingDate() {
        return PostingDate;
    }

    public String getNewsContent() {
        return NewsContent;
    }

    public String getNewsCoverImg() {
        return NewsCoverImg;
    }

    public String getNewsLink() {
        return NewsLink;
    }

    public NewsModel(String headLine, String postingDate, String newsContent,
                     String newsCoverImg, String newsLink,String newsId) {

        HeadLine = headLine;
        PostingDate = postingDate;
        NewsContent = newsContent;
        NewsCoverImg = newsCoverImg;
        NewsLink = newsLink;
        NewsId=newsId;
    }
}
