package com.app.model;

import java.time.LocalDateTime;

public class Ticket {
    private int id;
    private int userId;
    private String ticketType;
    private LocalDateTime creationDate;

    public Ticket(int id, int userId, String ticketType, LocalDateTime creationDate) {
        this.id = id;
        this.userId = userId;
        this.ticketType = ticketType;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
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
                ", userId=" + userId +
                ", ticketType='" + ticketType + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
