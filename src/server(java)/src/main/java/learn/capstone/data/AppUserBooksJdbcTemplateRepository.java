package learn.capstone.data;

import learn.capstone.data.mappers.AppUserBookWithCompletionStatusMapper;
import learn.capstone.data.mappers.BookGenreMapper;
import learn.capstone.data.mappers.BookIdMapper;
import learn.capstone.data.mappers.BookMapper;

import learn.capstone.models.Books;

import java.util.List;
import java.util.Random;
import java.util.Scanner;


import learn.capstone.models.AppUserBooks;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Null;

@Repository
public class AppUserBooksJdbcTemplateRepository implements AppUserBooksRepository {

    private final JdbcTemplate jdbcTemplate;


    public AppUserBooksJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//year directly displayed on ui
    @Override
    public List<Books> findAllUserBooks(int appUserId) {
        convertNullYearsTo6000ForBookFinds();
        final String sql = "Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, ab.completion_status, " +
                "au.author_first_name, au.author_last_name\n" +
                "from books b\n" +
                "inner join app_user_has_books ab on ab.idBooks = b.idBooks\n" +
                "inner join authors au on b.idAuthor = au.idAuthor\n" +
                "where ab.app_user_id = ?;";

        return jdbcTemplate.query(sql, new BookMapper(), appUserId); //returns a list of books
    }

    //Not relevant to displaying the year
    @Override
    public String findCompletionStatus(int appUserId, int bookId) {
        final String sql = "Select ab.completion_status from app_user_has_books ab " +
                "where ab.app_user_id = ? and ab.idBooks = ?;";

        return jdbcTemplate.queryForObject(sql, new AppUserBookWithCompletionStatusMapper(), appUserId, bookId); //returns String completion status
    }

    public void convertNullYearsTo6000ForBookFinds(){
        final String sql1 = "SET SQL_SAFE_UPDATES=0;";
        jdbcTemplate.execute(sql1);

        final String sql2 = "update books set publication_year = 6000 where publication_year is null;" ;
        jdbcTemplate.execute(sql2);

        final String sql3 = "SET SQL_SAFE_UPDATES=1;";
        jdbcTemplate.execute(sql3);
    }

    @Override
    public int findAppUserId(String username) {
        final String sql = "Select app_user_id " +
                "from app_user " +
                "where username = ?;";

        return jdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class); //returns a userId
    }

//{ appUserId: [value is grabbed from backend request--findappuseridFromUsername
// completionStatus: //
//    Book: {
    //idBooks: [value from useParams]
    //bookProperty2: [value from html input]
