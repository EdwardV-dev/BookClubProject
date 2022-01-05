package learn.capstone.data.mappers;

import learn.capstone.models.Books;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Books> {

    @Override
    public Books mapRow(ResultSet resultSet, int i) throws SQLException {
     Books book = new Books();
     book.setIdBooks(resultSet.getInt("idBooks"));
     book.setBookTitle(resultSet.getString("book_title"));
     book.setApprovalStatus(resultSet.getBoolean("approval_status"));
     book.setGenre(resultSet.getString("genre"));
     book.setYearPublished(resultSet.getInt("publication_year"));
     book.setIdAuthor(resultSet.getInt("idAuthor"));

     return book;
    }
}
