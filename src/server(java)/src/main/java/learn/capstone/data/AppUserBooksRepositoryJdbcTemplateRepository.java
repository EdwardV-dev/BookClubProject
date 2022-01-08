package learn.capstone.data;

import learn.capstone.data.mappers.BookIdMapper;
import learn.capstone.data.mappers.BookMapper;

import learn.capstone.models.Books;

import java.util.List;


import learn.capstone.models.AppUserBooks;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppUserBooksRepositoryJdbcTemplateRepository implements AppUserBooksRepository {

    private final JdbcTemplate jdbcTemplate;


    public AppUserBooksRepositoryJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Books> findAllUserBooks(int appUserId) {
        final String sql = "Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, ab.completion_status, " +
                "au.author_first_name, au.author_last_name\n" +
                "from books b\n" +
                "inner join app_user_has_books ab on ab.idBooks = b.idBooks\n" +
                "inner join authors au on b.idAuthor = au.idAuthor\n" +
                "where ab.app_user_id = ?;";

        return jdbcTemplate.query(sql, new BookMapper(), appUserId); //returns a list of books
    }


        @Override
        public boolean update(AppUserBooks appUserBooks){
            final String sql = "update app_user_has_books set "
                    + "completion_status = ? "
                    + "where idBooks = ? and app_user_id = ?;";

            return jdbcTemplate.update(sql,
                    appUserBooks.getCompletionStatus(),
                    appUserBooks.getBook().getIdBooks(), //useParams is used to fetch the id of the book, which is
                                                         //then used to set the id of the book in the put request
                    appUserBooks.getAppUserId()) > 0;
        }

        @Override
        public boolean delete(int userId, int bookId){
            final String sql = "delete from app_user_has_books "
                    + "where app_user_id = ? and idBooks = ?;";

            return jdbcTemplate.update(sql, userId, bookId) > 0;

        }

        //this method calls the add method below
        public Books findSpecificBookBasedOnTitle(String title){
        final String sql = "Select b.idBooks " +
                "from books b " +
                "where b.book_title = ?;";

            try {
                Books bookWithIdAttached = jdbcTemplate.queryForObject(sql, new BookIdMapper(), title); //returns a list of books
                return bookWithIdAttached;

                //if no object is returned from sql query, the catch block is triggered

                //For example, year 3000 and the book doesn't already exist in the books table.

                //if a completely new book was able to be added successfully to the books table in the first fetch,
                //the try block should work successfully.

                //If the newly-entered book is a duplicate that already exists, the try block should work
                //successfully

            } catch (Exception ex) {
                return null;
            }

        }


    @Override
    public boolean add(AppUserBooks appUserBooks) {

        Books specificBookWithIdAttached = findSpecificBookBasedOnTitle(appUserBooks.getBook().getBookTitle());

        final String sql = "insert into app_user_has_books (app_user_id, completion_status, idBooks) values "
                + "(?,?,?);";

        try {
            return jdbcTemplate.update(sql,
                    appUserBooks.getAppUserId(), //This is found by using jwtdecode to find their username and then querying sql
                    //by their username to find their id. Then we use their id in the http request.

                    appUserBooks.getCompletionStatus(), //This is set by extracting html value from react prior to http request

                    specificBookWithIdAttached.getIdBooks()) > 0; //Get the book id from the successful response.json
        } catch (Exception ex) {
            return false;
        }
    }
    }

    //Invalid year but existing book name: Book will be associated with the user's account
    //Invalid year and non-existent book name: Book will not be associated with user's account. They have to try again.
    //User enters a duplicate book. It gets rejected. However, the existing book is then associated with the user's account
   //

//     appuserId: 1
//     completionStaus: "DoneReading"
//    book : {
//    genre : "fantasy"
//    bookTitle: "Harry Potter"
//    }
