package com.accenture.academico.service;

import com.accenture.academico.dto.ClienteDTO;
import com.accenture.academico.dto.ClienteEnderecoDTO;
import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.model.EnderecoCliente;
import com.accenture.academico.repository.AgenciaRepository;
import com.accenture.academico.repository.ClienteRepository;
import com.accenture.academico.repository.ContaRepository;
import com.accenture.academico.repository.EnderecoClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	public List<Cliente> listarClientesPorAgencia(Integer numeroAgencia) {
		return clienteRepository.findAllByAgencia_NumeroAgencia(numeroAgencia);
	}

	public Optional<Cliente> obterClienteDeAgencia(Integer numeroAgencia, Integer idCliente) {
		return clienteRepository.findByIdAndAgencia_NumeroAgencia(idCliente, numeroAgencia);
	}

	public void deletarCliente(Integer numeroAgencia, Integer idCliente) {
		Cliente cliente = clienteRepository.findById(idCliente)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

		if (!cliente.getAgencia().getNumeroAgencia().equals(numeroAgencia)) {
			throw new RuntimeException("Cliente não pertence à agência informada");
		}

		clienteRepository.delete(cliente);
	}

	public Cliente criarCliente(Integer numeroAgencia, ClienteEnderecoDTO dto) {
		Agencia agencia = agenciaRepository.findByNumeroAgencia(numeroAgencia)
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

	public Optional<Cliente> atualizarCliente(Integer idCliente, ClienteDTO clienteDTO) {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);

        if (cliente.isPresent()) {
            Cliente clienteExistente = cliente.get();
            clienteExistente.setNome(clienteDTO.nome());
            clienteExistente.setCpf(clienteDTO.cpf());
            clienteExistente.setTelefone(clienteDTO.telefone());
            clienteExistente.setSenha(clienteDTO.senha());

            return Optional.of(clienteRepository.save(clienteExistente));
        } else {
            return Optional.empty();
        }
    }

	public Cliente buscarClientePorAgenciaCpfSenha(Integer numeroAgencia, String cpf, String senha) {
		return clienteRepository.findByAgencia_NumeroAgenciaAndCpfAndSenha(numeroAgencia, cpf, senha)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
	}

	
}
