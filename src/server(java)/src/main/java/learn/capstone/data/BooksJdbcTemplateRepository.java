package learn.capstone.data;

import learn.capstone.data.mappers.AuthorsMapper;
import learn.capstone.data.mappers.BookMapper;
import learn.capstone.models.Authors;
import learn.capstone.models.Books;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class BooksJdbcTemplateRepository implements BooksRepository {

    private final JdbcTemplate jdbcTemplate; //auto-injected, provided by Spring

    public BooksJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean update(Books books) {

        final String sql = "update books, authors "
                + "set books.approval_status = ?, "
                + "books.book_title = ?, "
                + "books.genre = ?, "
                + "books.publication_year = ?, "
                + "authors.author_first_name = ?, "
                + "authors.author_last_name = ? "
                + "where books.idBooks = ? "
                + "and books.idAuthor = authors.idAuthor;";

        return jdbcTemplate.update(sql,
                books.getApprovalStatus(),
                books.getBookTitle(),
                books.getGenre(),
                books.getYearPublished(),
                books.getAuthor().getAuthorFirstName(),
                books.getAuthor().getAuthorLastName(),
                books.getIdBooks()) > 0;
    }

    @Override
    public List<Books> findAllForAdmin() {
        final String sql = "select * from books b Inner join authors au on b.idAuthor = au.idAuthor;";
        return jdbcTemplate.query(sql, new BookMapper()); //returns a list of books
    }

    @Transactional
 public Books addToAuthorTableFirstThenBooks(Books book){
    final String sql = "insert into authors (author_first_name, author_last_name) "
            + " values (?,?);";

    KeyHolder keyHolder = new GeneratedKeyHolder();
    int rowsAffected = jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, book.getAuthor().getAuthorFirstName()); //author firstname is set during HTTP request
        ps.setString(2, book.getAuthor().getAuthorLastName());
        return ps;
    }, keyHolder);

    if (rowsAffected <= 0) {
        return null;
    }

    int idAuthorForeignKey = keyHolder.getKey().intValue(); //grabs the auto-incremented id of the newly-inserted author

    return addToBooksTable(book, idAuthorForeignKey); //this return value (a book) should include the id of the newly-inserted book
}

public Books addToBooksTable(Books book, int idAuthorForeignKey){
    final String sql = "insert into books (approval_status, book_title, genre, publication_year, idAuthor) "
            + " values (?,?,?,?,?);";

    KeyHolder keyHolder = new GeneratedKeyHolder();
    int rowsAffected = jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setBoolean(1, book.getApprovalStatus());
        ps.setString(2, book.getBookTitle());
        ps.setString(3, book.getGenre());
        ps.setInt(4, book.getYearPublished());
        ps.setInt(5, idAuthorForeignKey); //uses the id of the author that was just inserted
        return ps;
    }, keyHolder);

    if (rowsAffected <= 0) {
        return null;
    }

    book.setIdBooks(keyHolder.getKey().intValue()); //useful for testing purposes to see if a book was inserted

    return book;
}

    //For testing out http requests in the test table
    public void setKnownGoodState(){
        jdbcTemplate.update("call set_known_good_state();");
    }

}
