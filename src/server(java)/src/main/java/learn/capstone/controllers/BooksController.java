package learn.capstone.controllers;

import learn.capstone.domain.BooksService;
import learn.capstone.domain.Result;
import learn.capstone.models.Books;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BooksController {

    private final BooksService service;

    public BooksController(BooksService service) {
        this.service = service;
    }

    @GetMapping("/booksAdmin")
    public List<Books> findAllForAdmin() {
        return service.findAllForAdmin();
    }


    //Anyone can use this route
    @GetMapping("/books/{bookId}")
    public ResponseEntity<Books> findById(@PathVariable int bookId) {
        Books book = service.findBookByIdForAdmin(bookId);

        if(book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(book);
    }

    @PostMapping("/booksAdmin")
    public ResponseEntity<Object> add(@RequestBody Books book) {
        Result<Books> result = service.add(book);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/booksAdmin/{bookId}")
    public ResponseEntity<Object> updateAdmin(@PathVariable int bookId, @RequestBody Books book) {
        if (bookId != book.getIdBooks()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Books> result = service.updateAdmin(book);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }
}
