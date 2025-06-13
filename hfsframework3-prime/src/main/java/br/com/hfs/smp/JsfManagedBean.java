package br.com.hfs.smp;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.hfs.smp.cliente.ClienteService;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;

@Named
public class JsfManagedBean {

    private String titulo = "O managed bean desta página está anotado com @ManagedBean, ou seja, podemos ter beans JSF e Spring em um mesmo projeto!";

    private String clientes = "ERRO";
    
    @Autowired
    private ClienteService clienteService;

    @PostConstruct
    public void postConstruct() {
        clientes = clienteService.pesquisar2();
    }

    public String getClientes() {
    	return clientes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }
}