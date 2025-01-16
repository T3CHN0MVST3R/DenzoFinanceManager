package com.denzo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Класс Transaction представляет финансовую операцию.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private TransactionType type; // INCOME или EXPENSE
    private String category;
    private double amount;
    private LocalDateTime date;

    public Transaction(TransactionType type, String category, double amount) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public TransactionType getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
