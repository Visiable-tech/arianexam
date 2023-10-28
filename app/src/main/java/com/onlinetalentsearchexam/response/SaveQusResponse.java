package com.onlinetalentsearchexam.response;


import com.onlinetalentsearchexam.model.savequestion.SaveQus;

public class SaveQusResponse {
    public SaveQus posts;
    private Throwable error;

    public SaveQusResponse(SaveQus posts) {
        this.posts = posts;
        this.error = null;
    }

    public SaveQusResponse(Throwable error) {
        this.error = error;
        this.posts = null;
    }

    public SaveQus getPosts() {
        return posts;
    }

    public void setPosts(SaveQus posts) {
        this.posts = posts;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
