package com.accenture.academico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.repository.AgenciaRepository;
import com.accenture.academico.repository.ClienteRepository;
import com.accenture.academico.repository.ContaRepository;

@Service
public class ContaService {

	private static final double TAXA = 1.50;
	
	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
    private ClienteRepository clienteRepository;
	
	@Autowired
	private AgenciaRepository agenciaRepository;
	
	 public ContaBancaria criarConta(Integer idAgencia, Integer idCliente, ContaBancaria contaBancaria) throws Exception {
	        Optional<Agencia> agenciaOpt = agenciaRepository.findById(idAgencia);
	        Optional<Cliente> clienteOpt = clienteRepository.findById(idCliente);

	        if (agenciaOpt.isEmpty()) {
	            throw new Exception("Agência não encontrada");
	        }

	        if (clienteOpt.isEmpty()) {
	            throw new Exception("Cliente não encontrado");
	        }

	        Cliente cliente = clienteOpt.get();
	        contaBancaria.setCliente(cliente);
	        ContaBancaria contaSalva = contaRepository.save(contaBancaria);
	        cliente.getContaBancarias().add(contaSalva);
	        clienteRepository.save(cliente);

	        return contaSalva;
	    }

	public ContaBancaria depositar(Integer idConta, Double valor) {
		ContaBancaria conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada"));

		conta.depositar(valor);
		return contaRepository.save(conta);
	}

	public ContaBancaria sacar(Integer idConta, Double valor) {
		double valorTaxado = valor +TAXA;
		
		ContaBancaria conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada"));
		if (conta.getSaldo() < valorTaxado) {
			throw new IllegalArgumentException("Saldo insuficiente na conta remetente.");
		}
		
		conta.sacar(valorTaxado);
		return contaRepository.save(conta);
	}

	public void transferir(Integer idAgenciaRemetente, Integer idClienteRemetente, Integer idContaRemetente,
			Integer idAgenciaDestinataria, Integer idClienteDestinataria, Integer idContaDestinataria, Double valor) {
		if (valor <= 0) {
			throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
		}

		ContaBancaria contaRemetente = contaRepository.findById(idContaRemetente)
				.orElseThrow(() -> new IllegalArgumentException("Conta remetente não encontrada"));

		if (!contaRemetente.getCliente().getId().equals(idClienteRemetente)
				|| !contaRemetente.getCliente().getAgencia().getIdAgencia().equals(idAgenciaRemetente)) {
			throw new IllegalArgumentException("Conta remetente não pertence ao cliente ou agência corretos.");
		}

		if (contaRemetente.getSaldo() < valor) {
			throw new IllegalArgumentException("Saldo insuficiente na conta remetente.");
		}

		ContaBancaria contaDestinataria = contaRepository.findById(idContaDestinataria)
				.orElseThrow(() -> new IllegalArgumentException("Conta destinatária não encontrada"));

		if (!contaDestinataria.getCliente().getId().equals(idClienteDestinataria)
				|| !contaDestinataria.getCliente().getAgencia().getIdAgencia().equals(idAgenciaDestinataria)) {
			throw new IllegalArgumentException("Conta destinatária não pertence ao cliente ou agência corretos.");
		}

		contaRemetente.setSaldo(contaRemetente.getSaldo() - (valor));
		contaDestinataria.setSaldo(contaDestinataria.getSaldo() + valor);

		contaRepository.save(contaRemetente);
		contaRepository.save(contaDestinataria);
	}
	
	public Optional<List<ContaBancaria>> listarContasPorCliente(Integer idAgencia, Integer idCliente) {
        return clienteService.obterClienteDeAgencia(idAgencia, idCliente)
                .map(Cliente::getContaBancarias);
    }
	
	public Optional<ContaBancaria> obterContaPorId(Integer idAgencia, Integer idCliente, Integer idConta) {
        return contaRepository.findByIdAndClienteIdAndClienteAgenciaId(idConta, idCliente, idAgencia);
    }
	
	 public void deletarConta(Integer idAgencia, Integer idCliente, Integer idConta) {
	        Cliente cliente = clienteRepository.findById(idCliente)
	                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

	        if (!cliente.getAgencia().getIdAgencia().equals(idAgencia)) {
	            throw new RuntimeException("Cliente não pertence à agência informada");
	        }

	        ContaBancaria conta = contaRepository.findById(idConta)
	                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

	        if (!conta.getCliente().getId().equals(idCliente)) {
	            throw new RuntimeException("Conta não pertence ao cliente informado");
	        }

	        contaRepository.delete(conta);
	    }

}
