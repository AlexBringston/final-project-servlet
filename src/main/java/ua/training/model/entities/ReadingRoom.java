package ua.training.model.entities;


import java.util.Objects;

public class ReadingRoom {

    private Long id;
    private Book book;
    private User user;
    private Status status;

    public ReadingRoom() {
    }

    public ReadingRoom(Book book, User user, Status status) {
        this.book = book;
        this.user = user;
        this.status = status;
    }

    public ReadingRoom(Long id, Book book, User user, Status status) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReadingRoom)) return false;
        ReadingRoom that = (ReadingRoom) o;
        return getId().equals(that.getId()) && getBook().equals(that.getBook()) && getUser().equals(that.getUser()) && getStatus().equals(that.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBook(), getUser(), getStatus());
    }

    @Override
    public String toString() {
        return "ReadingRoom{" +
                "id=" + id +
                ", user=" + user +
                ", book=" + book +
                '}';
    }
}
