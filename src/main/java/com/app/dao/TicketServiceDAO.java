package com.app.dao;

import com.app.model.Ticket;
import com.app.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketServiceDAO {
    private final String url = "jdbc:postgresql://localhost:5432/myTicketServiceDB";
    private final String user = "postgres";
    private final String password = "Paroli123";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void saveUser(String name) throws SQLException {
        String SQL = "INSERT INTO public.User(name, creation_date) VALUES(?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, name);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();
        }
    }

    public void saveTicket(int userId, String ticketType) throws SQLException {
        String SQL = "INSERT INTO public.Ticket(user_id, ticket_type, creation_date) VALUES(?, ?::ticket_type, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, ticketType);
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();
        }
    }


    public User fetchUserById(int id) throws SQLException {
        String SQL = "SELECT * FROM public.User WHERE id = ?";
        User user = null;
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getInt("id"), rs.getString("name"), rs.getTimestamp("creation_date").toLocalDateTime());
            }
        }
        return user;
    }

    public List<Ticket> fetchTicketsByUserId(int userId) throws SQLException {
        String SQL = "SELECT * FROM public.Ticket WHERE user_id = ?";
        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket(rs.getInt("id"), rs.getInt("user_id"), rs.getString("ticket_type"), rs.getTimestamp("creation_date").toLocalDateTime());
                tickets.add(ticket);
            }
        }
        return tickets;
    }

    public void updateTicketType(int ticketId, String newType) throws SQLException {
        String SQL = "UPDATE public.Ticket SET ticket_type = ?::ticket_type WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, newType);
            pstmt.setInt(2, ticketId);
            pstmt.executeUpdate();
        }
    }

    public void deleteUserById(int userId) throws SQLException {
        String SQL1 = "DELETE FROM public.Ticket WHERE user_id = ?";
        String SQL2 = "DELETE FROM public.User WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt1 = conn.prepareStatement(SQL1);
             PreparedStatement pstmt2 = conn.prepareStatement(SQL2)) {
            pstmt1.setInt(1, userId);
            pstmt1.executeUpdate();
            pstmt2.setInt(1, userId);
            pstmt2.executeUpdate();
        }
    }

    public void updateUserAndTickets(int userId, String newUserName, int ticketId, String newTicketType) throws SQLException {
        String updateUserSQL = "UPDATE public.User SET name = ? WHERE id = ?";
        String updateTicketSQL = "UPDATE public.Ticket SET ticket_type = ?::ticket_type WHERE id = ?";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false);
            Savepoint savepoint1 = null;

            try (PreparedStatement updateUserPstmt = conn.prepareStatement(updateUserSQL);
                 PreparedStatement updateTicketPstmt = conn.prepareStatement(updateTicketSQL)) {

                updateUserPstmt.setString(1, newUserName);
                updateUserPstmt.setInt(2, userId);
                updateUserPstmt.executeUpdate();

                savepoint1 = conn.setSavepoint();

                updateTicketPstmt.setString(1, newTicketType);
                updateTicketPstmt.setInt(2, ticketId);
                updateTicketPstmt.executeUpdate();

                conn.commit();
            } catch (SQLException ex) {
                if (savepoint1 != null) {
                    conn.rollback(savepoint1);
                }
                conn.commit();
                throw ex;
            }
        }
    }
}

