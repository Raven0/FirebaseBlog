package com.preangerstd.firebaseblog;

/**
 * Created by azhzh on 8/28/2017.
 */

public class BlogPost {

    private String title;
    private String content;
    private String image;
    private String username;

    public BlogPost(){

    }

    public BlogPost(String title, String content, String image, String username) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
