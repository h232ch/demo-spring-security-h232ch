package com.h232ch.demospringsecurityh232ch.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("select b from Book b where b.author.id = ?#{ principal.account.id }") // SecurityContextHolder에 존재하는 account.id를 가져오는것
            // AccountService의 loadUserByUsername에 리턴값(UserAccount)에 존재하는 account.id를 가져옮
//    @Query("select b from Book b where b.author.username = ?#{ principal.username }") // Username으로 구현
    List<Book> findCurrentUserBooks();
}
