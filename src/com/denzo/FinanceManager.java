package com.denzo;

/**
 * Класс FinanceManager осуществляет операции (доходы, расходы, бюджеты) для конкретного пользователя.
 */
public class FinanceManager {
    private User user;

    public FinanceManager(User user) {
        this.user = user;
    }

    /**
     * Добавление дохода.
     */
    public void addIncome(String category, double amount) {
        if (amount <= 0) {
            System.out.println("Сумма дохода должна быть положительной.");
            return;
        }
        Transaction income = new Transaction(TransactionType.INCOME, category, amount);
        user.getWallet().addTransaction(income);
    }

    /**
     * Добавление расхода. При превышении бюджета или при общих расходах, превышающих доходы, выводятся предупреждения.
     */
    public void addExpense(String category, double amount) {
        if (amount <= 0) {
            System.out.println("Сумма расхода должна быть положительной.");
            return;
        }
        Transaction expense = new Transaction(TransactionType.EXPENSE, category, amount);
        user.getWallet().addTransaction(expense);

        // Проверка превышения бюджета по категории
        if (user.getWallet().getBudgets().containsKey(category)) {
            double remaining = user.getWallet().getRemainingBudget(category);
            if (remaining < 0) {
                System.out.println("Внимание: превышен бюджет по категории \"" + category + "\". Остаток: " + remaining);
            }
        }
        // Проверка: если общие расходы превышают общий доход
        if (user.getWallet().getTotalExpense() > user.getWallet().getTotalIncome()) {
            System.out.println("Внимание: расходы превышают доходы!");
        }
    }

    /**
     * Установка бюджета по заданной категории.
     */
    public void setBudget(String category, double limit) {
        if (limit <= 0) {
            System.out.println("Бюджет должен быть положительным.");
            return;
        }
        user.getWallet().setBudget(category, limit);
    }

    /**
     * Вывод статистики: общий доход, доходы по категориям, общий расход и бюджеты с оставшимся лимитом.
     */
    public void printStatistics() {
        Wallet wallet = user.getWallet();
        System.out.println("Общий доход: " + wallet.getTotalIncome());
        System.out.println("Доходы по категориям:");
        wallet.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getCategory)
                .distinct()
                .forEach(category -> {
                    double total = wallet.getTotalForCategory(category, TransactionType.INCOME);
                    System.out.println(category + ": " + total);
                });
        System.out.println("\nОбщие расходы: " + wallet.getTotalExpense());
        System.out.println("\nБюджет по категориям:");
        wallet.getBudgets().forEach((category, budget) -> {
            double remaining = wallet.getRemainingBudget(category);
            System.out.println(category + ": " + budget.getLimit() + ", Оставшийся бюджет: " + remaining);
        });
    }
}
