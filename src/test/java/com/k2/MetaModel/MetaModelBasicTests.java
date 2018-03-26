package com.k2.MetaModel;

import static org.junit.Assert.*;

import java.lang.invoke.MethodHandles;

import javax.persistence.InheritanceType;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.model.MetaModel;
import com.k2.MetaModel.model.MetaModelField;
import com.k2.MetaModel.model.MetaModelJoinedColumns;
import com.k2.MetaModel.model.MetaModelService;
import com.k2.MetaModel.model.MetaModelSubTypeValue;
import com.k2.MetaModel.model.MetaModelType;
import com.k2.MetaModel.model.fields.MetaModelLinkField;
import com.k2.MetaModel.model.fields.MetaModelTypeField;
import com.k2.MetaModel.model.types.MetaModelClass;
import com.k2.MetaModel.model.types.MetaModelSubType;
import com.k2.MetaModel.model.types.classes.MetaModelNative;
import com.k2.Util.Version.Version;

import example.app.AppConfig;
import example.app.serviceA.model.EntityA1;
import example.app.serviceA.model.EntityA1AA;


public class MetaModelBasicTests {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
		
	private static MetaModel testModel = MetaModel.reflect(AppConfig.class);
	private static MetaModelService testServiceA = testModel.metaModelService("serviceA");
	private static MetaModelService testServiceB = testModel.metaModelService("serviceBConfig");

	@Test
	public void metaModelTest()
    {
		
		assertNotNull(testModel);
		assertEquals("testApplication", testModel.alias());
		assertEquals("Test Application", testModel.title());
		assertEquals("This is a dummy application for testing the generation of the metamodel", testModel.description());
		assertEquals(Version.create(1, 2, 3, 1234), testModel.version());
		assertEquals("k2.com", testModel.organisation());
		assertEquals("http://www.k2.com", testModel.website().toString());

		assertEquals(2, testModel.implementedServices().size());

		
		
		assertNotNull(testServiceA);
		
		assertEquals("serviceA", testServiceA.alias());
		assertEquals("Service A", testServiceA.title());
		assertEquals("This is test service A", testServiceA.description());
		assertEquals(Version.create(0, 1, 2), testServiceA.version());
		assertEquals(1, testServiceA.modelPackageNames().length);
		assertEquals("example.app.serviceA.model", testServiceA.modelPackageNames()[0]);
		
		assertEquals(8, testServiceA.getManagedTypes().size());
		
		MetaModelType<?> entityA1 = testServiceA.getMetaClass("entityA1");
		
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

		MetaModelSubTypeValue<?,?> a1Type_a = a1Type.getTypeValueByName("A1A");
		assertEquals("A1A", a1Type_a.name());
		assertEquals("TypeA1 - Subtype A", a1Type_a.title());
		assertEquals("The subtype A of the type A1", a1Type_a.description());
		
		MetaModelSubTypeValue<?,?> a1Type_b = a1Type.getTypeValueByName("A1B");
		assertEquals("A1B", a1Type_b.name());
		assertEquals("TypeA1 - Subtype B", a1Type_b.title());
		assertEquals("The subtype B of the type A1", a1Type_b.description());
		
		assertEquals("A1A", EntityA1.A1Type.A1A.alias());
		assertEquals("TypeA1 - Subtype A", EntityA1.A1Type.A1A.title());
		assertEquals("The subtype A of the type A1", EntityA1.A1Type.A1A.description());
		
		
		
		
		testServiceB = testModel.metaModelService("serviceBConfig");
		
		assertNotNull(testServiceB);
		
		assertEquals("serviceBConfig", testServiceB.alias());
		assertEquals("Service B Config", testServiceB.title());
		assertEquals("", testServiceB.description());
		assertEquals(Version.create(0, 0, 1), testServiceB.version());
		assertEquals(1, testServiceB.modelPackageNames().length);
		assertEquals("example.app.serviceB.model", testServiceB.modelPackageNames()[0]);

    }
	
