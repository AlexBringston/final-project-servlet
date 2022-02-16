package ua.training.model.entities;


import java.time.LocalDate;
import java.util.Objects;

public class Abonnement {

    private Long id;
    private User user;
    private Book book;
    private Status status;
    private Double penalty;
    private LocalDate returnDate;

    public Abonnement() {
    }

    public Abonnement(Long id, User user, Book book, Status status, Double penalty, LocalDate returnDate) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.status = status;
        this.penalty = penalty;
        this.returnDate = returnDate;
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

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Abonnement)) return false;
        Abonnement that = (Abonnement) o;
        return getUser().equals(that.getUser()) && getBook().equals(that.getBook()) &&
                getStatus().equals(that.getStatus()) && getPenalty().equals(that.getPenalty()) &&
                getReturnDate().equals(that.getReturnDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getBook(), getStatus(), getPenalty(), getReturnDate());
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "id=" + id +
                ", user=" + user +
                ", book=" + book +
                ", status=" + status +
                ", penalty=" + penalty +
                ", returnDate=" + returnDate +
                '}';
    }
}
