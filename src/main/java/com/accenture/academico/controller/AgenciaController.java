//package com.accenture.academico.controller;
//
//import java.net.URI;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.accenture.academico.dto.ClienteEnderecoDTO;
//import com.accenture.academico.dto.ExtratoDTO;
//import com.accenture.academico.dto.OperacoesClienteDTO;
//import com.accenture.academico.model.Agencia;
//import com.accenture.academico.model.Cliente;
//import com.accenture.academico.model.ContaBancaria;
//import com.accenture.academico.repository.ClienteRepository;
//import com.accenture.academico.service.AgenciaService;
//import com.accenture.academico.service.ClienteService;
//import com.accenture.academico.service.ContaService;
//import com.accenture.academico.service.ExtratoService;
//import com.accenture.academico.service.OperacoesClienteService;
////import com.accenture.academico.service.EnderecoClienteService;
//
//@RestController
//@RequestMapping("/bank")
//public class AgenciaController {
//	
//	@Autowired
//	private ExtratoService extratoService;
//
//	@Autowired
//	private AgenciaService agenciaService;
//
//	@Autowired
//	private ClienteRepository clienteRepository;
//
//	@Autowired
//	private ClienteService clienteService;
//
//
//	@Autowired
//	private ContaService contaService;
//	
//	@Autowired
//	private OperacoesClienteService operacoesClienteService;
//	
//	@GetMapping // ===============================================================================================
//	public List<Agencia> listarAgencias() {
//		return agenciaService.listarAgencias();
//	}
//
//	@GetMapping("/{idAgencia}") // ===============================================================================================
//	public ResponseEntity<Agencia> obterAgencia(@PathVariable Integer idAgencia) {
//		return agenciaService.obterAgencia(idAgencia).map(ResponseEntity::ok)
//				.orElseGet(() -> ResponseEntity.notFound().build());
//	}
//
//	@GetMapping("/{idAgencia}/clientes") // ===============================================================================================
//	public ResponseEntity<List<Cliente>> listarClientesPorAgencia(@PathVariable Integer idAgencia) {
//		Optional<Agencia> agencia = agenciaService.obterAgencia(idAgencia);
//		if (agencia.isPresent()) {
//			List<Cliente> clientes = clienteService.listarClientesPorAgencia(idAgencia);
//			return ResponseEntity.ok(clientes);
//		} else {
//			return ResponseEntity.notFound().build();
//		}
//	}
//
//	@GetMapping("/{idAgencia}/clientes/{idCliente}") // ===============================================================================================
//	public ResponseEntity<Cliente> obterClienteDeAgencia(@PathVariable Integer idAgencia,
//			@PathVariable Integer idCliente) {
//		return clienteService.obterClienteDeAgencia(idAgencia, idCliente).map(ResponseEntity::ok)
//				.orElseGet(() -> ResponseEntity.notFound().build());
//	}
//
//	@GetMapping("/{idAgencia}/clientes/{idCliente}/contas") // ===============================================================================================
//	public ResponseEntity<List<ContaBancaria>> listarContasPorCliente(@PathVariable Integer idAgencia,
//			@PathVariable Integer idCliente) {
//		try {
//			return contaService.listarContasPorCliente(idAgencia, idCliente).map(ResponseEntity::ok)
//					.orElseGet(() -> ResponseEntity.notFound().build());
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(500).build();
//		}
//	}
//
//	@GetMapping("/{idAgencia}/clientes/{idCliente}/contas/{idConta}") // ===============================================================================================
//	public ResponseEntity<ContaBancaria> obterContaPorId(@PathVariable Integer idAgencia,
//			@PathVariable Integer idCliente, @PathVariable Integer idConta) {
//		try {
//			return contaService.obterContaPorId(idAgencia, idCliente, idConta).map(ResponseEntity::ok)
//					.orElseGet(() -> ResponseEntity.notFound().build());
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(500).build();
//		}
//	}
//
//	@PostMapping // ===============================================================================================
//	public ResponseEntity<Agencia> criarAgencia(@RequestBody Agencia agencia) {
//		try {
//			Agencia agenciaSalva = agenciaService.criarAgencia(agencia);
//			return ResponseEntity.created(URI.create("/bank/" + agenciaSalva.getIdAgencia())).body(agenciaSalva);
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().build();
//		}
//	}
//
//	@PostMapping("/{idAgencia}/clientes")
//    public ResponseEntity<Cliente> criarCliente(@PathVariable Integer idAgencia, @RequestBody ClienteEnderecoDTO dto) {
//        try {
//            Cliente clienteSalvo = clienteService.criarCliente(idAgencia, dto);
//            return ResponseEntity.created(URI.create("/bank/" + idAgencia + "/clientes/" + clienteSalvo.getId()))
//                    .body(clienteSalvo);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(null);
//        }
//    }
//
//	@PostMapping("/{idAgencia}/clientes/{idCliente}/contas") // ===============================================================================================
//	public ResponseEntity<ContaBancaria> criarConta(@PathVariable Integer idAgencia, @PathVariable Integer idCliente,
//	        @RequestBody ContaBancaria contaBancaria) {
//	    try {
//	        ContaBancaria contaSalva = contaService.criarConta(idAgencia, idCliente, contaBancaria);
//	        return ResponseEntity
//	                .created(URI.create("/bank/" + idAgencia + "/clientes/" + idCliente + "/contas/" + contaSalva.getNumeroConta()))
//	                .body(contaSalva);
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return ResponseEntity.status(500).body(null);
//	    }
//	}
//
//
//	@DeleteMapping("/{idAgencia}") // ===============================================================================================
//    public ResponseEntity<Void> deletarAgencia(@PathVariable Integer idAgencia) {
//        try {
//            agenciaService.deletarAgencia(idAgencia);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//	@DeleteMapping("/{idAgencia}/clientes/{idCliente}") // ===============================================================================================
//    public ResponseEntity<Void> deletarCliente(@PathVariable Integer idAgencia, @PathVariable Integer idCliente) {
//        try {
//            clienteService.deletarCliente(idAgencia, idCliente);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//	@DeleteMapping("/{idAgencia}/clientes/{idCliente}/contas/{idConta}")  // ===============================================================================================
//    public ResponseEntity<Void> deletarConta(@PathVariable Integer idAgencia, @PathVariable Integer idCliente,
//            @PathVariable Integer idConta) {
//        try {
//            contaService.deletarConta(idAgencia, idCliente, idConta);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//	 @PostMapping("/{idAgencia}/clientes/{idCliente}/contas/{idConta}/deposito") // ===============================================================================================
//	    public ResponseEntity<ContaBancaria> depositar(@PathVariable Integer idAgencia, @PathVariable Integer idCliente,
//	            @PathVariable Integer idConta, @RequestBody Map<String, Double> request) {
//	        try {
//	            Double valor = request.get("valor");
//	            ContaBancaria conta = contaService.depositar(idConta, valor);
//	            return ResponseEntity.ok(conta);
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return ResponseEntity.status(500).build();
//	        }
//	    }
//	
//	@GetMapping("/{idAgencia}/clientes/{idCliente}/operacoesCliente") 
//	public ResponseEntity<List<OperacoesClienteDTO>> listarOperacoesPorCliente(@PathVariable Integer idAgencia,
//	        @PathVariable Integer idCliente) {
//	    try {
//	        Optional<Cliente> clienteOpt = clienteRepository.findById(idCliente);
//	        if (clienteOpt.isPresent() && clienteOpt.get().getAgencia().getIdAgencia().equals(idAgencia)) {
//	            List<OperacoesClienteDTO> operacoesDTO = operacoesClienteService.getOperacoesPorCliente(idCliente);
//	            return ResponseEntity.ok(operacoesDTO);
//	        } else {
//	            return ResponseEntity.notFound().build();
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return ResponseEntity.status(500).build();
//	    }
//	}
//
//
//	@PostMapping("/{idAgencia}/clientes/{idCliente}/contas/{idConta}/saque") // ===============================================================================================
//    public ResponseEntity<ContaBancaria> sacar(@PathVariable Integer idAgencia, @PathVariable Integer idCliente,
//                                               @PathVariable Integer idConta, @RequestBody Map<String, Double> request) {
//        try {
//            Double valor = request.get("valor");
//            ContaBancaria conta = contaService.sacar(idConta, valor);
//
//            return ResponseEntity.ok(conta);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).build();
//        }
//    }
//	
//	
//	   
//
//	    @GetMapping("/{idAgencia}/clientes/{idCliente}/extrato") // ===============================================================================================
//	    public ResponseEntity<ExtratoDTO> obterExtrato(@PathVariable Integer idAgencia, @PathVariable Integer idCliente) {
//	        try {
//	            Optional<ExtratoDTO> extratoOpt = extratoService.obterExtrato(idAgencia, idCliente);
//	            return extratoOpt.map(ResponseEntity::ok)
//	                    .orElseGet(() -> ResponseEntity.notFound().build());
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return ResponseEntity.status(500).build();
//	        }
//	    }
//	
//
//
//
//	    @PostMapping("/{idAgenciaRemetente}/clientes/{idClienteRemetente}/contas/{idContaRemetente}/transferencia") // ===============================================================================================
//	    public ResponseEntity<String> transferirEntreContas(@PathVariable Integer idAgenciaRemetente,
//	                                                         @PathVariable Integer idClienteRemetente, @PathVariable Integer idContaRemetente,
//	                                                         @RequestParam Integer idAgenciaDestinataria, @RequestParam Integer idClienteDestinatario,
//	                                                         @RequestParam Integer idContaDestinataria, @RequestBody Double valor) {
//	        try {
//	            contaService.transferir(idAgenciaRemetente, idClienteRemetente, idContaRemetente,
//	                    idAgenciaDestinataria, idClienteDestinatario, idContaDestinataria, valor);
//
//	            return ResponseEntity.ok("Transferência realizada com sucesso.");
//	        } catch (IllegalArgumentException e) {
//	            return ResponseEntity.badRequest().body(e.getMessage());
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return ResponseEntity.status(500).body("Erro interno no servidor.");
//	        }
//	    }
//
//	
//	
//
//}

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.model.Agencia;
import com.accenture.academico.service.AgenciaService;

@RestController
@RequestMapping("/bank")
public class AgenciaController {

    @Autowired
    private AgenciaService agenciaService;

    @GetMapping
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
    
    @PutMapping("/atualizar/{idAgencia}")
    public ResponseEntity<Agencia> atualizarAgencia(@PathVariable Integer idAgencia, @RequestBody Agencia agencia) {
        Optional<Agencia> agenciaAtualizada = agenciaService.atualizarAgencia(idAgencia, agencia);
        return agenciaAtualizada.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

