package com.accenture.academico.controller;

import com.accenture.academico.model.Agencia;
import com.accenture.academico.repository.ClienteRepository;
import com.accenture.academico.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    @GetMapping("/{idAgencia}")
    public ResponseEntity<Agencia> obterAgencia(@PathVariable Integer idAgencia) {
        return agenciaService.obterAgencia(idAgencia).map(ResponseEntity::ok)
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

    @DeleteMapping("/{idAgencia}")
    public ResponseEntity<Void> deletarAgencia(@PathVariable Integer idAgencia) {
        try {
            agenciaService.deletarAgencia(idAgencia);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}

