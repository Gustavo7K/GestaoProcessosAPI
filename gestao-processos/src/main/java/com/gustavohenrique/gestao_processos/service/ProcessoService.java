package com.gustavohenrique.gestao_processos.service;

import com.gustavohenrique.gestao_processos.dto.processo.ProcessoCreateDto;
import com.gustavohenrique.gestao_processos.dto.subprocesso.SubprocessoCreateDto;
import com.gustavohenrique.gestao_processos.entity.Processo;
import com.gustavohenrique.gestao_processos.entity.Quadro;
import com.gustavohenrique.gestao_processos.entity.SubProcesso;
import com.gustavohenrique.gestao_processos.repository.ProcessoRepository;
import com.gustavohenrique.gestao_processos.repository.QuadroRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ProcessoService {
    private final QuadroRepository quadroRepository;
    private final ProcessoRepository processoRepository;

    public ProcessoService(QuadroRepository quadroRepository,
                           ProcessoRepository processoRepository) {
        this.quadroRepository = quadroRepository;
        this.processoRepository = processoRepository;
    }

    public Processo criarProcesso(UUID quadroId, ProcessoCreateDto dto) {
        Quadro quadro = quadroRepository.findById(quadroId)
                .orElseThrow(() -> new IllegalArgumentException("Quadro não encontrado"));

        Processo processo = new Processo();
        processo.setNome(dto.getNome());
        processo.setDescricao(dto.getDescricao());
        processo.setQuadro(quadro);

        Set<SubProcesso> subprocessos = new HashSet<>();
        for (SubprocessoCreateDto spDto : dto.getSubprocessos()) {
            SubProcesso sp = new SubProcesso();
            sp.setNome(spDto.getNome());
            sp.setDescricao(spDto.getDescricao());
            sp.setProcesso(processo);
            subprocessos.add(sp);
        }

        processo.setSubprocessos(subprocessos);
        processoRepository.save(processo);
        return processo;
    }

    public List<Processo> listarPorQuadro(UUID quadroId) {
        return processoRepository.findByQuadroId(quadroId);
    }

    public void deletarProcesso(UUID processoId) {
        processoRepository.deleteById(processoId);
    }
}
