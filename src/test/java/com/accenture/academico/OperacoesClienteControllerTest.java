package com.accenture.academico;

import com.accenture.academico.controller.OperacoesClienteController;
import com.accenture.academico.dto.ExtratoDTO;
import com.accenture.academico.dto.OperacoesClienteDTO;
import com.accenture.academico.model.enums.TipoOperacao;
import com.accenture.academico.repository.ClienteRepository;
import com.accenture.academico.service.ExtratoService;
import com.accenture.academico.service.OperacoesClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OperacoesClienteController.class)
class OperacoesClienteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OperacoesClienteService operacoesClienteService;

	@MockBean
	private ClienteRepository clienteRepository;

	@MockBean
	private ExtratoService extratoService;

	@Test
	void testListarOperacoesPorCliente_ClienteNotFound() throws Exception {
		when(clienteRepository.findById(1)).thenReturn(Optional.empty());

		mockMvc.perform(get("/bank/1/clientes/1/operacoesCliente").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testListarOperacoesPorCliente_Exception() throws Exception {
		when(clienteRepository.findById(1)).thenThrow(new RuntimeException("Erro ao buscar cliente"));

		mockMvc.perform(get("/bank/1/clientes/1/operacoesCliente").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}

	@Test
	void testObterExtrato() throws Exception {
		OperacoesClienteDTO.ClienteResumoDTO clienteResumo = new OperacoesClienteDTO.ClienteResumoDTO();
		clienteResumo.setId(1);
		clienteResumo.setNome("João");
		clienteResumo.setCpf("12345678900");

		OperacoesClienteDTO operacao1 = new OperacoesClienteDTO();
		operacao1.setId(1);
		operacao1.setTipoOperacao(TipoOperacao.DEPOSITO);
		operacao1.setValor(100.0);
		operacao1.setDataOperacao(LocalDateTime.now());
		operacao1.setCliente(clienteResumo);

		OperacoesClienteDTO operacao2 = new OperacoesClienteDTO();
		operacao2.setId(2);
		operacao2.setTipoOperacao(TipoOperacao.SAQUE);
		operacao2.setValor(50.0);
		operacao2.setDataOperacao(LocalDateTime.now().minusDays(1));
		operacao2.setCliente(clienteResumo);

		List<OperacoesClienteDTO> operacoes = Arrays.asList(operacao1, operacao2);

		ExtratoDTO extrato = new ExtratoDTO();
		extrato.setCliente(new ExtratoDTO.ClienteEnderecoDTO());
		extrato.getCliente().setNome("João");
		extrato.getCliente().setCpf("12345678900");
		extrato.getCliente().setTelefone("999999999");
		extrato.setOperacoes(operacoes);

		when(extratoService.obterExtrato(1, 1)).thenReturn(Optional.of(extrato));

		mockMvc.perform(get("/bank/1/clientes/1/extrato").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.cliente.nome").value("João"))
				.andExpect(jsonPath("$.operacoes[0].id").value(1))
				.andExpect(jsonPath("$.operacoes[0].tipoOperacao").value("DEPOSITO"))
				.andExpect(jsonPath("$.operacoes[0].valor").value(100.0))
				.andExpect(jsonPath("$.operacoes[1].id").value(2))
				.andExpect(jsonPath("$.operacoes[1].tipoOperacao").value("SAQUE"))
				.andExpect(jsonPath("$.operacoes[1].valor").value(50.0));
	}

	@Test
	void testObterExtrato_NotFound() throws Exception {
		when(extratoService.obterExtrato(1, 1)).thenReturn(Optional.empty());

		mockMvc.perform(get("/bank/1/clientes/1/extrato").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testObterExtrato_Exception() throws Exception {
		when(extratoService.obterExtrato(1, 1)).thenThrow(new RuntimeException("Erro ao obter extrato"));

		mockMvc.perform(get("/bank/1/clientes/1/extrato").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}
}
