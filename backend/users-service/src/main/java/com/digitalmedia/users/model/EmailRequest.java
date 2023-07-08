package com.digitalmedia.users.model;

public class EmailRequest {
    private String toEmail;
    private String subject;
    private String text;

    public EmailRequest(String toEmail, String subject, String text) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.text = text;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
