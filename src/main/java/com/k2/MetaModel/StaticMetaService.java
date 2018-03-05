package com.k2.MetaModel;

import com.k2.MetaModel.annotations.StaticService;
import com.k2.Util.classes.ClassUtil;
import com.k2.Util.StringUtil;
import com.k2.Util.Version.Version;

public interface StaticMetaService {

	public String alias();
	
	public String title();
	
	public Version version();
	
	public String description();
	
	public String[] typePackageNames();
	
	public static StaticMetaService service(Class<? extends StaticMetaService> metaServiceClass ) {
		StaticMetaService metaService;
		try {
			metaService = metaServiceClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new MetaModelError("No available zero arg constructor for {}", e, metaServiceClass.getName());
		}
		return metaService;
	}
	
	public static StaticMetaService service(String packageName, String alias ) {
		@SuppressWarnings("unchecked")
		Class<? extends StaticMetaService>[] serviceClasses = (Class<? extends StaticMetaService>[]) ClassUtil.getClasses(packageName, StaticService.class);
		for (Class<? extends StaticMetaService> staticServiceClass : serviceClasses) {
			if (staticServiceClass.getAnnotation(StaticService.class).alias().equals(alias))
				return service(staticServiceClass);
			String name = staticServiceClass.getSimpleName();
			String checkAlias = name;
			if (name.contains("_"))
				checkAlias = name.substring(0, name.indexOf("_"));
			checkAlias = StringUtil.initialLowerCase(checkAlias);
			if (alias.equals(checkAlias))
				return service(staticServiceClass);
		}
		throw new MetaModelError("No @StaticService class defined in package {} or its sub packages with alias {}", packageName, alias);
	}
}
