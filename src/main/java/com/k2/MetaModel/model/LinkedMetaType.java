package com.k2.MetaModel.model;

public interface LinkedMetaType {
	@SuppressWarnings("rawtypes")
	public Class forType();
	@SuppressWarnings("rawtypes")
	public void setMetaModelType(MetaModelType metaType);
}
