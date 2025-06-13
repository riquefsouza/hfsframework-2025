package br.com.hfs.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.hfs.admin.controller.dto.MenuItemDTO;
import br.com.hfs.admin.model.AdmMenu;
import br.com.hfs.admin.model.AdmPage;
import br.com.hfs.admin.model.AdmProfile;
import br.com.hfs.admin.model.AdmUser;
import br.com.hfs.admin.repository.AdmMenuRepository;
import br.com.hfs.admin.repository.AdmProfileRepository;
import br.com.hfs.admin.vo.AuthenticatedUserVO;
import br.com.hfs.admin.vo.MenuVO;
import br.com.hfs.admin.vo.PermissionVO;
import br.com.hfs.base.BaseService;
import jakarta.transaction.Transactional;

@Service
public class AdmProfileService extends BaseService<AdmProfile, Long, AdmProfileRepository> {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AdmMenuRepository menuRepository;
	
	//@Autowired
	private AdmMenuService admMenuService;
	
	public AdmProfileService() {
		super(AdmProfile.class);
		admMenuService = new AdmMenuService();
	}
	
	public Page<AdmProfile> findByDescriptionLike(String description, Pageable pagination){
		return repository.findByDescriptionLike(description, pagination);
	}
	
	public List<AdmProfile> findByDescriptionLike(String description){
		return repository.findByDescriptionLike(description);
	}
	
	public List<AdmProfile> findPaginated(int pageNumber, int pageSize) {
		return findPaginated("ADM_PROFILE", "PRF_SEQ", pageNumber, pageSize);
	}

	public List<AdmProfile> listByRange(int startInterval, int endInterval) {
		return listByRange("ADM_PROFILE", "PRF_SEQ", startInterval, endInterval);
	}

	@Transactional
	public void restartSequence(){
		repository.restartSequence();
	}

	@Transactional
	public void deleteAllPageProfileNative(){
		repository.deleteAllPageProfileNative();
	}
	
	public List<AdmProfile> findProfilesByUser(Long idUser) {
		return repository.findProfilesByUser(idUser);
	}
	
	public List<AdmProfile> findProfilesByPage(Long idPage) {
		return repository.findProfilesByPage(idPage);
	}
	
	public List<MenuItemDTO> mountMenuItem(List<String> listaProfile) {
		List<MenuItemDTO> lista = new ArrayList<MenuItemDTO>();
				
		this.findMenuParentByDescriptionPerfis(listaProfile).forEach(menu -> {			
			List<MenuItemDTO> item = new ArrayList<MenuItemDTO>();
			
			menu.getAdmSubMenus().forEach(submenu -> {
				MenuItemDTO submenuVO = new MenuItemDTO(submenu.getDescription(), submenu.getUrl());
				item.add(submenuVO);
			});
			
			MenuItemDTO vo = new MenuItemDTO(menu.getDescription(), menu.getUrl(), item);
			lista.add(vo);
		});
		
		this.findAdminMenuParentByDescriptionPerfis(listaProfile).forEach(menu -> {			
			List<MenuItemDTO> item = new ArrayList<MenuItemDTO>();
			
			menu.getAdmSubMenus().forEach(submenu -> {
				MenuItemDTO submenuVO = new MenuItemDTO(submenu.getDescription(), submenu.getUrl());
				item.add(submenuVO);
			});
			
			MenuItemDTO vo = new MenuItemDTO(menu.getDescription(), menu.getUrl(), item);
			lista.add(vo);
		});
		
		return lista;
	}

	public List<AdmMenu> findMenuParentByDescriptionPerfis(List<String> listaProfile){
		List<Long> listaIdPages = repository.findIdPagesByDescriptionPerfis(listaProfile);
		List<AdmMenu> lista = menuRepository.findMenuParentByIdPages(listaIdPages);

		for (AdmMenu admMenu : lista) {
			List<Long> sublistaIdPages = repository.findIdPagesByDescriptionPerfis(listaProfile);
			admMenu.setAdmSubMenus(menuRepository.findMenuByIdPages(sublistaIdPages, admMenu.getId()));
		}
		return lista;
	}

