package learn.capstone.domain;

import learn.capstone.data.AuthorsRepository;
import learn.capstone.models.Books;

import java.util.List;

public class AuthorsService {

    private final AuthorsRepository repository;

    public AuthorsService(AuthorsRepository repository) {
        this.repository = repository;
    }

    public List<Books> findAllBooksFromAuthorFirstOrLastName(String singleUserInput){
        return repository.findAllBooksFromAuthorFirstOrLastName(singleUserInput);
    }
}
