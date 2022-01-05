package learn.capstone.data;

import learn.capstone.models.Books;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BooksJdbcTemplateRepositoryTest {

    @Autowired
    BooksJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Books> books = repository.findAll();
        assertNotNull(books);

        // can't predict order
        // if delete is first, we're down to 2
        // if add is first, we may go as high as 4
        assertTrue(books.size() >= 1 && books.size() <= 4);

        System.out.println(books.size());

    }

}