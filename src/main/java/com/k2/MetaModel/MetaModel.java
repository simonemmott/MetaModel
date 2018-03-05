package com.k2.MetaModel;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.Version.Version;

public class MetaModel {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private StaticMetaApplication staticApp;
	
	Set<MetaModelService> implementedServices = new TreeSet<MetaModelService>();
	Map<String, MetaModelService> servicesByAlias = new HashMap<String, MetaModelService>();
	
	public static MetaModel prepare(Class<? extends StaticMetaApplication> metaApplicationClass) {
		return new MetaModel(metaApplicationClass);
	}

	private MetaModel(Class<? extends StaticMetaApplication> metaApplicationClass ) {
		logger.info("Extracting static application data for {}", metaApplicationClass.getName());
		staticApp = StaticMetaApplication.application(metaApplicationClass);
		
		logger.info("Generating metadata for application: {}: ({})", staticApp.title(), staticApp.alias());
		
		logger.info("Extracting implemented services");
		for (Class<? extends StaticMetaService> serviceClass : staticApp.implementedServiceClasses()) {
			MetaModelService service = new MetaModelService(this, StaticMetaService.service(serviceClass));
			implementedServices.add(service);
			servicesByAlias.put(service.alias(), service);
		}
	}

	public String alias() { return staticApp.alias(); }
	public String title() { return staticApp.title(); }
	public String description() { return staticApp.description(); }
	public Version version() { return staticApp.version(); }
	public String organisation() { return staticApp.organisation(); }
	public URL website() { return staticApp.website(); }
	
	public Set<MetaModelService> implementedServices() { return implementedServices; }
	public MetaModelService metaModelService(String alias) {
		MetaModelService service = servicesByAlias.get(alias);
		if (service != null)
			return service;
		throw new MetaModelError("The application {} does not know of the service with alias {}", title(), alias);
	}

}
