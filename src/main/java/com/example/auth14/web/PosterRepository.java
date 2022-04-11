package com.example.auth14.web;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PosterRepository extends JpaRepository<Poster, Integer> {

    List<Poster> findAllByChatUserUsername(String username);

    List<Poster> findAllByChatUserId(Long id);

    List<Poster> findAllByIsPublic(boolean cond);
}
