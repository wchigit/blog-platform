package com.example.blog.service;

import com.example.blog.model.Comment;
import com.example.blog.model.Post;
import com.example.blog.model.Tag;
import com.example.blog.model.User;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.TagRepository;
import com.example.blog.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       TagRepository tagRepository,
                       CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public List<Post> getPublishedPosts() {
        return postRepository.findPublishedPosts();
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPost(Long id) {
        return postRepository.findDetailedById(id);
    }

    @Transactional(readOnly = true)
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Comment> getComments(Long postId) {
        if (postId == null) {
            return commentRepository.findAllByOrderByCreatedAtDesc();
        }
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }

    @Transactional
    public Post createPost(String title,
                           String summary,
                           String content,
                           Long authorId,
                           List<Long> tagIds,
                           boolean published) {
        User author = userRepository.findById(authorId)
            .orElseThrow(() -> new IllegalArgumentException("Author not found: " + authorId));

        Post post = new Post();
        post.setTitle(title);
        post.setSummary(summary);
        post.setContent(content);
        post.setAuthor(author);
        post.setPublished(published);

        if (tagIds != null && !tagIds.isEmpty()) {
            post.setTags(new LinkedHashSet<>(tagRepository.findAllById(tagIds)));
        }

        return postRepository.save(post);
    }

    @Transactional
    public Comment addComment(Long postId, Long authorId, String content) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found: " + postId));
        User author = userRepository.findById(authorId)
            .orElseThrow(() -> new IllegalArgumentException("Author not found: " + authorId));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(content);
        return commentRepository.save(comment);
    }
}
