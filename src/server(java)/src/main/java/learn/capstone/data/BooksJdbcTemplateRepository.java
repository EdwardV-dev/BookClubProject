package learn.capstone.data;

import learn.capstone.data.mappers.AuthorsMapper;
import learn.capstone.data.mappers.BookMapper;
import learn.capstone.models.Books;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BooksJdbcTemplateRepository implements BooksRepository {

    private final JdbcTemplate jdbcTemplate; //auto-injected, provided by Spring

    public BooksJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public boolean update(Books books) {

        final String sql = "update books set "
                + "approval_status = ?, "
                + "book_title = ?, "
                + "genre = ?, "
                + "publication_year = ? "
                + "where idBooks = ?;";

        return jdbcTemplate.update(sql,
                books.isApprovalStatus(),
                books.getBookTitle(),
                books.getGenre(),
                books.getYearPublished(),
                books.getIdBooks()) > 0;
    }

    @Override
    public List<Books> findAllForAdmin() {
        final String sql = "select * from books b Inner join authors au on b.idAuthor = au.idAuthor;";
        return jdbcTemplate.query(sql, new BookMapper()); //returns a list of books
    }


    //For testing out http requests in the test table
    public void setKnownGoodState(){
        jdbcTemplate.update("call set_known_good_state();");
    }

}
