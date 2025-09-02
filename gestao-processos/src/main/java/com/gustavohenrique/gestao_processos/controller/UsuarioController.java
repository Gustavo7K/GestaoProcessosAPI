package com.gustavohenrique.gestao_processos.controller;

import com.gustavohenrique.gestao_processos.dto.usuario.UsuarioCreateDto;
import com.gustavohenrique.gestao_processos.dto.usuario.UsuarioResponseDto;
import com.gustavohenrique.gestao_processos.entity.Usuario;
import com.gustavohenrique.gestao_processos.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController (UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> cadastrar(@RequestBody @Valid UsuarioCreateDto dto){
        Usuario novo = usuarioService.cadastrarUsuario(dto.getNome(), dto.getEmail(), dto.getSenha());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UsuarioResponseDto(novo.getId(), novo.getNome(), novo.getEmail()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable UUID id){
        Usuario buscaU = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(new UsuarioResponseDto(buscaU.getId(), buscaU.getNome(), buscaU.getEmail()));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDto> buscarPorEmail(@PathVariable String email){
        return usuarioService.buscarPorEmail(email)
                .map(u -> ResponseEntity.ok(new UsuarioResponseDto(u.getId(), u.getNome(), u.getEmail())))
                .orElse(ResponseEntity.notFound().build());

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarNotFound(EntityNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

}
