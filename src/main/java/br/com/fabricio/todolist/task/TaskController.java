package br.com.fabricio.todolist.task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fabricio.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody Task task, HttpServletRequest request){
        int idUsuario = (int) request.getAttribute("idUser");
        task.setIdUsuario(idUsuario);

        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(task.getStartedAt()) || currentDate.isAfter(task.getEndAt())){
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("A data de inicio e de fim da task devem ser maiores que a data atual");
        }

        if(task.getStartedAt().isAfter(task.getEndAt())){
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("A data de inicio deve ser anterior a data fim.");
        }

        var taskCreated = this.taskRepository.save(task);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(taskCreated);
    }

    @GetMapping("/")
    public ResponseEntity list(HttpServletRequest request){
        Integer idUsuario = (int) request.getAttribute("idUser");
        List<Task> tasks = this.taskRepository
                        .findAllByIdUsuario(idUsuario);
        
        return ResponseEntity.status(200).body(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody Task task, @PathVariable Integer id, HttpServletRequest request){
        Integer idUsuario = (int) request.getAttribute("idUser");
        var taskFounded = this.taskRepository.findById(id).orElse(null);

        if(taskFounded == null){
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Essa task não está cadastrada no sistema.");
        }

        if(!taskFounded.getIdUsuario().equals(idUsuario)){
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("O usuário não tem permissão para alterar essa task");
        }

        Utils.copyNullProperties(task, taskFounded);
        var taskUpdated = this.taskRepository.save(taskFounded);
        
        return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
    }
    
}
