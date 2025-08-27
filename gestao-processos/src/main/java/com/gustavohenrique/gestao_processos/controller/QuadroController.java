package com.gustavohenrique.gestao_processos.controller;

import com.gustavohenrique.gestao_processos.dto.quadro.QuadroCreateDto;
import com.gustavohenrique.gestao_processos.entity.Quadro;
import com.gustavohenrique.gestao_processos.service.QuadroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quadros")
public class QuadroController {
    private final QuadroService quadroService;

    public QuadroController(QuadroService quadroService) {
        this.quadroService = quadroService;
    }

    @PostMapping
    public ResponseEntity<Quadro> criarQuadro(@RequestParam UUID usuarioId,
                                              @RequestBody @Valid QuadroCreateDto dto) {
        Quadro quadro = quadroService.criarQuadro(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(quadro);
    }

    @GetMapping("{id}")
    public ResponseEntity<Quadro> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(quadroService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Quadro>> listarPorUsuario(@PathVariable UUID usuarioId){
        return ResponseEntity.ok(quadroService.listarQuadrosPorUsuario(usuarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarQuadro(@PathVariable UUID id,
                                                @RequestParam UUID usuarioId) throws IllegalAccessException {
        quadroService.deletarQuadro(id, usuarioId); // TODO: 14/08/2025 revisar
        return ResponseEntity.noContent().build();
    }
}
