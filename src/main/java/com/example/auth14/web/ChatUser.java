package com.example.auth14.web;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

     String username;
     String password;

     @OneToMany(mappedBy = "chatUser")
        private List<Poster> posters;


    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "following_followers",

            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "followedby_id")
    )
    private Set<ChatUser> followers;


    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "following_followers",
            joinColumns = @JoinColumn(name = "followedby_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<ChatUser> following;


    public Set<ChatUser> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<ChatUser> followers) {
        this.followers = followers;
    }

    public Set<ChatUser> getFollowing() {
        return following;
    }

    public void setFollowing(Set<ChatUser> following) {
        this.following = following;
    }

    public ChatUser() {
    }

    public List<Poster> getPosters() {
        return posters;
    }

    public void setPosters(List<Poster> posters) {
        this.posters = posters;
    }

    public ChatUser(String username, String password) {
        this.username = username;
        this.password = password;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
