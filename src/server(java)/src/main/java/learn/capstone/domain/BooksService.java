package learn.capstone.domain;

import learn.capstone.data.BooksRepository;
import learn.capstone.models.Books;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class BooksService {

    private final BooksRepository repository;

    public BooksService(BooksRepository repository) {
        this.repository = repository;
    }

    public Result<Books> updateAdmin(Books books) {
        Result<Books> result;

        if (books.getYearPublished() == 6000){
            books.setYearPublished(0);
             result = validate(books);
            books.setYearPublished(6000);
        } else {
            result = validate(books);
        }


        if (!result.isSuccess()) {
            return result;
        }

        if (!repository.updateAdmin(books)) {
            result.addMessage(ResultType.NOT_FOUND, "book id `" + books.getIdBooks() + "` not found");
        }

        return result;
    }

    //used for domain testing
    public Books findBookFromAuthorFirstAndLastNameAndBookTitle(String input1, String input2, String input3){
        return repository.findBookFromAuthorFirstAndLastNameAndBookTitle(input1, input2, input3);
    }

    //used to fetch all books from the books table
    public List<Books> findAllForAdmin(){
        return repository.findAllForAdmin();
    }

    public void setKnowGoodState(){
         repository.setKnownGoodState();
    }

    public Result<Books> add(Books book){
        book.setIdBooks(0); //Ensures that annotation test will pass. Book id is not used for insertion in the repo layer sql

        Result<Books> result;

        if (book.getYearPublished() == 6000){
            book.setYearPublished(0);
            result = validate(book);
            book.setYearPublished(6000);
        } else {
            result = validate(book);
        }

        //describing an unsuccessful result
        if(!result.isSuccess()) {
            return result;
        }

     Books bookToBeAdded = repository.addToAuthorTableFirstThenBooks(book);
     result.setPayload(bookToBeAdded);
     return result;
    }

    public Books findBookByIdForAdmin(int bookId) {
        return repository.findById(bookId);
    }

    private Result<Books> validate(Books books) {
        Result<Books> result = new Result();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

            Set<ConstraintViolation<Books>> violations = validator.validate(books);
            //If there are domain errors that are not concerning duplicates....
            if(!violations.isEmpty()) {
                for (ConstraintViolation<Books> violation : violations) {
                    result.addMessage(ResultType.INVALID, violation.getMessage());
                    return result;
                }
            }

        Books specificBook = findBookFromAuthorFirstAndLastNameAndBookTitle(books.getAuthor().getAuthorFirstName(),
                books.getAuthor().getAuthorLastName(), books.getBookTitle());

       //If findBookFromAuthorFirstAndLastNameAndBookTitle returns the book title that we originally wanted to update,
       //do not add a duplicate books error message
        try {
            if (specificBook.getIdBooks() != books.getIdBooks() && specificBook != null) {
                result.addMessage(ResultType.INVALID, "Duplicate books are not allowed");
            }
        } catch (NullPointerException ex){
            return result; //null pointer means that the book doesn't already exist (e.g. adding a new book title) or that
                           //we're updating properties of the same book title. These are not considered duplicates
        }
        return result; //contains a list of all error messages



    }
}
