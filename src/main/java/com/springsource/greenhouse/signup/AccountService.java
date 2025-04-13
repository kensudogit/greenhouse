package com.springsource.greenhouse.signup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.EmailAlreadyOnFileException;
import com.springsource.greenhouse.account.Person;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account createAccount(Person person) throws EmailAlreadyOnFileException {
        return accountRepository.createAccount(person);
    }
}