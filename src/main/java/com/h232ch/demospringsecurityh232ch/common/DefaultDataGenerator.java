package com.h232ch.demospringsecurityh232ch.common;

import com.h232ch.demospringsecurityh232ch.account.Account;
import com.h232ch.demospringsecurityh232ch.account.AccountService;
import com.h232ch.demospringsecurityh232ch.book.Book;
import com.h232ch.demospringsecurityh232ch.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultDataGenerator implements ApplicationRunner {

    @Autowired
    AccountService accountService;

    @Autowired
    BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // sh - spring
        // sh2 - hibernate

        Account sh = createUesr("sh");
        Account sh2 = createUesr("sh2");

        createBook("spring", sh);
        createBook("hibernate", sh2);
    }

    private void createBook(String title, Account account) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(account);
        bookRepository.save(book);
    }

    private Account createUesr(String username){
        Account account = new Account();
        account.setUsername(username);
        account.setPassword("123");
        account.setRole("USER");
        return accountService.createNew(account); // Insert to DB
    }
}
