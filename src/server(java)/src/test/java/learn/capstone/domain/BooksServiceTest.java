package learn.capstone.domain;

import learn.capstone.data.BooksRepository;
import learn.capstone.models.Authors;
import learn.capstone.models.Books;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BooksServiceTest {

    @Autowired
    BooksService service;

    @MockBean
    BooksRepository repository;

    @Test
    void shouldUpdate() {
        Books book = makeBook();

        when(repository.updateAdmin(book)).thenReturn(true);
        Result<Books> actual = service.updateAdmin(book);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldAdd() {
        Books book = makeBook();


        when(repository.addToAuthorTableFirstThenBooks(book)).thenReturn(book);
        Result<Books> actual = service.add(book);
        assertEquals(ResultType.SUCCESS, actual.getType());

    }

    @Test
    void shouldNotAddInvalidBookWithNoAuthor() {
        Books book = makeInvalidBookWithNoAuthor();


        Result<Books> actual = service.add(book);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().contains("Book author is required."));
    }

    //same book, same author is not allowed
//    @Test
//    void shouldNotAddInvalidBookDuplication() {
//        Authors author = new Authors();
//        author.setAuthorFirstName("Henry");
//        author.setAuthorLastName("Smith");
//
//        Books book = new Books();
//        book.setIdBooks(2);
//        book.setBookTitle("Test");
//        book.setApprovalStatus(true);
//        book.setYearPublished(1999);
//        book.setGenre("fiction");
//        book.setAuthor(author);
//        return book;
//
//
//        Result<Books> actual = service.add(book);
//        assertEquals(ResultType.INVALID, actual.getType());
//        assertTrue(actual.getMessages().contains("Book author is required."));
//    }


    @Test
    void shouldNotUpdateInvalid() {
        Books book = makeBook();
        book.setYearPublished(2030);


        Result<Books> actual = service.updateAdmin(book);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    Books makeBook() {
        Authors author = new Authors();
        author.setIdAuthor(1);

        Books book = new Books();
        book.setIdBooks(2);
        book.setBookTitle("Test");
        book.setApprovalStatus(true);
        book.setYearPublished(1999);
        book.setGenre("fiction");
        book.setAuthor(author);
        return book;
    }

    Books makeInvalidBookWithNoAuthor() {


        Books book = new Books();
        book.setIdBooks(2);
        book.setBookTitle("Test");
        book.setApprovalStatus(true);
        book.setYearPublished(1999);
        book.setGenre("fiction");

        return book;
    }
}