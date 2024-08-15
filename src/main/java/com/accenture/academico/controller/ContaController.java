package com.accenture.academico.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.service.ContaService;

@RestController
@RequestMapping("/bank")
public class ContaController {

	@Autowired
	private ContaService contaService;

	@GetMapping("/{numeroAgencia}/clientes/{idCliente}/contas") // ===============================================================================================
	public ResponseEntity<List<ContaBancaria>> listarContasPorCliente(@PathVariable Integer numeroAgencia,
			@PathVariable Integer idCliente) {
		try {
			return contaService.listarContasPorCliente(numeroAgencia, idCliente).map(ResponseEntity::ok)
					.orElseGet(() -> ResponseEntity.notFound().build());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@GetMapping("/{numeroAgencia}/clientes/{idCliente}/contas/{idConta}") // ===============================================================================================
	public ResponseEntity<ContaBancaria> obterContaPorId(@PathVariable Integer numeroAgencia,
			@PathVariable Integer idCliente, @PathVariable Integer idConta) {
		try {
			return contaService.obterContaPorId(numeroAgencia, idCliente, idConta).map(ResponseEntity::ok)
					.orElseGet(() -> ResponseEntity.notFound().build());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@PostMapping("/{numeroAgencia}/clientes/{idCliente}/contas") // ===============================================================================================
	public ResponseEntity<ContaBancaria> criarConta(@PathVariable Integer numeroAgencia, @PathVariable Integer idCliente,
			@RequestBody ContaBancaria contaBancaria) {
		try {
			ContaBancaria contaSalva = contaService.criarConta(numeroAgencia, idCliente, contaBancaria);
			return ResponseEntity
					.created(URI.create(
							"/bank/" + numeroAgencia + "/clientes/" + idCliente + "/contas/" + contaSalva.getNumeroConta()))
					.body(contaSalva);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}

	@DeleteMapping("/{numeroAgencia}/clientes/{idCliente}/contas/{idConta}") // ===============================================================================================
	public ResponseEntity<Void> deletarConta(@PathVariable Integer numeroAgencia, @PathVariable Integer idCliente,
			@PathVariable Integer idConta) {
		try {
			contaService.deletarConta(numeroAgencia, idCliente, idConta);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@PostMapping("/{numeroAgencia}/clientes/{idCliente}/contas/{idConta}/deposito") // ===============================================================================================
	public ResponseEntity<ContaBancaria> depositar(@PathVariable Integer numeroAgencia, @PathVariable Integer idCliente,
			@PathVariable Integer idConta, @RequestBody Map<String, Double> request) {
		try {
			Double valor = request.get("valor");
			ContaBancaria conta = contaService.depositar(idConta, valor);
			return ResponseEntity.ok(conta);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@PostMapping("/{numeroAgencia}/clientes/{idCliente}/contas/{idConta}/saque") // ===============================================================================================
	public ResponseEntity<ContaBancaria> sacar(@PathVariable Integer numeroAgencia, @PathVariable Integer idCliente,
			@PathVariable Integer idConta, @RequestBody Map<String, Double> request) {
		try {
			Double valor = request.get("valor");
			ContaBancaria conta = contaService.sacar(idConta, valor);

			return ResponseEntity.ok(conta);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@PostMapping("/{numeroAgenciaRemetente}/clientes/{idClienteRemetente}/contas/{idContaRemetente}/transferencia") // ===============================================================================================
	public ResponseEntity<String> transferirEntreContas(@PathVariable Integer numeroAgenciaRemetente,
			@PathVariable Integer idClienteRemetente, @PathVariable Integer idContaRemetente,
			@RequestParam Integer numeroAgenciaDestinataria, @RequestParam Integer idClienteDestinatario,
			@RequestParam Integer idContaDestinataria, @RequestBody Double valor) {
		try {
			contaService.transferir(numeroAgenciaRemetente, idClienteRemetente, idContaRemetente, numeroAgenciaDestinataria,
					idClienteDestinatario, idContaDestinataria, valor);

			return ResponseEntity.ok("Transferência realizada com sucesso.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Erro interno no servidor.");
		}
	}
}
