package io.itit.domain;

import java.util.List;

/**
 * Created by Lee_3do on 2016/12/1.
 */

public class Item {


    private List<ItemsBean> items;

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * author : ccat
         * desc :  初学SQL的日子，感觉就像是刚学走路，步态可掬，跌跌撞撞。摔了不少可笑的跟头。拿出来大家娱乐一下，也互相提个醒，这样的错我们可以尽量避免的嘛。
         * 先看这个：例1 不合理的逗号：Select Field1, Field2, Field3, From
         * MyTable一执行就是个语法错误，什么意思嘛，这可是从书上抄的哎，你不能这么对我……呵呵呵，其实嘛，错误在于我在最后一个字段名后面加了一个逗号。逗号是分隔字段名或表名的嘛，字段名和Form之间加个逗号算什么
         * displayType : 0
         * fetchDate : 1450465400000
         * id : 149091
         * isIndexed : true
         * likeCount : 0
         * moreLikeTime : 1477899585000
         * providerId : 576
         * publishDate : 1450465400000
         * rank : 334
         * score : 0
         * title : [原]SQL Story摘录（六）————不可能的错误
         * type : 0
         * url : http://blog.csdn.net/ccat/article/details/8340
         * viewCount : 0
         */

        private String author;
        private String desc;
        private int displayType;
        private long fetchDate;
        private int id;
        private boolean isIndexed;
        private int likeCount;
        private long moreLikeTime;
        private int providerId;
        private long publishDate;
        private int rank;
        private int score;
        private String title;
        private int type;
        private String url;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        private String imgUrl;
        private int viewCount;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getDisplayType() {
            return displayType;
        }

        public void setDisplayType(int displayType) {
            this.displayType = displayType;
        }

        public long getFetchDate() {
            return fetchDate;
        }

        public void setFetchDate(long fetchDate) {
            this.fetchDate = fetchDate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIsIndexed() {
            return isIndexed;
        }

        public void setIsIndexed(boolean isIndexed) {
            this.isIndexed = isIndexed;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public long getMoreLikeTime() {
            return moreLikeTime;
        }

        public void setMoreLikeTime(long moreLikeTime) {
            this.moreLikeTime = moreLikeTime;
        }

        public int getProviderId() {
            return providerId;
        }

        public void setProviderId(int providerId) {
            this.providerId = providerId;
        }

        public long getPublishDate() {
            return publishDate;
        }

        public void setPublishDate(long publishDate) {
            this.publishDate = publishDate;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }
    }
}
