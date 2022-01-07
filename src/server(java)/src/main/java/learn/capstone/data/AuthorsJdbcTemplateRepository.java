package learn.capstone.data;

import learn.capstone.data.mappers.BookMapper;
import learn.capstone.models.Books;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorsJdbcTemplateRepository implements AuthorsRepository{

    private final JdbcTemplate jdbcTemplate; //auto-injected, provided by Spring

    public AuthorsJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //The following method is for the author search bar in react
    @Override
    public List<Books> findAllBooksFromAuthorFirstOrLastName(String input) {
        final String sql = "Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor\n" +
                "from books b\n" +
                "Inner join authors au \n" +
                "on b.idAuthor = au.idAuthor\n" +
                "where author_first_name = ? or author_last_name = ?;";
        return jdbcTemplate.query(sql, new BookMapper(), input, input);
    }



    //Used for domain layer validation. Prevents user adding the same book from the same author. If this method returns
    //a list of size 0 (i.e. no books found), go ahead and add it to SQL database.
    public List<Books> findAllBooksFromAuthorFirstAndLastName(String input1, String input2) {
        final String sql = "Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor\n" +
                "from books b\n" +
                "Inner join authors au \n" +
                "on b.idAuthor = au.idAuthor\n" +
                "where author_first_name = ? and author_last_name = ?;";
        return jdbcTemplate.query(sql, new BookMapper(), input1, input2);
    }

    public void setKnownGoodState(){
        jdbcTemplate.update("call set_known_good_state();");
    }
}
