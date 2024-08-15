package com.accenture.academico.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.dto.ClienteEnderecoDTO;
import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.model.EnderecoCliente;
import com.accenture.academico.repository.AgenciaRepository;
import com.accenture.academico.repository.ClienteRepository;
import com.accenture.academico.repository.ContaRepository;
import com.accenture.academico.repository.EnderecoClienteRepository;

@Service
public class ClienteService {

	@Autowired
    private ClienteRepository clienteRepository;
	
	@Autowired
	private AgenciaRepository agenciaRepository;
	
	@Autowired
	private EnderecoClienteRepository enderecoClienteRepository;
	
	@Autowired
	private ContaRepository contaRepository;
	
	public List<Cliente> listarClientesPorAgencia(Integer idAgencia) {
        return clienteRepository.findAllByAgencia_IdAgencia(idAgencia);
    }
	
	public Optional<Cliente> obterClienteDeAgencia(Integer idAgencia, Integer idCliente) {
	    return clienteRepository.findByIdAndAgencia_IdAgencia(idCliente, idAgencia);
	}
	
	public void deletarCliente(Integer idAgencia, Integer idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (!cliente.getAgencia().getIdAgencia().equals(idAgencia)) {
            throw new RuntimeException("Cliente não pertence à agência informada");
        }

        clienteRepository.delete(cliente);
    }

	 public Cliente criarCliente(Integer idAgencia, ClienteEnderecoDTO dto) {
	        Agencia agencia = agenciaRepository.findById(idAgencia)
	                .orElseThrow(() -> new RuntimeException("Agência não encontrada"));

	        EnderecoCliente endereco = dto.getEndereco();
	        enderecoClienteRepository.save(endereco);

	        Cliente cliente = new Cliente();
	        cliente.setNome(dto.getNome());
	        cliente.setCpf(dto.getCpf());
	        cliente.setTelefone(dto.getTelefone());
	        cliente.setSenha(dto.getSenha());
	        cliente.setEnderecoCliente(endereco);
	        cliente.setAgencia(agencia);

	        List<ContaBancaria> contas = dto.getContas().stream().map(contaDTO -> {
	            ContaBancaria conta = new ContaBancaria();
	            conta.setSaldo(contaDTO.getSaldo());
	            conta.setTipo(contaDTO.getTipo());
	            conta.setCliente(cliente);
	            return conta;
	        }).collect(Collectors.toList());
	        cliente.setContaBancarias(contas);

	        Cliente clienteSalvo = clienteRepository.save(cliente);
	        contaRepository.saveAll(contas);

	        clienteSalvo.setContaBancarias(contaRepository.findByCliente_Id(clienteSalvo.getId()));
	        return clienteSalvo;
	    }
	
}
