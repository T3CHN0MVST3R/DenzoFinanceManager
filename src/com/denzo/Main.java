package com.denzo;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Создаем экземпляр UserService для управления пользователями
        UserService userService = new UserService();
        Scanner scanner = new Scanner(System.in);
        User currentUser = null;

        // Цикл авторизации/регистрации
        while (currentUser == null) {
            System.out.println("Выберите действие:");
            System.out.println("1. Войти");
            System.out.println("2. Зарегистрироваться");
            System.out.print("Введите номер действия: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Логин: ");
                String username = scanner.nextLine();
                System.out.print("Пароль: ");
                String password = scanner.nextLine();

                currentUser = userService.login(username, password);
                if (currentUser == null) {
                    System.out.println("Неверные логин или пароль. Попробуйте еще раз.");
                }
            } else if (choice.equals("2")) {
                System.out.print("Придумайте логин: ");
                String username = scanner.nextLine();
                System.out.print("Придумайте пароль: ");
                String password = scanner.nextLine();

                if (userService.registerUser(username, password)) {
                    System.out.println("Пользователь успешно зарегистрирован. Теперь выполните вход.");
                } else {
                    System.out.println("Пользователь с таким логином уже существует.");
                }
            } else {
                System.out.println("Некорректный выбор. Пожалуйста, введите 1 или 2.");
            }
        }

        // Создаем экземпляр FinanceManager для работы с финансами авторизованного пользователя
        FinanceManager financeManager = new FinanceManager(currentUser);
        boolean exit = false;
        while (!exit) {
            System.out.println("\nВыберите команду:");
            System.out.println("1. Добавить доход");
            System.out.println("2. Добавить расход");
            System.out.println("3. Установить бюджет");
            System.out.println("4. Просмотреть статистику");
            System.out.println("5. Сохранить данные в файл");
            System.out.println("6. Выход");
            System.out.print("Введите номер команды: ");
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    // Добавление дохода
                    System.out.print("Введите категорию дохода: ");
                    String incomeCategory = scanner.nextLine();
                    System.out.print("Введите сумму дохода: ");
                    double incomeAmount = readDouble(scanner);
                    financeManager.addIncome(incomeCategory, incomeAmount);
                    break;
                case "2":
                    // Добавление расхода
                    System.out.print("Введите категорию расхода: ");
                    String expenseCategory = scanner.nextLine();
                    System.out.print("Введите сумму расхода: ");
                    double expenseAmount = readDouble(scanner);
                    financeManager.addExpense(expenseCategory, expenseAmount);
                    break;
                case "3":
                    // Установка бюджета для категории
                    System.out.print("Введите категорию для установки бюджета: ");
                    String budgetCategory = scanner.nextLine();
                    System.out.print("Введите лимит бюджета: ");
                    double budgetLimit = readDouble(scanner);
                    financeManager.setBudget(budgetCategory, budgetLimit);
                    break;
                case "4":
                    // Вывод статистики
                    financeManager.printStatistics();
                    break;
                case "5":
                    // Сохранение данных в файл с именем, заданным пользователем
                    System.out.print("Введите имя файла для сохранения (например, myData.txt): ");
                    String fileName = scanner.nextLine();
                    currentUser.getWallet().saveToFile(fileName);
                    System.out.println("Данные успешно сохранены в файл " + fileName);
                    break;
                case "6":
                    // Выход
                    exit = true;
                    break;
                default:
                    System.out.println("Неизвестная команда, попробуйте еще раз.");
                    break;
            }
        }

        // Перед завершением сохраняем данные пользователя в дефолтный файл (username.txt)
        userService.saveUser(currentUser);
        System.out.println("Данные сохранены. До свидания!");
        scanner.close();
    }

    /**
     * Метод для безопасного чтения числа типа double с консоли.
     */
    private static double readDouble(Scanner scanner) {
        double value = 0.0;
        boolean valid = false;
        while (!valid) {
            String input = scanner.nextLine();
            try {
                value = Double.parseDouble(input);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.print("Некорректное число. Пожалуйста, введите корректное значение: ");
            }
        }
        return value;
    }
}
