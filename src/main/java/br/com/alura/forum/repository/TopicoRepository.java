package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicoRepository extends JpaRepository <Topico, Long> {
    List<Topico> findByCursoNome(String nomeCurso); // Através do findByTitulo o Spring gera a query automaticamente
}
