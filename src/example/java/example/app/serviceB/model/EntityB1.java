package example.app.serviceB.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.k2.MetaModel.annotations.MetaClass;
import com.k2.MetaModel.annotations.MetaEntity;
import com.k2.MetaModel.annotations.MetaVersion;

@MetaVersion(major=0, minor=0, point=1)
@MetaClass
@MetaEntity
@Entity
@Table(name="TYPEB1S")
public class EntityB1 {

	// Id ------------------------------------------------------------------------------------
	@Id
	@Column(name="ID", nullable=false)
	private Long id;
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	// Type B2 -------------------------------------------------------------------------------
	@Column(name="B2ALIAS", insertable=false, updatable=false, length=50)
	private String entityB2Alias;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="B2ALIAS", referencedColumnName="ALIAS")
	private EntityB2 entityB2;
	public EntityB2 getTypeB2() { return entityB2; }
	public void setTypeB2(EntityB2 entityB2) { this.entityB2 = entityB2; }

	// String Value -------------------------------------------------------------------------------
	@Column(name="STRINGVALUE", nullable=true, length=50)
	private String stringValue;
	public String getStringValue() { return stringValue; }
	public void setStringValue(String stringValue) { this.stringValue = stringValue; }

	// Boolean Value -------------------------------------------------------------------------------
	@Column(name="BOOLEANVALUE", nullable=true)
	private Boolean boolValue;
	public Boolean getBoolValue() { return boolValue; }
	public void setBoolValue(Boolean boolValue) { this.boolValue = boolValue; }

	// Float Value -------------------------------------------------------------------------------
	@Column(name="FLOATVALUE", nullable=true, precision=3)
	private Float floatValue;
	public Float getFloatValue() { return floatValue; }
	public void setFloatValue(Float floatValue) { this.floatValue = floatValue; }

	// Double Value -------------------------------------------------------------------------------
	@Column(name="DOUBLEVALUE", nullable=true, precision=10)
	private Double doubleValue;
	public Double getDoubleValue() { return doubleValue; }
	public void setDoubleValue(Double doubleValue) { this.doubleValue = doubleValue; }

	// Date Value -------------------------------------------------------------------------------
	@Column(name="DATEVALUE", nullable=true)
	private Date	 dateValue;
	public Date getDateValue() { return dateValue; }
	public void setDateValue(Date dateValue) { this.dateValue = dateValue; }
	

}
