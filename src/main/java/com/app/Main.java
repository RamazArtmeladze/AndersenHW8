package com.app;

import com.app.dao.TicketServiceDAO;
import com.app.model.Ticket;
import com.app.model.TicketType;
import com.app.model.User;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        TicketServiceDAO dao = new TicketServiceDAO();

        User user = new User();
        user.setName("John Doe");
        user.setCreationDate(LocalDateTime.now());
        dao.saveUser(user);

        User fetchedUser = dao.fetchUserById(user.getId());
        System.out.println(fetchedUser);

        Ticket ticket = new Ticket();
        ticket.setUser(fetchedUser);
        ticket.setTicketType(TicketType.DAY);
        ticket.setCreationDate(LocalDateTime.now());
        dao.saveTicket(ticket);

        Ticket fetchedTicket = dao.fetchTicketsByUserId(fetchedUser.getId()).get(0);
        System.out.println(fetchedTicket);

        dao.updateUserAndTickets(fetchedUser.getId(), "John Smith", TicketType.MONTH);

        User updatedUser = dao.fetchUserById(fetchedUser.getId());
        System.out.println("Updated User: " + updatedUser);

        Ticket updatedTicket = dao.fetchTicketsByUserId(fetchedUser.getId()).get(0);
        System.out.println("Updated Ticket: " + updatedTicket);

        dao.deleteUserById(updatedUser.getId());
    }
}