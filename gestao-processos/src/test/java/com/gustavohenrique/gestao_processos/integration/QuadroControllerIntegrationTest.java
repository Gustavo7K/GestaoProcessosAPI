package com.gustavohenrique.gestao_processos.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavohenrique.gestao_processos.dto.quadro.QuadroCreateDto;
import com.gustavohenrique.gestao_processos.entity.Quadro;
import com.gustavohenrique.gestao_processos.entity.Usuario;
import com.gustavohenrique.gestao_processos.entity.UsuarioQuadro;
import com.gustavohenrique.gestao_processos.repository.UsuarioQuadroRepository;
import com.gustavohenrique.gestao_processos.repository.UsuarioRepository;
import com.gustavohenrique.gestao_processos.repository.QuadroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class QuadroControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private QuadroRepository quadroRepository;

    @Autowired
    private UsuarioQuadroRepository usuarioQuadroRepository;

    private Usuario criador;

    @BeforeEach
    void setup() {
        usuarioQuadroRepository.deleteAll();
        quadroRepository.deleteAll();
        usuarioRepository.deleteAll();

        criador = new Usuario();
        criador.setNome("Gustavo");
        criador.setEmail("gustavo@email.com");
        criador.setSenha("123456");
        criador = usuarioRepository.save(criador);
    }

    @Test
    public void deveCriarQuadroComSucesso() throws Exception {
        QuadroCreateDto dto = new QuadroCreateDto();
        dto.setNome("Quadro Financeiro");

        mockMvc.perform(post("/quadros")
                        .param("usuarioId", criador.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Quadro Financeiro"));

        List<Quadro> quadros = quadroRepository.findByCriadorId(criador.getId());
        assertEquals(1, quadros.size());

        List<UsuarioQuadro> vinculos = usuarioQuadroRepository.findByQuadroId(quadros.get(0).getId());
        assertEquals(1, vinculos.size());
        assertEquals(UsuarioQuadro.Role.ADMIN, vinculos.get(0).getRole());

    }

    @Test
    void deveListarQuadrosPorUsuario() throws Exception {
        // Cria Quadro 1
        var q1 = new QuadroCreateDto();
        q1.setNome("Quadro 1");
        mockMvc.perform(post("/quadros")
                        .param("usuarioId", criador.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(q1)))
                .andExpect(status().isCreated());

        // Cria Quadro 2
        var q2 = new QuadroCreateDto();
        q2.setNome("Quadro 2");
        mockMvc.perform(post("/quadros")
                        .param("usuarioId", criador.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(q2)))
                .andExpect(status().isCreated());

        // Sanidade: banco tem 2
        var todos = quadroRepository.findAll();
        assertEquals(2, todos.size(), "Banco deveria conter exatamente 2 quadros.");

        // Chama a listagem
        MvcResult result = mockMvc.perform(get("/quadros/usuario/" + criador.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(body);

        // Agora deve ser array simples de DTOs
        assertTrue(root.isArray(), "Resposta deveria ser um array de DTOs.");
        assertEquals(2, root.size(), "A API deve retornar 2 quadros para o usuário.");

        var nomes = new ArrayList<String>();
        root.forEach(n -> nomes.add(n.path("nome").asText()));
        assertTrue(nomes.contains("Quadro 1"), "Resposta não contém 'Quadro 1'.");
        assertTrue(nomes.contains("Quadro 2"), "Resposta não contém 'Quadro 2'.");
    }

    @Test
    void deveDeletarQuadroComPermissaoDeAdmin() throws Exception {
        Quadro quadro = new Quadro();
        quadro.setNome("Quadro Deletável");
        quadro.setCriador(criador);
        quadro = quadroRepository.save(quadro);

        UsuarioQuadro vinculo = new UsuarioQuadro();
        vinculo.setUsuario(criador);
        vinculo.setQuadro(quadro);
        vinculo.setRole(UsuarioQuadro.Role.ADMIN);
        usuarioQuadroRepository.save(vinculo);

        mockMvc.perform(delete("/quadros/" + quadro.getId())
                        .param("usuarioId", criador.getId().toString()))
                .andExpect(status().isNoContent());

        assertFalse(quadroRepository.findById(quadro.getId()).isPresent());
    }

    @Test
    void deveNegarDelecaoSeUsuarioNaoForAdmin() throws Exception {
        Usuario funcionario = new Usuario();
        funcionario.setNome("Funcionario");
        funcionario.setEmail("func@email.com");
        funcionario.setSenha("123456");
        funcionario = usuarioRepository.save(funcionario);

        Quadro quadro = new Quadro();
        quadro.setNome("Quadro Protegido");
        quadro.setCriador(criador);
        quadro = quadroRepository.save(quadro);

        UsuarioQuadro vinculo = new UsuarioQuadro();
        vinculo.setUsuario(funcionario);
        vinculo.setQuadro(quadro);
        vinculo.setRole(UsuarioQuadro.Role.EMPLOYEE);
        usuarioQuadroRepository.save(vinculo);

        mockMvc.perform(delete("/quadros/" + quadro.getId())
                        .param("usuarioId", funcionario.getId().toString()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Você não tem permissão para deletar este quadro")));

        assertTrue(quadroRepository.findById(quadro.getId()).isPresent());
    }


}
