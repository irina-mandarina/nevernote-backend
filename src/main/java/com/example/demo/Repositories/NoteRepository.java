package com.example.demo.Repositories;

import com.example.demo.Entities.Note;
import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository <Note, Long> {

    List<Note> findAllByUser(User user);
    List<Note> findAllByUserAndDeadlineIsNull(User user); // find all notes - those without a deadline
    List<Note> findAllByUserAndDeadlineIsNotNull(User user); // find all notes with a deadline = find all tasks
    List<Note> findAllByUserAndCompletedFalseAndDeadlineAfter(User user, Timestamp now); // find tasks that need to be done
    List<Note> findAllByUserAndDeadlineIsNotNullAndDeadlineBefore(User user, Timestamp now); // find expired tasks
    List<Note> findAllByUserAndDeadlineIsNotNullAndCompletedTrue(User user); // completed tasks
    Note findNoteById(Long id);

    List<Note> findAll();
    List<Note> findAllByDeadlineIsNull(); // find all notes - those without a deadline
    List<Note> findAllByDeadlineIsNotNull(); // find all notes with a deadline = find all tasks
    List<Note> findAllByCompletedFalseAndDeadlineAfter(Timestamp now); // find tasks that need to be done
    List<Note> findAllByDeadlineIsNotNullAndDeadlineBefore(Timestamp now); // find expired tasks
    List<Note> findAllByDeadlineIsNotNullAndCompletedTrue(); // completed tasks

}
