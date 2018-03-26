package com.k2.MetaModel.service;

import java.io.Serializable;

public interface ServiceManager {
	
	public <E> E find(Class<E> type, Serializable ... values);
	public <E> E newEntity(Class<E> type);
	public <E> void save(E entity);
	public <E> void delete(E entity);

}
