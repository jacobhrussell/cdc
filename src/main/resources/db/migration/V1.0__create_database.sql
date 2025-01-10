CREATE TABLE book
(
    id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(500) NULL
);