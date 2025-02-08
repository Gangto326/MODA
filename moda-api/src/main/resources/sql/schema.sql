DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS category_order CASCADE;
DROP TABLE IF EXISTS cards CASCADE;
DROP TABLE IF EXISTS url_caches CASCADE;

DROP TABLE IF EXISTS content_type CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE category (
    category_id BIGSERIAL PRIMARY KEY,
    category VARCHAR NOT NULL
);

CREATE TABLE content_type (
    type_id BIGSERIAL PRIMARY KEY,
    type VARCHAR(100) NOT NULL
);

CREATE TABLE users (
    user_id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(60) NOT NULL,
    profile_img VARCHAR(255),
    nickname VARCHAR(100) NOT NULL,
    status VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Category Order Table
CREATE TABLE category_order (
    category_id BIGSERIAL NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    position INT NOT NULL,
    PRIMARY KEY (category_id, user_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Cards Table
CREATE TABLE cards (
    card_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    type_id INT NOT NULL,
    url_hash VARCHAR(64) NOT NULL,
    category_id BIGSERIAL NOT NULL,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    thumbnail_content TEXT NOT NULL,
    thumbnail_url TEXT NOT NULL,
    view_count INT NOT NULL DEFAULT 0,
    embedding VECTOR(768) NOT NULL,
    keywords text[],
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    subcontents text[] DEFAULT ARRAY[]::text[],
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id),
    FOREIGN KEY (type_id) REFERENCES content_type(type_id)
);

CREATE TABLE url_caches (
    url_hash VARCHAR(64) PRIMARY KEY,
    original_url TEXT NOT NULL,
    cached_title VARCHAR(100) NOT NULL,
    cached_content TEXT NOT NULL,
    keywords text[],
    subcontents text[] DEFAULT ARRAY[]::text[],
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notifications (
    notification_id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('user', 'follow', 'board', 'card', 'report')),
    content_id VARCHAR(36) NOT NULL,
    content VARCHAR(200) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE images (
    image_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    category_id BIGSERIAL NOT NULL,
    embedding VECTOR(768) NOT NULL,
    keywords text[] NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    view_count INT NOT NULL DEFAULT 0,
    url VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

CREATE TABLE fcm_tokens (
    token_id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE, 
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,  
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
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
