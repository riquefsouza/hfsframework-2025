package br.com.hfs.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.hfs.base.report.BaseReportImpl;
import br.com.hfs.base.report.BaseViewReportController;
import br.com.hfs.base.report.IBaseReport;
import br.com.hfs.base.report.IBaseViewReport;
import br.com.hfs.base.report.ReportGroupVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

public abstract class BaseViewRegisterPaged<T, 
	I extends Serializable, 
	B extends BaseService<T, I, ? extends JpaRepository<T, I>>
		> extends BaseViewReportController implements IBaseViewReport {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Autowired
	protected B service;

	private String listPage;

	private String editPage;
	
	private String reportName;
	
	private Boolean forceDownload;
	
	private Class<T> clazz;
	
	public BaseViewRegisterPaged(String listPage, String editPage, String reportName, Class<T> clazz) {
		super();
		this.forceDownload = false;
		
		log = LoggerFactory.getLogger(BaseViewRegisterPaged.class);
		
		this.listPage = listPage;
		this.editPage = editPage;
		this.reportName = reportName;
		this.clazz = clazz;
	}

	@GetMapping
	public String listPaged(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size, 
			@RequestParam(value = "sort", required = false, defaultValue = "ASC,id") String sort,
			@RequestParam(value = "columnOrder", required = false, defaultValue = "0") int columnOrder,
			@RequestParam(value = "columnTitle", required = false, defaultValue = "id") String columnTitle,			
			Model model, HttpServletRequest request) {
		
		Sort sorted = Sort.by(Direction.ASC, "id");
	    String[] paramSort = sort.split(",", 2);
	    
	    if (paramSort.length > 0) {
	    	if (paramSort[0].toUpperCase().equals("ASC")) 
	    		sorted = Sort.by(Direction.ASC, paramSort[1].toLowerCase());
	    	else
	    		sorted = Sort.by(Direction.DESC, paramSort[1].toLowerCase());
	    }	    
	    
	    String uri = request.getRequestURI().replaceFirst(request.getContextPath(), "").trim();
	    model.addAttribute("pagedRequestURI", uri);
	    
		model.addAttribute("pagedBean", service.getPage(pageNumber, size, sorted, sort, columnOrder, columnTitle));
		return getListPage();
	}
	 
	/*
	@GetMapping
	public ModelAndView list(T bean) {
		Optional<ModelAndView> mv = getPage(getListPage());
		if (mv.isPresent()) {
			mv.get().addObject("bean", bean);
			
			List<T> lista = service.findAll();
			mv.get().addObject("listBean", lista);
		}
		return mv.get();
	}
	*/

	@GetMapping("/add")
	public ModelAndView add(T bean) throws Exception {
		Optional<ModelAndView> mv = getPage(getEditPage());
		
		if (mv.isPresent()) {
			bean = clazz.getDeclaredConstructor().newInstance();
			mv.get().addObject("bean", bean);
		}
		
		return mv.get();
	}
	
	@GetMapping("/edit/{id}")
	public ModelAndView edit(@PathVariable I id) {
		Optional<ModelAndView> mv = getPage(getEditPage());
		
		Optional<T> obj = service.findById(id);
		mv.get().addObject("bean", obj.get());
		
		return mv.get();
	}

	@PostMapping
	public ModelAndView save(@Valid @ModelAttribute T bean, 
			BindingResult result, RedirectAttributes attributes) {
		Optional<ModelAndView> mv = getPage(this.listPage);

		if (result.hasErrors()){
			mv.get().setViewName(this.editPage);
			return mv.get();
		}
		
		try {
			
			//if (bean.getId()==null) 
				//this.service.insert(bean);
			//else
				this.service.update(bean);
			
			mv.get().addObject("bean", bean);
			
			//List<T> lista = service.findAll();
			//mv.get().addObject("listBean", lista);
			mv.get().addObject("pagedBean", service.getPage(1, 10, Sort.by(Direction.ASC, "id"), "ASC,id", 0, "id"));
						
		} catch (RestClientException e) {
			this.showDangerMessage(mv.get(), e);
			return mv.get();
		}
		
		return mv.get();
	}

	protected ModelAndView saveCallableBefore(@Valid @ModelAttribute T bean, 
			BindingResult result, RedirectAttributes attributes, Callable<String> fnc) {
		Optional<ModelAndView> mv = getPage(this.listPage);

		if (result.hasErrors()){
			mv.get().setViewName(this.editPage);
			return mv.get();
		}
		
		try {
			
			if (fnc!=null){
				fnc.call();
			}
			
			//if (bean.getId()==null) 
				//this.service.insert(bean);
			//else
				this.service.update(bean);
			
			mv.get().addObject("bean", bean);
			
		} catch (Exception e) {
			this.showDangerMessage(mv.get(), e);
			return mv.get();
		}
		
		return mv.get();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<T> delete(@PathVariable I id) {
		
		Optional<T> obj = service.findById(id);

		if (!obj.isPresent()) {
			log.info("DELETE NOT FOUND: " + id);
			return ResponseEntity.notFound().build();
		}

		service.delete(obj.get());

		return ResponseEntity.ok(obj.get());
		
		//return getListPage();
	}

	@ResponseBody
	@GetMapping(value = "/export")	
	public String export(HttpServletResponse response,
			@RequestParam(name = "reportType", required = true, defaultValue = "PDF") String reportType,
			@RequestParam(name = "forceDownload", required = true, defaultValue = "false") String forceDownload,
			@RequestParam(name = "params", required = false) List<String> params) {
		
		Map<String, Object> params1 = this.getParametros();
		params1.put("PARAMETER1", "");

		IBaseReport report = new BaseReportImpl(reportName.isEmpty() ? "report" : reportName);
		this.setReportType(reportType);	
		this.export(report, service.findAll(), 
					params1, Boolean.parseBoolean(forceDownload));
		return "";
	}
	
	@ModelAttribute("listReportType")
	public List<ReportGroupVO> getListReportType() {
		return super.getListReportType();
	}

	public String getListPage() {
		return listPage;
	}

	public String getEditPage() {
		return editPage;
	}

	public Boolean getForceDownload() {
		return forceDownload;
	}

	public void setForceDownload(Boolean forceDownload) {
		this.forceDownload = forceDownload;
	}

}
