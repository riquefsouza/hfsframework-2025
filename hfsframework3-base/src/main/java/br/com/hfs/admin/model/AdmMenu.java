package br.com.hfs.admin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.hfs.admin.controller.form.AdmMenuForm;
import br.com.hfs.admin.vo.MenuVO;
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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * The persistent class for the ADM_MENU database table.
 * 
 */
@Entity
@Table(name = "ADM_MENU")
@NamedQueries({	
	@NamedQuery(name = "AdmMenu.getDescriptionById", query = "SELECT c.description FROM AdmMenu c WHERE c.id = ?1"),
	@NamedQuery(name = "AdmMenu.countNovo", query = "SELECT COUNT(c) FROM AdmMenu c WHERE LOWER(c.description) = ?1"),
	@NamedQuery(name = "AdmMenu.countAntigo", query = "SELECT COUNT(c) FROM AdmMenu c WHERE LOWER(c.description) <> ?1 AND LOWER(c.description) = ?2"),	

	@NamedQuery(name = "AdmMenu.getMenuById", query = "SELECT m FROM AdmMenu m WHERE m.id = ?1"),

	@NamedQuery(name = "AdmMenu.findMenuRoot", 
	query = "SELECT m FROM AdmMenu m left join m.idMenuParent mp left join m.admPage f WHERE m.idMenuParent IS NULL ORDER BY m.order"),
	@NamedQuery(name = "AdmMenu.findMenuRootByDescription", 
	query = "SELECT m FROM AdmMenu m left join m.idMenuParent mp left join m.admPage f WHERE m.idMenuParent IS NULL AND m.description = ?1 ORDER BY m.order"),

	@NamedQuery(name = "AdmMenu.findChildrenMenus", 
	query = "SELECT m FROM AdmMenu m WHERE m.idMenuParent = ?1 ORDER BY m.order"),

	@NamedQuery(name = "AdmMenu.findAdminMenuParentByIdPages", 
	query="SELECT DISTINCT t FROM AdmMenu t WHERE t.id IN " +
	"(SELECT m.idMenuParent FROM AdmMenu m INNER JOIN m.admPage p WHERE p.id IN ?1 AND m.id <= 6) ORDER BY t.id, t.order"),

	@NamedQuery(name = "AdmMenu.findMenuParentByIdPages", 
	query="SELECT DISTINCT t FROM AdmMenu t WHERE t.id IN " +
	"(SELECT m.idMenuParent FROM AdmMenu m INNER JOIN m.admPage p WHERE p.id IN ?1 AND m.id > 6) ORDER BY t.id, t.order"),

	@NamedQuery(name = "AdmMenu.findAdminMenuByIdPages", 
	query="SELECT DISTINCT m FROM AdmMenu m INNER JOIN m.admPage p WHERE p.id IN ?1 " + 
	"AND m.id <= 6 AND m.idMenuParent = ?2 ORDER BY m.id, m.order"),		

	@NamedQuery(name = "AdmMenu.findMenuByIdPages", 
	query="SELECT DISTINCT m FROM AdmMenu m INNER JOIN m.admPage p WHERE p.id IN ?1 " + 
	"AND m.id > 6 AND m.idMenuParent = ?2 ORDER BY m.id, m.order")
	
})

