package learn.capstone.data.mappers;

import learn.capstone.models.Authors;
import learn.capstone.models.Books;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorsMapper implements RowMapper<Authors> {


    @Override
    public Authors mapRow(ResultSet resultSet, int i) throws SQLException {
        Authors author = new Authors();

        author.setIdAuthor(resultSet.getInt("idAuthor"));
        author.setAuthorFirstName(resultSet.getString("author_first_name"));
        author.setAuthorLastName(resultSet.getString("author_last_name"));
        
        return author;
    }
}
