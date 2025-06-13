package br.com.hfs.smp.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Component
@Scope("view")
public class RegistroClienteBean {

    private Cliente cliente = new Cliente();

    @Autowired
    private ClienteService clienteService;

    public void salvar() {
        cliente = clienteService.salvar(cliente);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Cliente salvo!"));
    }

    public void buscar() {
        cliente = clienteService.buscar(cliente.getId());
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
