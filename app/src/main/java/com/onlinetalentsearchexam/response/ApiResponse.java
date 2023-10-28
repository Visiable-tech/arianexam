package com.onlinetalentsearchexam.response;


import com.onlinetalentsearchexam.model.Login;

public class ApiResponse {
    public Login posts;
    private Throwable error;

    public ApiResponse(Login posts) {
        this.posts = posts;
        this.error = null;
    }

    public ApiResponse(Throwable error) {
        this.error = error;
        this.posts = null;
    }

    public Login getPosts() {
        return posts;
    }

    public void setPosts(Login posts) {
        this.posts = posts;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
