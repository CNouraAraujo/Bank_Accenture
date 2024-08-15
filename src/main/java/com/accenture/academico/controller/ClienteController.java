package com.accenture.academico.controller;

import com.accenture.academico.dto.ClienteDTO;
import com.accenture.academico.dto.ClienteEnderecoDTO;
import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.service.AgenciaService;
import com.accenture.academico.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bank")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private AgenciaService agenciaService;

	@GetMapping("/{numeroAgencia}/clientes") // ===============================================================================================
	public ResponseEntity<List<Cliente>> listarClientesPorAgencia(@PathVariable Integer numeroAgencia) {
		Optional<Agencia> agencia = agenciaService.obterAgencia(numeroAgencia);
		if (agencia.isPresent()) {
			List<Cliente> clientes = clienteService.listarClientesPorAgencia(numeroAgencia);
			return ResponseEntity.ok(clientes);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{numeroAgencia}/clientes/{idCliente}") // ===============================================================================================
	public ResponseEntity<Cliente> obterClienteDeAgencia(@PathVariable Integer numeroAgencia,
			@PathVariable Integer idCliente) {
		return clienteService.obterClienteDeAgencia(numeroAgencia, idCliente).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/{numeroAgencia}/clientes")
	public ResponseEntity<Cliente> criarCliente(@PathVariable Integer numeroAgencia, @RequestBody ClienteEnderecoDTO dto) {
		try {
			Cliente clienteSalvo = clienteService.criarCliente(numeroAgencia, dto);
			return ResponseEntity.created(URI.create("/bank/" + numeroAgencia + "/clientes/" + clienteSalvo.getId()))
					.body(clienteSalvo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}

	@DeleteMapping("/{numeroAgencia}/clientes/{idCliente}") // ===============================================================================================
	public ResponseEntity<Void> deletarCliente(@PathVariable Integer numeroAgencia, @PathVariable Integer idCliente) {
		try {
			clienteService.deletarCliente(numeroAgencia, idCliente);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}
	
	@PutMapping("/atualizar/{idCliente}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Integer idCliente, @RequestBody ClienteEnderecoDTO clienteDTO) {
        Optional<Cliente> clienteAtualizado = clienteService.atualizarCliente(idCliente, clienteDTO);
        return clienteAtualizado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
	
	
}
