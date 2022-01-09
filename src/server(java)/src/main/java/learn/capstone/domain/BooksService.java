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
        Result<Books> result = validate(books);

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

        Result<Books> result = validate(book);

        //describing an unsuccessful result
        if(!result.isSuccess()) {
            return result;
        }

     Books bookToBeAdded = repository.addToAuthorTableFirstThenBooks(book);
     result.setPayload(bookToBeAdded);
     return result;
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

      if (findBookFromAuthorFirstAndLastNameAndBookTitle(books.getAuthor().getAuthorFirstName(),
              books.getAuthor().getAuthorLastName(), books.getBookTitle()) != null) {
          result.addMessage(ResultType.INVALID, "Duplicate books are not allowed");
      }

        return result; //contains a list of all error messages



    }
}
