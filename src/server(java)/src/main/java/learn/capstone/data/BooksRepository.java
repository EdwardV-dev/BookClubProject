package learn.capstone.data;

import learn.capstone.models.Books;

import java.util.List;

public interface BooksRepository {

    List<Books> findAllForAdmin();

    Books addToAuthorTableFirstThenBooks(Books book);

    boolean updateAdmin(Books books);

    Books findBookFromAuthorFirstAndLastNameAndBookTitle(String input1, String input2, String input3);

  Books addToBooksTable(Books book, int idAuthorForeignKey);

    void setKnownGoodState();

    Books findById(int bookId);
}