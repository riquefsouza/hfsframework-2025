package br.com.hfs.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.hfs.admin.model.AdmUser;

public interface AdmUserRepository extends JpaRepository<AdmUser, Long> {

	@Query(name = "AdmUser.findByLogin")
	Optional<AdmUser> findByLogin(String login);
	
	@Query(name = "AdmUser.findByLikeEmail")
	List<AdmUser> findByLikeEmail(String email);
	
	@Query(value = "SELECT SYS_CONTEXT('USERENV', 'IP_ADDRESS', 15) FROM DUAL", nativeQuery = true)
	List<?> findIPByOracle();

	@Query(value = "SELECT substr(CAST(inet_client_addr() AS VARCHAR),1,strpos(CAST(inet_client_addr() AS VARCHAR),'/')-1)", nativeQuery = true)
	List<?> findIPByPostgreSQL();

	@Query(value = "select set_config('myvars.usuario_login', ?1, false)", nativeQuery = true)
	List<?> setLoginPostgreSQL(String login);

	@Query(value = "select set_config('myvars.usuario_ip', ?1, false)", nativeQuery = true)
	List<?> setIPPostgreSQL(String ip);
}