public class AdmMenu implements Serializable, Comparable<AdmMenu> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADM_MENU_ID_GENERATOR")
	@SequenceGenerator(name = "ADM_MENU_ID_GENERATOR", sequenceName = "ADM_MENU_SEQ", initialValue = 1, allocationSize = 1)	
	@Column(name = "MNU_SEQ")
	private Long id;

	/** The description. */
	@NotNull
	@NotBlank
	@Size(min=4, max=255)
	@Column(name = "MNU_DESCRIPTION", unique = true, nullable = false, length = 255)
	private String description;

	/** The order. */
	@Column(name = "MNU_ORDER")
	private Integer order;

	/** The id pagina. */
	@Column(name = "MNU_PAG_SEQ", nullable = true)
	private Long idPage;

	/** The adm pagina. */
	// bi-directional many-to-one association to AdmPage
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "MNU_PAG_SEQ", nullable = true, insertable = false, updatable = false)
	private AdmPage admPage;

	@Column(name = "MNU_PARENT_SEQ", nullable = true)
	private Long idMenuParent;

	/** The adm menu. */
	// bi-directional many-to-one association to AdmMenu
	//@ManyToOne(fetch = FetchType.LAZY) //(cascade={CascadeType.ALL}) //
	//@JoinColumn(name = "MNU_PARENT_SEQ", nullable = true, insertable = false, updatable = false)
	//private AdmMenu admMenuParent;

	@Transient
	private AdmMenu admMenuParent;

	/** The adm menus. */
	// bi-directional many-to-one association to AdmMenu
	//@OrderBy("order")
	//@JsonIgnore
	//@JsonSerialize(using = AdmMenuListSerializer.class)
	//@Fetch(FetchMode.SUBSELECT)
	//@OneToMany(mappedBy = "idMenuParent", fetch = FetchType.EAGER)

	@Transient
	private List<AdmMenu> admSubMenus;

	/**
	 * Instantiates a new adm menu.
	 */
	public AdmMenu() {
		this.admSubMenus = new ArrayList<AdmMenu>();
		clean();
	}
	
	public AdmMenu(String description, Long idMenuParent, Long idPage, Integer order) {
		super();
		this.description = description;
		this.order = order;
		this.idPage = idPage;
		this.idMenuParent = idMenuParent;	
	}
		
	public AdmMenu(AdmMenuForm m) {
		this();
		this.id = m.getId();
		this.description = m.getDescription();
		this.order = m.getOrder();
		this.idPage = m.getIdPage();
		this.idMenuParent = m.getIdMenuParent();
	}

	/**
	 * Limpar.
	 */
	public void clean() {
		this.id = null;
		this.description = null;
		this.order = null;
		this.idPage = null;
		//this.admPage = new AdmPage();
		this.idMenuParent = null;
		this.admSubMenus.clear();
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
	 * Pega o the order.
	 *
	 * @return o the order
	 */
	public Integer getOrder() {
		return this.order;
	}

	/**
	 * Atribui o the order.
	 *
	 * @param order
	 *            o novo the order
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * Pega o the adm menus.
	 *
	 * @return o the adm menus
	 */
	public List<AdmMenu> getAdmSubMenus() {
		if (this.admSubMenus!=null && !this.admSubMenus.isEmpty()){
			Collections.sort(this.admSubMenus, new Comparator<AdmMenu>() {
				@Override
				public int compare(AdmMenu o1, AdmMenu o2) {
					return o1.getOrder().compareTo(o2.getOrder());
				}
			});
		}
		return this.admSubMenus;
	}

	/**
	 * Atribui o the adm menus.
	 *
	 * @param admSubMenus
	 *            o novo the adm menus
	 */
	public void setAdmSubMenus(List<AdmMenu> admSubMenus) {
		this.admSubMenus = admSubMenus;
	}

	/**
	 * Checks if is sub menu.
	 *
	 * @return true, if is sub menu
	 */
	public boolean isSubMenu() {
		return getIdPage() == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AdmMenu m) {
		return getDescription().compareTo(m.getDescription());
	}

	/*
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return this.admPage != null ? this.admPage.getUrl() : null;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.description;
	}

	/**
	 * Pega o the id pagina.
	 *
	 * @return o the id pagina
	 */
	public Long getIdPage() {
		return idPage;
	}

	/**
	 * Atribui o the id pagina.
	 *
	 * @param idPage
	 *            o novo the id pagina
	 */
	public void setIdPage(Long idPage) {
		this.idPage = idPage;
	}
	
	public Long getIdMenuParent() {
		return idMenuParent;
	}

	public void setIdMenuParent(Long idMenuParent) {
		this.idMenuParent = idMenuParent;
	}

	public AdmPage getAdmPage() {
		return admPage;
	}

	public void setAdmPage(AdmPage admPage) {
		this.admPage = admPage;
	}

	public AdmMenu getAdmMenuParent() {
		return admMenuParent;
	}

	public void setAdmMenuParent(AdmMenu admMenuParent) {
		this.admMenuParent = admMenuParent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((idPage == null) ? 0 : idPage.hashCode());
		result = prime * result + ((idMenuParent == null) ? 0 : idMenuParent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdmMenu other = (AdmMenu) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (idPage == null) {
			if (other.idPage != null)
				return false;
		} else if (!idPage.equals(other.idPage))
			return false;
		if (idMenuParent == null) {
			if (other.idMenuParent != null)
				return false;
		} else if (!idMenuParent.equals(other.idMenuParent))
			return false;
		return true;
	}

	/**
	 * Gets the nome recursivo.
	 *
	 * @param m
	 *            the m
	 * @return the nome recursivo
	 */
	private String getNomeRecursivo(AdmMenu m) {
		return m.getAdmPage() == null ? m.getDescription()
				: m.getAdmMenuParent() != null ? getNomeRecursivo(m.getAdmMenuParent()) + " -> " + m.getDescription() : "";
	}
	
	/**
	 * Gets the nome recursivo.
	 *
	 * @return the nome recursivo
	 */
	public String getNomeRecursivo() {
		return this.getNomeRecursivo(this);
	}
	
	public MenuVO toMenuVO() {
		MenuVO m = new MenuVO();
		
		m.setId(id);
		m.setDescription(description);
		m.setOrder(order);
		m.setIdPage(idPage);
		if (admPage!=null) {
				m.setPage(admPage.toPageVO());
		}
		for (AdmMenu admSubMenu : admSubMenus) {
			m.getSubMenus().add(admSubMenu.toMenuVO());
		}
		
		return m;
	}
	
}