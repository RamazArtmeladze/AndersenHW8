package com.app;

import com.app.config.AppConfig;
import com.app.dao.TicketServiceDAO;
import com.app.model.Ticket;
import com.app.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TicketServiceDAO dao = context.getBean(TicketServiceDAO.class);

        try {
            dao.saveUser("John Doe");
            User user = dao.fetchUserById(1);
            if (user != null) {
                System.out.println(user);

                dao.saveTicket(1, "DAY");
                Ticket ticket = dao.fetchTicketsByUserId(1).get(0);
                System.out.println(ticket);

                dao.updateUserAndTickets(1, "John Doe1", ticket.getId(), "MONTH");

                user = dao.fetchUserById(1);
                System.out.println("Updated User: " + user);

                ticket = dao.fetchTicketsByUserId(1).get(0);
                System.out.println("Updated Ticket: " + ticket);

                dao.deleteUserById(1);
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}