package br.com.hfs.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.hfs.admin.model.AdmMenu;
import br.com.hfs.admin.repository.AdmMenuRepository;
import br.com.hfs.admin.vo.MenuVO;
import br.com.hfs.base.BaseService;
import jakarta.transaction.Transactional;

@Service
public class AdmMenuService extends BaseService<AdmMenu, Long, AdmMenuRepository> {

	private static final long serialVersionUID = 1L;

	//@Autowired
	private AdmPageService admPageService;

	public AdmMenuService() {
		super(AdmMenu.class);
		admPageService = new AdmPageService();
	}

	private void setTransient(AdmMenu item) {
		if (item!=null) {
			if (item.getIdMenuParent()!=null){
				Optional<AdmMenu> parent = repository.getMenuById(item.getIdMenuParent());
				if (parent.isPresent()){
					item.setAdmMenuParent(parent.get());
				}				
			}
			admPageService.setTransient(item.getAdmPage());
			item.setAdmSubMenus(repository.findChildrenMenus(item.getId()));
		}
	}

	private List<AdmMenu> setTransient(List<AdmMenu> lista) {
		lista.stream().forEach(item -> setTransient(item));
		return lista;
	}

	public List<AdmMenu> findMenuRoot(){
		return repository.findMenuRoot();
	}
	
	public Optional<AdmMenu> getMenuById(Long id){
		Optional<AdmMenu> item = repository.getMenuById(id);
		if (item.isPresent()) 
			setTransient(item.get());
		return item;
	}

	public List<AdmMenu> findAll() {
		List<AdmMenu> lista = repository.findAll();		
		return setTransient(lista);
	}

	public Page<AdmMenu> findByDescriptionLike(String description, Pageable pagination){
		return repository.findByDescriptionLike(description, pagination);
	}
	
	public List<AdmMenu> findByDescriptionLike(String description){
		List<AdmMenu> lista = repository.findByDescriptionLike(description);
		return setTransient(lista);
	}
	
	public List<AdmMenu> findPaginated(int pageNumber, int pageSize) {
		List<AdmMenu> lista = findPaginated("ADM_MENU", "MNU_SEQ", pageNumber, pageSize);
		return setTransient(lista);
	}

	public List<AdmMenu> listByRange(int startInterval, int endInterval) {
		List<AdmMenu> lista = listByRange("ADM_MENU", "MNU_SEQ", startInterval, endInterval);
		return setTransient(lista);
	}
	
	@Transactional
	public int updateMenu(String description, Long idMenuParent, Long idPage, Integer order, Long id){
		return repository.updateMenu(description, idMenuParent, idPage, order, id);
	}

	@Transactional
	public int deleteMenu(Long id){
		return repository.deleteMenu(id);
	}

	@Transactional
	public void restartSequence(){
		repository.restartSequence();
	}

	@Transactional
	public void deleteAllNative(){
		repository.deleteAllNative();
	}

	@Transactional
	public void deleteWithChildrens(Long id) {
		List<AdmMenu> listaMenuFilhos = repository.findChildrenMenus(id);
		if ((listaMenuFilhos != null) && (!listaMenuFilhos.isEmpty())) {
			for (AdmMenu menuFilho : listaMenuFilhos) {
				this.deleteMenu(menuFilho.getId());
			}
		}
		Optional<AdmMenu> menu = this.getMenuById(id.longValue());
		if (menu.isPresent()) {
			this.deleteMenu(menu.get().getId());
		}
	}

	public List<MenuVO> toListMenuVO(List<AdmMenu> listaMenu){
		List<MenuVO> lista = new ArrayList<MenuVO>();
		for (AdmMenu menu : listaMenu) {
			lista.add(menu.toMenuVO());
		}		
		return lista;
	}
	

}
