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
    @GetMapping("/{authorFirstName}/{appUserId}")
    public List<Books> findAll(@PathVariable String authorFirstName, @PathVariable int appUserId) {
        return service.findAllBooksFromAuthorFirstOrLastName(authorFirstName, appUserId);
    }

    @GetMapping("/setKnownGoodState")
    public void callSetKnownGoodState() {
        service.setKnowGoodState();
    }
}
