package br.com.hfs.smp.cliente;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private static final Map<Integer, Cliente> BANCO_DE_DADOS = new HashMap<>();

    private final Random random = new Random();

    public Cliente salvar(Cliente cliente) {
        if (cliente.getId() == null) {
            cliente.setId(Math.abs(random.nextInt()));
        }

        BANCO_DE_DADOS.put(cliente.getId(), Cliente.copiar(cliente));

        return cliente;
    }

    public Cliente buscar(Integer id) {
        return Cliente.copiar(BANCO_DE_DADOS.get(id));
    }

    public Collection<Cliente> pesquisar() {
        return BANCO_DE_DADOS.values();
    }
    
    public String pesquisar2() {
        return "OMEGA";
    }
    
}
