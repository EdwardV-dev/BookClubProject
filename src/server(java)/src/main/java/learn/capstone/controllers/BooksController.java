package learn.capstone.controllers;

import learn.capstone.domain.BooksService;
import learn.capstone.models.Books;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booksAdmin")
public class BooksController {

    private final BooksService service;

    public BooksController(BooksService service) {
        this.service = service;
    }

    @GetMapping
    public List<Books> findAllForAdmin() {
        return service.findAllForAdmin();
    }

//    @GetMapping("/{bookId}")
//    public ResponseEntity<Books> findById(@PathVariable int bookId) {
//        //Books book = service
//        return ResponseEntity.ok();
//    }
}
