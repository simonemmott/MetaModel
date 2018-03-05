package com.k2.MetaModel;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.Version.Version;


public interface StaticMetaApplication {

	static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	static final String defaultUrl = "http://www.k2.com";

	public String alias();
	
	public String title();
	
	public Version version();
	
	public String description();
	
	public String organisation();
	
	public URL website();
	
	public Class<? extends StaticMetaService>[] implementedServiceClasses();
	
	public static StaticMetaApplication application(Class<? extends StaticMetaApplication> metaApplicationClass ) {
		StaticMetaApplication metaApplication;
		try {
			metaApplication = metaApplicationClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new MetaModelError("No available zero arg constructor for {}", e, metaApplicationClass.getName());
		}
		return metaApplication;
	}
	
	public static URL toURL(String www) {
		URL url = null;
		try {
			url = new URL(www);
		} catch (MalformedURLException e) {
			logger.warn("Malformed URL {}, using default {}", www, defaultUrl);
			try {
				url = new URL(defaultUrl);
			} catch (MalformedURLException e1) {
				logger.error("Malformed default URL {} returning null", defaultUrl);
			}
		}
		return url;
	}
}
