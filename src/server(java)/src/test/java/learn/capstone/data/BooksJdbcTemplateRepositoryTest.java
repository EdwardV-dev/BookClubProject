package learn.capstone.data;

import learn.capstone.models.Authors;
import learn.capstone.models.Books;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BooksJdbcTemplateRepositoryTest {

    @Autowired
    BooksJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    private final int NEXT_ID = 4;

    @Test
    void shouldUpdate() {
        Books book = new Books();
        book.setIdBooks(2);
        book.setBookTitle("Test");
        book.setApprovalStatus(true);
        book.setYearPublished(1996);
        book.setGenre("fiction");

        assertTrue(repository.update(book));

    }

    @Test
    void shouldFindAll() {
        List<Books> books = repository.findAllForAdmin();
        assertNotNull(books);

        // can't predict order
        // if delete is first, we're down to 2
        // if add is first, we may go as high as 4
        assertTrue(books.size() >= 1 && books.size() <= 4);

        assertNotNull(books.get(0).getAuthor().getAuthorFirstName());
        System.out.println(books.size());
        System.out.println(books.get(0).getAuthor().getAuthorFirstName());

    }

    @Test
    void shouldAddToAuthorsTableAndBooksTable() {
        Books book = makeBook();

        Books actual = repository.addToAuthorTableFirstThenBooks(book);

        assertNotNull(actual);

        assertEquals(NEXT_ID, actual.getIdBooks()); //The newly-inserted book should have an id of 4
        assertTrue(actual.getAuthor().getAuthorFirstName().equalsIgnoreCase("Erica"));
    }

    private Books makeBook() {
        Books book = new Books();
        book.setYearPublished(2002);
        book.setGenre("Fiction");
        book.setApprovalStatus(true);
        book.setAuthor(makeAuthor());
        book.setBookTitle("Scary Stories");
        return book;
    }

    private Authors makeAuthor() {
        Authors author = new Authors();
     author.setAuthorFirstName("Erica");
     author.setAuthorLastName("Jackson");
        return author;
    }

}