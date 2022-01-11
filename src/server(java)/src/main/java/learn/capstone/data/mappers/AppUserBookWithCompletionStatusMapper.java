package learn.capstone.data.mappers;

import learn.capstone.models.AppUser;
import learn.capstone.models.AppUserBooks;
import learn.capstone.models.Books;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppUserBookWithCompletionStatusMapper implements RowMapper<String> {

    @Override
    public String mapRow(ResultSet resultSet, int i) throws SQLException {

       return resultSet.getString("completion_status");

    }

}
