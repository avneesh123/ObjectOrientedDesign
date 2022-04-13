package com.library.librarymanagement.core;

import com.library.librarymanagement.Person;
import com.library.librarymanagement.references.AccountStatus;

public abstract class Account {
    private String id;
    private String password;
    private AccountStatus status;
    private Person person;

    public abstract boolean resetPassword();
}
