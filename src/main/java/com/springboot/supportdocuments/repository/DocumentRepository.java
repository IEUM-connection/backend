package com.springboot.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<com.springboot.image.entity.Document, Long> {
    //해당 포스트의 모든 그림
    List<com.springboot.image.entity.Document> findAllByPostId(long postid);
}
