package com.arianinstitute.response;


import com.arianinstitute.model.start_test.StartTest;

public class StartTestResponse {
    public StartTest posts;
    private Throwable error;

    public StartTestResponse(StartTest posts) {
        this.posts = posts;
        this.error = null;
    }

    public StartTestResponse(Throwable error) {
        this.error = error;
        this.posts = null;
    }

    public StartTest getPosts() {
        return posts;
    }

    public void setPosts(StartTest posts) {
        this.posts = posts;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
