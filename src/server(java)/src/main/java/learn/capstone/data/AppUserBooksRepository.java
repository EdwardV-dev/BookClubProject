package learn.capstone.data;

import learn.capstone.models.Books;

import java.util.List;

public interface AppUserBooksRepository {
    List<Books> findAllUserBooks(int appUserId);

    boolean update(learn.capstone.models.AppUserBooks appUserBooks);

    boolean delete(int userId, int bookId);

    boolean add(learn.capstone.models.AppUserBooks appUserBooks);

    int findAppUserId (String username);

    Books findBookViaMostReadGenre(int userId);

    String findCompletionStatus(int appUserId, int bookId);
}
