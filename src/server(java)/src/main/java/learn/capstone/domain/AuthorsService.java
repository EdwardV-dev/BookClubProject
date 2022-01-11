package learn.capstone.domain;

import learn.capstone.data.AuthorsRepository;
import learn.capstone.models.Books;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorsService {

    private final AuthorsRepository repository;

    public AuthorsService(AuthorsRepository repository) {
        this.repository = repository;
    }

    public List<Books> findAllBooksFromAuthorFirstOrLastName(String nameInput, int userId){
        return repository.findAllBooksFromAuthorFirstOrLastName(nameInput, userId);
    }

    public void setKnowGoodState(){
        repository.setKnownGoodState();
    }
}
