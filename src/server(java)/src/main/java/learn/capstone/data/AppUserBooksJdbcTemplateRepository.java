package learn.capstone.data;

import learn.capstone.data.mappers.BookMapper;
import learn.capstone.models.AppUser;
import learn.capstone.models.Books;

import java.util.List;

public class AppUserBooksJdbcTemplateRepository {

    public List<Books> findAllUserBooks(int appUserId){
        final String sql = "Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, ab.completion_status\n" +
        "from books b\n" +
        "inner join app_user_has_books ab on ab.idBooks = b.idBooks\n" +
        "where au.app_user_id = ?;";

        return jdbcTemplate.query(sql, new BookMapper()); //returns a list of books
    }
}
