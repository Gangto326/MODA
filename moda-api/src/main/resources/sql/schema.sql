DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS url_caches CASCADE;
DROP TABLE IF EXISTS cards CASCADE;
DROP TABLE IF EXISTS category_order CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS content_type CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS saved CASCADE;
DROP TABLE IF EXISTS refresh_tokens CASCADE;
DROP TABLE IF EXISTS child_cards CASCADE;


CREATE TABLE refresh_tokens (
    refresh_token_id BIGSERIAL PRIMARY KEY,
    token TEXT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_name VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE content_type (
type_id BIGSERIAL PRIMARY KEY,
type VARCHAR(100) NOT NULL
);


CREATE TABLE category (
category_id BIGSERIAL PRIMARY KEY,
category VARCHAR NOT NULL
);


CREATE TABLE users (
user_id VARCHAR(36) PRIMARY KEY,
email VARCHAR(100) NOT NULL,
password VARCHAR(60) NOT NULL,
user_name VARCHAR(100) NOT NULL,
nickname VARCHAR(100) NOT NULL,
created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
deleted_at TIMESTAMP
);

CREATE TABLE saved (
    user_id VARCHAR(36) NOT NULL,
    type_id BIGSERIAL NOT NULL,
    type_count INT NOT NULL DEFAULT 0
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

CREATE TABLE url_caches (
url_hash VARCHAR(64) PRIMARY KEY,
original_url TEXT NOT NULL,
type_id INT NOT NULL,
category_id INT NOT NULL,
cached_title VARCHAR(100) NOT NULL,
cached_content TEXT NOT NULL,
cached_thumbnail_content TEXT NOT NULL,
cached_thumbnail_url TEXT NOT NULL,
cached_embedding VECTOR(768) NOT NULL,
cached_keywords text[],
cached_subcontents text[] DEFAULT ARRAY[]::text[],
created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (type_id) REFERENCES content_type(type_id),
FOREIGN KEY (category_id) REFERENCES category(category_id)
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
sub_contents text[] DEFAULT ARRAY[]::text[],
bookmark BOOLEAN NOT NULL DEFAULT FALSE,
FOREIGN KEY (user_id) REFERENCES users(user_id),
FOREIGN KEY (category_id) REFERENCES category(category_id),
FOREIGN KEY (type_id) REFERENCES content_type(type_id),
FOREIGN KEY (url_hash) REFERENCES url_caches(url_hash)
);
CREATE TABLE child_cards (
    child_id VARCHAR(36) PRIMARY KEY,
    card_id VARCHAR(36) NOT NULL,
    type_id INT NOT NULL,
    url_hash VARCHAR(64) NOT NULL,
    category_id BIGSERIAL NOT NULL,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    thumbnail_content TEXT NOT NULL,
    thumbnail_url TEXT NOT NULL,
    "order" INT NOT NULL,
    embedding VECTOR(768) NOT NULL,
    keywords text[] NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subcontents text[] DEFAULT ARRAY[]::TEXT[],
    FOREIGN KEY (card_id) REFERENCES cards(card_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id),
    FOREIGN KEY (type_id) REFERENCES content_type(type_id),
    FOREIGN KEY (url_hash) REFERENCES url_caches(url_hash)
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



INSERT INTO users (user_id, email, password, profile_image, nickname, role) VALUES
('user', 'jongwon', '1234', 'https://img1.daumcdn.net/thumb/R1280x0.fjpg/?fname=http://t1.daumcdn.net/brunch/service/user/cnoC/image/XqjxGakdgqypZUy-BrrP5nq70AQ.jpg', '키티', 'USER');

INSERT INTO content_type (type_id, type) VALUES
(1, 'YOUTUBE'),
(2, 'BLOG'),
(3, 'NEWS'),
(4, 'IMG'),
(5, 'SHORTS'),
(6, 'OTHERS');

INSERT INTO category (category_id, category) VALUES
(1, 'All'),
(2, 'Trends'),
(3, 'Entertainment'),
(4, 'Finance'),
(5, 'Travel'),
(6, 'Food'),
(7, 'IT'),
(8, 'Design'),
(9, 'Society'),
(10, 'Health');

INSERT INTO public.category_order (category_id, user_id, position) VALUES
(1, 'user',1),
(2, 'user',2),
(3, 'user',3),
(4, 'user',4),
(5, 'user',5),
(6, 'user',6),
(7, 'user',7),
(8, 'user',8),
(9,'user',9),
(10, 'user',10);
