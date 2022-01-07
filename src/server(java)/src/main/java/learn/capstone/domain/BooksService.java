package learn.capstone.domain;

import learn.capstone.data.BooksRepository;
import learn.capstone.models.Books;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
public class BooksService {

    private final BooksRepository repository;

    public BooksService(BooksRepository repository) {
        this.repository = repository;
    }

    public Result<Books> updateAdmin(Books books) {
        Result<Books> result = validate(books);

        if(!result.isSuccess()) {
            return result;
        }

        if(!repository.update(books)) {
            result.addMessage(ResultType.NOT_FOUND, "book id `" + books.getIdBooks() + "` not found");
        }

        return result;
    }

    private Result<Books> validate(Books books) {
        Result<Books> result = new Result();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Books>> violations = validator.validate(books);

        if(!violations.isEmpty()) {
            for (ConstraintViolation<Books> violation : violations) {
                result.addMessage(ResultType.INVALID, violation.getMessage());
            }
            return result;
        }
        return result;
    }
}
