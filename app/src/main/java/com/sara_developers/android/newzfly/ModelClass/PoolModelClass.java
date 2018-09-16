package com.sara_developers.android.newzfly.ModelClass;

public class PoolModelClass {

    String PoolDocId;
    String PoolHeadLine;
    String PoolImageCover;
    String PoolNewsContent;
    long Total_positiveVote;
    long Total_negativeVote;
    String PoolPostingDate;
    String PoolQuestion;

    public PoolModelClass(String poolDocId,String poolHeadLine, String poolImageCover,
                          String poolNewsContent,
                          long total_positiveVote, long total_negativeVote,
                          String poolPostingDate,String poolQuestion) {
        PoolDocId=poolDocId;
        PoolHeadLine = poolHeadLine;
        PoolImageCover = poolImageCover;
        PoolNewsContent = poolNewsContent;
        Total_positiveVote = total_positiveVote;
        Total_negativeVote = total_negativeVote;
        PoolPostingDate = poolPostingDate;
        PoolQuestion=poolQuestion;
    }


    public String getPoolDocId() {
        return PoolDocId;
    }

    public String getPoolQuestion() {
        return PoolQuestion;
    }

    public String getPoolHeadLine() {
        return PoolHeadLine;
    }

    public String getPoolImageCover() {
        return PoolImageCover;
    }

    public String getPoolNewsContent() {
        return PoolNewsContent;
    }

    public long getTotal_positiveVote() {
        return Total_positiveVote;
    }

    public long getTotal_negativeVote() {
        return Total_negativeVote;
    }

    public String getPoolPostingDate() {
        return PoolPostingDate;
    }
}
