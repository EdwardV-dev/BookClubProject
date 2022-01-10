package learn.capstone.models;

public class BooksAppUser {
private int idBooks;
private String completionStatus;
private AppUser appUser;

    public int getIdBooks() {
        return idBooks;
    }

    public void setIdBooks(int idBooks) {
        this.idBooks = idBooks;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
