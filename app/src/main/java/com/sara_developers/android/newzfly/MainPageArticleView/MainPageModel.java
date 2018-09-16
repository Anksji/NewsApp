package com.sara_developers.android.newzfly.MainPageArticleView;

public class MainPageModel {

    String ImageLink;
    String NewsHeadLine;
    String NewsContent;
    String NewsSourceLink;

    public MainPageModel(String imageLink, String newsHeadLine, String newsContent, String newsSourceLink) {
        ImageLink = imageLink;
        NewsHeadLine = newsHeadLine;
        NewsContent = newsContent;
        NewsSourceLink = newsSourceLink;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public String getNewsHeadLine() {
        return NewsHeadLine;
    }

    public String getNewsContent() {
        return NewsContent;
    }

    public String getNewsSourceLink() {
        return NewsSourceLink;
    }

    public MainPageModel() {
    }
}

