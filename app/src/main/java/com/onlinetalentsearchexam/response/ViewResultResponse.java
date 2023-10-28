package com.onlinetalentsearchexam.response;


import com.onlinetalentsearchexam.model.viewresult.ViewResult;

public class ViewResultResponse {
    public ViewResult posts;
    private Throwable error;

    public ViewResultResponse(ViewResult posts) {
        this.posts = posts;
        this.error = null;
    }

    public ViewResultResponse(Throwable error) {
        this.error = error;
        this.posts = null;
    }

    public ViewResult getPosts() {
        return posts;
    }

    public void setPosts(ViewResult posts) {
        this.posts = posts;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
