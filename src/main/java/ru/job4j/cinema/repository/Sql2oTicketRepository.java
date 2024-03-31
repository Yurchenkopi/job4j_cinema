package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private final Sql2o sql2o;

    private static final Logger LOG = LoggerFactory.getLogger(Sql2oTicketRepository.class.getName());

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> buy(Ticket ticket) throws Exception {
        Optional<Ticket> rsl;
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO tickets (session_id, row_number, place_number, user_id)
                    VALUES
                    (:sessionId, :rowNumber, :placeNumber, :userId);
                    """;
            var query = connection.createQuery(sql, true);
            query.addParameter("sessionId", ticket.getSessionId())
                    .addParameter("rowNumber", ticket.getRowNumber())
                    .addParameter("placeNumber", ticket.getPlaceNumber())
                    .addParameter("userId", ticket.getUserId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedId);
            rsl = Optional.of(ticket);
        } catch (Exception e) {
            LOG.error("Выбранное место уже занято.", e);
            throw new Exception("Не удалось купить билет. Место уже занято.");
        }
        return rsl;
    }

    @Override
    public Ticket getById(int id) {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM tickets
                    WHERE id = :id;
                    """;
            var query = connection.createQuery(sql);
            query.addParameter("id", id);
            return query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetchFirst(Ticket.class);
        }
    }

    @Override
    public Collection<Ticket> findByUser(int userId) {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM tickets
                    WHERE user_id = :userId;
                    """;
            var query = connection.createQuery(sql);
            query.addParameter("userId", userId);
            return query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetch(Ticket.class);
        }
    }

    @Override
    public Collection<Ticket> findAll() {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM tickets;
                    """;
            var query = connection.createQuery(sql);
            return query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetch(Ticket.class);
        }
    }

    @Override
    public boolean refund(Ticket ticket) {
        try (var connection = sql2o.open()) {
            var sql = """
                    DELETE FROM tickets
                    WHERE id = :id;
                    """;
            var query = connection.createQuery(sql);
            query.addParameter("id", ticket.getId());
            var affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }
}
