package com.k2.MetaModel.model;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.classes.ClassUtil;
import com.k2.MetaModel.annotations.MetaType;
import com.k2.MetaModel.model.types.MetaModelClass;
import com.k2.MetaModel.model.types.MetaModelInterface;
import com.k2.MetaModel.model.types.MetaModelPrimitive;
import com.k2.MetaModel.model.types.MetaModelSubType;
import com.k2.MetaModel.model.types.classes.MetaModelEmbeddable;
import com.k2.MetaModel.model.types.classes.MetaModelEntity;
import com.k2.MetaModel.model.types.classes.MetaModelNative;
import com.k2.MetaModel.model.types.classes.MetaModelTransient;
import com.k2.MetaModel.MetaModelError;
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
	
	private Set<MetaModelType<?>> managedTypes = new TreeSet<MetaModelType<?>>();
	private Map<String, MetaModelType<?>> managedTypesByAlias = new TreeMap<String, MetaModelType<?>>();
	private Map<Class<?>, MetaModelType<?>> managedTypesByClass = new HashMap<Class<?>, MetaModelType<?>>();
	
	private Set<MetaModelClass<?>> managedClasses = new TreeSet<MetaModelClass<?>>();
	private Map<String, MetaModelClass<?>> managedClassesByAlias = new TreeMap<String, MetaModelClass<?>>();
	private Map<Class<?>, MetaModelClass<?>> managedClassesByClass = new HashMap<Class<?>, MetaModelClass<?>>();
	
	private Set<MetaModelInterface<?>> managedInterfaces = new TreeSet<MetaModelInterface<?>>();
	private Map<String, MetaModelInterface<?>> managedInterfacesByAlias = new TreeMap<String, MetaModelInterface<?>>();
	private Map<Class<?>, MetaModelInterface<?>> managedInterfacesByClass = new HashMap<Class<?>, MetaModelInterface<?>>();
	
	private Set<MetaModelSubType<?,?>> managedSubTypes = new TreeSet<MetaModelSubType<?,?>>();
	private Map<String, MetaModelSubType<?,?>> managedSubTypesByAlias = new TreeMap<String, MetaModelSubType<?,?>>();
	private Map<Class<?>, MetaModelSubType<?,?>> managedSubTypesByClass = new HashMap<Class<?>, MetaModelSubType<?,?>>();
	
	private Set<MetaModelEntity<?>> managedEntities = new TreeSet<MetaModelEntity<?>>();
	private Map<String, MetaModelEntity<?>> managedEntitiesByAlias = new TreeMap<String, MetaModelEntity<?>>();
	private Map<Class<?>, MetaModelEntity<?>> managedEntitiesByClass = new HashMap<Class<?>, MetaModelEntity<?>>();
	
	private Set<MetaModelEmbeddable<?>> managedEmbeddables = new TreeSet<MetaModelEmbeddable<?>>();
	private Map<String, MetaModelEmbeddable<?>> managedEmbeddablesByAlias = new TreeMap<String, MetaModelEmbeddable<?>>();
	private Map<Class<?>, MetaModelEmbeddable<?>> managedEmbeddablesByClass = new HashMap<Class<?>, MetaModelEmbeddable<?>>();
	
	private Set<MetaModelTransient<?>> managedTransients = new TreeSet<MetaModelTransient<?>>();
	private Map<String, MetaModelTransient<?>> managedTransientsByAlias = new TreeMap<String, MetaModelTransient<?>>();
	private Map<Class<?>, MetaModelTransient<?>> managedTransientsByClass = new HashMap<Class<?>, MetaModelTransient<?>>();
	
	private List<LinkedMetaType> links = new ArrayList<LinkedMetaType>();
	public void addLink(LinkedMetaType link) { this.links.add(link); }
	
	@SuppressWarnings("unchecked")
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
			for (Class<?> k2ManagedTypeClass : ClassUtil.getClasses(modelPackageNames[i], MetaType.class)) {
				register(k2ManagedTypeClass);
			}
		}

		while (links.size() > 0) {
			List<LinkedMetaType> workingList = Arrays.asList(links.toArray(new LinkedMetaType[links.size()]));
			links.clear();
			for (LinkedMetaType link : workingList) 
				link.setMetaModelType(reflect(link.forType()));
		}
		links = null;
	}

	public <T> MetaModelType<T> reflect(Class<T> cls) {
		if (cls.isPrimitive()) 
			return MetaModelPrimitive.staticType(cls);
		if (cls.getPackage().getName().startsWith("java."))
			return MetaModelNative.staticType(cls);
		
		@SuppressWarnings("unchecked")
		MetaModelType<T> mCls = (MetaModelType<T>) managedTypesByClass.get(cls);
		if (mCls != null)
			return mCls;
		for (String packageName : modelPackageNames) 
			if (cls.getPackage().getName().startsWith(packageName)) 
				return register(cls);
		throw new MetaModelError("The class '{}' is not part of the service '{}'({})", cls.getName(), title(), alias());
	}
	
	@SuppressWarnings("unchecked")
	private <T> MetaModelType<T> register(Class<T> k2ManagedTypeClass) {
		logger.trace("register({})", k2ManagedTypeClass.getName());
		if (managedTypesByClass.containsKey(k2ManagedTypeClass)) {
			logger.trace("{} already registered", k2ManagedTypeClass.getName());
			return (MetaModelType<T>) managedTypesByClass.get(k2ManagedTypeClass);
		}
		logger.trace("Getting reflection of {}", k2ManagedTypeClass.getName());
		
		MetaModelType<T> metaType = MetaModelType.forClass(this, k2ManagedTypeClass);
		managedTypes.add(metaType);
		managedTypesByClass.put(k2ManagedTypeClass, metaType);
		managedTypesByAlias.put(metaType.alias(), metaType);
		logger.trace("Registering {} as a type", k2ManagedTypeClass.getName());
		if (metaType instanceof MetaModelClass) {
			MetaModelClass<T> metaClass = (MetaModelClass<T>)metaType;
			managedClasses.add(metaClass);
			managedClassesByClass.put(k2ManagedTypeClass, metaClass);
			managedClassesByAlias.put(metaClass.alias(), metaClass);
			logger.trace("Registering {} as a class", k2ManagedTypeClass.getName());
			if (metaType instanceof MetaModelEntity) {
				MetaModelEntity<T> metaEntity = (MetaModelEntity<T>) metaType;
				managedEntities.add(metaEntity);
				managedEntitiesByClass.put(k2ManagedTypeClass, metaEntity);
				managedEntitiesByAlias.put(metaEntity.alias(), metaEntity);
				logger.trace("Registering {} as an entity", k2ManagedTypeClass.getName());
			}
			if (metaType instanceof MetaModelTransient) {
				MetaModelTransient<T> metaTransient = (MetaModelTransient<T>) metaType;
				managedTransients.add(metaTransient);
				managedTransientsByClass.put(k2ManagedTypeClass, metaTransient);
				managedTransientsByAlias.put(metaTransient.alias(), metaTransient);
				logger.trace("Registering {} as a transient", k2ManagedTypeClass.getName());
			}
			if (metaType instanceof MetaModelEmbeddable) {
				MetaModelEmbeddable<T> metaEmbeddable = (MetaModelEmbeddable<T>) metaType;
				managedEmbeddables.add(metaEmbeddable);
				managedEmbeddablesByClass.put(k2ManagedTypeClass, metaEmbeddable);
				managedEmbeddablesByAlias.put(metaEmbeddable.alias(), metaEmbeddable);
				logger.trace("Registering {} as an embeddable", k2ManagedTypeClass.getName());
			}
		}
		if (metaType instanceof MetaModelInterface) {
			MetaModelInterface<?> metaInterface = (MetaModelInterface<?>)metaType;
			managedInterfaces.add(metaInterface);
			managedInterfacesByClass.put(k2ManagedTypeClass, metaInterface);
			managedInterfacesByAlias.put(metaInterface.alias(), metaInterface);
			logger.trace("Registering {} as an interface", k2ManagedTypeClass.getName());
		}
		if (metaType instanceof MetaModelSubType) {
			MetaModelSubType<?,?> metaSubType = (MetaModelSubType<?,?>)metaType;
			managedSubTypes.add(metaSubType);
			managedSubTypesByClass.put(k2ManagedTypeClass, metaSubType);
			managedSubTypesByAlias.put(metaSubType.alias(), metaSubType);
			logger.trace("Registering {} as a subType", k2ManagedTypeClass.getName());
		}
		
		for (Class<?> e : k2ManagedTypeClass.getDeclaredClasses()) {
			if (e.isAnnotationPresent(MetaSubType.class)) {
				metaType.declaresSubType((MetaModelSubType<T, ?>) register(e));
			}
		}
		return metaType;
	}
	
	@Override
	public int compareTo(MetaModelService o) {
		return alias.compareTo(o.alias);
	}
	
	public MetaModel getMetaModel() { return metaModel; }
	
	public MetaModelType<?> getMetaType(String alias) {
		MetaModelType<?> mCls = managedTypesByAlias.get(alias);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No type with alias '{}' is managed by the service '{}'({})", alias, title(), alias());
	}
	
	@SuppressWarnings("unchecked")
	public <T> MetaModelType<T> getMetaType(Class<T> cls) {
		MetaModelType<T> mCls = (MetaModelType<T>) managedTypesByClass.get(cls);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No type for class '{}' is managed by the service '{}'({})", cls.getName(), title(), alias());
	}
	
	public MetaModelClass<?> getMetaClass(String alias) {
		MetaModelClass<?> mCls = managedClassesByAlias.get(alias);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No class with alias '{}' is managed by the service '{}'({})", alias, title(), alias());
	}
	
	@SuppressWarnings("unchecked")
	public <T> MetaModelClass<T> getMetaClass(Class<T> cls) {
		MetaModelClass<T> mCls = (MetaModelClass<T>) managedClassesByClass.get(cls);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No class for class '{}' is managed by the service '{}'({})", cls.getName(), title(), alias());
	}
	
	public MetaModelInterface<?> getMetaInterface(String alias) {
		MetaModelInterface<?> mCls = managedInterfacesByAlias.get(alias);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No interface with alias '{}' is managed by the service '{}'({})", alias, title(), alias());
	}
	
	@SuppressWarnings("unchecked")
	public <T> MetaModelInterface<T> getMetaInterface(Class<T> cls) {
		MetaModelInterface<T> mCls = (MetaModelInterface<T>) managedTypesByClass.get(cls);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No interface for class '{}' is managed by the service '{}'({})", cls.getName(), title(), alias());
	}
	
	public MetaModelSubType<?,?> getMetaSubType(String alias) {
		MetaModelSubType<?,?> mCls = managedSubTypesByAlias.get(alias);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No interface with alias '{}' is managed by the service '{}'({})", alias, title(), alias());
	}
	
	@SuppressWarnings("unchecked")
	public <T> MetaModelSubType<?,T> getMetaSubType(Class<T> cls) {
		MetaModelSubType<?,T> mCls = (MetaModelSubType<?,T>) managedSubTypesByClass.get(cls);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No subtype for class '{}' is managed by the service '{}'({})", cls.getName(), title(), alias());
	}
	
	public MetaModelEntity<?> getMetaEntity(String alias) {
		MetaModelEntity<?> mCls = managedEntitiesByAlias.get(alias);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No entity with alias '{}' is managed by the service '{}'({})", alias, title(), alias());
	}
	
	@SuppressWarnings("unchecked")
	public <T> MetaModelEntity<T> getMetaEntity(Class<T> cls) {
		MetaModelEntity<T> mCls = (MetaModelEntity<T>) managedEntitiesByClass.get(cls);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No entity for class '{}' is managed by the service '{}'({})", cls.getName(), title(), alias());
	}
	
	public MetaModelEmbeddable<?> getMetaEmbeddable(String alias) {
		MetaModelEmbeddable<?> mCls = managedEmbeddablesByAlias.get(alias);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No embeddable with alias '{}' is managed by the service '{}'({})", alias, title(), alias());
	}
	
	@SuppressWarnings("unchecked")
	public <T> MetaModelEmbeddable<T> getMetaEmbeddable(Class<T> cls) {
		MetaModelEmbeddable<T> mCls = (MetaModelEmbeddable<T>) managedEmbeddablesByClass.get(cls);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No embeddable for class '{}' is managed by the service '{}'({})", cls.getName(), title(), alias());
	}
	
	public MetaModelTransient<?> getMetaTransient(String alias) {
		MetaModelTransient<?> mCls = managedTransientsByAlias.get(alias);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No transient with alias '{}' is managed by the service '{}'({})", alias, title(), alias());
	}
	
	@SuppressWarnings("unchecked")
	public <T> MetaModelTransient<T> getMetaTransiet(Class<T> cls) {
		MetaModelTransient<T> mCls = (MetaModelTransient<T>) managedTransientsByClass.get(cls);
		if (mCls != null)
			return mCls;
		throw new MetaModelError("No transient for class '{}' is managed by the service '{}'({})", cls.getName(), title(), alias());
	}
	
	public Set<MetaModelType<?>> getManagedTypes() { return managedTypes; }
	public Set<MetaModelInterface<?>> getManagedInterfaces() { return managedInterfaces; }
	public Set<MetaModelClass<?>> getManagedClasses() { return managedClasses; }
	public Set<MetaModelSubType<?,?>> getManagedSubTypes() { return managedSubTypes; }
	public Set<MetaModelEntity<?>> getManagedEntities() { return managedEntities; }
	public Set<MetaModelEmbeddable<?>> getManagedEmbeddables() { return managedEmbeddables; }
	public Set<MetaModelTransient<?>> getManagedTransients() { return managedTransients; }
	
	public String alias() { return alias; }
	public String title() { return title; }
	public Version version() { return version; }
	public String description() { return metaService.description(); }
	public String[] modelPackageNames() { return modelPackageNames; }


}
