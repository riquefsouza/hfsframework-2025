package br.com.hfs.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.hfs.admin.model.AdmMenu;
import br.com.hfs.admin.model.AdmPage;
import br.com.hfs.admin.model.AdmProfile;

public interface AdmProfileRepository extends JpaRepository<AdmProfile, Long> {

	Page<AdmProfile> findByDescriptionLike(String description, Pageable pagination);
	
	List<AdmProfile> findByDescriptionLike(String description);
	
	@Query(name = "AdmProfile.findProfilesByUser")
	List<AdmProfile> findProfilesByUser(Long idUser);
	
	@Query(name = "AdmProfile.findProfilesByPage")
	List<AdmProfile> findProfilesByPage(Long idPage);

	@Query(name = "AdmProfile.findByGeneral")
	List<AdmProfile> findByGeneral(Boolean geral);

	@Query(name = "AdmProfile.findIdPagesByDescriptionPerfis")
	List<Long> findIdPagesByDescriptionPerfis(List<String> listaProfile);

	@Modifying
	@Query(value = "ALTER SEQUENCE public.adm_profile_seq RESTART WITH 1", nativeQuery = true)
	void restartSequence();

	@Modifying(flushAutomatically = true)
	@Query(value = "delete from public.adm_page_profile", nativeQuery = true)
	void deleteAllPageProfileNative();
	
	@Query(name = "AdmProfile.findAdminMenuParentByProfile")
	List<AdmMenu> findAdminMenuParentByProfile(AdmProfile profile);

	@Query(name = "AdmProfile.findAdminMenuByProfile")
	List<AdmMenu> findAdminMenuByProfile(AdmProfile profile, Long idMenu);

	@Query(name = "AdmProfile.findMenuParentByProfile")
	List<AdmMenu> findMenuParentByProfile(AdmProfile profile);

	@Query(name = "AdmProfile.findMenuByProfile")
	List<AdmMenu> findMenuByProfile(AdmProfile profile, Long idMenu);

	@Query(name = "AdmProfile.findUsersByProfile")
	public List<Long> findUsersByProfile(AdmProfile profile);
	
	@Query(name = "AdmProfile.findMenuParentByIdPerfis")
	public List<AdmMenu> findMenuParentByIdPerfis(List<Long> listaIdProfile);

	@Query(name = "AdmProfile.findMenuByIdPerfis")
	public List<AdmMenu> findMenuByIdPerfis(List<Long> listaIdProfile, Long idMenu);

	@Query(name = "AdmProfile.findAdminMenuParentByIdPerfis")
	public List<AdmMenu> findAdminMenuParentByIdPerfis(List<Long> listaIdProfile);

	@Query(name = "AdmProfile.findAdminMenuByIdPerfis")
	public List<AdmMenu> findAdminMenuByIdPerfis(List<Long> listaIdProfile, Long idMenu);

	@Query(name = "AdmProfile.findPagesByProfile")
	List<AdmPage> findPagesByProfile(AdmProfile profile);
	
}
