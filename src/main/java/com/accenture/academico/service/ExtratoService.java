package com.accenture.academico.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.dto.ExtratoDTO;
import com.accenture.academico.dto.OperacoesClienteDTO;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.repository.ClienteRepository;

@Service
public class ExtratoService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private OperacoesClienteService operacoesClienteService;

    public Optional<ExtratoDTO> obterExtrato(Integer numeroAgencia, Integer idCliente) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(idCliente);

        if (clienteOpt.isPresent() && clienteOpt.get().getAgencia().getNumeroAgencia().equals(numeroAgencia)) {
            Cliente cliente = clienteOpt.get();
            
            // Preencher dados do cliente
            ExtratoDTO.ClienteEnderecoDTO clienteDTO = new ExtratoDTO.ClienteEnderecoDTO();
            clienteDTO.setNome(cliente.getNome());
            clienteDTO.setCpf(cliente.getCpf());
            clienteDTO.setTelefone(cliente.getTelefone());
            clienteDTO.setSenha(cliente.getSenha());
            clienteDTO.setEndereco(cliente.getEnderecoCliente());
            clienteDTO.setContas(cliente.getContaBancarias().stream()
                .map(conta -> {
                    ExtratoDTO.ClienteEnderecoDTO.ContaDTO contaDTO = new ExtratoDTO.ClienteEnderecoDTO.ContaDTO();
                    contaDTO.setSaldo(conta.getSaldo());
                    contaDTO.setTipo(conta.getTipo());
                    return contaDTO;
                }).collect(Collectors.toList()));

            // Preencher operações do cliente
            List<OperacoesClienteDTO> operacoesDTO = operacoesClienteService.getOperacoesPorCliente(idCliente);

            // Criar DTO unificado
            ExtratoDTO extratoDTO = new ExtratoDTO();
            extratoDTO.setCliente(clienteDTO);
            extratoDTO.setOperacoes(operacoesDTO);

            return Optional.of(extratoDTO);
        }

        return Optional.empty();
    }
}
