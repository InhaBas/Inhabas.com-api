package com.inhabas.api.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inhabas.api.domain.comment.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {}
