package br.com.hfs.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.hfs.admin.model.AdmMenu;

public interface AdmMenuRepository extends JpaRepository<AdmMenu, Long> {
	
	Page<AdmMenu> findByDescriptionLike(String description, Pageable pagination);
	
	List<AdmMenu> findByDescriptionLike(String description);

	@Query(name = "AdmMenu.getMenuById")
	Optional<AdmMenu> getMenuById(Long id);

	@Query(name = "AdmMenu.findChildrenMenus")
	List<AdmMenu> findChildrenMenus(Long idMenu);

	@Query(name = "AdmMenu.findMenuRoot")
	List<AdmMenu> findMenuRoot();
	
	@Query(name = "AdmMenu.findAdminMenuParentByIdPages")
	List<AdmMenu> findAdminMenuParentByIdPages(List<Long> listaIdPages);

	@Query(name = "AdmMenu.findMenuParentByIdPages")
	List<AdmMenu> findMenuParentByIdPages(List<Long> listaIdPages);

	@Query(name = "AdmMenu.findAdminMenuByIdPages")
	List<AdmMenu> findAdminMenuByIdPages(List<Long> listaIdPages, Long idMenu);

	@Query(name = "AdmMenu.findMenuByIdPages")
	List<AdmMenu> findMenuByIdPages(List<Long> listaIdPages, Long idMenu);

	@Modifying(flushAutomatically = true)
	@Query("UPDATE AdmMenu m SET m.description=?1, m.idMenuParent=?2, m.idPage=?3, m.order=?4 WHERE m.id=?5")
	int updateMenu(String description, Long idMenuParent, Long idPage, Integer order, Long id);

	@Modifying(flushAutomatically = true)
	@Query("DELETE FROM AdmMenu m WHERE m.id=?1")
	int deleteMenu(Long id);

	@Modifying
	@Query(value = "ALTER SEQUENCE public.adm_menu_seq RESTART WITH 1", nativeQuery = true)
	void restartSequence();

	@Modifying(flushAutomatically = true)
	@Query(value = "DELETE FROM public.adm_menu", nativeQuery = true)
	void deleteAllNative();
}
