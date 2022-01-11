package learn.capstone.controllers;

import learn.capstone.domain.AuthorsService;
import learn.capstone.models.Authors;
import learn.capstone.models.Books;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{authorFirstName}")
    public List<Books> findAll(@PathVariable String authorFirstName) {
        return service.findAllBooksFromAuthorFirstOrLastName(authorFirstName);
    }

    @GetMapping("/setKnownGoodState")
    public void callSetKnownGoodState() {
        service.setKnowGoodState();
    }
}
