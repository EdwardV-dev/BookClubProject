package learn.capstone.domain;

import learn.capstone.data.AppUserBooksRepository;
import learn.capstone.data.BooksRepository;

public class AppUserBooksService {

    private final AppUserBooksRepository repository;

    public AppUserBooksService(AppUserBooksRepository repository) {
        this.repository = repository;
    }

}
