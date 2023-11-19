package ca.sheridancollege.charanit.database;

import ca.sheridancollege.charanit.beans.Book;
import ca.sheridancollege.charanit.beans.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DatabaseAccess {

    @Autowired
    protected NamedParameterJdbcTemplate jdbc;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void insertBook(Book book) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("bookTitle", book.getTitle());
        namedParameters.addValue("bookAuthor", book.getAuthor());
        namedParameters.addValue("bookIsbn", book.getIsbn());
        namedParameters.addValue("bookPrice", book.getPrice());
        namedParameters.addValue("bookDescription", book.getDescription());
        String query = "INSERT INTO books(title, author, isbn, price, description) VALUES(:bookTitle, :bookAuthor, :bookIsbn, :bookPrice, :bookDescription)";
        int rowsAffected = jdbc.update(query, namedParameters);
        if (rowsAffected > 0) {
            System.out.println("Book inserted into database");
        }
    }

    public List<Book> getAllBooksList() {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM books";
        return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Book>(Book.class));
    }

    public void deleteBookByIsbn(Long isbn) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "DELETE FROM books WHERE isbn = :isbn";
        namedParameters.addValue("isbn", isbn);
        if (jdbc.update(query, namedParameters) > 0) {
            System.out.println("Deleted book " + isbn + " from database");
        }
    }

    public void updateBook(Book updatedBook) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "UPDATE books SET title = :title";
        namedParameters.addValue("title", updatedBook.getTitle());
        int rowsAffected = jdbc.update(query, namedParameters);
        if (rowsAffected > 0) {
            System.out.println("Updated book name with " + updatedBook.getTitle() + " in the database");
        }
    }

    public List<Book> getBookByIsbn(Long isbn) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM books WHERE isbn = :isbn";
        namedParameters.addValue("isbn", isbn);
        return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Book>(Book.class));
    }

    public User findUserAccount(String email) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM sec_user WHERE email = :email";
        namedParameters.addValue("email", email);
        try {
            return jdbc.queryForObject(query, namedParameters, new BeanPropertyRowMapper<>(User.class));
        } catch (EmptyResultDataAccessException erdae) {
            return null;
        }
    }

    public List<String> getRolesById(Long userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT sec_role.roleName FROM user_role, sec_role WHERE user_role.roleId = sec_role.roleId AND userId = :userId";
        namedParameters.addValue("userId", userId);
        return jdbc.queryForList(query, namedParameters, String.class);
    }

    public void addUser(String username, String email, String password) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "INSERT INTO sec_user (username, email, password, enabled) VALUES (:username, :email, :password, 1)";
        namedParameters.addValue("username", username);
        namedParameters.addValue("email", email);
        namedParameters.addValue("password", passwordEncoder.encode(password));
        jdbc.update(query, namedParameters);
    }

    public void addRole(Long userId, Long roleId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "INSERT INTO user_role (userId, roleId) VALUES (:userId, :roleId)";
        namedParameters.addValue("userId", userId);
        namedParameters.addValue("roleId", roleId);
        jdbc.update(query, namedParameters);
    }

//    SHOPPING CART

    public void addToCart(Long userId, Long bookIsbn, int quantity) {
        for (int i = 0; i < quantity; i++) {
            // Add the book to the user's shopping cart
            addUserBookToCart(userId, bookIsbn);
        }
    }

    public List<Book> getUserShoppingCart(Long userId) {
        // Retrieve the user's shopping cart based on userId
        String query = "SELECT b.* FROM books b INNER JOIN user_shopping_cart usc ON b.isbn = usc.book_isbn WHERE usc.user_id = :userId";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource().addValue("userId", userId);
        return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<>(Book.class));
    }

    private void addUserBookToCart(Long userId, Long bookIsbn) {
        // Add the book to the user's shopping cart
        String insertQuery = "INSERT INTO user_shopping_cart (user_id, book_isbn) VALUES (:userId, :bookIsbn)";
        MapSqlParameterSource insertParams = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("bookIsbn", bookIsbn);
        jdbc.update(insertQuery, insertParams);
    }

}
