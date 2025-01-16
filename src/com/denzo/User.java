package com.denzo;

import java.io.Serializable;

/**
 * Класс User представляет пользователя с логином, паролем и кошельком.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private Wallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallet = new Wallet();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
