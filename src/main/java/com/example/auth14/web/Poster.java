package com.example.auth14.web;

import javax.persistence.*;

@Entity
public class Poster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
    private String content;
    private boolean isPublic;
    @ManyToOne
    @JoinColumn(name="id")
    ChatUser chatUser;

    public Poster() {
    }

    public Poster(String content, boolean isPublic, ChatUser chatUser) {
        this.content = content;
        this.isPublic = isPublic;
        this.chatUser = chatUser;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public ChatUser getChatUser() {
        return chatUser;
    }

    public void setChatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
    }
}
