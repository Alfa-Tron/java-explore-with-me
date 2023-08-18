package ru.practicum.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findAll(Pageable pageable);

    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}

