package com.example.demo.Repositories;

import com.example.demo.Entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository <Note, Long> {
}
