package learn.capstone.data;

import learn.capstone.data.mappers.AuthorsMapper;
import learn.capstone.data.mappers.BookMapper;
import learn.capstone.models.Authors;
import learn.capstone.models.Books;
import org.springframework.dao.EmptyResultDataAccessException;
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

    //If domain layer does not allow an update to go through, show an error message (e.g. book already exists in the database)
// to the admin.
    @Override
    public boolean updateAdmin(Books books) {

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

    // { ApprovalStatus: whatever
    // book {
//    BookId: 3
//  }

//}

    public void convertNullYearsTo6000ForFinds(){
        final String sql1 = "SET SQL_SAFE_UPDATES=0;";
        jdbcTemplate.execute(sql1);

        final String sql2 = "update books set publication_year = 6000 where publication_year is null;" ;
        jdbcTemplate.execute(sql2);

        final String sql3 = "SET SQL_SAFE_UPDATES=1;";
        jdbcTemplate.execute(sql3);
    }

    //This method needs to have nulls converted to 6,000
    @Override
    public List<Books> findAllForAdmin() {
        convertNullYearsTo6000ForFinds();
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

    //Used for domain layer validation. Prevents user adding the same book from the same author. If this method returns
    //a list of size 0 (i.e. no books found), go ahead and add it to SQL database.
   public Books findBookFromAuthorFirstAndLastNameAndBookTitle(String input1, String input2, String input3) {
        convertNullYearsTo6000ForFinds();

        final String sql = "Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, " +
                "au.author_first_name, au.author_last_name " +
                "from books b\n" +
                "Inner join authors au \n" +
                "on b.idAuthor = au.idAuthor\n" +
                "where au.author_first_name = ? and au.author_last_name = ? and book_title = ?;";
        try{
        Books specificBook = jdbcTemplate.queryForObject(sql, new BookMapper(), input1, input2, input3);
        return specificBook;

        } catch (EmptyResultDataAccessException ex){
            return null; //returning null means a duplicate book was not found
        }

    }

    //For testing out http requests in the test table
    public void setKnownGoodState(){
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Override
    public Books findById(int bookId) {
        convertNullYearsTo6000ForFinds();
        final String sql = "select * from books b "
                + "inner join authors au on b.idAuthor = au.idAuthor "
                + "where b.idBooks = ?;";
        return jdbcTemplate.query(sql, new BookMapper(), bookId)
                .stream().findFirst().orElse(null);
    }
}
