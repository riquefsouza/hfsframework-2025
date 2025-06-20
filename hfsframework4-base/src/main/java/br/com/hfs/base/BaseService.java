package br.com.hfs.base;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hfs.base.pagination.BasePaged;
import br.com.hfs.base.pagination.BasePaging;
import br.com.hfs.util.exceptions.TransactionException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

public abstract class BaseService<T, I extends Serializable, C extends JpaRepository<T, I>>
		implements IBaseCrud<T, I> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The log. */
	//@Inject
	protected Logger log = LogManager.getLogger(BaseService.class);;

	@Autowired
	protected C repository;

	//@Inject
	//protected AplicacaoUtil aplicacaoUtil;
	
	@Autowired
	protected EntityManager em;
	
	private Class<T> clazz;

	public BaseService(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public Class<T> getClazz() {
		return clazz;
	}

	@Override
	public Optional<T> findById(I id) {	
		return repository.findById(id);
	}

	@Override
	public List<T> findAll() {
		return repository.findAll();
	}
	
	@Override
	public Page<T> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}
	
	public BasePaged<T> getPage(int pageNumber, int size, Sort sort, String paramSort, int columnOrder, String columnTitle) {
		PageRequest request = PageRequest.of(pageNumber - 1, size, sort);
        Page<T> objPage = repository.findAll(request);
        return new BasePaged<T>(objPage, BasePaging.of(objPage.getTotalPages(), pageNumber, size, paramSort, columnOrder, columnTitle));
    }
	
	@Override
	public T insert(T bean) throws TransactionException {
		try {
			return repository.saveAndFlush(bean);
		} catch (Exception e) {
			throw new TransactionException(log, ERROR_INSERT + e.getMessage(), e);
		}
	}

	@Override
	public T update(T bean) throws TransactionException {
		try {
			return repository.saveAndFlush(bean);
		} catch (Exception e) {
			throw new TransactionException(log, ERROR_UPDATE + e.getMessage(), e);
		}
	}
	
	@Override
	public void delete(T bean) throws TransactionException {
		try {
	        if (!em.contains(bean)){
	            bean = em.merge(bean);
	        }	        
	        repository.delete(bean);
	        repository.flush();

		} catch (Exception e) {
			throw new TransactionException(log, ERROR_DELETE + e.getMessage(), e);
		}
	}
	
	@Override
	public void deleteById(I id) throws TransactionException {
		try {
			Optional<T> obean = repository.findById(id);
			if (obean.isPresent()) {
				//T bean = obean.get();
				
		        //if (!em.contains(bean)){
		        //    bean = em.merge(bean);
		        //}
				repository.deleteById(id);
				repository.flush();
			}
		} catch (Exception e) {
			throw new TransactionException(log, ERROR_DELETE + e.getMessage(), e);
		}
	}
	
	@Transactional
	public int directDeleteById(I id) throws TransactionException {
		try {
			Query query = em.createQuery("DELETE FROM " + clazz.getName() + " m WHERE m.id = ?1");
			query.setParameter(1, id);
			 
			return query.executeUpdate(); 
		} catch (Exception e) {
			throw new TransactionException(log, ERROR_DELETE + e.getMessage(), e);
		}
	}
	
	@Override
	public List<T> findAll(int start, int max) {
		TypedQuery<T> query = em.createQuery("SELECT x FROM " + clazz.getName() + " x", clazz);
		if (start > 0) {
			query.setFirstResult(start);
		}
		if (max > 0) {
			query.setMaxResults(max);
		}
		return query.getResultList();
	}

	@Override
	public Long count() {
		return repository.count();
	}

	@SuppressWarnings("unchecked")
	protected List<T> findPaginated(String tableName, String pkName, int pageNumber, int pageSize) {
		String sql = "select * from (select tabela.*, " + pkName + " linha from (select * from " + tableName + " order by " + pkName + ") tabela "
				+ "where " + pkName + " < ((?1 * ?2) + 1 )) where linha >= (((?1-1) * ?2) + 1)";

		Query q = em.createNativeQuery(sql, clazz);
		q.setParameter(1, pageNumber);
		q.setParameter(2, pageSize);
		
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> listByRange(String tableName, String pkName, int startInterval, int endInterval) {
		String sql = "select * from (select tabela.*, " + pkName + " linha from (select * from " + tableName + " order by " + pkName + ") tabela "
				+ "where " + pkName + " <= ?2) where linha >= ?1";
		
		Query q = em.createNativeQuery(sql, clazz);
		q.setParameter(1, startInterval);
		q.setParameter(2, endInterval);
		
		return q.getResultList();
	}

	@Override
	public String getFieldById(String field, I id) {
		TypedQuery<T> q = em.createQuery("SELECT x." + field + " FROM " + clazz.getName() + " x WHERE x.id = ?1", clazz);
		q.setParameter(1, id);
		
		return (String) q.getSingleResult();
	}

	@Override
	public boolean thereIsFieldNew(String field, String isNew) {
		TypedQuery<T> q = em.createQuery("SELECT COUNT(x) FROM " + clazz.getName() + " x WHERE LOWER(x." + field + ") = ?1", clazz);
		q.setParameter(1, isNew.toLowerCase());
		Long existe = (Long) q.getSingleResult();
		return (existe > 0);
	}

	@Override
	public boolean thereIsFieldOld(String field, I id, String isNew) {
		String old = getFieldById(field, id);
		
		TypedQuery<T> q = em.createQuery("SELECT COUNT(x) FROM " + clazz.getName() + " x WHERE LOWER(x." + field + ") <> ?1 AND LOWER(x." + field + ") = ?2", clazz);
		q.setParameter(1, old.toLowerCase());
		q.setParameter(2, isNew.toLowerCase());
		Long existe = (Long) q.getSingleResult();
		return (existe > 0);
	}

	@Override
	public List<T> insert(List<T> listEntity) throws TransactionException {
		try {
			return repository.saveAll(listEntity);
		} catch (Exception e) {
			throw new TransactionException(log, ERROR_INSERT + e.getMessage(), e);
		}
	}

	@Override
	public List<T> update(List<T> listEntity) throws TransactionException {
		try {
			return repository.saveAll(listEntity);
		} catch (Exception e) {
			throw new TransactionException(log, ERROR_UPDATE + e.getMessage(), e);
		}
	}

	@Override
	public void delete(List<T> listEntity) throws TransactionException {
		try {
			repository.deleteAll(listEntity);
		} catch (Exception e) {
			throw new TransactionException(log, ERROR_DELETE + e.getMessage(), e);
		}
	}

	public C getRepository() {
		return repository;
	}
	
}