	@Test
	public void extendedTypeTest() {	
		
		MetaModelClass<?> entityA1 = testServiceA.getMetaClass("entityA1");
		
		assertTrue(entityA1.isExtended());
		assertEquals(1, entityA1.getDeclaredSubTypes().size());
		MetaModelSubType<?,?> a1Type = entityA1.getDeclaredSubTypes().toArray(new MetaModelSubType[1])[0];
		assertEquals("a1Type", a1Type.alias());
		assertEquals("A1Type", a1Type.name());
		assertEquals("A1 Types", a1Type.title());
		assertEquals("A discriminator type for the A1 sub types", a1Type.description());
		
		assertEquals(InheritanceType.JOINED, entityA1.inheritanceStrategy());
		MetaModelTypeField<?,?> entityA1_discriminator = entityA1.getDiscriminatorTypeField();
		assertNotNull(entityA1_discriminator);
		assertEquals("a1Type", entityA1_discriminator.alias());
		assertEquals("A1 Type", entityA1_discriminator.title());
		assertEquals("The type of this A1 instance", entityA1_discriminator.description());

		assertEquals("A1TYPE", entityA1.discriminatorColumnName());
		
		assertEquals(2, entityA1.getSubClasses().size());
		MetaModelClass<?> entityA1A = entityA1.getSubClass("entityA1A");
		assertEquals("entityA1A", entityA1A.alias());
		assertEquals("Entity A 1 A", entityA1A.title());
		assertEquals("Test entity A1A", entityA1A.description());
		
		MetaModelClass<?> entityA1B = entityA1.getSubClass("entityA1B");
		assertEquals("entityA1B", entityA1B.alias());
		assertEquals("Entity A 1 B", entityA1B.title());
		assertEquals("", entityA1B.description());
		
	}
	
	@Test
	public void nativeFieldsTest() {
		
		MetaModelClass<?> entityA1 = testServiceA.getMetaClass("entityA1");
		MetaModelClass<?> entityA1A = testServiceA.getMetaClass("entityA1A");
		MetaModelClass<?> entityA1AA = testServiceA.getMetaClass(EntityA1AA.class);
		
		assertEquals(8, entityA1.getDeclaredFields().size());
		assertEquals(2, entityA1A.getDeclaredFields().size());
		assertEquals(1, entityA1AA.getDeclaredFields().size());
		
		MetaModelField<?,?> entityA1_doubleValue = entityA1.getMetaField("doubleValue");
		assertEquals("doubleValue", entityA1_doubleValue.alias());
		assertEquals("Double Value", entityA1_doubleValue.title());
		assertEquals("A double value", entityA1_doubleValue.description());
		assertEquals("DOUBLEVALUE", entityA1_doubleValue.getMetaColumn().getName());
		assertEquals(10, entityA1_doubleValue.getMetaColumn().getPrecision());
		assertEquals(MetaModelNative.DOUBLE, entityA1_doubleValue.getFieldMetaType());
		assertEquals(entityA1, entityA1_doubleValue.getDeclaringMetaClass());
		

		MetaModelField<?,?> entityA1A_name = entityA1A.getMetaField("a1aName");
		assertEquals("a1aName", entityA1A_name.alias());
		assertEquals("A1A Name", entityA1A_name.title());
		assertEquals("The A1A name", entityA1A_name.description());
		assertEquals("A1ANAME", entityA1A_name.getMetaColumn().getName());
		assertEquals(80, entityA1A_name.getMetaColumn().getLength());
		assertEquals(MetaModelNative.STRING, entityA1A_name.getFieldMetaType());
		assertEquals(entityA1A, entityA1A_name.getDeclaringMetaClass());

		MetaModelField<?,?> entityA1AA_name = entityA1AA.getMetaField("a1aaName");
		assertEquals("a1aaName", entityA1AA_name.alias());
		assertEquals("A1AA Name", entityA1AA_name.title());
		assertEquals("The A1AA name for this instance", entityA1AA_name.description());
		assertEquals("A1AANAME", entityA1AA_name.getMetaColumn().getName());
		assertEquals(80, entityA1AA_name.getMetaColumn().getLength());
		assertEquals(MetaModelNative.STRING, entityA1AA_name.getFieldMetaType());
		assertEquals(entityA1AA, entityA1AA_name.getDeclaringMetaClass());

		MetaModelField<?,?> entityA1A_id = entityA1A.getMetaField("id");
		assertEquals("id", entityA1A_id.alias());
		assertEquals("Id", entityA1A_id.title());
		assertEquals("The primary key", entityA1A_id.description());
		assertEquals("ID", entityA1A_id.getMetaColumn().getName());
		assertEquals(MetaModelNative.LONG, entityA1A_id.getFieldMetaType());
		assertEquals(entityA1, entityA1A_id.getDeclaringMetaClass());

		MetaModelField<?,?> entityA1AA_id = entityA1AA.getMetaField("id");
		assertEquals("id", entityA1AA_id.alias());
		assertEquals("Id", entityA1AA_id.title());
		assertEquals("The primary key", entityA1AA_id.description());
		assertEquals("ID", entityA1AA_id.getMetaColumn().getName());
		assertEquals(MetaModelNative.LONG, entityA1AA_id.getFieldMetaType());
		assertEquals(entityA1, entityA1AA_id.getDeclaringMetaClass());

		
	}
	
