package com.bobocode.web.controller;

import com.bobocode.dao.AccountDao;
import com.bobocode.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountRestController {
    private final AccountDao accountDao;

    @GetMapping
    public List<Account> getAll() {
        return accountDao.findAll();
    }

    @GetMapping("/{id}")
    public Account getById(@PathVariable long id) {
        return accountDao.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@RequestBody Account account) {
        return accountDao.save(account);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long id, @RequestBody Account account) {
        if (!account.getId().equals(id)) {
            throw new IllegalStateException("Account id and path variable must match");
        }
        accountDao.save(account);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        Account account = accountDao.findById(id);
        accountDao.remove(account);
    }
}
