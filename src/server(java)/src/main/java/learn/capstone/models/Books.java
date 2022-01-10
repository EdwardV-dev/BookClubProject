package learn.capstone.models;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Books {

    private int currentYear = LocalDate.now().getYear();
    private int actualYear = currentYear;

    @PositiveOrZero(message = "id must be set for an update operation")
    private int idBooks;

    @NotNull
    private boolean approvalStatus;

    @NotBlank(message = "Book title is required.")
    private String bookTitle;

    @NotBlank(message = "Book genre is required.")
    @Pattern(regexp = "^[a-zA-z0-9]*$", message = "Invalid book genre.")
    private String genre;

    @NotNull(message = "Book author is required.")
    private Authors author;

    @Max(value = 2022, message = "Year published cannot be in the future")
    private int yearPublished;

    private List<BooksAppUser> users = new ArrayList<>();

    public Authors getAuthor() {
        return author;
    }

    public void setAuthor(Authors author) {
        this.author = author;
    }

    public int getIdBooks() {
        return idBooks;
    }

    public void setIdBooks(int idBooks) {
        this.idBooks = idBooks;
    }

    public boolean getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }
}
