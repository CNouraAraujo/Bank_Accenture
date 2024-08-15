package com.accenture.academico.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.dto.ClienteEnderecoDTO;
import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.service.AgenciaService;
import com.accenture.academico.service.ClienteService;

@RestController
@RequestMapping("/bank")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private AgenciaService agenciaService;

	@GetMapping("/{idAgencia}/clientes") // ===============================================================================================
	public ResponseEntity<List<Cliente>> listarClientesPorAgencia(@PathVariable Integer idAgencia) {
		Optional<Agencia> agencia = agenciaService.obterAgencia(idAgencia);
		if (agencia.isPresent()) {
			List<Cliente> clientes = clienteService.listarClientesPorAgencia(idAgencia);
			return ResponseEntity.ok(clientes);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{idAgencia}/clientes/{idCliente}") // ===============================================================================================
	public ResponseEntity<Cliente> obterClienteDeAgencia(@PathVariable Integer idAgencia,
			@PathVariable Integer idCliente) {
		return clienteService.obterClienteDeAgencia(idAgencia, idCliente).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/{idAgencia}/clientes")
	public ResponseEntity<Cliente> criarCliente(@PathVariable Integer idAgencia, @RequestBody ClienteEnderecoDTO dto) {
		try {
			Cliente clienteSalvo = clienteService.criarCliente(idAgencia, dto);
			return ResponseEntity.created(URI.create("/bank/" + idAgencia + "/clientes/" + clienteSalvo.getId()))
					.body(clienteSalvo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}

	@DeleteMapping("/{idAgencia}/clientes/{idCliente}") // ===============================================================================================
	public ResponseEntity<Void> deletarCliente(@PathVariable Integer idAgencia, @PathVariable Integer idCliente) {
		try {
			clienteService.deletarCliente(idAgencia, idCliente);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}
}
