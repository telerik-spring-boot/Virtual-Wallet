package com.telerik.virtualwallet.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="url")
    private String url;

    @Column(name="published_on")
    private LocalDateTime publishedOn;

    public Article() {
    }

    public Article(int id, String title, String description, String url, LocalDateTime publishedOn) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.publishedOn = publishedOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(LocalDateTime publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
