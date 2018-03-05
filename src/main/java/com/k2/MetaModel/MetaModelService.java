package com.k2.MetaModel;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.classes.ClassUtil;
import com.k2.MetaModel.annotations.MetaSubType;
import com.k2.Util.Version.Version;

public class MetaModelService implements Comparable<MetaModelService>{
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private StaticMetaService staticService;
	private MetaModel metaApp;
	
	private Set<MetaModelClass<?>> managedClasses = new TreeSet<MetaModelClass<?>>();
	private Map<String, MetaModelClass<?>> managedClassesByAlias = new TreeMap<String, MetaModelClass<?>>();
	private Map<Class<?>, MetaModelClass<?>> managedClassesByClass = new HashMap<Class<?>, MetaModelClass<?>>();
	private Set<MetaModelEntity<?>> managedEntities = new TreeSet<MetaModelEntity<?>>();
	private Set<MetaModelEmbeddable<?>> managedEmbeddables = new TreeSet<MetaModelEmbeddable<?>>();
	private Set<MetaModelTransient<?>> managedTransients = new TreeSet<MetaModelTransient<?>>();
	private Set<MetaModelSubType<?,?>> managedSubTypes = new TreeSet<MetaModelSubType<?,?>>();
	
	MetaModelService(MetaModel metaApp, StaticMetaService staticService) {
		logger.info("{} implements service '{}' ({})", metaApp.title(), staticService.title(), staticService.alias());
		this.metaApp = metaApp;
		this.staticService = staticService;
		for (String packageName : staticService.typePackageNames()) {
			logger.info("Scanning package {} and sub packages for implementations of @MetaClass", packageName);
			for (Class<?> k2ManagedTypeClass : ClassUtil.getClasses(packageName, com.k2.MetaModel.annotations.MetaClass.class)) {
				MetaModelClass<?> metaClass = MetaModelClass.forClass(this, k2ManagedTypeClass);
				managedClasses.add(metaClass);
				managedClassesByAlias.put(metaClass.alias(), metaClass);
				if (metaClass instanceof MetaModelEntity)
					managedEntities.add((MetaModelEntity<?>) metaClass);
				if (metaClass instanceof MetaModelTransient)
					managedTransients.add((MetaModelTransient<?>) metaClass);
				if (metaClass instanceof MetaModelEmbeddable)
					managedEmbeddables.add((MetaModelEmbeddable<?>) metaClass);
				
				for (Class<?> e : k2ManagedTypeClass.getDeclaredClasses()) {
					if (e.isAnnotationPresent(MetaSubType.class)) {
						managedSubTypes.add(MetaModelSubType.forEnum(metaClass, e));
					}
				}
				
			}
		}
	}

	@Override
	public int compareTo(MetaModelService o) {
		return staticService.alias().compareTo(o.staticService.alias());
	}
	
	public MetaModel getMetaModel() { return metaApp; }
	
	public MetaModelClass<?> getMetaClass(String alias) {
		MetaModelClass<?> mCls = managedClassesByAlias.get(alias);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No class with alias '{}' is managed by the service '{}'({})", alias, title(), alias());
	}
	
	public <T> MetaModelClass<T> getMetaClass(Class<T> cls) {
		@SuppressWarnings("unchecked")
		MetaModelClass<T> mCls = (MetaModelClass<T>) managedClassesByClass.get(cls);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("The class '{}' is not managed by the service '{}'({})", cls.getName(), title(), alias());
	}
	
	public Set<MetaModelClass<?>> getMamagedClasses() { return managedClasses; }
	public Set<MetaModelEntity<?>> getMamagedEntities() { return managedEntities; }
	public Set<MetaModelEmbeddable<?>> getMamagedEmbeddables() { return managedEmbeddables; }
	public Set<MetaModelTransient<?>> getMamagedTransients() { return managedTransients; }
	
	public String alias() { return staticService.alias(); }
	public String title() { return staticService.title(); }
	public Version version() { return staticService.version(); }
	public String description() { return staticService.description(); }

}
