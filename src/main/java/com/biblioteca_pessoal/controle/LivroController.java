package com.biblioteca_pessoal.controle;

import com.biblioteca_pessoal.modelo.Livro;
import com.biblioteca_pessoal.repositorio.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros") // Endpoint principal conforme requisito
public class LivroController {

    @Autowired
    private LivroRepository repository;

    // 1. Criar Livro (POST)
    @PostMapping
    public Livro criarLivro(@RequestBody Livro livro) {
        return repository.save(livro);
    }

    // 2. Ler todos (GET /api/livros)
    @GetMapping
    public List<Livro> lerTodos() {
        return repository.findAll();
    }

    // 3. Ler por ID (GET /api/livros/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Livro> lerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Atualizar Livro (PUT /api/livros/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable Long id, @RequestBody Livro livroDetalhes) {
        return repository.findById(id)
                .map(livro -> {
                    livro.setTitulo(livroDetalhes.getTitulo());
                    livro.setAutor(livroDetalhes.getAutor());
                    livro.setAnoPublicacao(livroDetalhes.getAnoPublicacao());
                    livro.setLido(livroDetalhes.isLido());
                    Livro atualizado = repository.save(livro);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Deletar Livro (DELETE /api/livros/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarLivro(@PathVariable Long id) {
        return repository.findById(id)
                .map(livro -> {
                    repository.delete(livro);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}