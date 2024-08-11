package com.accenture.academico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.dto.ClienteEnderecoDTO;
import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.EnderecoCliente;
import com.accenture.academico.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
    private ClienteRepository clienteRepository;
	
	@Autowired
	private AgenciaService agenciaService;
	
//	@Autowired
//	private EnderecoClienteService enderecoClienteService;
	
	public List<Cliente> listarClientesPorAgencia(Integer idAgencia) {
        return clienteRepository.findAllByAgencia_IdAgencia(idAgencia);
    }
	
	public Optional<Cliente> obterClienteDeAgencia(Integer idAgencia, Integer idCliente) {
	    return clienteRepository.findByIdAndAgencia_IdAgencia(idCliente, idAgencia);
	}
	
//	public Cliente criarCliente(Integer idAgencia, ClienteEnderecoDTO dto) {
//	    // Obtém a agência a partir do id, ou lança uma exceção se não for encontrada.
//	    Agencia agencia = agenciaService.obterAgencia(idAgencia)
//	            .orElseThrow(() -> new IllegalArgumentException("Agência não encontrada."));
//
//	    // Cria e preenche o endereço do cliente com os valores do DTO.
//	    EnderecoCliente enderecoCliente = new EnderecoCliente();
//	    enderecoCliente.setLogradouro(dto.getLogradouro());
//	    enderecoCliente.setCep(dto.getCep());
//	    enderecoCliente.setNumero(dto.getNumero());
//	    enderecoCliente.setComplemento(dto.getComplemento());
//	    enderecoCliente.setEstado(dto.getEstado());
//	    enderecoCliente.setCidade(dto.getCidade());
//	    
//	    // Salva o endereço do cliente.
//	    enderecoCliente = enderecoClienteService.criarEnderecoCliente(enderecoCliente);
//
//	    // Cria e preenche o cliente com os valores do DTO, associando-o à agência e ao endereço.
//	    Cliente cliente = new Cliente();
//	    cliente.setNome(dto.getNome());
//	    cliente.setCpf(dto.getCpf());
//	    cliente.setAgencia(agencia);
//	    cliente.setEnderecoCliente(enderecoCliente);
//
//	    // Salva e retorna o cliente.
//	    return clienteRepository.save(cliente);
//	}


	
}
