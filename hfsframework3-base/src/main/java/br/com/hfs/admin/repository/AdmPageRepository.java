package br.com.hfs.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.hfs.admin.model.AdmPage;

public interface AdmPageRepository extends JpaRepository<AdmPage, Long> {

	Page<AdmPage> findByDescriptionLike(String description, Pageable pagination);
	
	List<AdmPage> findByDescriptionLike(String description);
	
	@Modifying
	@Query(value = "ALTER SEQUENCE public.adm_page_seq RESTART WITH 1", nativeQuery = true)
	void restartSequence();

}
