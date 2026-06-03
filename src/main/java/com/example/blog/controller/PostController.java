package com.example.blog.controller;

import com.example.blog.model.Comment;
import com.example.blog.model.Post;
import com.example.blog.model.Tag;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        Post post = postService.getPost(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        model.addAttribute("post", post);
        model.addAttribute("users", userService.getAllUsers());
        return "post-detail";
    }

    @GetMapping("/posts/new")
    public String createPostForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("tags", postService.getAllTags());
        return "create-post";
    }

    @PostMapping("/posts")
    public String createPost(@RequestParam String title,
                             @RequestParam(required = false) String summary,
                             @RequestParam String content,
                             @RequestParam Long authorId,
                             @RequestParam(required = false) List<Long> tagIds,
                             @RequestParam(defaultValue = "false") boolean published,
                             RedirectAttributes redirectAttributes) {
        Post post = postService.createPost(title, summary, content, authorId, tagIds, published);
        redirectAttributes.addFlashAttribute("message", "Post created successfully.");
        return "redirect:/posts/" + post.getId();
    }

    @PostMapping("/posts/{id}/comments")
    public String addComment(@PathVariable Long id,
                             @RequestParam Long authorId,
                             @RequestParam String content,
                             RedirectAttributes redirectAttributes) {
        postService.addComment(id, authorId, content);
        redirectAttributes.addFlashAttribute("message", "Comment added successfully.");
        return "redirect:/posts/" + id;
    }

    @GetMapping("/api/posts")
    @ResponseBody
    public List<PostSummaryResponse> getPostsApi() {
        return postService.getPublishedPosts().stream()
            .map(this::toPostSummaryResponse)
            .toList();
    }

    @PostMapping(value = "/api/posts", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PostDetailResponse> createPostApi(@RequestBody CreatePostApiRequest request) {
        Post post = postService.createPost(
            request.title(),
            request.summary(),
            request.content(),
            request.authorId(),
            request.tagIds(),
            request.published()
        );
        Post persisted = postService.getPost(post.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return ResponseEntity.status(HttpStatus.CREATED).body(toPostDetailResponse(persisted));
    }

    @GetMapping("/api/comments")
    @ResponseBody
    public List<CommentResponse> getCommentsApi(@RequestParam(required = false) Long postId) {
        return postService.getComments(postId).stream()
            .map(this::toCommentResponse)
            .toList();
    }

    @PostMapping(value = "/api/comments", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CommentResponse> addCommentApi(@RequestBody CreateCommentApiRequest request) {
        Comment comment = postService.addComment(request.postId(), request.authorId(), request.content());
        return ResponseEntity.status(HttpStatus.CREATED).body(toCommentResponse(comment));
    }

    private PostSummaryResponse toPostSummaryResponse(Post post) {
        return new PostSummaryResponse(
            post.getId(),
            post.getTitle(),
            post.getSummary(),
            post.getAuthor() != null ? post.getAuthor().getId() : null,
            post.getAuthor() != null ? defaultString(post.getAuthor().getDisplayName(), post.getAuthor().getUsername()) : "Unknown",
            post.getCreatedAt(),
            post.getPublished(),
            post.getTags().stream().map(Tag::getName).toList()
        );
    }

    private PostDetailResponse toPostDetailResponse(Post post) {
        return new PostDetailResponse(
            post.getId(),
            post.getTitle(),
            post.getSummary(),
            post.getContent(),
            post.getAuthor() != null ? post.getAuthor().getId() : null,
            post.getAuthor() != null ? defaultString(post.getAuthor().getDisplayName(), post.getAuthor().getUsername()) : "Unknown",
            post.getCreatedAt(),
            post.getUpdatedAt(),
            post.getPublished(),
            post.getTags().stream().map(Tag::getName).toList(),
            post.getComments().stream().map(this::toCommentResponse).toList()
        );
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getPost() != null ? comment.getPost().getId() : null,
            comment.getAuthor() != null ? comment.getAuthor().getId() : null,
            comment.getAuthor() != null ? defaultString(comment.getAuthor().getDisplayName(), comment.getAuthor().getUsername()) : "Unknown",
            comment.getContent(),
            comment.getCreatedAt()
        );
    }

    private String defaultString(String primary, String fallback) {
        return primary != null && !primary.isBlank() ? primary : fallback;
    }

    public record CreatePostApiRequest(String title,
                                       String summary,
                                       String content,
                                       Long authorId,
                                       List<Long> tagIds,
                                       boolean published) {
    }

    public record CreateCommentApiRequest(Long postId, Long authorId, String content) {
    }

    public record PostSummaryResponse(Long id,
                                      String title,
                                      String summary,
                                      Long authorId,
                                      String authorName,
                                      LocalDateTime createdAt,
                                      boolean published,
                                      List<String> tags) {
    }

    public record PostDetailResponse(Long id,
                                     String title,
                                     String summary,
                                     String content,
                                     Long authorId,
                                     String authorName,
                                     LocalDateTime createdAt,
                                     LocalDateTime updatedAt,
                                     boolean published,
                                     List<String> tags,
                                     List<CommentResponse> comments) {
    }

    public record CommentResponse(Long id,
                                  Long postId,
                                  Long authorId,
                                  String authorName,
                                  String content,
                                  LocalDateTime createdAt) {
    }
}
