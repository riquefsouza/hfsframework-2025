package br.com.hfs.hfsfullstack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.hfs.hfsfullstack.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
	
	@Query(name = "Funcionario.findByNomeLike")
	List<Funcionario> findByNomeLike(String nome);

}
