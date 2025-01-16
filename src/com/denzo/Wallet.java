package com.denzo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс Wallet представляет кошелёк пользователя.
 */
public class Wallet {
    private double currentBalance;
    private List<Transaction> transactions;
    private Map<String, Budget> budgets;

    public Wallet() {
        currentBalance = 0.0;
        transactions = new ArrayList<>();
        budgets = new HashMap<>();
    }

    /**
     * Добавление транзакции. При этом баланс изменяется в зависимости от типа транзакции.
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        if (transaction.getType() == TransactionType.INCOME) {
            currentBalance += transaction.getAmount();
        } else {
            currentBalance -= transaction.getAmount();
        }
    }

    /**
     * Установка бюджета по категории.
     */
    public void setBudget(String category, double limit) {
        budgets.put(category, new Budget(category, limit));
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Map<String, Budget> getBudgets() {
        return budgets;
    }

    /**
     * Подсчет общего дохода.
     */
    public double getTotalIncome() {
        double total = 0.0;
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.INCOME) {
                total += t.getAmount();
            }
        }
        return total;
    }

    /**
     * Подсчет общего расхода.
     */
    public double getTotalExpense() {
        double total = 0.0;
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.EXPENSE) {
                total += t.getAmount();
            }
        }
        return total;
    }

    /**
     * Подсчет суммы по категории для заданного типа транзакции.
     */
    public double getTotalForCategory(String category, TransactionType type) {
        double total = 0.0;
        for (Transaction t : transactions) {
            if (t.getType() == type && t.getCategory().equalsIgnoreCase(category)) {
                total += t.getAmount();
            }
        }
        return total;
    }

    /**
     * Вычисление оставшегося бюджета для категории.
     */
    public double getRemainingBudget(String category) {
        Budget budget = budgets.get(category);
        if (budget == null) {
            return 0.0;
        }
        double expenses = getTotalForCategory(category, TransactionType.EXPENSE);
        return budget.getLimit() - expenses;
    }

    /**
     * Сохранение данных кошелька в текстовый файл.
     */
    public void saveToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("BALANCE," + currentBalance);
            for (Transaction t : transactions) {
                // Формат: TRANSACTION,<type>,<category>,<amount>,<date>
                writer.println("TRANSACTION," + t.getType() + "," +
                        t.getCategory() + "," + t.getAmount() + "," + t.getDate());
            }
            writer.println("BUDGETS");
            for (Budget b : budgets.values()) {
                // Формат: BUDGET,<category>,<limit>
                writer.println("BUDGET," + b.getCategory() + "," + b.getLimit());
            }
        } catch (IOException e) {
            System.out.println("Ошибка сохранения данных кошелька: " + e.getMessage());
        }
    }

    /**
     * Загрузка данных кошелька из текстового файла.
     */
    public void loadFromFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean budgetsSection = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 0) continue;
                if (parts[0].equals("BALANCE")) {
                    currentBalance = Double.parseDouble(parts[1]);
                } else if (parts[0].equals("TRANSACTION")) {
                    // Формат: TRANSACTION,<type>,<category>,<amount>,<date>
                    TransactionType type = TransactionType.valueOf(parts[1]);
                    String category = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    // Для простоты создаем транзакцию (дата устанавливается заново)
                    Transaction t = new Transaction(type, category, amount);
                    transactions.add(t);
                } else if (parts[0].equals("BUDGETS")) {
                    budgetsSection = true;
                } else if (budgetsSection && parts[0].equals("BUDGET")) {
                    String category = parts[1];
                    double limit = Double.parseDouble(parts[2]);
                    budgets.put(category, new Budget(category, limit));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка загрузки данных кошелька: " + e.getMessage());
        }
    }
}
