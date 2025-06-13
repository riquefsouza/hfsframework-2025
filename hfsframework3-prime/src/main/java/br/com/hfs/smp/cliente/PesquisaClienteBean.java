package br.com.hfs.smp.cliente;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Scope("view")
public class PesquisaClienteBean {

    private Collection<Cliente> clientes = Collections.emptyList();

    @Autowired
    private ClienteService clienteService;

    @PostConstruct
    public void postConstruct() {
        clientes = clienteService.pesquisar();
    }

    public Collection<Cliente> getClientes() {
        return clientes;
    }
}
