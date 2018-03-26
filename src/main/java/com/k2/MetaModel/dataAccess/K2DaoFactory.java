package com.k2.MetaModel.dataAccess;

public interface K2DaoFactory {
	
	public <E> K2Dao<E,?> getDao(Class<E> type);

}
