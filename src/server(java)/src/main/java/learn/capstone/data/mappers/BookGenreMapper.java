package learn.capstone.data.mappers;

import learn.capstone.models.Books;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookGenreMapper implements RowMapper<Books> {

    @Override
    public Books mapRow(ResultSet resultSet, int i) throws SQLException {
        Books book = new Books();
        book.setGenre(resultSet.getString("genre"));

        return book;
    }

}
