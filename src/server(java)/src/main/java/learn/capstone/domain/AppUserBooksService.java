package learn.capstone.domain;

import learn.capstone.data.AppUserBooksRepository;
import learn.capstone.models.AppUserBooks;
import learn.capstone.models.Books;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class AppUserBooksService {

    private final AppUserBooksRepository repository;

    public AppUserBooksService(AppUserBooksRepository repository) {
        this.repository = repository;
    }

    //Get request uses this method
    public List<Books> findAllUserBooks(int appUserId){
        return repository.findAllUserBooks(appUserId);
    }

    public Result<AppUserBooks> add(AppUserBooks appUserBooks) {
        Result<AppUserBooks> result = validate(appUserBooks);

        if(!result.isSuccess()) {
            return result;
        }

        //HTTP request might not include the book id; therefore, a bridge table association is not possible
        if(!repository.add(appUserBooks)) {
            result.addMessage(ResultType.NOT_FOUND, "Book ID " + appUserBooks.getBook().getIdBooks() + " not found.");
        }

        return result;
    }

    public Result<AppUserBooks> update(AppUserBooks appUserBooks) {

        Result<AppUserBooks> result = validate(appUserBooks);

        if(!result.isSuccess()) {
            return result;
        }

        if(!repository.update(appUserBooks)) {
            result.addMessage(ResultType.NOT_FOUND, "Book ID " + appUserBooks.getBook().getIdBooks() + " not found.");
        }

        return result;
    }

    public Result<AppUserBooks> deleteByBookId(int userId, int bookId) {
        Result<AppUserBooks> result = new Result<>();

        if(!repository.delete(userId, bookId)) {
            result.addMessage(ResultType.NOT_FOUND, "Book ID " + bookId + " not found.");
        }
        return result;
    }


    private Result<AppUserBooks> validate(AppUserBooks appUserBooks) {
        Result<AppUserBooks> result = new Result();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<AppUserBooks>> violations = validator.validate(appUserBooks);

        if(!violations.isEmpty()) {
            for (ConstraintViolation<AppUserBooks> violation : violations) {
                result.addMessage(ResultType.INVALID, violation.getMessage());
            }
            return result;
        }

        return result;
    }
}
