package ua.training.model.dao.mappers;

import ua.training.model.entities.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AbonnementMapper implements ObjectMapper<Abonnement> {
    @Override
    public Abonnement extractFromResultSet(ResultSet resultSet) throws SQLException {
        Abonnement abonnement = new Abonnement();
        abonnement.setId(resultSet.getLong("abonnement_id"));
        User user = new UserMapper().extractFromResultSet(resultSet);
        abonnement.setUser(user);

        Status status = new StatusMapper().extractFromResultSet(resultSet);
        abonnement.setStatus(status);

        abonnement.setPenalty(resultSet.getDouble("penalty"));
        abonnement.setReturnDate(resultSet.getDate("return_date").toLocalDate());

        Book book = new BookMapper().extractFromResultSet(resultSet);

        abonnement.setBook(book);
        return abonnement;
    }
}
