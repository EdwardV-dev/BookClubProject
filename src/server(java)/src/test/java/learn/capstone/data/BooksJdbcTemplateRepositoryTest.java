package learn.capstone.data;

import learn.capstone.models.Books;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BooksJdbcTemplateRepositoryTest {

    @Autowired
    BooksJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void shouldUpdate() {
        Books book = new Books();
        book.setIdBooks(2);
        book.setBookTitle("Test");
        book.setApprovalStatus(true);
        //book.setYearPublished(1996);
        book.setGenre("fiction");

        assertTrue(repository.update(book));

    }
}