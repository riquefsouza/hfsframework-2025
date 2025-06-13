package br.com.hfs.base.report;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hfs.base.BaseService;
import br.com.hfs.util.interceptors.HandlingExpectedErrors;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
@HandlingExpectedErrors
public abstract class BaseViewReport<T, I extends Serializable, B extends BaseService<T, I, ? extends JpaRepository<T, I>>>
		extends BaseViewReportController implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Autowired
	private B service;

	@Autowired
	private T entity;

	public BaseViewReport() {
		super();
	}

	@PostConstruct
	public void init() {
		super.init();
	}

	public T getEntity() {
		return this.entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public B getService() {
		return service;
	}

}
