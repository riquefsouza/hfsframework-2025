package br.com.hfs.hfsfullstack.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hfs.hfsfullstack.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
	
	//List<Funcionario> findByNomeLike(String nome);

	Page<Funcionario> findByNomeLike(String nome, Pageable pagination);
}
