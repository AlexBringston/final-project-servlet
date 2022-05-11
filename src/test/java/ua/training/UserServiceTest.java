package ua.training;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.training.model.dto.Page;
import ua.training.model.entities.Role;
import ua.training.model.entities.User;
import ua.training.model.services.UserService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private UserService userService;


    @Before
    public void setupMock() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        userService = new UserService();
    }

    @Test
    public void testLoadUserByUserName() throws SQLException {
        User testUser = getLoggedUser();

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("user_id")).thenReturn(1L);
        when(resultSet.getString("user_name")).thenReturn("John");
        when(resultSet.getString("surname")).thenReturn("Hopkins");
        when(resultSet.getString("username")).thenReturn("login");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getLong("role_id")).thenReturn(3L);
        when(resultSet.getString("role_name")).thenReturn("ROLE_READER");
        when(resultSet.getBoolean("is_account_non_blocked")).thenReturn(true);
        when(resultSet.getString("birth_date")).thenReturn("1990-12-12");
        User returnedUser = userService.findUserByUsername("login", connection);
        assertEquals("User is not the same as expected", testUser, returnedUser);
    }

    @Test
    public void testRegisterUser() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        boolean registerNewUser = userService.registerNewUser(getUser(), "ROLE_READER", connection);
        assertTrue("User was not created", registerNewUser);

    }

    @Test
    public void testFindUserById() throws SQLException {
        User testUser = getLoggedUser();
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("user_id")).thenReturn(1L);
        when(resultSet.getString("user_name")).thenReturn("John");
        when(resultSet.getString("surname")).thenReturn("Hopkins");
        when(resultSet.getString("username")).thenReturn("login");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getLong("role_id")).thenReturn(3L);
        when(resultSet.getString("role_name")).thenReturn("ROLE_READER");
        when(resultSet.getBoolean("is_account_non_blocked")).thenReturn(true);
        when(resultSet.getString("birth_date")).thenReturn("1990-12-12");
        User returnedUser = userService.findUserById(1L, connection);
        assertEquals("User is not the same as expected", testUser, returnedUser);
    }

    @Test
    public void testSetLibrarianRole() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        boolean registerNewUser = userService.registerNewUser(getUser(), "ROLE_LIBRARIAN", connection);
        assertTrue("Librarian user was not created", registerNewUser);
    }

    @Test
    public void testGetUsersOnPage() throws SQLException {
        Page<User> users = new Page.Builder<User>()
                .objects(new ArrayList<>(Arrays.asList(getLoggedUser())))
                .currentPage(0)
                .firstPage(0)
                .lastPage(0)
                .totalPages(0)
                .sortField("id")
                .sortDirection("ASC")
                .build();
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1);
        when(resultSet.getLong("user_id")).thenReturn(1L);
        when(resultSet.getString("user_name")).thenReturn("John");
        when(resultSet.getString("surname")).thenReturn("Hopkins");
        when(resultSet.getString("username")).thenReturn("login");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getLong("role_id")).thenReturn(3L);
        when(resultSet.getString("role_name")).thenReturn("ROLE_READER");
        when(resultSet.getBoolean("is_account_non_blocked")).thenReturn(true);
        when(resultSet.getString("birth_date")).thenReturn("1990-12-12");
        Page<User> returnedUsers = userService.getUsersByRolePerPage("ROLE_READER", 1, 0, "id", "ASC", connection);
        assertEquals("Users on the page are not those as expected", users, returnedUsers);
    }

    @Test
    public void testFindUsers() throws SQLException {
        Page<User> users = new Page.Builder<User>()
                .objects(new ArrayList<>(Arrays.asList(getLoggedUser())))
                .currentPage(0)
                .firstPage(0)
                .lastPage(0)
                .totalPages(0)
                .sortField("id")
                .sortDirection("ASC")
                .build();
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1);
        when(resultSet.getLong("user_id")).thenReturn(1L);
        when(resultSet.getString("user_name")).thenReturn("John");
        when(resultSet.getString("surname")).thenReturn("Hopkins");
        when(resultSet.getString("username")).thenReturn("login");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getLong("role_id")).thenReturn(3L);
        when(resultSet.getString("role_name")).thenReturn("ROLE_READER");
        when(resultSet.getBoolean("is_account_non_blocked")).thenReturn(true);
        when(resultSet.getString("birth_date")).thenReturn("1990-12-12");
        List<Integer> ages = new ArrayList<>(Collections.singletonList(31));
        List<Integer> numberOfBooksTaken = new ArrayList<>(Collections.singletonList(0));
        Page<User> userPage = userService.getUsersByRolePerPage("ROLE_READER", 1,
                0, "id", "ASC", connection);
        assertEquals("Users are not those as expected", users, userPage);
        assertEquals("Ages are not as expected", ages, userService.findUsersAges(userPage.getObjects()));
        assertEquals("Numbers are not as expected", numberOfBooksTaken,
                userService.countBooksInUsersAbonnements(userPage.getObjects(), connection));
    }

    private User getLoggedUser() {
        return new User(1L, "John", "Hopkins", "login",
                "password", true,
                LocalDate.of(1990, 12, 12), new Role(3L, "ROLE_READER"));
    }

    private User getUser() {
        return new User(1L, "John", "Hopkins", "login",
                "password", true,
                LocalDate.of(1990, 12, 12));
    }
}
