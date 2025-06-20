package br.com.hfs.admin.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "ADM_USER_PROFILE", 
	uniqueConstraints = 
		@UniqueConstraint(name = "adm_user_profile_uk",columnNames={"USP_USE_SEQ", "USP_PRF_SEQ"}))
@NamedQueries({
	@NamedQuery(name = "AdmUserProfile.deleteByProfile", query = "DELETE FROM AdmUserProfile fp WHERE fp.profileSeq = ?1"),
	@NamedQuery(name = "AdmUserProfile.deleteByCodUser", query = "DELETE FROM AdmUserProfile fp WHERE fp.userSeq = ?1")
})
public class AdmUserProfile implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADM_USER_PROFILE_ID_GENERATOR")
	@SequenceGenerator(name = "ADM_USER_PROFILE_ID_GENERATOR", sequenceName = "ADM_USER_PROFILE_SEQ", initialValue = 1, allocationSize = 1)
	@Column(name = "USP_SEQ")
	private Long id;
	
	/** The cod usuario. */
	@Column(name = "USP_USE_SEQ", nullable=false)
	private Long userSeq;

	/** The profile seq. */
	@Column(name = "USP_PRF_SEQ", nullable=false)
	private Long profileSeq;	

	/* The adm profile. */
	// bi-directional many-to-one association to AdmProfile
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "USP_PRF_SEQ", nullable=false, insertable = false, updatable = false)
	private AdmProfile admProfile;

	/**
	 * Instantiates a new adm cargo profile.
	 */
	public AdmUserProfile() {
		clean();
	}
	
	public AdmUserProfile(Long userSeq, Long profileSeq) {
		super();
		this.userSeq = userSeq;
		this.profileSeq = profileSeq;
	}

	/**
	 * Limpar.
	 */
	public void clean(){
		this.userSeq = 0L;
		this.profileSeq = 0L;
	}

	/**
	 * Pega o the id.
	 *
	 * @return o the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Atribui o the id.
	 *
	 * @param id
	 *            o novo the id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the user seq.
	 *
	 * @return the user seq
	 */
	public Long getUserSeq() {
		return userSeq;
	}

	/**
	 * Sets the user seq.
	 *
	 * @param userSeq the new user seq
	 */
	public void setUserSeq(Long userSeq) {
		this.userSeq = userSeq;
	}

	/**
	 * Pega o the profile seq.
	 *
	 * @return o the profile seq
	 */
	public Long getProfileSeq() {
		return profileSeq;
	}

	/**
	 * Atribui o the profile seq.
	 *
	 * @param profileSeq
	 *            o novo the profile seq
	 */
	public void setProfileSeq(Long profileSeq) {
		this.profileSeq = profileSeq;
	}

	/*
	 * Pega o the adm profile.
	 *
	 * @return o the adm profile
	 */
	public AdmProfile getAdmProfile() {
		return this.admProfile;
	}

	/**
	 * Atribui o the adm profile.
	 *
	 * @param admProfile
	 *            o novo the adm profile
	 */
	public void setAdmProfile(AdmProfile admProfile) {
		this.admProfile = admProfile;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdmUserProfile other = (AdmUserProfile) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}