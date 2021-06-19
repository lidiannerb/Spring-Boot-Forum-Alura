package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/topicos") //URl definida apenas uma vez em cima da classe, indicando que todos os métodos começam com "/topicos" e os verbos são definidos em cada método
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping    //método para listar o curso
    public List<TopicoDto> lista(String nomeCurso){
        if (nomeCurso==null){
            List<Topico> topicos = topicoRepository.findAll();
            return TopicoDto.converter(topicos);
        }else{
            List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
            return TopicoDto.converter(topicos);
        }
    }

    @PostMapping  //O @RequestBody informa ao Spring que ele deve pegar o TopicoForm do corpo da requisição e não como parâmetro da URL
    public ResponseEntity<TopicoDto> cadastrar (@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder){    //TopicoForm > classe que indica que são dados do cliente que chegam para a API
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);   // Iremos receber um form, portando temos que converte-lo para um topico

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")  //indica que parte da URL é dinamica, e chamamos essa parte de id
    public DetalhesDoTopicoDto detalhar(@PathVariable Long id){
        Topico topico = topicoRepository.getOne(id);
        return new DetalhesDoTopicoDto(topico);

    }

    @PutMapping("/{id}")  //PUT: atualiza todas as informações PATCH: atualiza uma pequena atualização
    @Transactional  //dispara o commit no banco de dados
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form){
        Topico topico = form.atualizar(id, topicoRepository);

        return ResponseEntity.ok(new TopicoDto(topico));
    }
}
