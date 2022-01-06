package learn.capstone.data;

import learn.capstone.data.mappers.BookMapper;

import learn.capstone.models.Books;

import java.util.List;


import learn.capstone.models.AppUserBooks;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppUserBooksJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;


    public AppUserBooksJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Books> findAllUserBooks(int appUserId) {
        final String sql = "Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, ab.completion_status\n" +
                "from books b\n" +
                "inner join app_user_has_books ab on ab.idBooks = b.idBooks\n" +
                "where au.app_user_id = ?;";

        return jdbcTemplate.query(sql, new BookMapper()); //returns a list of books
    }

        public boolean update (AppUserBooks appUserBooks){
            final String sql = "update app_user_has_books set "
                    + "completion_status = ? "
                    + "where idBooks = ? and app_user_id = ?;";

            return jdbcTemplate.update(sql,
                    appUserBooks.getCompletionStatus(),
                    appUserBooks.getBook().getIdBooks(),
                    appUserBooks.getAppUserId()) > 0;
        }

        public boolean delete ( int userId, int bookId){
            final String sql = "delete from app_user_has_books "
                    + "where app_user_id = ? and idBooks = ?;";

            return jdbcTemplate.update(sql, userId, bookId) > 0;

        }
    }
