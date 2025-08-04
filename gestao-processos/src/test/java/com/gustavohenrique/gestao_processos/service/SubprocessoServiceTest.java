package com.gustavohenrique.gestao_processos.service;

import com.gustavohenrique.gestao_processos.dto.subprocesso.SubprocessoCreateDto;
import com.gustavohenrique.gestao_processos.entity.Processo;
import com.gustavohenrique.gestao_processos.entity.SubProcesso;
import com.gustavohenrique.gestao_processos.repository.ProcessoRepository;
import com.gustavohenrique.gestao_processos.repository.SubprocessoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubprocessoServiceTest {

    @Mock
    private SubprocessoRepository subprocessoRepository;

    @Mock
    private ProcessoRepository processoRepository;

    @InjectMocks
    private SubprocessoService subprocessoService;

    @Test
    public void deveCriarSubprocessoComSucesso(){
        UUID processoId = UUID.randomUUID();
        Processo processo = new Processo();
        processo.setId(processoId);

        SubprocessoCreateDto dto = new SubprocessoCreateDto();
        dto.setNome("Planejamento");
        dto.setDescricao("Elaboração de metas");

        SubProcesso subprocessoSalvo = new SubProcesso();
        subprocessoSalvo.setNome(dto.getNome());
        subprocessoSalvo.setDescricao(dto.getDescricao());
        subprocessoSalvo.setProcesso(processo);

        when(processoRepository.findById(processoId)).thenReturn(Optional.of(processo));
        when(subprocessoRepository.save(any())).thenReturn(subprocessoSalvo);

        SubProcesso resultado = subprocessoService.criarSubprocesso(processoId, dto);

        assertNotNull(resultado);
        assertEquals("Planejamento", resultado.getNome());
        assertEquals("Elaboração de metas", resultado.getDescricao());
        assertEquals(processo, resultado.getProcesso());
    }

    @Test
    public void deveLancarErroSeProcessoNaoExisteAoCriarSubprocesso() {
        UUID processoId = UUID.randomUUID();

        SubprocessoCreateDto dto = new SubprocessoCreateDto();
        dto.setNome("Falho");
        dto.setDescricao("Sem processo");

        when(processoRepository.findById(processoId)).thenReturn(Optional.empty());

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            subprocessoService.criarSubprocesso(processoId, dto);
        });

        assertEquals("Processo não encontrado", e.getMessage());
    }

    @Test
    public void deveListarSubprocessosPorProcesso() {
        UUID processoId = UUID.randomUUID();

        SubProcesso s1 = new SubProcesso();
        SubProcesso s2 = new SubProcesso();

        when(subprocessoRepository.findByProcessoId(processoId))
                .thenReturn(List.of(s1, s2));

        List<SubProcesso> resultado = subprocessoService.listarPorProcesso(processoId);

        assertEquals(2, resultado.size());
        verify(subprocessoRepository).findByProcessoId(processoId);
    }

    @Test
    public void deveEditarSubprocessoComSucesso() {
        UUID id = UUID.randomUUID();

        SubProcesso existente = new SubProcesso();
        existente.setId(id);
        existente.setNome("Antigo");
        existente.setDescricao("Antes de editar");

        SubprocessoCreateDto dto = new SubprocessoCreateDto();
        dto.setNome("Atualizado");
        dto.setDescricao("Depois de editar");

        when(subprocessoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(subprocessoRepository.save(any())).thenReturn(existente);

        SubProcesso resultado = subprocessoService.editarSubprocesso(id, dto);

        assertEquals("Atualizado", resultado.getNome());
        assertEquals("Depois de editar", resultado.getDescricao());
        verify(subprocessoRepository).save(existente);
    }

    @Test
    public void deveDeletarSubprocessoComSucesso() {
        UUID id = UUID.randomUUID();

        subprocessoService.deletarSubprocesso(id);

        verify(subprocessoRepository).deleteById(id);
    }
}
