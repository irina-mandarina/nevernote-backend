package com.example.demo.Repositories;

import com.example.demo.Entities.Note;
import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository <Note, Long> {

    List<Note> findAllByUser(User user);

    Note findNoteById(Long id);
}
