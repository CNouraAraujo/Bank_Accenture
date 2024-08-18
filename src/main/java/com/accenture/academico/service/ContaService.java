package com.accenture.academico.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.model.OperacoesCliente;
import com.accenture.academico.model.enums.TipoOperacao;
import com.accenture.academico.repository.AgenciaRepository;
import com.accenture.academico.repository.ClienteRepository;
import com.accenture.academico.repository.ContaRepository;
import com.accenture.academico.repository.OperacoesClienteRepository;

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

	@Autowired
	private OperacoesClienteRepository operacoesClienteRepository;

	public ContaBancaria criarConta(Integer numeroAgencia, Integer idCliente, ContaBancaria contaBancaria)
			throws Exception {
		Optional<Agencia> agenciaOpt = agenciaRepository.findByNumeroAgencia(numeroAgencia);
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

		OperacoesCliente operacao = new OperacoesCliente();
		operacao.setCliente(conta.getCliente());
		operacao.setTipoOperacao(TipoOperacao.DEPOSITO);
		operacao.setValor(valor);
		operacao.setDataOperacao(LocalDateTime.now());
		operacoesClienteRepository.save(operacao);

		return contaRepository.save(conta);
	}

	public ContaBancaria sacar(Integer idConta, Double valor) {
		double valorTaxado = valor + TAXA;

		ContaBancaria conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada"));

		if (conta.getSaldo() < valorTaxado) {
			throw new IllegalArgumentException("Saldo insuficiente na conta.");
		}

		conta.sacar(valorTaxado);
		ContaBancaria contaAtualizada = contaRepository.save(conta);

		OperacoesCliente operacao = new OperacoesCliente();
		operacao.setCliente(conta.getCliente());
		operacao.setTipoOperacao(TipoOperacao.SAQUE);
		operacao.setValor(valor);
		operacao.setDataOperacao(LocalDateTime.now());
		operacoesClienteRepository.save(operacao);

		return contaAtualizada;
	}

	public void transferir(Integer numeroAgenciaRemetente, Integer idClienteRemetente, Integer idContaRemetente,
			Integer numeroAgenciaDestinataria, Integer idClienteDestinatario, Integer idContaDestinataria,
			Double valor) {
		if (valor <= 0) {
			throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
		}

		ContaBancaria contaRemetente = contaRepository.findById(idContaRemetente)
				.orElseThrow(() -> new IllegalArgumentException("Conta remetente não encontrada"));

		if (!contaRemetente.getCliente().getId().equals(idClienteRemetente)
				|| !contaRemetente.getCliente().getAgencia().getNumeroAgencia().equals(numeroAgenciaRemetente)) {
			throw new IllegalArgumentException("Conta remetente não pertence ao cliente ou agência corretos.");
		}

		if (contaRemetente.getSaldo() < valor) {
			throw new IllegalArgumentException("Saldo insuficiente na conta remetente.");
		}

		ContaBancaria contaDestinataria = contaRepository.findById(idContaDestinataria)
				.orElseThrow(() -> new IllegalArgumentException("Conta destinatária não encontrada"));

		if (!contaDestinataria.getCliente().getId().equals(idClienteDestinatario)
				|| !contaDestinataria.getCliente().getAgencia().getNumeroAgencia().equals(numeroAgenciaDestinataria)) {
			throw new IllegalArgumentException("Conta destinatária não pertence ao cliente ou agência corretos.");
		}

		contaRemetente.setSaldo(contaRemetente.getSaldo() - valor);
		contaDestinataria.setSaldo(contaDestinataria.getSaldo() + valor);

		contaRepository.save(contaRemetente);
		contaRepository.save(contaDestinataria);

// Registrar a operação de transferência para a conta remetente
		OperacoesCliente operacaoRemetente = new OperacoesCliente();
		operacaoRemetente.setCliente(contaRemetente.getCliente());
		operacaoRemetente.setTipoOperacao(TipoOperacao.TRANSFERENCIA_REMETENTE);
		operacaoRemetente.setValor(-valor); // Valor negativo para indicar saída
		operacaoRemetente.setDataOperacao(LocalDateTime.now());
		operacoesClienteRepository.save(operacaoRemetente);

// Registrar a operação de transferência para a conta destinatária
		OperacoesCliente operacaoDestinatario = new OperacoesCliente();
		operacaoDestinatario.setCliente(contaDestinataria.getCliente());
		operacaoDestinatario.setTipoOperacao(TipoOperacao.TRANSFERENCIA_DESTINATARIO);
		operacaoDestinatario.setValor(valor); // Valor positivo para indicar entrada
		operacaoDestinatario.setDataOperacao(LocalDateTime.now());
		operacoesClienteRepository.save(operacaoDestinatario);
	}

	public Optional<List<ContaBancaria>> listarContasPorCliente(Integer idAgencia, Integer idCliente) {
		return clienteService.obterClienteDeAgencia(idAgencia, idCliente).map(Cliente::getContaBancarias);
	}

	public Optional<ContaBancaria> obterContaPorId(Integer numeroAgencia, Integer idCliente, Integer idConta) {
		return contaRepository.findByNumeroContaAndClienteIdAndClienteAgenciaNumero(idConta, idCliente, numeroAgencia);
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
