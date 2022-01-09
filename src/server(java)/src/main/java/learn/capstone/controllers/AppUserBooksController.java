package learn.capstone.controllers;

import learn.capstone.domain.AppUserBooksService;
import learn.capstone.domain.Result;
import learn.capstone.models.AppUser;
import learn.capstone.models.AppUserBooks;
import learn.capstone.models.Books;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class AppUserBooksController {

    private final AppUserBooksService service;

    public AppUserBooksController(AppUserBooksService service) {
        this.service = service;
    }

    @GetMapping
    public List<Books> findAllUserBooks(@PathVariable int userId) {
        return service.findAllUserBooks(userId);
    }

    //username is set in the JSON request by utilizing the information from jwtdecode
    @GetMapping("/findUserId")
    public int findAppUserId(@RequestBody AppUser appUser) {
        return service.findAppUserId(appUser.getUsername());
    }

    //appUserId is supplied by authContext, which calls /books/findUserId upon starting the react application
    @GetMapping("/{appUserId}")
    public Books findBookViaMostReadGenre(@RequestBody AppUser appUser) {
        return service.findBookViaMostReadGenre(appUser.getAppUserId());
    }

    //JSON request should include appUserId from authContext, completionStatus from HTML field, and books object with
    //at least a book id field that's obtained from response.json of the first POST fetch to the books table
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody AppUserBooks appUserBook) {
        Result<AppUserBooks> result = service.add(appUserBook);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping()
    public ResponseEntity<Object> update(@RequestBody AppUserBooks appUserBooks) {
        //First, check that the item you wish to update is present. If not, a not found error will be built
        Result<AppUserBooks> result = service.update(appUserBooks);

        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);

    }
    // { appUserId: useContext value
    //   completionStatus: Html field
    //   Book : {
   //         "idBooks": useParams value
// }

//}

    @DeleteMapping()
    public ResponseEntity<Object> deleteById(@RequestBody AppUserBooks appUserBooks) {
        //First, check that the item you wish to delete is present. if not, a nof found error will be built
        Result<AppUserBooks> result = service.deleteByBookId(appUserBooks.getAppUserId(), appUserBooks.getBook().getIdBooks());

        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);

    }



}