	public List<AdmMenu> findAdminMenuParentByDescriptionPerfis(List<String> listaProfile){
		List<Long> listaIdPages = repository.findIdPagesByDescriptionPerfis(listaProfile);
		List<AdmMenu> lista = menuRepository.findAdminMenuParentByIdPages(listaIdPages);

		for (AdmMenu admMenu : lista) {
			List<Long> sublistaIdPages = repository.findIdPagesByDescriptionPerfis(listaProfile);
			admMenu.setAdmSubMenus(menuRepository.findAdminMenuByIdPages(sublistaIdPages, admMenu.getId()));
		}
		return lista;
	}

	public List<AdmProfile> findByGeneral(Boolean geral) {
		return repository.findByGeneral(geral);
	}

	public List<PermissionVO> getPermission(AuthenticatedUserVO authenticatedUser) {
		List<PermissionVO> lista = new ArrayList<PermissionVO>();
		PermissionVO permission;
		
		AdmUser admUser = new AdmUser(authenticatedUser.getUser());
		List<AdmProfile> profiles = repository.findProfilesByUser(admUser.getId());
		List<AdmProfile> perfisGeral = repository.findByGeneral(Boolean.TRUE);
		for (AdmProfile perfilGeral : perfisGeral) {
			if (!profiles.contains(perfilGeral)) {
				profiles.add(perfilGeral);
			}
		}

		for (AdmProfile profile : profiles) {
			permission = new PermissionVO();

			permission.setProfile(profile.toProfileVO());

			for (AdmPage admPage : profile.getAdmPages()) {
				permission.getPages().add(admPage.toPageVO());
			}
			
			lista.add(permission);
		}
		
		return lista;
	}
	
	public List<AdmMenu> findAdminMenuParentByProfile(AdmProfile profile){
		List<AdmMenu> lista = repository.findAdminMenuParentByProfile(profile);
		for (AdmMenu admMenu : lista) {
			admMenu.setAdmSubMenus(repository.findAdminMenuByProfile(profile, admMenu.getId()));
		}
		return lista;
	}
		
	public List<AdmMenu> findMenuParentByProfile(AdmProfile profile){
		List<AdmMenu> lista = repository.findMenuParentByProfile(profile);
		for (AdmMenu admMenu : lista) {
			admMenu.setAdmSubMenus(repository.findMenuByProfile(profile, admMenu.getId()));
		}
		return lista;
	}
	
	public List<AdmMenu> findMenuParentByIdPerfis(List<Long> listaIdProfile){
		List<AdmMenu> lista = repository.findMenuParentByIdPerfis(listaIdProfile);
		for (AdmMenu admMenu : lista) {
			admMenu.setAdmSubMenus(repository.findMenuByIdPerfis(listaIdProfile, admMenu.getId()));
		}
		return lista;
	}
	
	public List<MenuVO> findMenuParentByProfile(List<Long> listaIdProfile){
		List<AdmMenu> listaMenuParent = this.findMenuParentByIdPerfis(listaIdProfile);
		return admMenuService.toListMenuVO(listaMenuParent);		
	}
	
	public List<AdmMenu> findAdminMenuParentByIdPerfis(List<Long> listaIdProfile){
		List<AdmMenu> lista = repository.findAdminMenuParentByIdPerfis(listaIdProfile);
		for (AdmMenu admMenu : lista) {
			admMenu.setAdmSubMenus(repository.findAdminMenuByIdPerfis(listaIdProfile, admMenu.getId()));
		}
		return lista;
	}
	
	public List<MenuVO> findAdminMenuParentByProfile(List<Long> listaIdProfile){
		List<AdmMenu> listaAdminMenuParent = this.findAdminMenuParentByIdPerfis(listaIdProfile);						
		return admMenuService.toListMenuVO(listaAdminMenuParent);
	}

	public List<AdmPage> findPagesByProfile(AdmProfile profile){
		return repository.findPagesByProfile(profile);
	}
	
	
}
