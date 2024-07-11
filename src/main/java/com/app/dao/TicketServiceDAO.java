package com.app.dao;

import com.app.model.Ticket;
import com.app.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
public class TicketServiceDAO {
    private final JdbcTemplate jdbcTemplate;
    private final boolean updateEnabled;

    public TicketServiceDAO(DataSource dataSource, @Value("${update.enabled}") boolean updateEnabled) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.updateEnabled = updateEnabled;
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

    @Transactional
    public void updateUserAndCreateTicket(int userId, String newUserName, String ticketType) {
        if (!updateEnabled) {
            throw new IllegalStateException("Update and create ticket feature is disabled.");
        }

        String updateUserSQL = "UPDATE public.User SET name = ? WHERE id = ?";
        String createTicketSQL = "INSERT INTO public.Ticket(user_id, ticket_type, creation_date) VALUES(?, ?::ticket_type, ?)";

        jdbcTemplate.update(updateUserSQL, newUserName, userId);
        jdbcTemplate.update(createTicketSQL, userId, ticketType, Timestamp.valueOf(LocalDateTime.now()));
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("name"), rs.getTimestamp("creation_date").toLocalDateTime());
    }

    private RowMapper<Ticket> ticketRowMapper() {
        return (rs, rowNum) -> new Ticket(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("ticket_type"),
                rs.getString("ticket_class"),
                rs.getTimestamp("creation_date").toLocalDateTime(),
                rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                rs.getInt("price")
        );
    }

    public List<Ticket> loadTicketsFromFile(ResourceLoader resourceLoader, String filePath) throws IOException {
        Resource resource = resourceLoader.getResource(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return Arrays.asList(objectMapper.readValue(resource.getInputStream(), Ticket[].class));
    }
}