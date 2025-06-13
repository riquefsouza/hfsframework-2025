package br.com.hfs.admin.repository;

import java.util.List;

import br.com.hfs.admin.model.AdmUserProfile;
import br.com.hfs.base.BaseRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AdmUserProfileRepository extends BaseRepository<AdmUserProfile, Long> {

	public AdmUserProfileRepository() {
		super(AdmUserProfile.class);
	}
	
	public List<AdmUserProfile> findPaginated(int pageNumber, int pageSize) {
		return findPaginated("ADM_USER_PROFILE", "USP_SEQ", pageNumber, pageSize);
	}

	public List<AdmUserProfile> listByRange(int startInterval, int endInterval) {
		return listByRange("ADM_USER_PROFILE", "USP_SEQ", startInterval, endInterval);
	}

}
