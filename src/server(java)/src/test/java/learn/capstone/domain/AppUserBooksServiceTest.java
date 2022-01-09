package learn.capstone.domain;

import learn.capstone.data.AppUserBooksRepository;
import learn.capstone.models.AppUserBooks;
import learn.capstone.models.Authors;
import learn.capstone.models.Books;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppUserBooksServiceTest {

    @Autowired
    AppUserBooksService service;

    @MockBean
    AppUserBooksRepository repository;

    @Test
    void shouldAddAssociation() {
        Books book = makeBook();
        AppUserBooks userBook = new AppUserBooks();

        userBook.setAppUserId(1);
        userBook.setBook(book);
        userBook.setCompletionStatus("DoneReading");

        when(repository.add(userBook)).thenReturn(true);
        Result<AppUserBooks> actual = service.add(userBook);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotAddAssociationIfCompletionStatusInvalid() {
        Books book = makeBook();
        AppUserBooks userBook = new AppUserBooks();

        userBook.setAppUserId(1);
        userBook.setBook(book);
        userBook.setCompletionStatus("Invalid");


        Result<AppUserBooks> actual = service.add(userBook);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldNotAddAssociationIfBookIsNull() {

        AppUserBooks userBook = new AppUserBooks();

        userBook.setAppUserId(1);
//        userBook.setBook(book);
        userBook.setCompletionStatus("Invalid");

       
        Result<AppUserBooks> actual = service.add(userBook);
        assertEquals(ResultType.INVALID, actual.getType());
    }


    @Test
    void shouldUpdateCompletionStatus() {
        Books book = makeBook();
        AppUserBooks userBook = new AppUserBooks();

        userBook.setAppUserId(1);
        userBook.setBook(book);
        userBook.setCompletionStatus("DoneReading");

        when(repository.update(userBook)).thenReturn(true);
        Result<AppUserBooks> actual = service.update(userBook);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateInvalid() {
        Books book = makeBook();
        AppUserBooks userBook = new AppUserBooks();

        userBook.setAppUserId(1);
        userBook.setBook(book);
        userBook.setCompletionStatus("Invalid");

        Result<AppUserBooks> actual = service.update(userBook);
        assertEquals(ResultType.INVALID, actual.getType());
        assertEquals("Invalid completion status.", actual.getMessages().get(0));
    }

    @Test
    void shouldDelete() {
        Books book = makeBook();
        AppUserBooks userBook = new AppUserBooks();

        userBook.setAppUserId(1);
        userBook.setBook(book);

        when(repository.delete(userBook.getAppUserId(), userBook.getBook().getIdBooks())).thenReturn(true);
        Result<AppUserBooks> actual = service.deleteByBookId(userBook.getAppUserId(), userBook.getBook().getIdBooks());
        assertEquals(ResultType.SUCCESS, actual.getType());
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