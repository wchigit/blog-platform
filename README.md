# Blog Platform

A full-stack blog platform built with Spring Boot 3, Thymeleaf, PostgreSQL, and Flyway.

## Features
- Server-side rendered blog UI with Thymeleaf
- PostgreSQL persistence with Flyway migrations
- Sample seed data for users, posts, tags, and comments
- REST endpoints for posts and comments at `/api/posts` and `/api/comments`
- Docker Compose setup for the local database

## Requirements
- Java 17+
- Maven 3.9+ (or use the included wrapper script)
- Docker Desktop (only needed when you want to run PostgreSQL)

## Project Structure
- `src/main/java/com/example/blog`: application source
- `src/main/resources/templates`: Thymeleaf views
- `src/main/resources/db/migration`: Flyway migrations
- `docker-compose.yml`: PostgreSQL container definition

## Configuration
Copy `.env.example` to `.env` if you want a convenient reference for local environment variables:

```powershell
Copy-Item .env.example .env
```

Application defaults are defined in `src/main/resources/application.properties`. The provided `.env.example` points to PostgreSQL on host port `5433`.

## Compile
From `C:\dev\samplerepo\blog-platform` run:

```powershell
.\mvnw.cmd -Dmaven.repo.local=.m2 compile
```

## Run the Application
1. Start PostgreSQL when you are ready:
   ```powershell
   docker compose up -d
   ```
2. Export the database variables from `.env.example` or rely on the defaults.
3. Run the Spring Boot app:
   ```powershell
   .\mvnw.cmd -Dmaven.repo.local=.m2 spring-boot:run
   ```
4. Open `http://localhost:8080`

## Main Routes
- `/` - published post listing
- `/posts/new` - create a post
- `/posts/{id}` - post details and comments
- `/users/{id}` - author profile

## REST API
- `GET /api/posts`
- `POST /api/posts`
- `GET /api/comments?postId={id}`
- `POST /api/comments`
