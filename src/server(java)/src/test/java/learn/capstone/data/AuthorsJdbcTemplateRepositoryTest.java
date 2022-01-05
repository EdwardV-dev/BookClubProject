package learn.capstone.data;

import learn.capstone.models.Books;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AuthorsJdbcTemplateRepositoryTest {

    @Autowired
    AuthorsJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void findAllBooksFromAuthorFirstOrLastName() {
        List<Books> books = repository.findAllBooksFromAuthorFirstOrLastName("Rowling");
        assertNotNull(books);

        //should find one book
        assertTrue(books.size() == 1);
        assertTrue(books.get(0).getYearPublished() == 1996);

        System.out.println(books.size());
    }
}