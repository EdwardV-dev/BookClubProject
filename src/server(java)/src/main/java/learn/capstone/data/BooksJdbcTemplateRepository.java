package learn.capstone.data;

import learn.capstone.models.Books;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BooksJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

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

}
