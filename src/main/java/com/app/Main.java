package com.app;

import com.app.dao.TicketServiceDAO;
import com.app.model.Ticket;
import com.app.model.User;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        TicketServiceDAO dao = new TicketServiceDAO();
        try {
            dao.saveUser("John Doe");
            User user = dao.fetchUserById(1);
            System.out.println(user);

            dao.saveTicket(1, "DAY");
            Ticket ticket = dao.fetchTicketsByUserId(1).get(0);
            System.out.println(ticket);

            dao.updateUserAndTickets(1, "John Smith", ticket.getId(), "MONTH");

            user = dao.fetchUserById(1);
            System.out.println("Updated User: " + user);

            ticket = dao.fetchTicketsByUserId(1).get(0);
            System.out.println("Updated Ticket: " + ticket);

            dao.deleteUserById(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}