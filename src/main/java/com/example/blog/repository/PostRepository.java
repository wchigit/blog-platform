package com.example.blog.repository;

import com.example.blog.model.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"author", "tags"})
    @Query("select p from Post p where p.published = true order by p.createdAt desc")
    List<Post> findPublishedPosts();

    @EntityGraph(attributePaths = {"author", "tags", "comments", "comments.author"})
    @Query("select p from Post p where p.id = :id")
    Optional<Post> findDetailedById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"author", "tags"})
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);
}
