package learn.capstone.data.mappers;

import learn.capstone.models.Books;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookIdMapper implements RowMapper<Books> {
    int bookId;

    @Override
    public Books mapRow(ResultSet resultSet, int i) throws SQLException {
        Books book = new Books();
        book.setIdBooks(resultSet.getInt("idBooks"));

        return book;
    }
}
