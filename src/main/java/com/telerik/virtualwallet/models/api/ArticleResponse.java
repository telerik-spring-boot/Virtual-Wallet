package com.telerik.virtualwallet.models.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleResponse {

    private String headline;

    private String summary;

    private String  url;

    private Long datetime;


    public ArticleResponse() {
    }

    public ArticleResponse(String headline, String url, String summary, Long  datetime) {
        this.headline = headline;
        this.url = url;
        this.summary = summary;
        this.datetime = datetime;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

}
