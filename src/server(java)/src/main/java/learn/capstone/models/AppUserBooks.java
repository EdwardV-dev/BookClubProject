package learn.capstone.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AppUserBooks {
    @NotNull(message = "app user id cannot be null")
    private int appUserId;

    @NotNull
    @Pattern(regexp = "(Reading)|(WantToRead)|(DoneReading)", message = "Invalid completion status.")
    private String completionStatus;

    @NotNull(message = "book cannot be null")
    private Books book;


    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public Books getBook() {
        return book;
    }

    public void setBook(Books book) {
        this.book = book;
    }
}
