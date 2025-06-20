package br.com.hfs.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.hfs.admin.model.AdmParameterCategory;

public interface AdmParameterCategoryRepository extends JpaRepository<AdmParameterCategory, Long> {

	Page<AdmParameterCategory> findByDescriptionLike(String description, Pageable pagination);
	
	List<AdmParameterCategory> findByDescriptionLike(String description);

	@Modifying
	@Query(value = "ALTER SEQUENCE public.adm_parameter_category_seq RESTART WITH 1", nativeQuery = true)
	void restartSequence();

}
