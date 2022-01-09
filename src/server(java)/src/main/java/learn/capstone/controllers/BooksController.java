package learn.capstone.controllers;

import learn.capstone.domain.BooksService;
import learn.capstone.domain.Result;
import learn.capstone.models.Books;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{bookId}")
    public ResponseEntity<Books> findById(@PathVariable int bookId) {
        Books book = service.findBookByIdForAdmin(bookId);

        if(book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(book);
    }



    @PutMapping("/{bookId}")
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
