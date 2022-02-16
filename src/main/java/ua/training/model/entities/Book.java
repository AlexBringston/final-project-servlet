package ua.training.model.entities;


import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class Book {

    private Long id;
    private String name;
    private boolean onlyForReadingHall;
    private boolean isAvailable;
    private Set<Author> authors;
    private Publisher publisher;
    private Integer quantity;
    private LocalDate publishedAt;
    private String imgUrl;
    private Author mainAuthor;
    private Integer amountOfBooksTaken;

    public Book() {

    }
    public Book(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.onlyForReadingHall = builder.onlyForReadingHall;
        this.isAvailable = builder.isAvailable;
        this.authors = builder.authors;
        this.publisher = builder.publisher;
        this.quantity = builder.quantity;
        this.publishedAt = builder.publishedAt;
        this.imgUrl = builder.imgUrl;
        this.mainAuthor = builder.mainAuthor;
        this.amountOfBooksTaken = builder.amountOfBooksTaken;
    }

    public static class Builder{
        private Long id;
        private String name;
        private boolean onlyForReadingHall;
        private boolean isAvailable;
        private Set<Author> authors;
        private Publisher publisher;
        private Integer quantity;
        private LocalDate publishedAt;
        private String imgUrl;
        private Author mainAuthor;
        private Integer amountOfBooksTaken;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder onlyForReadingHall(boolean onlyForReadingHall) {
            this.onlyForReadingHall = onlyForReadingHall;
            return this;
        }

        public Builder available(boolean available) {
            isAvailable = available;
            return this;
        }

        public Builder authors(Set<Author> authors) {
            this.authors = authors;
            return this;
        }

        public Builder publisher(Publisher publisher) {
            this.publisher = publisher;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder publishedAt(LocalDate publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public Builder imgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public Builder mainAuthor(Author mainAuthor) {
            this.mainAuthor = mainAuthor;
            return this;
        }

        public Builder amountOfBooksTaken(Integer amountOfBooksTaken) {
            this.amountOfBooksTaken = amountOfBooksTaken;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnlyForReadingHall() {
        return onlyForReadingHall;
    }

    public void setOnlyForReadingHall(boolean onlyForReadingHall) {
        this.onlyForReadingHall = onlyForReadingHall;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDate publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Author getMainAuthor() {
        return mainAuthor;
    }

    public void setMainAuthor(Author mainAuthor) {
        this.mainAuthor = mainAuthor;
    }

    public Integer getAmountOfBooksTaken() {
        return amountOfBooksTaken;
    }

    public void setAmountOfBooksTaken(Integer amountOfBooksTaken) {
        this.amountOfBooksTaken = amountOfBooksTaken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return isOnlyForReadingHall() == book.isOnlyForReadingHall()
                && isAvailable() == book.isAvailable()
                && getId().equals(book.getId())
                && getName().equals(book.getName())
                && Objects.equals(getAuthors(), book.getAuthors())
                && getPublisher().equals(book.getPublisher())
                && getQuantity().equals(book.getQuantity())
                && getPublishedAt().equals(book.getPublishedAt())
                && getImgUrl().equals(book.getImgUrl())
                && getMainAuthor().equals(book.getMainAuthor())
                && getAmountOfBooksTaken().equals(book.getAmountOfBooksTaken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), isOnlyForReadingHall(), isAvailable(),
                getAuthors(), getPublisher(), getQuantity(), getPublishedAt(),
                getImgUrl(), getMainAuthor(), getAmountOfBooksTaken());
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", onlyForReadingHall=" + onlyForReadingHall +
                ", isAvailable=" + isAvailable +
                ", authors=" + authors +
                ", publisher=" + publisher +
                ", quantity=" + quantity +
                ", publishedAt=" + publishedAt +
                ", imgUrl='" + imgUrl + '\'' +
                ", mainAuthor=" + mainAuthor +
                ", amountOfBooksTaken=" + amountOfBooksTaken +
                '}';
    }
}
