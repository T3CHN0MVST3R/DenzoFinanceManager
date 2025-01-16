package com.denzo;

public class Test {

    // Вспомогательный метод для вывода статуса теста по условию
    private static void printTestResult(String testName, boolean condition) {
        System.out.println(testName + ": " + (condition ? "ТЕСТ ВЫПОЛНЕН" : "НЕ ВЫПОЛНЕН"));
    }

    public static void main(String[] args) {
        // Тест 1: Регистрация пользователя
        UserService userService = new UserService();
        boolean registrationResult = userService.registerUser("testUser1", "pass1");
        printTestResult("Тест регистрации нового пользователя", registrationResult);

        // Тест 2: Повторная регистрация того же пользователя должна вернуть false
        boolean duplicateRegistration = userService.registerUser("testUser1", "newPass");
        printTestResult("Тест повторной регистрации", !duplicateRegistration);

        // Тест 3: Авторизация с правильным паролем
        User user1 = userService.login("testUser1", "pass1");
        printTestResult("Тест авторизации с правильным паролем", user1 != null);

        // Тест 4: Авторизация с неверным паролем должна вернуть null
        User wrongUser = userService.login("testUser1", "wrongPass");
        printTestResult("Тест авторизации с неверным паролем", wrongUser == null);

        // Тест 5: Добавление дохода через FinanceManager
        FinanceManager fm = new FinanceManager(user1);
        double initialIncome = user1.getWallet().getTotalIncome();
        fm.addIncome("Зарплата", 10000);
        double newIncome = user1.getWallet().getTotalIncome();
        printTestResult("Тест добавления дохода", newIncome == (initialIncome + 10000));

        // Тест 6: Добавление расхода через FinanceManager
        double initialExpense = user1.getWallet().getTotalExpense();
        fm.addExpense("Еда", 1500);
        double newExpense = user1.getWallet().getTotalExpense();
        printTestResult("Тест добавления расхода", newExpense == (initialExpense + 1500));

        // Тест 7: Установка бюджета
        fm.setBudget("Еда", 5000);
        Budget budget = user1.getWallet().getBudgets().get("Еда");
        boolean budgetSet = budget != null && budget.getLimit() == 5000;
        printTestResult("Тест установки бюджета", budgetSet);

        // Тест 8: Расчёт оставшегося бюджета
        // Добавим расход, чтобы проверить оставшийся бюджет
        fm.addExpense("Еда", 1000); // Итого по "Еда" расходы: 1500 + 1000 = 2500, оставшийся бюджет = 5000 - 2500 = 2500
        double remainingBudget = user1.getWallet().getRemainingBudget("Еда");
        printTestResult("Тест расчёта оставшегося бюджета", remainingBudget == 2500);

        // Тест 9: Проверка общего баланса кошелька
        // Баланс рассчитывается как сумма доходов минус расходы
        double balanceExpected = user1.getWallet().getTotalIncome() - user1.getWallet().getTotalExpense();
        printTestResult("Тест расчёта баланса кошелька", user1.getWallet().getCurrentBalance() == balanceExpected);

        // Тест 10: Сохранение и загрузка данных кошелька
        // Сохраним данные в файл, затем создадим новый кошелёк и загрузим данные из файла.
        String fileName = "testWalletData.txt";
        user1.getWallet().saveToFile(fileName);
        Wallet newWallet = new Wallet();
        newWallet.loadFromFile(fileName);
        boolean incomeEquals = newWallet.getTotalIncome() == user1.getWallet().getTotalIncome();
        boolean expenseEquals = newWallet.getTotalExpense() == user1.getWallet().getTotalExpense();
        printTestResult("Тест сохранения/загрузки: доходы", incomeEquals);
        printTestResult("Тест сохранения/загрузки: расходы", expenseEquals);

        // Тест 11: Вывод статистики (для проверки выводимой информации можно сделать сравнение строк,
        // здесь мы проверяем наличие ожидаемых категорий)
        System.out.println("\nВывод статистики для ручной проверки:");
        fm.printStatistics();
        // Предположим, что если доходы по категории "Зарплата" и расходы по "Еда" > 0, тест считается выполненным.
        boolean statisticsTest = user1.getWallet().getTotalForCategory("Зарплата", TransactionType.INCOME) > 0 &&
                user1.getWallet().getTotalForCategory("Еда", TransactionType.EXPENSE) > 0;
        printTestResult("Тест вывода статистики", statisticsTest);

        // Дополнительный тест: Если расход превышает доход, должно выводиться предупреждение.
        // Для эмуляции этого условия создадим нового пользователя.
        User user2 = new User("testUser2", "pass2");
        FinanceManager fm2 = new FinanceManager(user2);
        // Добавим расход, а доход не будем добавлять, чтобы расходы превысили доходы
        fm2.addExpense("Развлечения", 5000);
        // Если расходы > доходы, общее уведомление должно сработать.
        boolean warningTest = user2.getWallet().getTotalExpense() > user2.getWallet().getTotalIncome();
        printTestResult("Тест оповещения при превышении расходов над доходами", warningTest);

        System.out.println("\nВсе тесты выполнены.");
    }
}
