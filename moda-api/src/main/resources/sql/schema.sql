DROP TABLE IF EXISTS "notifications";
DROP TYPE IF EXISTS "notification_type";
DROP TABLE IF EXISTS "reports";
DROP TABLE IF EXISTS "report_contents";
DROP TABLE IF EXISTS "unread_cards";
DROP TABLE IF EXISTS "cards";
DROP TABLE IF EXISTS "url_caches";
DROP TABLE IF EXISTS "content_type";
DROP TABLE IF EXISTS "read_boards";
DROP TABLE IF EXISTS "board_bookmarks";
DROP TABLE IF EXISTS "boards";
DROP TABLE IF EXISTS "user_blocks";
DROP TABLE IF EXISTS "follows";
DROP TABLE IF EXISTS "users";
DROP TABLE IF EXISTS "Category";

CREATE TABLE "Category" (
    "category_id" BIGSERIAL NOT NULL,
    "category" VARCHAR NOT NULL,
    PRIMARY KEY ("category_id")
);

CREATE TABLE "users" (
    "user_id" VARCHAR(36) NOT NULL,
    "email" VARCHAR(100) NOT NULL,
    "password" VARCHAR(60) NOT NULL,
    "profile_img" VARCHAR(255) NULL,
    "nickname" VARCHAR(100) NOT NULL,
    "status" VARCHAR(10) NOT NULL,
    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "deleted_at" TIMESTAMP NULL,
    PRIMARY KEY ("user_id")
);

CREATE TABLE "content_type" (
    "type_id" BIGSERIAL NOT NULL,
    "type" VARCHAR(100) NOT NULL,
    PRIMARY KEY ("type_id")
);

CREATE TABLE "url_caches" (
    "url_hash" VARCHAR(64) NOT NULL,
    "original_url" TEXT NOT NULL,
    "cached_title" VARCHAR(100) NOT NULL,
    "cached_content" TEXT NOT NULL,
    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY ("url_hash")
);
CREATE TABLE "report_contents" (
    "report_contents_id" BIGSERIAL NOT NULL,
    "content" VARCHAR(255) NOT NULL,
    PRIMARY KEY ("report_contents_id")
);


CREATE TABLE "follows" (
    "follow_id" VARCHAR(36) NOT NULL,
    "follower_id" VARCHAR(100) NOT NULL,
    "following_id" VARCHAR(100) NOT NULL,
    "followed_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY ("follow_id"),
    FOREIGN KEY ("follower_id") REFERENCES "users" ("user_id"),
    FOREIGN KEY ("following_id") REFERENCES "users" ("user_id")
);

CREATE TABLE "user_blocks" (
    "blocker_id" VARCHAR(36) NOT NULL,
    "blocked_id" VARCHAR(36) NOT NULL,
    "blocked_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY ("blocker_id", "blocked_id"),
    FOREIGN KEY ("blocker_id") REFERENCES "users" ("user_id"),
    FOREIGN KEY ("blocked_id") REFERENCES "users" ("user_id")
);

CREATE TABLE "boards" (
    "board_id" VARCHAR(36) NOT NULL,
    "user_id" VARCHAR(36) NOT NULL,
    "category_id" BIGSERIAL NOT NULL,
    "title" VARCHAR(100) NOT NULL,
    "is_public" BOOLEAN DEFAULT TRUE NOT NULL,
    "position" INT NOT NULL,
    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    PRIMARY KEY ("board_id"),
    FOREIGN KEY ("user_id") REFERENCES "users" ("user_id"),
    FOREIGN KEY ("category_id") REFERENCES "Category" ("category_id")
);


CREATE TABLE "board_bookmarks" (
    "user_id" VARCHAR(36) NOT NULL,
    "board_id" VARCHAR(36) NOT NULL,
    "position" INT NOT NULL,
    PRIMARY KEY ("user_id", "board_id"),
    FOREIGN KEY ("user_id") REFERENCES "users" ("user_id"),
    FOREIGN KEY ("board_id") REFERENCES "boards" ("board_id")
);

CREATE TABLE "read_boards" (
    "user_id" VARCHAR(36) NOT NULL,
    "board_id" VARCHAR(36) NOT NULL,
    PRIMARY KEY ("user_id", "board_id"),
    FOREIGN KEY ("user_id") REFERENCES "users" ("user_id"),
    FOREIGN KEY ("board_id") REFERENCES "boards" ("board_id")
);

CREATE TABLE "cards" (
    "card_id" VARCHAR(36) NOT NULL,
    "board_id" VARCHAR(36) NOT NULL,
    "type_id" INT NOT NULL,
    "url_hash" VARCHAR(64) NOT NULL,
    "title" VARCHAR(100) NOT NULL,
    "content" TEXT NOT NULL,
    "thumbnail_content" TEXT NOT NULL,
    "thumbnail_url" TEXT NOT NULL,
    "view_count" INT DEFAULT 0 NOT NULL,
    "embedding" VECTOR(768) NOT NULL,
    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    "deleted_at" TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ("card_id"),
    FOREIGN KEY ("board_id") REFERENCES "boards" ("board_id"),
    FOREIGN KEY ("type_id") REFERENCES "content_type" ("type_id"),
    FOREIGN KEY ("url_hash") REFERENCES "url_caches" ("url_hash")
);

CREATE TABLE "unread_cards" (
    "user_id" VARCHAR(36) NOT NULL,
    "card_id" VARCHAR(36) NOT NULL,
    
    PRIMARY KEY ("user_id", "card_id"),
    FOREIGN KEY ("user_id") REFERENCES "users" ("user_id"),
    FOREIGN KEY ("card_id") REFERENCES "cards" ("card_id")
);

CREATE TABLE "reports" (
    "report_id" VARCHAR(36) NOT NULL,
    "user_id" VARCHAR(36) NOT NULL,
    "card_id" VARCHAR(36) NOT NULL,
    "report_contents_id" INT NOT NULL,
    "reported_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY ("report_id"),
    FOREIGN KEY ("user_id") REFERENCES "users" ("user_id"),
    FOREIGN KEY ("card_id") REFERENCES "cards" ("card_id"),
    FOREIGN KEY ("report_contents_id") REFERENCES "report_contents" ("report_contents_id")
);

CREATE TYPE notification_type AS ENUM ('user', 'follow', 'board', 'card', 'report');

CREATE TABLE "notifications" (
    "notification_id" BIGSERIAL NOT NULL,
    "user_id" VARCHAR(36) NOT NULL,
    "type" notification_type NOT NULL,
    "content_id" VARCHAR(36) NOT NULL,
    "content" VARCHAR(200) NOT NULL,
    "is_read" BOOLEAN DEFAULT FALSE NOT NULL,
    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY ("notification_id"),
    FOREIGN KEY ("user_id") REFERENCES "users" ("user_id")
);

INSERT INTO users (user_id, email, password, profile_img, nickname, status) VALUES
('01234', 'jongwon', '1234', '1234', '1234', '123');

INSERT INTO content_type (type_id, type) VALUES
(1, 'YOUTUBE'),
(2, 'BLOG'),
(3, 'NEWS'),
(4, 'IMG'),
(5, 'SHORTS'),
(6, 'OTHERS'); 

INSERT INTO category (category_id, category) VALUES
(1, 'Food'),
(2, 'Culture');

