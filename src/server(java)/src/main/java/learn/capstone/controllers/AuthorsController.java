package learn.capstone.controllers;

import learn.capstone.domain.AuthorsService;
import learn.capstone.models.Authors;
import learn.capstone.models.Books;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@RequestMapping("/authorBooks")
public class AuthorsController {
    private final AuthorsService service;


    public AuthorsController(AuthorsService service) {
        this.service = service;
    }

    //JSON request will activate the setter for the authorFirstName. However, user might be trying to search for
    //their last name instead.
    @GetMapping
    public List<Books> findAll(@RequestBody Authors author) {
        return service.findAllBooksFromAuthorFirstOrLastName(author.getAuthorFirstName());
    }

    @GetMapping("/setKnownGoodState")
    public void callSetKnownGoodState() {
        service.setKnowGoodState();
    }
}
