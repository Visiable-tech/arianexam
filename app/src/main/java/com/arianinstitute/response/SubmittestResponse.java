package com.arianinstitute.response;



import com.arianinstitute.model.SubmitTest;

public class SubmittestResponse {
    public SubmitTest posts;
    private Throwable error;

    public SubmittestResponse(SubmitTest posts) {
        this.posts = posts;
        this.error = null;
    }

    public SubmittestResponse(Throwable error) {
        this.error = error;
        this.posts = null;
    }

    public SubmitTest getPosts() {
        return posts;
    }

    public void setPosts(SubmitTest posts) {
        this.posts = posts;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
