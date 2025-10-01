package com.biblioteca_pessoal.controle;

import com.biblioteca_pessoal.modelo.Livro;
import com.biblioteca_pessoal.servico.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/livros") // Endpoint principal conforme requisito
public class LivroController {

    @Autowired
    private LivroService service;

    // 1. Criar Livro (POST)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Livro criarLivro(@RequestBody Livro livro) {
        return service.save(livro);
    }

    // 2. Ler todos (GET /api/livros)
    @GetMapping
    public List<Livro> lerTodos() {
        return service.findAll();
    }

    // 3. Ler por ID (GET /api/livros/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Livro> lerPorId(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Atualizar Livro (PUT /api/livros/{id})
    @PutMapping("/{id}")
    public Livro atualizarLivro(@PathVariable Long id, @RequestBody Livro livroDetalhes) {
        // O tratamento de erro 404 está dentro do Service (ResponseStatusException)
        return service.update(id, livroDetalhes);
    }

    // 5. Deletar Livro (DELETE /api/livros/{id})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 (No Content) em caso de sucesso
    public void deletarLivro(@PathVariable Long id) {
        // O tratamento de erro 404 está dentro do Service
        service.delete(id);
    }

    // 6. Ação Complexa: Marcar como lido/não lido (PATCH /api/livros/{id}/lido)
    // PATCH é mais apropriado para atualizações parciais
    @PatchMapping("/{id}/lido")
    public Livro marcarLido(@PathVariable Long id, @RequestParam boolean status) {
        // A lógica de negócio está centralizada no Service
        return service.atualizarStatusLido(id, status);
    }
}