package service;

import entity.Usuario;
import org.springframework.stereotype.Service;
import repository.UsuarioRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrarUsuario(String nome, String email, String senha){
        Usuario usuario = new Usuario(nome, email, senha);
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(UUID id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado"));
    }

    public Optional<Usuario> buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }
}
