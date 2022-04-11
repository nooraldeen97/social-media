package com.example.auth14.web;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser,Long> {
public ChatUser findByUsername(String username);
}
