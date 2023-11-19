CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    isbn LONG,
    price FLOAT,
    description VARCHAR(500)
);

-- CREATE TABLE users (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     username VARCHAR(255),
--     password VARCHAR(255),
--     email VARCHAR(255)
-- );

CREATE TABLE sec_user (
    userId BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    email VARCHAR(75) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    enabled BIT NOT NULL
);

CREATE TABLE sec_role(
    roleId BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    roleName VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE user_role
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    userId BIGINT NOT NULL,
    roleId BIGINT NOT NULL
);

ALTER TABLE user_role
    ADD CONSTRAINT user_role_uk UNIQUE (userId, roleId);

ALTER TABLE user_role
    ADD CONSTRAINT user_role_fk1 FOREIGN KEY (userId)
        REFERENCES sec_user (userId);

ALTER TABLE user_role
    ADD CONSTRAINT user_role_fk2 FOREIGN KEY (roleId)
        REFERENCES sec_role (roleId);
