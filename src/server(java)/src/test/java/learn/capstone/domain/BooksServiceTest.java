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

        when(repository.update(book)).thenReturn(true);
        Result<Books> actual = service.updateAdmin(book);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Books book = makeBook();

        when(repository.update(book)).thenReturn(false);
        Result<Books> actual = service.updateAdmin(book);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateInvalid() {
        Books book = makeBook();
        book.setYearPublished(2030);

        when(repository.update(book)).thenReturn(true);
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
}