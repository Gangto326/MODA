package com.moda.moda_api.summary.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moda.moda_api.summary.domain.model.Post;

public interface PostRepository extends JpaRepository<Post,String> {
}
