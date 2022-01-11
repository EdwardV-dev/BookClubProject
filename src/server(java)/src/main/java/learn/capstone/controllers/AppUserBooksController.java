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

    @GetMapping("/userId")
    public List<Books> findAllUserBooks(@RequestParam int userId) {
        return service.findAllUserBooks(userId);
    }

    //username is set in the JSON request url by utilizing the information from jwtdecode
    @GetMapping("/userName")
    public int findAppUserId(@RequestParam String userName) {
        return service.findAppUserId(userName);
    }

    //appUserId is supplied by authContext, which calls /books/findUserId upon starting the react application
    @GetMapping("/userIdRecommended")
    public Books findBookViaMostReadGenre(@RequestParam int userIdRecommended) {
        return service.findBookViaMostReadGenre(userIdRecommended);
    }

    @GetMapping("/completionStatusFinder")
    public String findCompletionStatus(@RequestParam int userId, @RequestParam int bookId) {
        return service.findCompletionStatus(userId, bookId);
    }

    //JSON request should include appUserId from authContext, completionStatus from HTML field, and books object with
    //at least a book id field that's obtained from response.json of the first POST fetch to the books table
    @PostMapping()
    public ResponseEntity<Object> add(@RequestBody AppUserBooks appUserBook) {
        Result<AppUserBooks> result = service.add(appUserBook);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Object> update(@RequestBody AppUserBooks appUserBooks, @PathVariable int bookId) {
        //First, check that the item you wish to update in request body is the same as the one you sent in via url

        if (bookId != appUserBooks.getBook().getIdBooks()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }


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

    @DeleteMapping("/{appUserId}/{idBooks}")
    public ResponseEntity<Object> deleteById(@PathVariable int appUserId, @PathVariable int idBooks) {
        //First, check that the item you wish to delete is present. if not, a not found error will be built
        Result<AppUserBooks> result = service.deleteByBookId(appUserId, idBooks);

        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);

    }



}
