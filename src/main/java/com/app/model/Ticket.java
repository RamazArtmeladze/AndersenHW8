package com.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ticket {
    private int id;
    private int userId;
    private String ticketType;
    private String ticketClass;
    private LocalDateTime creationDate;
    private LocalDate startDate;
    private int price;

    public Ticket() {
    }

    public Ticket(int id, int userId, String ticketType, String ticketClass, LocalDateTime creationDate, LocalDate startDate, int price) {
        this.id = id;
        this.userId = userId;
        this.ticketType = ticketType;
        this.ticketClass = ticketClass;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.price = price;
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

    public String getTicketClass() {
        return ticketClass;
    }

    public void setTicketClass(String ticketClass) {
        this.ticketClass = ticketClass;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", userId=" + userId +
                ", ticketType='" + ticketType + '\'' +
                ", ticketClass='" + ticketClass + '\'' +
                ", creationDate=" + creationDate +
                ", startDate=" + startDate +
                ", price=" + price +
                '}';
    }
}