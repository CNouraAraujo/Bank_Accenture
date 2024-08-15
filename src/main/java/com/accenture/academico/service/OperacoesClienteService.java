package com.accenture.academico.service;

import com.accenture.academico.dto.OperacoesClienteDTO;
import com.accenture.academico.model.OperacoesCliente;
import com.accenture.academico.repository.OperacoesClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperacoesClienteService {

    @Autowired
    private OperacoesClienteRepository operacoesClienteRepository;

    public List<OperacoesClienteDTO> getOperacoesPorCliente(Integer idCliente) {
        List<OperacoesCliente> operacoes = operacoesClienteRepository.findByCliente_Id(idCliente);
        
        return operacoes.stream().map(operacao -> {
            OperacoesClienteDTO dto = new OperacoesClienteDTO();
            dto.setId(operacao.getId());
            dto.setTipoOperacao(operacao.getTipoOperacao());
            dto.setValor(operacao.getValor());
            dto.setDataOperacao(operacao.getDataOperacao());

            OperacoesClienteDTO.ClienteResumoDTO clienteDTO = new OperacoesClienteDTO.ClienteResumoDTO();
            clienteDTO.setId(operacao.getCliente().getId());
            clienteDTO.setNome(operacao.getCliente().getNome());
            clienteDTO.setCpf(operacao.getCliente().getCpf());

            dto.setCliente(clienteDTO);
            return dto;
        }).collect(Collectors.toList());
    }
}
