package ua.training.model.entities;

import java.time.LocalDate;
import java.util.Objects;

public class User {

    private Long id;
    private String name;
    private String surname;
    private String username;
    private String password;

    private boolean isAccountNonLocked = true;

    private LocalDate birthDate;

    private Role role;

    public User() {
    }

    public User(String name, String surname, String username, String password,
                LocalDate birthDate) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
    }

    public User(Long id, String name, String surname, String username, String password, boolean isAccountNonLocked,
                LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.isAccountNonLocked = isAccountNonLocked;
        this.birthDate = birthDate;
    }

    public User(Long id, String name, String surname, String username, String password, boolean isAccountNonLocked, LocalDate birthDate, Role role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.isAccountNonLocked = isAccountNonLocked;
        this.birthDate = birthDate;
        this.role = role;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return isAccountNonLocked() == user.isAccountNonLocked() && Objects.equals(getId(), user.getId()) && getName().equals(user.getName()) && getSurname().equals(user.getSurname()) && getUsername().equals(user.getUsername()) && getPassword().equals(user.getPassword()) && getBirthDate().equals(user.getBirthDate()) && getRole().equals(user.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSurname(), getUsername(), getPassword(), isAccountNonLocked(), getBirthDate(), getRole());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", birthDate=" + birthDate +
                ", role=" + role +
                '}';
    }
}



