package example.app.serviceA.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.k2.MetaModel.TypeValue;
import com.k2.MetaModel.annotations.MetaType;
import com.k2.MetaModel.annotations.MetaTypeField;
import com.k2.MetaModel.annotations.MetaClass;
import com.k2.MetaModel.annotations.MetaEntity;
import com.k2.MetaModel.annotations.MetaField;
import com.k2.MetaModel.annotations.MetaLinkField;
import com.k2.MetaModel.annotations.MetaSubType;
import com.k2.MetaModel.annotations.MetaSubTypeValue;
import com.k2.MetaModel.annotations.MetaVersion;
import com.k2.Util.classes.ClassUtil;

@MetaVersion(major=0, minor=0, point=1)
@MetaType(description="Test entity A1")
@MetaClass
@MetaEntity
@Entity
@Table(name="A1S")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="A1TYPE")
public class EntityA1 {

	@MetaType(title="A1 Types", description="A discriminator type for the A1 sub types")
	@MetaSubType
	public enum A1Type implements TypeValue {
		@MetaSubTypeValue(title="TypeA1 - Subtype A", description="The subtype A of the type A1") A1A,
		@MetaSubTypeValue(title="TypeA1 - Subtype B", description="The subtype B of the type A1") A1B;
		
		A1Type() { this.metaValue = ClassUtil.getField(this.getClass(), this.name()).getAnnotation(MetaSubTypeValue.class); }
		private MetaSubTypeValue metaValue;
		@Override public String alias() { return this.name(); }
		@Override public String title() { return metaValue.title(); }
		@Override public String description() { return metaValue.description(); }
	}

	// Id ------------------------------------------------------------------------------------
	@MetaField(title="Id", description="The primary key")
	@Id
	@Column(name="ID", nullable=false)
	private Long id;
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	// A1 Type -------------------------------------------------------------------------------
	@MetaField(title="A1 Type", description="The type of this A1 instance")
	@MetaTypeField
	@Column(name="A1TYPE", nullable=false, insertable=false, updatable=false)
	@Enumerated(EnumType.STRING)
	private A1Type a1Type;
	public A1Type getA1Type() { return a1Type; }
	public void setA1Type(A1Type a1Type) { this.a1Type = a1Type; }

	// Type A2 -------------------------------------------------------------------------------
	@Column(name="A2ALIAS", insertable=false, updatable=false, length=50)
	private String entityA2Alias;
	@MetaField(title="A2", description="The A2 instance for this A1 instance")
	@MetaLinkField
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="A2ALIAS", referencedColumnName="ALIAS")
	private EntityA2 entityA2;
	public EntityA2 getTypeA2() { return entityA2; }
	public void setTypeA2(EntityA2 entityA2) { 
		this.entityA2 = entityA2; 
		this.entityA2Alias = entityA2.getAlias();
	}

	// String Value -------------------------------------------------------------------------------
	@MetaField(title="String Value", description="A string value")
	@Column(name="STRINGVALUE", nullable=true, length=50)
	private String stringValue;
	public String getStringValue() { return stringValue; }
	public void setStringValue(String stringValue) { this.stringValue = stringValue; }

	// Boolean Value -------------------------------------------------------------------------------
	@MetaField(title="Boolean Value", description="A boolean value")
	@Column(name="BOOLEANVALUE", nullable=true)
	private Boolean boolValue;
	public Boolean getBoolValue() { return boolValue; }
	public void setBoolValue(Boolean boolValue) { this.boolValue = boolValue; }

	// Float Value -------------------------------------------------------------------------------
	@MetaField(title="Float Value", description="A float value")
	@Column(name="FLOATVALUE", nullable=true, precision=3)
	private Float floatValue;
	public Float getFloatValue() { return floatValue; }
	public void setFloatValue(Float floatValue) { this.floatValue = floatValue; }

	// Double Value -------------------------------------------------------------------------------
	@MetaField(title="Double Value", description="A double value")
	@Column(name="DOUBLEVALUE", nullable=true, precision=10)
	private Double doubleValue;
	public Double getDoubleValue() { return doubleValue; }
	public void setDoubleValue(Double doubleValue) { this.doubleValue = doubleValue; }

	// Date Value -------------------------------------------------------------------------------
	@MetaField(title="Date Value", description="A date value")
	@Column(name="DATEVALUE", nullable=true)
	private Date	 dateValue;
	public Date getDateValue() { return dateValue; }
	public void setDateValue(Date dateValue) { this.dateValue = dateValue; }
	
	
}
