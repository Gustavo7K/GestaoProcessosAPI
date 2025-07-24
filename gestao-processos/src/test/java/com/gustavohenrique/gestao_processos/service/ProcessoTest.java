package com.gustavohenrique.gestao_processos.service;

import com.gustavohenrique.gestao_processos.dto.processo.ProcessoCreateDto;
import com.gustavohenrique.gestao_processos.dto.quadro.QuadroCreateDto;
import com.gustavohenrique.gestao_processos.dto.subprocesso.SubprocessoCreateDto;
import com.gustavohenrique.gestao_processos.entity.Processo;
import com.gustavohenrique.gestao_processos.entity.Quadro;
import com.gustavohenrique.gestao_processos.repository.ProcessoRepository;
import com.gustavohenrique.gestao_processos.repository.QuadroRepository;
import com.gustavohenrique.gestao_processos.repository.UsuarioRepository;
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
public class ProcessoTest {
    @Mock
    private QuadroRepository quadroRepository;

    @Mock
    private ProcessoRepository processoRepository;

    @InjectMocks
    private ProcessoService processoService;

    @Test
    public void deveCriarProcessoComSubprocesso(){
        UUID quadroId = UUID.randomUUID();
        Quadro quadro = new Quadro();
        quadro.setId(quadroId);

        ProcessoCreateDto dto = new ProcessoCreateDto();
        dto.setNome("Financeiro");
        dto.setDescricao("Controlar Contas");
        dto.setSubprocessos(List.of(
                new SubprocessoCreateDto(),
                new SubprocessoCreateDto()
        ));

        when(quadroRepository.findById(quadroId)).thenReturn(Optional.of(quadro));
        when(processoRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Processo processoCriado = processoService.criarProcesso(quadroId, dto);

        assertNotNull(processoCriado);
        assertEquals("Financeiro",processoCriado.getNome());
        assertEquals(2, processoCriado.getSubprocessos().size());

        verify(processoRepository).save(any());
    }

    @Test
    public void deveListarProcessosPorQuadro() {
        UUID quadroId = UUID.randomUUID();
        List<Processo> processos = List.of(new Processo(), new Processo());

        when(processoRepository.findByQuadroId(quadroId)).thenReturn(processos);

        List<Processo> resultado = processoService.listarPorQuadro(quadroId);

        assertEquals(2, resultado.size());
        verify(processoRepository).findByQuadroId(quadroId);
    }

    @Test
    public void deveDeletarProcessoComSucesso() {
        UUID processoId = UUID.randomUUID();

        processoService.deletarProcesso(processoId);

        verify(processoRepository).deleteById(processoId);
    }
}
