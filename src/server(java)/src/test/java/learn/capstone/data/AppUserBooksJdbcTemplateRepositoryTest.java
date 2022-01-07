package learn.capstone.data;

import learn.capstone.models.AppUserBooks;
import learn.capstone.models.Books;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserBooksJdbcTemplateRepositoryTest {

    @Autowired
    AppUserBooksRepositoryJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void shouldFindAllUserBooks(){
        List<Books> books = repository.findAllUserBooks(1); //EdwardV is the appUser here
        assertNotNull(books);

        // can't predict order
        // if delete is first, we're down to 2
        // if add is first, we may go as high as 4
        assertTrue(books.size() >= 2 && books.size() <= 4);

    }

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

    //adding a new book association (id 3) to amyR (id 2)
    @Test
    void shouldAddBookToUsersList() {
        Books book = new Books();
        book.setIdBooks(3);

        AppUserBooks userBook = new AppUserBooks();
        userBook.setBook(book);
        userBook.setAppUserId(2);
        userBook.setCompletionStatus("DoneReading");

        assertTrue(repository.add(userBook));
    }
}