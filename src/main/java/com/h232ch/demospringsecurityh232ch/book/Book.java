package com.h232ch.demospringsecurityh232ch.book;

import com.h232ch.demospringsecurityh232ch.account.Account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Book {

    @Id @GeneratedValue
    private Integer id;

    private String title;

    @ManyToOne // N:1 관계 (단일 Account는 다수의 Books를 갖는다)
    private Account author;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Account getAuthor() {
        return author;
    }

    public void setAuthor(Account author) {
        this.author = author;
    }
}