	@Test
	public void typeFieldTest() {
		MetaModelClass<?> entityA1 = testServiceA.getMetaClass("entityA1");

		MetaModelTypeField<?,?> entityA1_a1Type = (MetaModelTypeField<?,?>) entityA1.getMetaField("a1Type");
		
		assertNotNull(entityA1_a1Type);
		
		MetaModelSubType<?,?> subType = entityA1_a1Type.getMetaSubType();
		
		assertNotNull(subType);
		
		assertEquals("a1Type", subType.alias());
		assertEquals("A1 Types", subType.title());
		assertEquals("A discriminator type for the A1 sub types", subType.description());
		assertEquals(2, subType.getTypeValues().size());
		
	}
		
	@Test
	public void linkFieldTest() {

		MetaModelClass<?> entityA1 = testServiceA.getMetaClass("entityA1");

		MetaModelLinkField<?,?> entityA1_entityA2 = (MetaModelLinkField<?,?>) entityA1.getMetaField("entityA2");
		
		assertNotNull(entityA1_entityA2);
		assertEquals("entityA2", entityA1_entityA2.alias());
		assertEquals("A2", entityA1_entityA2.title());
		assertEquals("The A2 instance for this A1 instance", entityA1_entityA2.description());
		
		MetaModelClass<?> entityA2 = entityA1_entityA2.getLinkedMetaClass();
		assertNotNull(entityA2);
		assertEquals("entityA2", entityA2.alias());
		assertEquals("Entity A 2", entityA2.title());
		assertEquals("", entityA2.description());
		
		assertEquals(1, entityA1_entityA2.getMetaJoins().size());
		
		MetaModelJoinedColumns<?,?> joinedColumns = entityA1_entityA2.getMetaJoins().get(0);
		
		assertEquals("A2ALIAS", joinedColumns.getSourceColumn().getName());
		assertNull(joinedColumns.getSourceColumn().getMetaField());
		
		assertEquals("ALIAS", joinedColumns.getTargetColumn().getName());
		assertNotNull(joinedColumns.getTargetColumn().getMetaField());
		assertEquals("alias", joinedColumns.getTargetColumn().getMetaField().alias());
		assertEquals("The alias", joinedColumns.getTargetColumn().getMetaField().title());
		assertEquals("The A2 primary key", joinedColumns.getTargetColumn().getMetaField().description());
		
		
		

	}
	
	@Test
	public void methodTest() {
		
	}
	
	@Test
	public void ownedDataTest() {
		
	}
	
	@Test
	public void typeTest() {
		assertEquals(2, testServiceA.getManagedSubTypes().size());
		
		MetaModelSubType<?,?> a1Type = testServiceA.getMetaSubType("a1Type");
		
		assertNotNull(a1Type);
		assertEquals("a1Type", a1Type.alias());
		assertEquals("A1 Types", a1Type.title());
		assertEquals("A discriminator type for the A1 sub types", a1Type.description());
		
		assertEquals(2, a1Type.getTypeValues().size());
	}
	
	@Test
	public void entityTest() {
		
	}
	
	@Test
	public void interfaceTest() {
		
	}

	@Test
	public void embeddableTest() {
		
	}
	
	@Test
	public void transientTest() {
		
	}
	

}
