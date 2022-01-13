package learn.capstone.data;

import learn.capstone.models.Books;

import java.util.List;

public interface AuthorsRepository {
    List<Books> findAllBooksFromAuthorFirstOrLastName(String input, int userId);

    void setKnownGoodState();
}
