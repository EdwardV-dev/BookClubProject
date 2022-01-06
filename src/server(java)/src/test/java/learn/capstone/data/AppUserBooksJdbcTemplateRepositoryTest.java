package learn.capstone.data;

import learn.capstone.models.AppUserBooks;
import learn.capstone.models.Books;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserBooksJdbcTemplateRepositoryTest {

    @Autowired
    AppUserBooksJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void shouldUpdateCompletionStatus() {
        Books book = new Books();
        book.setIdBooks(2);

        AppUserBooks userBook = new AppUserBooks();
        userBook.setBook(book);
        userBook.setAppUserId(1);
        userBook.setCompletionStatus("DoneReading");

        assertTrue(repository.update(userBook));
    }

    @Test
    void shouldDeleteBookFromUserBookList() {
        assertTrue(repository.delete(1, 1));
        assertFalse(repository.delete(1, 1000));

    }
}