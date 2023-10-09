package com.arianinstitute.response;


import com.arianinstitute.model.Login;
import com.arianinstitute.model.examtest.Examtest;

public class ExamResponse {
    public Examtest posts;
    private Throwable error;

    public ExamResponse(Examtest posts) {
        this.posts = posts;
        this.error = null;
    }

    public ExamResponse(Throwable error) {
        this.error = error;
        this.posts = null;
    }

    public Examtest getPosts() {
        return posts;
    }

    public void setPosts(Examtest posts) {
        this.posts = posts;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
