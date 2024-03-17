package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private final Sql2o sql2o;

    private static final Logger LOG = LoggerFactory.getLogger(Sql2oTicketRepository.class.getName());

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> buy(Ticket ticket) {
        Optional<Ticket> rsl = Optional.empty();
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
        }
        return rsl;
    }

    @Override
    public boolean refund(Ticket ticket) {
        try(var connection = sql2o.open()) {
            var sql = """
                    DELETE FROM tickets
                    WHERE id = :id;
                    """;
            var query = connection.createQuery(sql);
            query.addParameter("id", ticket.getId());
            return query.executeUpdate().getResult() > 0;
        }
    }
}