//  }
// }

        @Override
        public boolean update(AppUserBooks appUserBooks){
            final String sql = "update app_user_has_books set "
                    + "completion_status = ? "
                    + "where idBooks = ? and app_user_id = ?;";

            return jdbcTemplate.update(sql,
                    appUserBooks.getCompletionStatus(), //Grabbed from the html value
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



        //this method is called upon by the add method below. This method is necessary because we don't immediately
       //know if the book was added successfully to the books table

    //Helper method (not directly displayed on ui). Finds a unique book based on title, first name, and last name--combined.
        public Books findSpecificBookBasedOnTitleFirstNameAndLastName(String title, String firstName, String lastName){

        final String sql = "Select b.idBooks " +
                "from books b " +
                "Inner join authors au on au.idAuthor = b.idAuthor " +
                "where b.book_title = ? and au.author_first_name = ? and au.author_last_name = ?;";

            try {
                Books bookWithIdAttached = jdbcTemplate.queryForObject(sql, new BookIdMapper(), title, firstName, lastName); //returns a book
                return bookWithIdAttached;

                //if no object is returned from sql query, the catch block is triggered

                //For example, year 3000 and the book doesn't already exist in the books table.

                //if a completely new book was able to be added successfully to the books table in the first fetch,
                //the try block should work successfully.

                //If the newly-entered book is a duplicate that already exists, the try block should work
                //successfully

            } catch (EmptyResultDataAccessException ex) {
                return null;
            }

        }


    @Override
    public boolean add(AppUserBooks appUserBooks) {
        Scanner console = new Scanner(System.in);

        System.out.println(appUserBooks.getBook().getAuthor());
        System.out.println(appUserBooks.getBook().getAuthor().getAuthorFirstName());

        Books specificBookWithIdAttached = findSpecificBookBasedOnTitleFirstNameAndLastName(appUserBooks.getBook().getBookTitle(),
                appUserBooks.getBook().getAuthor().getAuthorFirstName(), appUserBooks.getBook().getAuthor().getAuthorLastName());

        final String sql = "insert into app_user_has_books (app_user_id, completion_status, idBooks) values "
                + "(?,?,?);";

        try {
            return jdbcTemplate.update(sql,
                    appUserBooks.getAppUserId(), //This is found by using jwtdecode to find their username and then querying sql
                    //by their username to find their id. Then we use their id in the http request.

                    appUserBooks.getCompletionStatus(), //This is set by extracting html value from react prior to http request

                    specificBookWithIdAttached.getIdBooks()) > 0; //Id is grabbed from calling findSpecificBookBasedOnTitle.
                                                                  //specificBook can return null if it's not found in the database
        }
        //This catch block is required if specificBookWithIdAttached is null
        catch (EmptyResultDataAccessException | NullPointerException ex){
            return false;
        }
    }


    //Year is directly displayed on ui
    public Books findMostReadGenre(int userId){
        convertNullYearsTo6000ForBookFinds();

        final String sql = "Select genre, COUNT(genre) as genreCount From books b\n" +
                "Inner join app_user_has_books ab on ab.idBooks = b.idbooks\n" +
                "Inner join app_user au on au.app_user_id = ab.app_user_id \n" +
                "Where au.app_user_id=? \n" +
                "GROUP BY genre\n" +
                "Order by genreCount desc\n" +
                "limit 1;";

        Books bookWithGenreAttached = jdbcTemplate.queryForObject(sql, new BookGenreMapper(), userId);

        return bookWithGenreAttached;
    }

     int previousColumnPick = 0;


    public Books findBookViaMostReadGenre(int userId) {
        Books bookWithGenreAttached = findMostReadGenre(userId);

        final String sqlCountRows = "Select Count(*) from books b where b.genre = ? and b.approval_status = true;";
        //Indicates how many table rows of books there are with a specific genre that's also approved

        int rowCount;

        try {
             rowCount = jdbcTemplate.queryForObject(sqlCountRows, new Object[]{bookWithGenreAttached.getGenre()}, Integer.class);
        } catch (NullPointerException ex) {
            return null;
        }

        Random random = new Random();

        int randomColumnPick;

        do {
//            System.out.println("try again");
            randomColumnPick = random.nextInt(rowCount) + 1;
        } while (randomColumnPick == previousColumnPick && rowCount > 1);

        final String sql = "Select * From\n" +
                "(Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, au.author_first_name, au.author_last_name,\n" +
                "ROW_NUMBER() OVER(Order by idBooks) as row_numbering\n" +
                "from books b\n" +
                "Inner join authors au \n" +
                "on b.idAuthor = au.idAuthor) as innerTable\n" +
                "where genre= ? and  row_numbering = ?;";


        previousColumnPick = randomColumnPick; //storing the columnPick in memory (will be retrieved for future method calls), i.e. columnPick is preserved after this method executes

       return jdbcTemplate.queryForObject(sql, new BookMapper(), bookWithGenreAttached.getGenre(), randomColumnPick);
    }
    }



    //Invalid year but existing book name: Book will be associated with the user's account
    //Invalid year and non-existent book name: Book will not be associated with user's account. They have to try again.
    //User enters a duplicate book. It gets rejected. However, the existing book is then associated with the user's account


//     appuserId: 1
//     completionStaus: "DoneReading"
//    book : {
//    genre : "fantasy"
//    bookTitle: "Harry Potter"
//    }
