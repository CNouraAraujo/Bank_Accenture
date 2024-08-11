package com.accenture.academico.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.dto.ClienteEnderecoDTO;
import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.model.EnderecoCliente;
import com.accenture.academico.repository.AgenciaRepository;
import com.accenture.academico.repository.ClienteRepository;
import com.accenture.academico.repository.ContaRepository;
import com.accenture.academico.repository.EnderecoClienteRepository;
import com.accenture.academico.service.ContaService;

@RestController
@RequestMapping("/bank")
public class AgenciaController {

	@Autowired
	private AgenciaRepository agenciaRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoClienteRepository enderecoClienteRepository;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ContaService contaService;
	
	@GetMapping
	public List<Agencia> listarAgencias() {
		return agenciaRepository.findAll();
	}

	@GetMapping("/{idAgencia}")
	public ResponseEntity<Agencia> obterAgencia(@PathVariable Integer idAgencia) {
		Optional<Agencia> agencia = agenciaRepository.findById(idAgencia);
		return agencia.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/{idAgencia}/clientes")
	public ResponseEntity<List<Cliente>> listarClientesPorAgencia(@PathVariable Integer idAgencia) {
		Optional<Agencia> agencia = agenciaRepository.findById(idAgencia);
		if (agencia.isPresent()) {
			List<Cliente> clientes = clienteRepository.findAllByAgencia_IdAgencia(idAgencia);
			return ResponseEntity.ok(clientes);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{idAgencia}/clientes/{idCliente}")
	public ResponseEntity<Cliente> obterClienteDeAgencia(@PathVariable Integer idAgencia,
			@PathVariable Integer idCliente) {
		Optional<Agencia> agencia = agenciaRepository.findById(idAgencia);
		if (agencia.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Optional<Cliente> cliente = clienteRepository.findById(idCliente);
		if (cliente.isEmpty() || !cliente.get().getAgencia().getIdAgencia().equals(idAgencia)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(cliente.get());
	}

	@GetMapping("/{idAgencia}/clientes/{idCliente}/contas")
	public ResponseEntity<List<ContaBancaria>> listarContasPorCliente(@PathVariable Integer idAgencia,
			@PathVariable Integer idCliente) {
		try {
			Optional<Agencia> agenciaOpt = agenciaRepository.findById(idAgencia);
			Optional<Cliente> clienteOpt = clienteRepository.findById(idCliente);
			if (agenciaOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			if (clienteOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			Cliente cliente = clienteOpt.get();
			List<ContaBancaria> contaBancarias = cliente.getContaBancarias();
			return ResponseEntity.ok(contaBancarias);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@GetMapping("/{idAgencia}/clientes/{idCliente}/contas/{idConta}")
	public ResponseEntity<ContaBancaria> obterContaPorId(@PathVariable Integer idAgencia,
			@PathVariable Integer idCliente, @PathVariable Integer idConta) {
		try {
			Cliente cliente = clienteRepository.findById(idCliente)
					.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
			Optional<ContaBancaria> contaOpt = contaRepository.findById(idConta);
			if (contaOpt.isPresent()) {
				ContaBancaria contaBancaria = contaOpt.get();
				if (contaBancaria.getCliente().getId().equals(idCliente)) {
					return ResponseEntity.ok(contaBancaria);
				} else {
					return ResponseEntity.status(404).body(null);
				}
			} else {
				return ResponseEntity.status(404).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@PostMapping
	public ResponseEntity<Agencia> criarAgencia(@RequestBody Agencia agencia) {
		try {
			Agencia agenciaSalva = agenciaRepository.save(agencia);
			return ResponseEntity.created(URI.create("/bank/" + agenciaSalva.getIdAgencia())).body(agenciaSalva);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/{idAgencia}/clientes")
	public ResponseEntity<Cliente> criarCliente(@PathVariable Integer idAgencia, @RequestBody ClienteEnderecoDTO dto) {
		try {
			Agencia agencia = agenciaRepository.findById(idAgencia)
					.orElseThrow(() -> new RuntimeException("Agência não encontrada"));

			EnderecoCliente endereco = dto.getEndereco();
			enderecoClienteRepository.save(endereco);

			Cliente cliente = new Cliente();
			cliente.setNome(dto.getNome());
			cliente.setCpf(dto.getCpf());
			cliente.setTelefone(dto.getTelefone());
			cliente.setEndereco(endereco);
			cliente.setAgencia(agencia);

			List<ContaBancaria> contaBancarias = dto.getContas().stream().map(contaDTO -> {
				ContaBancaria contaBancaria = new ContaBancaria();
				contaBancaria.setSaldo(contaDTO.getSaldo());
				contaBancaria.setTipo(contaDTO.getTipo());
				contaBancaria.setCliente(cliente);
				return contaBancaria;
			}).collect(Collectors.toList());
			cliente.setContaBancarias(contaBancarias);
			Cliente clienteSalvo = clienteRepository.save(cliente);
			contaRepository.saveAll(contaBancarias);
			clienteSalvo.setContaBancarias(contaRepository.findByCliente_Id(clienteSalvo.getId()));
			return ResponseEntity.created(URI.create("/bank/" + idAgencia + "/clientes/" + clienteSalvo.getId()))
					.body(clienteSalvo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}

	@PostMapping("/{idAgencia}/clientes/{idCliente}/contas")
	public ResponseEntity<ContaBancaria> criarConta(@PathVariable Integer idAgencia, @PathVariable Integer idCliente,
			@RequestBody ContaBancaria contaBancaria) {
		try {
			Optional<Agencia> agenciaOpt = agenciaRepository.findById(idAgencia);
			Optional<Cliente> clienteOpt = clienteRepository.findById(idCliente);

			if (agenciaOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			if (clienteOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			Cliente cliente = clienteOpt.get();
			contaBancaria.setCliente(cliente);
			ContaBancaria contaSalva = contaRepository.save(contaBancaria);
			cliente.getContaBancarias().add(contaSalva);
			clienteRepository.save(cliente);
			return ResponseEntity
					.created(URI.create(
							"/bank/" + idAgencia + "/clientes/" + idCliente + "/conta/" + contaSalva.getNumeroConta()))
					.body(contaSalva);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}

	@DeleteMapping("/{idAgencia}")
	public ResponseEntity<Void> deletarAgencia(@PathVariable Integer idAgencia) {
		try {
			Optional<Agencia> agenciaOpt = agenciaRepository.findById(idAgencia);
			if (agenciaOpt.isPresent()) {
				agenciaRepository.deleteById(idAgencia);
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@DeleteMapping("/{idAgencia}/clientes/{idCliente}")
	public ResponseEntity<Void> deletarCliente(@PathVariable Integer idAgencia, @PathVariable Integer idCliente) {
		try {
			Optional<Cliente> clienteOpt = clienteRepository.findById(idCliente);
			if (clienteOpt.isPresent() && clienteOpt.get().getAgencia().getIdAgencia().equals(idAgencia)) {
				clienteRepository.deleteById(idCliente);
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@DeleteMapping("/{idAgencia}/clientes/{idCliente}/contas/{idConta}")
	public ResponseEntity<Void> deletarConta(@PathVariable Integer idAgencia, @PathVariable Integer idCliente,
			@PathVariable Integer idConta) {
		try {
			Optional<Cliente> clienteOpt = clienteRepository.findById(idCliente);
			if (clienteOpt.isPresent() && clienteOpt.get().getAgencia().getIdAgencia().equals(idAgencia)) {
				Optional<ContaBancaria> contaOpt = contaRepository.findById(idConta);
				if (contaOpt.isPresent() && contaOpt.get().getCliente().getId().equals(idCliente)) {
					contaRepository.deleteById(idConta);
					return ResponseEntity.noContent().build();
				} else {
					return ResponseEntity.notFound().build();
				}
			} else {
				return ResponseEntity.notFound().build(); 
															
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	@PostMapping("/{idAgencia}/clientes/{idCliente}/contas/{idConta}/deposito")
	public ResponseEntity<ContaBancaria> depositar(
	        @PathVariable Integer idAgencia,
	        @PathVariable Integer idCliente,
	        @PathVariable Integer idConta,
	        @RequestBody Map<String, Double> request) {
	    try {
	        Double valor = request.get("valor");
	        ContaBancaria conta = contaService.depositar(idConta, valor);
	        return ResponseEntity.ok(conta);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).build();
	    }
	}
	
	@PostMapping("/{idAgencia}/clientes/{idCliente}/contas/{idConta}/saque")
	public ResponseEntity<ContaBancaria> sacar(
	        @PathVariable Integer idAgencia,
	        @PathVariable Integer idCliente,
	        @PathVariable Integer idConta,
	        @RequestBody Map<String, Double> request) {
	    try {
	        Double valor = request.get("valor");
	        ContaBancaria conta = contaService.sacar(idConta, valor);
	        return ResponseEntity.ok(conta);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).build();
	    }
	}
	
	@PostMapping("/{idAgenciaRemetente}/clientes/{idClienteRemetente}/contas/{idContaRemetente}/transferencia")
	public ResponseEntity<String> transferirEntreContas(
	        @PathVariable Integer idAgenciaRemetente,
	        @PathVariable Integer idClienteRemetente,
	        @PathVariable Integer idContaRemetente,
	        @RequestParam Integer idAgenciaDestinataria,
	        @RequestParam Integer idClienteDestinataria,
	        @RequestParam Integer idContaDestinataria,
	        @RequestBody Double valor) {
	    try {
	        contaService.transferir(
	            idAgenciaRemetente, idClienteRemetente, idContaRemetente,
	            idAgenciaDestinataria, idClienteDestinataria, idContaDestinataria, valor
	        );
	        return ResponseEntity.ok("Transferência realizada com sucesso.");
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Erro interno no servidor.");
	    }
	}




}
