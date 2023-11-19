package ca.sheridancollege.charanit.controllers;

import ca.sheridancollege.charanit.beans.Book;
import ca.sheridancollege.charanit.beans.User;
import ca.sheridancollege.charanit.database.DatabaseAccess;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@SessionAttributes("shoppingCart")
public class BookController {

    List<Book> bookList = new CopyOnWriteArrayList<>();

    @Autowired
    @Lazy
    private DatabaseAccess database;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("bookList", database.getAllBooksList());
        return "index";
    }

    @PostMapping("/insertBook")
    public String insertBook(Model model, @ModelAttribute Book book) {
        database.insertBook(book);
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", database.getAllBooksList());
        return "editBooks";
    }

    @GetMapping("/deleteBookByIsbn/{isbn}")
    public String deleteBookByIsbn(Model model, @PathVariable Long isbn) {
        model.addAttribute("bookList", database.getAllBooksList());
        model.addAttribute("book", new Book());
        database.deleteBookByIsbn(isbn);
        return "editBooks";
    }

    @GetMapping("/updateBook/{isbn}")
    public String updateBook(Model model, @PathVariable Long isbn) {
        Book book = database.getBookByIsbn(isbn).get(0);
        model.addAttribute("book", book);
        database.deleteBookByIsbn(isbn);
        model.addAttribute("bookList", database.getAllBooksList());
        return "editBooks";
    }

    @GetMapping("/viewBook/{isbn}")
    public String viewBook(Model model, @PathVariable Long isbn) {
        Book book = database.getBookByIsbn(isbn).get(0);
        model.addAttribute("book", book);
        return "viewBook";
    }

//    SECURITY

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/secure")
    public String secure(Model model) {
        return "secure/index";
    }

    @GetMapping("/permission-denied")
    public String permissionDenied(Model model) {
        return "/error/permission-denied";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        database.addUser(username, email, password);
        Long userId = database.findUserAccount(email).getUserId();
        database.addRole(userId, Long.valueOf(1));
        return "redirect:/secure/index";
    }

    @GetMapping("/editBooks")
    public String editBooks(Model model) {
        model.addAttribute("bookList", database.getAllBooksList());
        model.addAttribute("book", new Book());
        return "editBooks";
    }

//    SHOPPING CART

    @ModelAttribute("shoppingCart")
    public List<Book> initShoppingCart() {
        return new CopyOnWriteArrayList<>();
    }

    @GetMapping("/addToCart/{isbn}")
    public String addToCart(Model model, HttpSession session, @PathVariable Long isbn, @RequestParam int quantity) {
        Book book = database.getBookByIsbn(isbn).get(0);

        List<Book> shoppingCart = (List<Book>) session.getAttribute("shoppingCart");
        for (int i = 0; i < quantity; i++) {
            shoppingCart.add(book);
        }

        model.addAttribute("book", new Book());
        model.addAttribute("bookList", database.getAllBooksList());
        model.addAttribute("shoppingCart", shoppingCart);
        return "viewBook";
    }

    @GetMapping("/viewCart")
    public String viewCart(Model model, @ModelAttribute("shoppingCart") List<Book> shoppingCart) {
        model.addAttribute("shoppingCart", shoppingCart);
        return "viewCart";
    }
}
