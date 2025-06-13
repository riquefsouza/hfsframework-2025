package br.com.hfs.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

public abstract class BaseRepository<T, I extends Serializable> implements PanacheRepository<T> {

	private Class<T> clazz;

	public BaseRepository(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Class<T> getClazz() {
		return clazz;
	}
	
	/*
	public Optional<T> findById(I id) {
		//return (Optional<T>) Optional.ofNullable(em.find(clazz, id));
		return findByIdOptional((Long)id);
	}
	
	
	public List<T> findAll() {
		return getEntityManager().createQuery("SELECT x FROM " + clazz.getName() + " x", clazz).getResultList();
	}
*/
	
	public Integer queryIntegerSingle(String nameQuery, Object... lparams) {
		TypedQuery<Integer> query = getEntityManager().createNamedQuery(nameQuery, Integer.class);
		for(int i = 0; i < lparams.length; i++){
			Object param = lparams[i];
			query.setParameter(i+1, param);
		}
		return query.getSingleResult();
	}

	public Long queryLongSingle(String nameQuery, Object... lparams) {
		TypedQuery<Long> query = getEntityManager().createNamedQuery(nameQuery, Long.class);
		for(int i = 0; i < lparams.length; i++){
			Object param = lparams[i];
			query.setParameter(i+1, param);
		}
		return query.getSingleResult();
	}

	public List<Long> queryLongList(String nameQuery, Object... lparams) {
		TypedQuery<Long> query = getEntityManager().createNamedQuery(nameQuery, Long.class);
		for(int i = 0; i < lparams.length; i++){
			Object param = lparams[i];
			query.setParameter(i+1, param);
		}
		return query.getResultList();
	}

	public String queryStringSingle(String nameQuery, Object... lparams) {
		TypedQuery<String> query = getEntityManager().createNamedQuery(nameQuery, String.class);
		for(int i = 0; i < lparams.length; i++){
			Object param = lparams[i];
			query.setParameter(i+1, param);
		}
		return query.getSingleResult();
	}

	public <X> TypedQuery<X> query(Class<X> resultClass, String nameQuery, Object... lparams) {
		TypedQuery<X> query = getEntityManager().createNamedQuery(nameQuery, resultClass);
		for(int i = 0; i < lparams.length; i++){
			Object param = lparams[i];
			query.setParameter(i+1, param);
		}
		return query;
	}
		
	public List<T> queryList(String nameQuery) {
		//TypedQuery<T> query = getEntityManager().createNamedQuery(nameQuery, this.clazz);
		//return query.getResultList();

		return list("#" + nameQuery);
	}
	
	public List<T> queryList(String nameQuery, Object... lparams) {
		//TypedQuery<T> query = getEntityManager().createNamedQuery(nameQuery, this.clazz);
		//for(int i = 0; i < lparams.length; i++){
		//	Object param = lparams[i];
		//	query.setParameter(i+1, param);
		//}
		//return query.getResultList();
		return list("#" + nameQuery, lparams);
	}
	
	@Transactional
	public T insert(T entity) {		
		//em.persist(entity);
		//em.flush();
		persistAndFlush(entity);
		return entity;
	}
	
	@Transactional
	public T update(T entity) {
		getEntityManager().merge(entity);
		getEntityManager().flush();
		return entity;
	}
	
	/*
	@Transactional
	public void delete(T entity) {
		if (!getEntityManager().contains(entity)) {
			entity = getEntityManager().merge(entity);
		}
		getEntityManager().remove(entity);
		getEntityManager().flush();
	}
	*/
	
	@Transactional
	public boolean deleteById(I id) {
		return deleteById((Long)id);
		/*
		Optional<T> entity = findById(id);
		if (entity.isPresent()) {
			delete(entity.get());
		}
		*/
	}
	
	@Transactional
	public int directDeleteById(I id) {	
		Query query = getEntityManager().createQuery("DELETE FROM " + clazz.getName() + " m WHERE m.id = ?1");
		query.setParameter(1, id);		
		 
		return query.executeUpdate(); 
	}

	public List<T> findAll(int start, int max) {
		TypedQuery<T> query = getEntityManager().createQuery("SELECT x FROM " + clazz.getName() + " x", clazz);
		if (start >= 0) {
			query.setFirstResult(start);
		}
		if (max > 0) {
			query.setMaxResults(max);
		}
		return query.getResultList();
	}

	/*
	public Long count() {
		return (Long) getEntityManager().createQuery("SELECT COUNT(x) FROM " + clazz.getName() + " x", Long.class).getSingleResult();
	}
	*/
	
	@SuppressWarnings("unchecked")
	protected List<T> findPaginated(String tableName, String pkName, int pageNumber, int pageSize) {
		String sql = "select * from (select tabela.*, " + pkName + " linha from (select * from " + tableName + " order by " + pkName + ") tabela "
				+ "where " + pkName + " < ((?1 * ?2) + 1 )) dados where linha >= (((?1-1) * ?2) + 1)";

		Query q = getEntityManager().createNativeQuery(sql, clazz);
		q.setParameter(1, pageNumber);
		q.setParameter(2, pageSize);
		
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> listByRange(String tableName, String pkName, int startInterval, int endInterval) {
		String sql = "select * from (select tabela.*, " + pkName + " linha from (select * from " + tableName + " order by " + pkName + ") tabela "
				+ "where " + pkName + " <= ?2) dados where linha >= ?1";
		
		Query q = getEntityManager().createNativeQuery(sql, clazz);
		q.setParameter(1, startInterval);
		q.setParameter(2, endInterval);
		
		return q.getResultList();
	}
	
	public String getFieldById(String field, I id) {
		try {
			TypedQuery<String> q = getEntityManager().createQuery("SELECT x." + field + " FROM " + clazz.getName() + " x WHERE x.id = ?1", String.class);
			q.setParameter(1, id);
			
			return (String) q.getSingleResult();
		} catch (Exception e) {
			return "";
		}
	}

	public boolean thereIsFieldNew(String field, String isNew) {
		TypedQuery<Long> q = getEntityManager().createQuery("SELECT COUNT(x) FROM " + clazz.getName() + " x WHERE LOWER(x." + field + ") = ?1", Long.class);
		q.setParameter(1, isNew.toLowerCase());
		Long existe = q.getSingleResult();
		return (existe > 0);
	}

	public boolean thereIsFieldOld(String field, I id, String isNew) {
		String old = getFieldById(field, id);
		
		TypedQuery<Long> q = getEntityManager().createQuery("SELECT COUNT(x) FROM " + clazz.getName() + " x WHERE LOWER(x." + field + ") <> ?1 AND LOWER(x." + field + ") = ?2", Long.class);
		q.setParameter(1, old.toLowerCase());
		q.setParameter(2, isNew.toLowerCase());
		Long existe = q.getSingleResult();
		return (existe > 0);
	}
	
	@Transactional
	public List<T> insert(List<T> listEntity) {
		List<T> listOut = new ArrayList<T>();
		listEntity.forEach(entity -> {
			listOut.add(insert(entity));
		});
		return listOut;
	}
	
	@Transactional
	public List<T> update(List<T> listEntity) {
		List<T> listOut = new ArrayList<T>();
		listEntity.forEach(entity -> {
			listOut.add(update(entity));
		});
		return listOut;
	}
	
	@Transactional
	public void delete(List<T> listEntity) {
		listEntity.forEach(entity -> delete(entity));
	}

}
