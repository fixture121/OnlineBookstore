INSERT INTO books (title, author, isbn, price, description)
VALUES ('A Promised Land', 'Barack Obama', '9781524763169', '62.99', 'A riveting, deeply personal account of history in the making—from the president who inspired us to believe in the power of democracy.'),
       ('Hamlet', 'William Shakespeare', '9780743477123', '6.99', 'Hamlet is Shakespeare’s most popular, and most puzzling, play.'),
       ('Harry Potter and the Philosophers Stone', 'J.K. Rowling', '9781551923963', '19.95', 'Harry Potter, an ordinary boy who lives in a cupboard under the stairs, discovers his true heritage at Hogwarts School of Wizardry and Witchcraft.'),
       ('The Catcher in the Rye', 'J.D. Salinger', '9780316769488', '12.99', 'The hero-narrator of The Catcher in the Rye is an ancient child of sixteen, a native New Yorker named Holden Caufield. Through circumstances that tend to preclude adult, secondhand description, he leaves his prep school in Pennsylvania and goes underground in New York City for three days.');

-- INSERT INTO users (username, password, email) VALUES ('user', 'test', 'user@test.com');

INSERT INTO sec_user (username, email, password, enabled)
VALUES ('Fahad', 'fahad.jan@sheridancollege.ca', '$2a$10$1ltibqiyyBJMJQ4hqM7f0OusP6np/IHshkYc4TjedwHnwwNChQZCy', 1);

INSERT INTO sec_user (username, email, password, enabled)
VALUES ('Frank', 'frank@sheridancollege.ca', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 2);

INSERT INTO sec_role (roleName)
VALUES ('ROLE_USER');

INSERT INTO sec_role (roleName)
VALUES ('ROLE_GUEST');

INSERT INTO user_role (userId, roleId)
VALUES (1, 1);

INSERT INTO user_role (userId, roleId)
VALUES (2, 2);
