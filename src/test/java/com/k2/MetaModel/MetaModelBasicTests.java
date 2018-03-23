package com.k2.MetaModel;

import static org.junit.Assert.*;

import java.lang.invoke.MethodHandles;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.Version.Version;

import example.app.AppConfig;
import example.app.serviceA.model.EntityA1;


public class MetaModelBasicTests {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
		
	MetaModel testModel = MetaModel.reflect(AppConfig.class);

	@Test
	public void metaModelTest() throws ClassNotFoundException
    {
		
		assertNotNull(testModel);
		assertEquals("testApplication", testModel.alias());
		assertEquals("Test Application", testModel.title());
		assertEquals("This is a dummy application for testing the generation of the metamodel", testModel.description());
		assertEquals(Version.create(1, 2, 3, 1234), testModel.version());
		assertEquals("k2.com", testModel.organisation());
		assertEquals("http://www.k2.com", testModel.website().toString());

		assertEquals(2, testModel.implementedServices().size());

		MetaModelService testServiceA = testModel.metaModelService("serviceA");
		
		assertNotNull(testServiceA);
		
		assertEquals("serviceA", testServiceA.alias());
		assertEquals("Service A", testServiceA.title());
		assertEquals("This is test service A", testServiceA.description());
		assertEquals(Version.create(0, 1, 2), testServiceA.version());
		assertEquals(1, testServiceA.modelPackageNames().length);
		assertEquals("example.app.serviceA.model", testServiceA.modelPackageNames()[0]);
		
		assertEquals(6, testServiceA.getMamagedClasses().size());
		
		MetaModelClass<?> entityA1 = testServiceA.getMetaClass("entityA1");
		
		assertNotNull(entityA1);
		assertEquals("example.app.serviceA.model", entityA1.packageName());
		assertEquals("example.app.serviceA.model.EntityA1", entityA1.className());
		assertEquals("EntityA1", entityA1.name());
		assertEquals("entityA1", entityA1.alias());
		assertEquals("Entity A 1", entityA1.title());
		assertEquals("Test entity A1", entityA1.description());
		
		assertEquals(1, entityA1.getDeclaredSubTypes().size());
		
		MetaModelSubType<?,?> a1Type = entityA1.getSubTypesByName("A1Type");
		assertNotNull(a1Type);
		assertEquals("A1Type", a1Type.name());
		assertEquals("example.app.serviceA.model.EntityA1$A1Type", a1Type.className());
		assertEquals("example.app.serviceA.model", a1Type.packageName());
		assertEquals("A1 Types", a1Type.title());
		assertEquals("A discriminator type for the A1 sub types", a1Type.description());
		
		assertEquals(2, a1Type.getTypeValues().size());

		MetaModelSubTypeValue<?,?> a1Type_a = a1Type.getTypeValuesbyName("A1A");
		assertEquals("A1A", a1Type_a.name());
		assertEquals("TypeA1 - Subtype A", a1Type_a.title());
		assertEquals("The subtype A of the type A1", a1Type_a.description());
		
		MetaModelSubTypeValue<?,?> a1Type_b = a1Type.getTypeValuesbyName("A1B");
		assertEquals("A1B", a1Type_b.name());
		assertEquals("TypeA1 - Subtype B", a1Type_b.title());
		assertEquals("The subtype B of the type A1", a1Type_b.description());
		
		assertEquals("A1A", EntityA1.A1Type.A1A.alias());
		assertEquals("TypeA1 - Subtype A", EntityA1.A1Type.A1A.title());
		assertEquals("The subtype A of the type A1", EntityA1.A1Type.A1A.description());
		
		
		
		
		MetaModelService testServiceB = testModel.metaModelService("serviceBConfig");
		
		assertNotNull(testServiceB);
		
		assertEquals("serviceBConfig", testServiceB.alias());
		assertEquals("Service B Config", testServiceB.title());
		assertEquals("", testServiceB.description());
		assertEquals(Version.create(0, 0, 1), testServiceB.version());
		assertEquals(1, testServiceB.modelPackageNames().length);
		assertEquals("example.app.serviceB.model", testServiceB.modelPackageNames()[0]);
		
		

    }
	
	
}
