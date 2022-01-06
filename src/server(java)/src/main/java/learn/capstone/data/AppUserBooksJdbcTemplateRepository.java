package learn.capstone.data;

import learn.capstone.models.AppUserBooks;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppUserBooksJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;


    public AppUserBooksJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean update(AppUserBooks appUserBooks) {
        final String sql = "update app_user_has_books set "
                + "completion_status = ? "
                + "where idBooks = ? and app_user_id = ?;";

        return jdbcTemplate.update(sql,
                appUserBooks.getCompletionStatus(),
                appUserBooks.getBook().getIdBooks(),
                appUserBooks.getAppUserId()) > 0;
    }

    public boolean delete(int userId, int bookId) {
        final String sql = "delete from app_user_has_books "
                + "where app_user_id = ? and idBooks = ?;";

        return jdbcTemplate.update(sql, userId, bookId) > 0;
    }
}
