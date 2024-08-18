package com.accenture.academico.controller;

import com.accenture.academico.model.Agencia;
import com.accenture.academico.repository.ClienteRepository;
import com.accenture.academico.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bank")
public class AgenciaController {

	@Autowired
	private ExtratoService extratoService;

	@Autowired
	private AgenciaService agenciaService;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ContaService contaService;

	@Autowired
	private OperacoesClienteService operacoesClienteService;

	@GetMapping // ===============================================================================================
	public List<Agencia> listarAgencias() {
		return agenciaService.listarAgencias();
	}

	@GetMapping("/{numeroAgencia}")
	public ResponseEntity<Agencia> obterAgencia(@PathVariable Integer numeroAgencia) {
		return agenciaService.obterAgencia(numeroAgencia).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Agencia> criarAgencia(@RequestBody Agencia agencia) {
		try {
			Agencia agenciaSalva = agenciaService.criarAgencia(agencia);
			return ResponseEntity.created(URI.create("/bank/" + agenciaSalva.getIdAgencia())).body(agenciaSalva);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{numeroAgencia}")
	public ResponseEntity<Void> deletarAgencia(@PathVariable Integer numeroAgencia) {
		try {
			agenciaService.deletarAgencia(numeroAgencia);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@PutMapping("/atualizar/{numeroAgencia}")
	public ResponseEntity<Agencia> atualizarAgencia(@PathVariable Integer numeroAgencia, @RequestBody Agencia agencia) {
		Optional<Agencia> agenciaAtualizada = agenciaService.atualizarAgencia(numeroAgencia, agencia);
		return agenciaAtualizada.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

}
