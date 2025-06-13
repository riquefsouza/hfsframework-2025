package br.com.hfs.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.hfs.admin.model.AdmPage;
import br.com.hfs.admin.repository.AdmPageRepository;
import br.com.hfs.base.BaseService;
import jakarta.transaction.Transactional;

@Service
public class AdmPageService extends BaseService<AdmPage, Long, AdmPageRepository> {

	private static final long serialVersionUID = 1L;

	public AdmPageService() {
		super(AdmPage.class);
	}

	public void setTransient(AdmPage item) {
		if (item!=null) {
			List<String> listPageProfiles = new ArrayList<String>();

			if (item.getAdmProfiles()!=null){			
				item.getAdmProfiles().forEach(profile -> {
					item.getAdmIdProfiles().add(profile.getId());
					listPageProfiles.add(profile.getDescription());
				});
				item.setPageProfiles(listPageProfiles.stream().collect(Collectors.joining(",")));
			}
		}
	}
	
	private List<AdmPage> setTransient(List<AdmPage> lista) {
		lista.stream().forEach(item -> setTransient(item));
		return lista;
	}

	public Optional<AdmPage> findById(Long id){
		Optional<AdmPage> item = repository.findById(id);
		if (item.isPresent()) 
			setTransient(item.get());
		return item;
	}

	public List<AdmPage> findAll() {
		List<AdmPage> lista = repository.findAll();		
		return setTransient(lista);
	}

	public Page<AdmPage> findByDescriptionLike(String description, Pageable pagination){
		return repository.findByDescriptionLike(description, pagination);
	}

	public List<AdmPage> findByDescriptionLike(String description){
		List<AdmPage> lista = repository.findByDescriptionLike(description);
		return setTransient(lista);
	}

	public List<AdmPage> findPaginated(int pageNumber, int pageSize) {
		List<AdmPage> lista = findPaginated("ADM_PAGE", "PAG_SEQ", pageNumber, pageSize);
		return setTransient(lista);
	}

	public List<AdmPage> listByRange(int startInterval, int endInterval) {
		List<AdmPage> lista = listByRange("ADM_PAGE", "PAG_SEQ", startInterval, endInterval);
		return setTransient(lista);
	}
	
	@Transactional
	public void restartSequence(){
		repository.restartSequence();
	}

}
