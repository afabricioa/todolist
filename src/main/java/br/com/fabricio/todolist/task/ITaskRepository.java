package br.com.fabricio.todolist.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<Task, Integer>{
    List<Task> findAllByIdUsuario(Integer idUsuario);
}
