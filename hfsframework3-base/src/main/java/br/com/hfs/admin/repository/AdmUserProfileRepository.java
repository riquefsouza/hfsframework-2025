package br.com.hfs.admin.repository;

import br.com.hfs.admin.model.AdmUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdmUserProfileRepository extends JpaRepository<AdmUserProfile, Long> {
	
}
