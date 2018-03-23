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
import com.k2.MetaModel.annotations.MetaService;
import com.k2.MetaModel.annotations.MetaSubType;
import com.k2.Util.StringUtil;
import com.k2.Util.Version.Version;

public class MetaModelService implements Comparable<MetaModelService>{
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	static MetaModelService reflect(MetaModel metaModel, Class<?> serviceClass) {
		return new MetaModelService(metaModel, serviceClass);
	}

	private MetaModel metaModel;
	private MetaService metaService;
	
	private String alias;
	private String title;
	private Version version;
	private String[] modelPackageNames;
	
	private Set<MetaModelClass<?>> managedClasses = new TreeSet<MetaModelClass<?>>();
	private Map<String, MetaModelClass<?>> managedClassesByAlias = new TreeMap<String, MetaModelClass<?>>();
	private Map<Class<?>, MetaModelClass<?>> managedClassesByClass = new HashMap<Class<?>, MetaModelClass<?>>();
	private Set<MetaModelEntity<?>> managedEntities = new TreeSet<MetaModelEntity<?>>();
	private Set<MetaModelEmbeddable<?>> managedEmbeddables = new TreeSet<MetaModelEmbeddable<?>>();
	private Set<MetaModelTransient<?>> managedTransients = new TreeSet<MetaModelTransient<?>>();
	private Set<MetaModelSubType<?,?>> managedSubTypes = new TreeSet<MetaModelSubType<?,?>>();
	
	private MetaModelService(MetaModel metaModel, Class<?> serviceClass) {
		if ( ! serviceClass.isAnnotationPresent(MetaService.class))
			throw new MetaModelError("The given service configuration class {} does not implment the @MetaService annotation.", serviceClass.getName());
		
		this.metaModel = metaModel;

		metaService = serviceClass.getAnnotation(MetaService.class);
		
		alias = (StringUtil.isSet(metaService.alias())) ? metaService.alias() : ClassUtil.alias(serviceClass);
		title = (StringUtil.isSet(metaService.title())) ? metaService.title() : ClassUtil.title(serviceClass);

		logger.info("{} implements service '{}' ({})", metaModel.title(), title, alias);
		
		version = Version.create(metaService.version().major(), metaService.version().minor(), metaService.version().point(), metaService.version().build());

		modelPackageNames = new String[metaService.modelPackageNames().length];
		
		for (int i=0; i< metaService.modelPackageNames().length; i++) {
			
			modelPackageNames[i] = (StringUtil.isSet(metaService.modelPackageNames()[i])) ? metaService.modelPackageNames()[i] : serviceClass.getPackage().getName()+".model";
			
			logger.info("Scanning package {} and sub packages for implementations of @MetaClass", modelPackageNames[i]);
			for (Class<?> k2ManagedTypeClass : ClassUtil.getClasses(modelPackageNames[i], com.k2.MetaModel.annotations.MetaClass.class)) {
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
		return alias.compareTo(o.alias);
	}
	
	public MetaModel getMetaModel() { return metaModel; }
	
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
	
	public String alias() { return alias; }
	public String title() { return title; }
	public Version version() { return version; }
	public String description() { return metaService.description(); }
	public String[] modelPackageNames() { return modelPackageNames; }


}
