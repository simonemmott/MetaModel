package example.app.serviceA.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.k2.MetaModel.TypeValue;
import com.k2.MetaModel.annotations.MetaType;
import com.k2.MetaModel.annotations.MetaTypeField;
import com.k2.MetaModel.annotations.MetaClass;
import com.k2.MetaModel.annotations.MetaEntity;
import com.k2.MetaModel.annotations.MetaField;
import com.k2.MetaModel.annotations.MetaSubType;
import com.k2.MetaModel.annotations.MetaSubTypeValue;
import com.k2.MetaModel.annotations.MetaVersion;
import com.k2.Util.classes.ClassUtil;

@MetaVersion(major=0, minor=0, point=1)
@MetaType(description="Test entity A1A")
@MetaClass
@MetaEntity
@Entity
@Table(name="A1AS")
@DiscriminatorValue("A1A")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="A1ATYPE")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="ID")
public class EntityA1A extends EntityA1 {

	@MetaType(title="A1A Types", description="A discriminator type for the A1A sub types")
	@MetaSubType
	public enum A1AType implements TypeValue {
		@MetaSubTypeValue(title="TypeA1A - Subtype A", description="The subtype A of the type A1A") A1AA,
		@MetaSubTypeValue(title="TypeA1A - Subtype B", description="The subtype B of the type A1A") A1AB;
		
		A1AType() { this.metaValue = ClassUtil.getField(this.getClass(), this.name()).getAnnotation(MetaSubTypeValue.class); }
		private MetaSubTypeValue metaValue;
		@Override public String alias() { return this.name(); }
		@Override public String title() { return metaValue.title(); }
		@Override public String description() { return metaValue.description(); }
	}

	
	// A1A Type -------------------------------------------------------------------------------
	@MetaField(title="A1A Type", description="The type of A1A instance")
	@MetaTypeField
	@Column(name="A1ATYPE", nullable=false, insertable=false, updatable=false)
	@Enumerated(EnumType.STRING)
	private A1AType a1aType;
	public A1AType getA1aType() { return a1aType; }
	public void setA1aType(A1AType a1aType) { this.a1aType = a1aType; }
	
	// String Value -------------------------------------------------------------------------------
	@MetaField(title="A1A Name", description="The A1A name")
	@Column(name="A1ANAME", nullable=true, length=80)
	private String a1aName;
	public String getA1aName() { return a1aName; }
	public void setA1aName(String a1aName) { this.a1aName = a1aName; }

}
