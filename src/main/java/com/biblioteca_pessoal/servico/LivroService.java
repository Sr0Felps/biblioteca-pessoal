package com.biblioteca_pessoal.servico;

import com.biblioteca_pessoal.modelo.Livro;
import com.biblioteca_pessoal.repositorio.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    // Ação "complexa": Atualiza apenas um campo (lido), garantindo que o livro exista
    public Livro atualizarStatusLido(Long id, boolean novoStatus) {
        return repository.findById(id)
                .map(livro -> {
                    // Lógica de negócio: verifica se o status é diferente antes de salvar
                    if (livro.isLido() != novoStatus) {
                        livro.setLido(novoStatus);
                        return repository.save(livro);
                    }
                    // Se o status já é o desejado, apenas retorna o livro sem salvar novamente
                    return livro;
                })
                // Se o livro não for encontrado, lança uma exceção HTTP 404 (Not Found)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado com ID: " + id));
    }

    // Métodos de CRUD simples (mantidos aqui para boa prática, delegando ao repositório)

    public List<Livro> findAll() {
        return repository.findAll();
    }

    public Livro save(Livro livro) {
        return repository.save(livro);
    }

    public Optional<Livro> findById(Long id) {
        return repository.findById(id);
    }

    public Livro update(Long id, Livro livroDetalhes) {
        return repository.findById(id)
                .map(livro -> {
                    livro.setTitulo(livroDetalhes.getTitulo());
                    livro.setAutor(livroDetalhes.getAutor());
                    livro.setAnoPublicacao(livroDetalhes.getAnoPublicacao());
                    livro.setLido(livroDetalhes.isLido());
                    return repository.save(livro);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado para atualização com ID: " + id));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado para exclusão com ID: " + id);
        }
        repository.deleteById(id);
    }
}