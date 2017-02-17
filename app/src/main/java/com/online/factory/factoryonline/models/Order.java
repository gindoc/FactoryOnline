package com.online.factory.factoryonline.models;

/**
 * 作者: GIndoc
 * 日期: 2017/2/17 10:23
 * 作用:
 */

public class Order {
    private long publishTime;
    private int matchRange;
    private String description;

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public int getMatchRange() {
        return matchRange;
    }

    public void setMatchRange(int matchRange) {
        this.matchRange = matchRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
