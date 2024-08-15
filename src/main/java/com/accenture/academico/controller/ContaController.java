package com.accenture.academico.controller;

import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bank")
public class ContaController {

	@Autowired
	private ContaService contaService;

	@GetMapping("/{idAgencia}/clientes/{idCliente}/contas") // ===============================================================================================
	public ResponseEntity<List<ContaBancaria>> listarContasPorCliente(@PathVariable Integer idAgencia,
			@PathVariable Integer idCliente) {
		try {
			return contaService.listarContasPorCliente(idAgencia, idCliente).map(ResponseEntity::ok)
					.orElseGet(() -> ResponseEntity.notFound().build());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@GetMapping("/{idAgencia}/clientes/{idCliente}/contas/{idConta}") // ===============================================================================================
	public ResponseEntity<ContaBancaria> obterContaPorId(@PathVariable Integer idAgencia,
			@PathVariable Integer idCliente, @PathVariable Integer idConta) {
		try {
			return contaService.obterContaPorId(idAgencia, idCliente, idConta).map(ResponseEntity::ok)
					.orElseGet(() -> ResponseEntity.notFound().build());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@PostMapping("/{idAgencia}/clientes/{idCliente}/contas") // ===============================================================================================
	public ResponseEntity<ContaBancaria> criarConta(@PathVariable Integer idAgencia, @PathVariable Integer idCliente,
			@RequestBody ContaBancaria contaBancaria) {
		try {
			ContaBancaria contaSalva = contaService.criarConta(idAgencia, idCliente, contaBancaria);
			return ResponseEntity
					.created(URI.create(
							"/bank/" + idAgencia + "/clientes/" + idCliente + "/contas/" + contaSalva.getNumeroConta()))
					.body(contaSalva);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}

	@DeleteMapping("/{idAgencia}/clientes/{idCliente}/contas/{idConta}") // ===============================================================================================
	public ResponseEntity<Void> deletarConta(@PathVariable Integer idAgencia, @PathVariable Integer idCliente,
			@PathVariable Integer idConta) {
		try {
			contaService.deletarConta(idAgencia, idCliente, idConta);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@PostMapping("/{idAgencia}/clientes/{idCliente}/contas/{idConta}/deposito") // ===============================================================================================
	public ResponseEntity<ContaBancaria> depositar(@PathVariable Integer idAgencia, @PathVariable Integer idCliente,
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

	@PostMapping("/{idAgencia}/clientes/{idCliente}/contas/{idConta}/saque") // ===============================================================================================
	public ResponseEntity<ContaBancaria> sacar(@PathVariable Integer idAgencia, @PathVariable Integer idCliente,
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

	@PostMapping("/{idAgenciaRemetente}/clientes/{idClienteRemetente}/contas/{idContaRemetente}/transferencia") // ===============================================================================================
	public ResponseEntity<String> transferirEntreContas(@PathVariable Integer idAgenciaRemetente,
			@PathVariable Integer idClienteRemetente, @PathVariable Integer idContaRemetente,
			@RequestParam Integer idAgenciaDestinataria, @RequestParam Integer idClienteDestinatario,
			@RequestParam Integer idContaDestinataria, @RequestBody Double valor) {
		try {
			contaService.transferir(idAgenciaRemetente, idClienteRemetente, idContaRemetente, idAgenciaDestinataria,
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
