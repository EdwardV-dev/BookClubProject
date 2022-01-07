package learn.capstone.domain;

import learn.capstone.data.BooksRepository;
import learn.capstone.models.Books;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BooksServiceTest {

    @Autowired
    BooksService service;

    @MockBean
    BooksRepository repository;

    @Test
    void shouldUpdate() {
        Books book = new Books();
        book.setIdBooks(2);
        book.setBookTitle("Test");
        book.setApprovalStatus(true);
        book.setYearPublished(1996);
        book.setGenre("fiction");

        when(repository.update(book)).thenReturn(true);
        Result<Books> actual = service.update(book);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }
}