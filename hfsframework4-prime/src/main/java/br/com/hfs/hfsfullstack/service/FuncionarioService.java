package br.com.hfs.hfsfullstack.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.hfs.base.BaseService;
import br.com.hfs.hfsfullstack.model.Funcionario;
import br.com.hfs.hfsfullstack.repository.FuncionarioRepository;

@Service
public class FuncionarioService extends BaseService<Funcionario, Long, FuncionarioRepository> {

	private static final long serialVersionUID = 1L;

	public FuncionarioService() {
		super(Funcionario.class);
	}

	public Page<Funcionario> findByNomeLike(String nome, Pageable pagination){
		return repository.findByNomeLike(nome, pagination);
	}
/*
	public List<Funcionario> findByNomeLike(String nome){
		return repository.findByNomeLike(nome + "%");
	}
*/	
	public List<Funcionario> findPaginated(int pageNumber, int pageSize) {
		return findPaginated("vw_adm_funcionario", "cod_funcionario", pageNumber, pageSize);
	}

	public List<Funcionario> listByRange(int startInterval, int endInterval) {
		return listByRange("vw_adm_funcionario", "cod_funcionario", startInterval, endInterval);
	}

}
