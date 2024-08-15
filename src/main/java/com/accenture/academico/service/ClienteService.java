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
	
	public void deletarCliente(Integer idAgencia, Integer idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (!cliente.getAgencia().getIdAgencia().equals(idAgencia)) {
            throw new RuntimeException("Cliente não pertence à agência informada");
        }

        clienteRepository.delete(cliente);
    }

	
}
