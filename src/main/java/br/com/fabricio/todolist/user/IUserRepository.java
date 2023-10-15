package br.com.fabricio.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Integer>{
    User findByUsername(String username);
}
