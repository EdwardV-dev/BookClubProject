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

    @Override
    public List<Books> findAll() {
        final String sql = "select * from books limit 1000;";
        return jdbcTemplate.query(sql, new BookMapper());
    }


    //For testing out http requests in the test table
    public void setKnownGoodState(){
        jdbcTemplate.update("call set_known_good_state();");
    }


}
