package com.denzo;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс UserService обеспечивает регистрацию, авторизацию и сохранение данных пользователей.
 */
public class UserService {
    private Map<String, User> users;

    public UserService() {
        users = new HashMap<>();
    }

    /**
     * Регистрация нового пользователя.
     * Если файл с данными найден, данные кошелька загружаются.
     */
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        User user = new User(username, password);
        String fileName = username + ".txt";
        user.getWallet().loadFromFile(fileName);
        users.put(username, user);
        return true;
    }

    /**
     * Авторизация пользователя по логину и паролю.
     * Если пользователь не найден, создается новый с загрузкой данных из файла.
     */
    public User login(String username, String password) {
        User user = users.get(username);
        if (user == null) {
            user = new User(username, password);
            String fileName = username + ".txt";
            user.getWallet().loadFromFile(fileName);
            users.put(username, user);
        }
        return user.getPassword().equals(password) ? user : null;
    }

    /**
     * Сохранение данных пользователя в файл с именем username.txt.
     */
    public void saveUser(User user) {
        String fileName = user.getUsername() + ".txt";
        user.getWallet().saveToFile(fileName);
    }

    /**
     * Сохранение данных всех пользователей.
     */
    public void saveAllUsers() {
        for (User user : users.values()) {
            saveUser(user);
        }
    }
}
