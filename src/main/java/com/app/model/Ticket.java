package com.app.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"Ticket\"")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type", nullable = false)
    private TicketType ticketType;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;


    public Ticket() {}

    public Ticket(int id, User user, TicketType ticketType, LocalDateTime creationDate) {
        this.id = id;
        this.user = user;
        this.ticketType = ticketType;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", user=" + user +
                ", ticketType='" + ticketType + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}