package com.k2.MetaModel.dataAccess;

import java.io.Serializable;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

public interface K2Dao<E,K extends Serializable> {

	public Class<E> daoType();
	public Class<K> keyType();
	public K key(Serializable ... values);
	public E find(K key);
	public E insert(E entity);
	public E update(E entity);
	public void delete(E Entity);
	public List<E> fetch(CriteriaQuery<E> criteria);
	
}
