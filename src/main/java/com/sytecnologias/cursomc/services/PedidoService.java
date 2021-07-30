package com.sytecnologias.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sytecnologias.cursomc.domain.ItemPedido;
import com.sytecnologias.cursomc.domain.PagamentoComBoleto;
import com.sytecnologias.cursomc.domain.Pedido;
import com.sytecnologias.cursomc.domain.enums.EstadoPagamento;
import com.sytecnologias.cursomc.repositories.ItemPedidoRepository;
import com.sytecnologias.cursomc.repositories.PagamentoRepository;
import com.sytecnologias.cursomc.repositories.PedidoRepository;
import com.sytecnologias.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	ItemPedidoRepository itemPedidoRepository;
	
	public Pedido buscar(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento()); 
		for(ItemPedido ip: obj.getItens()) {
			 ip.setDesconto(0.0);
			 ip.setPreco(produtoService.buscar(ip.getProduto().getId()).getPreco());
			 ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		
		return obj;
	}
}
