package br.com.hfs.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.hfs.admin.model.AdmUserProfile;
import br.com.hfs.admin.repository.AdmUserProfileRepository;
import br.com.hfs.base.BaseService;

@Service
public class AdmUserProfileService extends BaseService<AdmUserProfile, Long, AdmUserProfileRepository> {

	private static final long serialVersionUID = 1L;

	public AdmUserProfileService() {
		super(AdmUserProfile.class);
	}

	public List<AdmUserProfile> findPaginated(int pageNumber, int pageSize) {
		return findPaginated("ADM_USER_PROFILE", "USP_SEQ", pageNumber, pageSize);
	}

	public List<AdmUserProfile> listByRange(int startInterval, int endInterval) {
		return listByRange("ADM_USER_PROFILE", "USP_SEQ", startInterval, endInterval);
	}
	
}
