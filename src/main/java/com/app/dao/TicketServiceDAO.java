package com.app.dao;

import com.app.model.Ticket;
import com.app.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TicketServiceDAO {
    private final JdbcTemplate jdbcTemplate;

    public TicketServiceDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveUser(String name) {
        String SQL = "INSERT INTO public.User(name, creation_date) VALUES(?, ?)";
        jdbcTemplate.update(SQL, name, Timestamp.valueOf(LocalDateTime.now()));
    }

    public void saveTicket(int userId, String ticketType) {
        String SQL = "INSERT INTO public.Ticket(user_id, ticket_type, creation_date) VALUES(?, ?::ticket_type, ?)";
        jdbcTemplate.update(SQL, userId, ticketType, Timestamp.valueOf(LocalDateTime.now()));
    }

    public User fetchUserById(int id) {
        String SQL = "SELECT * FROM public.User WHERE id = ?";
        List<User> users = jdbcTemplate.query(SQL, new Object[]{id}, userRowMapper());
        return users.isEmpty() ? null : users.get(0);
    }

    public List<Ticket> fetchTicketsByUserId(int userId) {
        String SQL = "SELECT * FROM public.Ticket WHERE user_id = ?";
        return jdbcTemplate.query(SQL, new Object[]{userId}, ticketRowMapper());
    }

    public void updateTicketType(int ticketId, String newType) {
        String SQL = "UPDATE public.Ticket SET ticket_type = ?::ticket_type WHERE id = ?";
        jdbcTemplate.update(SQL, newType, ticketId);
    }

    public void deleteUserById(int userId) {
        String SQL1 = "DELETE FROM public.Ticket WHERE user_id = ?";
        String SQL2 = "DELETE FROM public.User WHERE id = ?";
        jdbcTemplate.update(SQL1, userId);
        jdbcTemplate.update(SQL2, userId);
    }

    public void updateUserAndTickets(int userId, String newUserName, int ticketId, String newTicketType) {
        String updateUserSQL = "UPDATE public.User SET name = ? WHERE id = ?";
        String updateTicketSQL = "UPDATE public.Ticket SET ticket_type = ?::ticket_type WHERE id = ?";

        jdbcTemplate.update(updateUserSQL, newUserName, userId);
        jdbcTemplate.update(updateTicketSQL, newTicketType, ticketId);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("name"), rs.getTimestamp("creation_date").toLocalDateTime());
    }

    private RowMapper<Ticket> ticketRowMapper() {
        return (rs, rowNum) -> new Ticket(rs.getInt("id"), rs.getInt("user_id"), rs.getString("ticket_type"), rs.getTimestamp("creation_date").toLocalDateTime());
    }
}