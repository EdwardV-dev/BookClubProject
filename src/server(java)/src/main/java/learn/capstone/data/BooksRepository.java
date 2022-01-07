package learn.capstone.data;

import learn.capstone.models.Books;

import java.util.List;

public interface BooksRepository {

    List<Books> findAllForAdmin();

//    Books add (Books books);
//
    boolean update(Books books);
//
//    boolean deleteById(int idBooks);

    void setKnownGoodState();
}