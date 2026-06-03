INSERT INTO users (id, username, email, password_hash, display_name, bio, created_at) VALUES
    (1, 'alice', 'alice@example.com', '$2a$10$alicehashedpasswordexample', 'Alice Johnson', 'Spring developer and platform maintainer.', CURRENT_TIMESTAMP - INTERVAL '10 day'),
    (2, 'bob', 'bob@example.com', '$2a$10$bobhashedpasswordexample', 'Bob Smith', 'Writes about developer experience, testing, and clean code.', CURRENT_TIMESTAMP - INTERVAL '8 day'),
    (3, 'charlie', 'charlie@example.com', '$2a$10$charliehashedpasswordexample', 'Charlie Brown', 'Full-stack engineer exploring modern Java web apps.', CURRENT_TIMESTAMP - INTERVAL '6 day');

INSERT INTO tags (id, name) VALUES
    (1, 'Spring Boot'),
    (2, 'Thymeleaf'),
    (3, 'PostgreSQL'),
    (4, 'Docker'),
    (5, 'Flyway');

INSERT INTO posts (id, title, content, summary, author_id, published, created_at, updated_at) VALUES
    (1, 'Welcome to the Blog Platform',
     'This sample application shows how to build a classic server-rendered blog with Spring Boot, Thymeleaf, and PostgreSQL.\n\nIt includes Flyway migrations, a seeded dataset, and REST endpoints for posts and comments.',
     'A guided introduction to the sample Spring Boot blog platform.',
     1,
     TRUE,
     CURRENT_TIMESTAMP - INTERVAL '5 day',
     CURRENT_TIMESTAMP - INTERVAL '5 day'),
    (2, 'Why Server-Side Rendering Still Matters',
     'Server-side rendering keeps initial page loads fast, simplifies SEO, and is often a great fit for content-heavy products like blogs and documentation portals.\n\nThymeleaf gives you a productive templating model that works naturally with Spring MVC.',
     'A quick look at the strengths of Thymeleaf and server-side rendering.',
     2,
     TRUE,
     CURRENT_TIMESTAMP - INTERVAL '3 day',
     CURRENT_TIMESTAMP - INTERVAL '2 day'),
    (3, 'Preparing Your Local PostgreSQL Environment',
     'Docker Compose makes it easy to provision a local PostgreSQL instance that mirrors production settings more closely than an in-memory database.\n\nUsing Flyway keeps your schema changes versioned and repeatable.',
     'Using Docker Compose and Flyway for reliable local development.',
     3,
     TRUE,
     CURRENT_TIMESTAMP - INTERVAL '1 day',
     CURRENT_TIMESTAMP - INTERVAL '1 day');

INSERT INTO post_tags (post_id, tag_id) VALUES
    (1, 1), (1, 2), (1, 3),
    (2, 1), (2, 2),
    (3, 3), (3, 4), (3, 5);

INSERT INTO comments (id, content, post_id, author_id, created_at) VALUES
    (1, 'Great starter project. I like that it includes both MVC pages and JSON endpoints.', 1, 2, CURRENT_TIMESTAMP - INTERVAL '4 day'),
    (2, 'The seeded data makes it much easier to explore the UI immediately.', 1, 3, CURRENT_TIMESTAMP - INTERVAL '4 day'),
    (3, 'SSR is still a strong default for editorial experiences.', 2, 1, CURRENT_TIMESTAMP - INTERVAL '2 day'),
    (4, 'Having PostgreSQL on port 5433 is handy when other local stacks already use 5432.', 3, 1, CURRENT_TIMESTAMP - INTERVAL '12 hour');

SELECT setval(pg_get_serial_sequence('users', 'id'), COALESCE((SELECT MAX(id) FROM users), 1), true);
SELECT setval(pg_get_serial_sequence('tags', 'id'), COALESCE((SELECT MAX(id) FROM tags), 1), true);
SELECT setval(pg_get_serial_sequence('posts', 'id'), COALESCE((SELECT MAX(id) FROM posts), 1), true);
SELECT setval(pg_get_serial_sequence('comments', 'id'), COALESCE((SELECT MAX(id) FROM comments), 1), true);
