package com.accenture.academico.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.dto.ExtratoDTO;
import com.accenture.academico.dto.OperacoesClienteDTO;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.repository.ClienteRepository;
import com.accenture.academico.service.ExtratoService;
import com.accenture.academico.service.OperacoesClienteService;

@RestController
@RequestMapping("/bank")
public class OperacoesClienteController {

	@Autowired
	private OperacoesClienteService operacoesClienteService;

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ExtratoService extratoService;

	@GetMapping("/{idAgencia}/clientes/{idCliente}/operacoesCliente")
	public ResponseEntity<List<OperacoesClienteDTO>> listarOperacoesPorCliente(@PathVariable Integer idAgencia,
			@PathVariable Integer idCliente) {
		try {
			Optional<Cliente> clienteOpt = clienteRepository.findById(idCliente);
			if (clienteOpt.isPresent() && clienteOpt.get().getAgencia().getIdAgencia().equals(idAgencia)) {
				List<OperacoesClienteDTO> operacoesDTO = operacoesClienteService.getOperacoesPorCliente(idCliente);
				return ResponseEntity.ok(operacoesDTO);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}
	
	 @GetMapping("/{idAgencia}/clientes/{idCliente}/extrato") // ===============================================================================================
	    public ResponseEntity<ExtratoDTO> obterExtrato(@PathVariable Integer idAgencia, @PathVariable Integer idCliente) {
	        try {
	            Optional<ExtratoDTO> extratoOpt = extratoService.obterExtrato(idAgencia, idCliente);
	            return extratoOpt.map(ResponseEntity::ok)
	                    .orElseGet(() -> ResponseEntity.notFound().build());
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).build();
	        }
	    }
}
