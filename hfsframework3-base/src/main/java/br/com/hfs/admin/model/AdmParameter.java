package br.com.hfs.admin.model;

import java.io.Serializable;

import br.com.hfs.admin.controller.form.AdmParameterForm;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="ADM_PARAMETER")
@NamedQueries({
	@NamedQuery(name = "AdmParameter.getDescriptionById", query = "SELECT c.description FROM AdmParameter c WHERE c.id = ?1"),
	@NamedQuery(name = "AdmParameter.countNovo", query = "SELECT COUNT(c) FROM AdmParameter c WHERE LOWER(c.description) = ?1"),
	@NamedQuery(name = "AdmParameter.countAntigo", query = "SELECT COUNT(c) FROM AdmParameter c WHERE LOWER(c.description) <> ?1 AND LOWER(c.description) = ?2"),
	@NamedQuery(name = "AdmParameter.getValueByCode", query = "SELECT c.value FROM AdmParameter c WHERE c.code= ?1")
})
public class AdmParameter implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ADM_PARAMETER_ID_GENERATOR")
	@SequenceGenerator(name = "ADM_PARAMETER_ID_GENERATOR", sequenceName = "ADM_PARAMETER_SEQ", initialValue = 1, allocationSize = 1)
	@Column(name="PAR_SEQ")
	private Long id;

	/** The code. */
	@NotBlank
	@Size(min=4, max=64)
	@Column(name="PAR_CODE", nullable = false, length = 64)
	private String code;

	/** The description. */
	@NotNull
	@NotBlank
	@NotEmpty	
	@Size(min=4, max=255)
	@Column(name="PAR_DESCRIPTION", unique = true, nullable = false, length = 255)
	private String description;

	/** The value. */
	@Column(name="PAR_VALUE", length = 4000)
	private String value;
	
	/** The id adm parameter category. */
	@Column(name = "PAR_PMC_SEQ", nullable=false)
	private Long idAdmParameterCategory;

	/** The adm parameter category. */
	//bi-directional many-to-one association to AdmParameterCategory
	//@JsonSerialize(using = AdmParameterCategorySerializer.class)
	@ManyToOne(optional = false, fetch=FetchType.EAGER)
	@JoinColumn(name="PAR_PMC_SEQ", nullable=false, insertable = false, updatable = false)
	private AdmParameterCategory admParameterCategory;

	/**
	 * Instantiates a new adm parameter.
	 */
	public AdmParameter() {
		clean();
	}
	
	public AdmParameter(String value, String description, String code, Long idAdmParameterCategory) {
		super();
		this.code = code;
		this.description = description;
		this.value = value;
		this.idAdmParameterCategory = idAdmParameterCategory;
	}

	public AdmParameter(AdmParameterForm p) {
		this();
		this.id = p.getId();
		this.code = p.getCode();
		this.description = p.getDescription();
		this.value = p.getValue();
		this.idAdmParameterCategory = p.getAdmParameterCategory().getId();
	}

	/**
	 * Limpar.
	 */
	public void clean() {
		this.id = null;
		this.code = null;
		this.description = null;
		this.value = null;
		this.admParameterCategory = new AdmParameterCategory();
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
	 * Pega o the code.
	 *
	 * @return o the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Atribui o the code.
	 *
	 * @param code
	 *            o novo the code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Pega o the description.
	 *
	 * @return o the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Atribui o the description.
	 *
	 * @param description
	 *            o novo the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Pega o the value.
	 *
	 * @return o the value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Atribui o the value.
	 *
	 * @param value
	 *            o novo the value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public AdmParameterCategory getAdmParameterCategory() {
		return this.admParameterCategory;
	}
	
	public void setAdmParameterCategory(AdmParameterCategory admParameterCategory) {
		this.admParameterCategory = admParameterCategory;
	}

	public Long getIdAdmParameterCategory() {
		return idAdmParameterCategory;
	}

	public void setIdAdmParameterCategory(Long idAdmParameterCategory) {
		this.idAdmParameterCategory = idAdmParameterCategory;
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
		AdmParameter other = (AdmParameter) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}