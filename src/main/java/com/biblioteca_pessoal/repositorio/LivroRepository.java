package com.biblioteca_pessoal.repositorio;

import com.biblioteca_pessoal.modelo.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Deve ser uma Interface e estender JpaRepository, não uma classe
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    // Todos os métodos CRUD (save, findById, findAll, delete) são herdados
}