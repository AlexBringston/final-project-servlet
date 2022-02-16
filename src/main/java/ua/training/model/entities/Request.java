package ua.training.model.entities;


import java.util.Objects;

public class Request {

    private Long id;
    private User user;
    private Book book;
    private Status status;

    public Request() {
    }

    public Request(User user, Book book, Status status) {
        this.user = user;
        this.book = book;
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
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return getId().equals(request.getId()) && getUser().equals(request.getUser()) && getBook().equals(request.getBook()) && getStatus().equals(request.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getBook(), getStatus());
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", user=" + user +
                ", book=" + book +
                ", status=" + status +
                '}';
    }
}
