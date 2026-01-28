CREATE TABLE MemberGroup (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE,
    slug VARCHAR NOT NULL UNIQUE,
    description VARCHAR,
    hashtag VARCHAR UNIQUE,
    leader BIGINT NOT NULL,
    FOREIGN KEY (leader) REFERENCES Member(id)
);