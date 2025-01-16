package com.denzo;

import java.io.Serializable;

/**
 * Класс Budget представляет бюджет для определенной категории.
 */
public class Budget implements Serializable {
    private static final long serialVersionUID = 1L;
    private String category;
    private double limit;

    public Budget(String category, double limit) {
        this.category = category;
        this.limit = limit;
    }

    public String getCategory() {
        return category;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }
}
