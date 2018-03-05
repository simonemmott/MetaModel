package dao;

import com.k2.MetaModel.MetaModel;

public interface MetaModelDAO {

	/**
	 * In any application there is only MetaModel. The implementations of MetaModelDAO must be configured to fetch the correct instance of the metadata for the application
	 * Consequently the fetch method of the MetaModelDAO doesn't require a key value to identify the meta data for the application.
	 * 
	 * Normal applications 
	 * @return	The metadata for the current application
	 */
	public MetaModel fetch();
}
