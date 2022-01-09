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
        Books book = makeBook();
        book.setIdBooks(1);

        assertTrue(repository.updateAdmin(book));
        assertEquals("Erica", book.getAuthor().getAuthorFirstName());
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

    @Test
    void shouldNotReturnNullWhenFindingExistingBook(){ //Reminder: the repo method searches by book title and author name
        Books book = new Books();
        book.setBookTitle("Fossils and more!");

        Authors author = new Authors();
        author.setAuthorFirstName("Henry");
        author.setAuthorLastName("Smith");

        book.setAuthor(author);

        Books actual = repository.findBookFromAuthorFirstAndLastNameAndBookTitle(book.getAuthor().getAuthorFirstName()
        , book.getAuthor().getAuthorLastName(), book.getBookTitle());

        assertNotNull(actual);
    }

    @Test
    void shouldReturnNullWhenFindingNonExistingBook(){ //Reminder: the repo method searches by book title and author name
        Books book = new Books();
        book.setBookTitle("Dinos and more!");

        Authors author = new Authors();
        author.setAuthorFirstName("Jack");
        author.setAuthorLastName("Smith");

        book.setAuthor(author);

        Books actual = repository.findBookFromAuthorFirstAndLastNameAndBookTitle(book.getAuthor().getAuthorFirstName()
                , book.getAuthor().getAuthorLastName(), book.getBookTitle());

        assertNull(actual);
    }

    @Test
    void shouldFindById() {
        Books book = repository.findById(1);
        assertNotNull(book);
        assertEquals("Winnie the Pooh", book.getBookTitle());

    }

    @Test
    void shouldNotFindMissingBook() {
        Books book = repository.findById(1000);
        assertNull(book);
    }

    @Test
   void findBookViaMostReadGenre(int userId){

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