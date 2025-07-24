package com.gustavohenrique.gestao_processos.service;

import com.gustavohenrique.gestao_processos.dto.subprocesso.SubprocessoCreateDto;
import com.gustavohenrique.gestao_processos.entity.Processo;
import com.gustavohenrique.gestao_processos.entity.SubProcesso;
import com.gustavohenrique.gestao_processos.repository.ProcessoRepository;
import com.gustavohenrique.gestao_processos.repository.SubprocessoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubprocessoService {
    private final SubprocessoRepository subprocessoRepository;
    private final ProcessoRepository processoRepository;

    public SubprocessoService(SubprocessoRepository subprocessoRepository,
                              ProcessoRepository processoRepository) {
        this.subprocessoRepository = subprocessoRepository;
        this.processoRepository = processoRepository;
    }

    public SubProcesso criarSubprocesso(UUID processoId, SubprocessoCreateDto dto) {
        Processo processo = processoRepository.findById(processoId)
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));

        SubProcesso subprocesso = new SubProcesso();
        subprocesso.setNome(dto.getNome());
        subprocesso.setDescricao(dto.getDescricao());
        subprocesso.setProcesso(processo);

        return subprocessoRepository.save(subprocesso);
    }


    public List<SubProcesso> listarPorProcesso(UUID processoId) {
        return subprocessoRepository.findByProcessoId(processoId);
    }

    public SubProcesso editarSubprocesso(UUID id, SubprocessoCreateDto dto) {
        SubProcesso sp = subprocessoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subprocesso não encontrado"));

        sp.setNome(dto.getNome());
        sp.setDescricao(dto.getDescricao());
        return subprocessoRepository.save(sp);
    }

    public void deletarSubprocesso(UUID id) {
        subprocessoRepository.deleteById(id);
    }
}
