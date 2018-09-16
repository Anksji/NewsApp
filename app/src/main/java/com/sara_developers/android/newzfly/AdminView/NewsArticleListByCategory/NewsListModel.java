package com.sara_developers.android.newzfly.AdminView.NewsArticleListByCategory;

public class NewsListModel {

    String CoverImageLink;
    String NewsHeadLine;
    String NewsPostingDate;
    String MoreOptions;

    public NewsListModel(String coverImageLink, String newsHeadLine, String newsPostingDate, String moreOptions) {
        CoverImageLink = coverImageLink;
        NewsHeadLine = newsHeadLine;
        NewsPostingDate = newsPostingDate;
        MoreOptions = moreOptions;
    }

    public String getCoverImageLink() {
        return CoverImageLink;
    }

    public String getNewsHeadLine() {
        return NewsHeadLine;
    }

    public String getNewsPostingDate() {
        return NewsPostingDate;
    }

    public String getMoreOptions() {
        return MoreOptions;
    }
}


