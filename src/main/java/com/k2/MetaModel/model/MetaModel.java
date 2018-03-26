package com.k2.MetaModel.model;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.annotations.MetaApplication;
import com.k2.Util.StringUtil;
import com.k2.Util.Version.Version;
import com.k2.Util.classes.ClassUtil;

public class MetaModel {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private MetaApplication appConfig;
	private Version version;
	private URL website;
	private String alias;
	private String title;
	private Class<?> appConfigClass;
	
	Set<MetaModelService> implementedServices = new TreeSet<MetaModelService>();
	Map<String, MetaModelService> servicesByAlias = new HashMap<String, MetaModelService>();
	
	public static MetaModel reflect(Class<?> appConfigClass) {
		return new MetaModel(appConfigClass);
	}

	private MetaModel(Class<?> appConfigClass ) {
		this.appConfigClass = appConfigClass;
		if (!appConfigClass.isAnnotationPresent(MetaApplication.class))
			throw new MetaModelError("The supplied application class {} is not annotated with @MetaApplication. Unable to extract the application metamodel");
		logger.info("Extracting static application data for {}", appConfigClass.getName());
		
		appConfig = appConfigClass.getAnnotation(MetaApplication.class);
		alias = (StringUtil.isSet(appConfig.alias())) ? appConfig.alias() : ClassUtil.alias(appConfigClass);
		title = (StringUtil.isSet(appConfig.title())) ? appConfig.title() : ClassUtil.title(appConfigClass);

		logger.info("Generating metadata for application: {}: ({})", title, alias);

		version = Version.create(appConfig.version().major(), appConfig.version().minor(), appConfig.version().point(), appConfig.version().build());

		if (StringUtil.isSet(appConfig.website())) {
			try {
				website = new URL(appConfig.website()); 
			} catch (MalformedURLException e) {
				try {
					website = new URL("http://"+appConfig.website());
				} catch (MalformedURLException e1) {
					logger.info("Malformed URL {} - {}", e, appConfig.website(), e.getMessage());
				}
			}
		}
		
		
		logger.info("Extracting implemented services");
		for (Class<?> serviceClass : appConfig.services()) {
			MetaModelService service = MetaModelService.reflect(this, serviceClass);
			implementedServices.add(service);
			servicesByAlias.put(service.alias(), service);
		}
	}

	public String alias() { return alias; }
	public String title() { return title; }
	public String description() { return appConfig.description(); }
	public Version version() { return version; }
	public String organisation() { return appConfig.organisation(); }
	public URL website() { return website; }
	public Class<?> configurationClass() { return appConfigClass; }
	
	public Set<MetaModelService> implementedServices() { return implementedServices; }
	public MetaModelService metaModelService(String alias) {
		MetaModelService service = servicesByAlias.get(alias);
		if (service != null)
			return service;
		throw new MetaModelError("The application {} does not know of the service with alias {}", title(), alias);
	}

}
