package example.app.serviceA.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.k2.MetaModel.annotations.MetaType;
import com.k2.MetaModel.annotations.MetaClass;
import com.k2.MetaModel.annotations.MetaEntity;
import com.k2.MetaModel.annotations.MetaField;
import com.k2.MetaModel.annotations.MetaVersion;

@MetaVersion(major=0, minor=0, point=1)
@MetaClass
@MetaType
@MetaEntity
@Entity
@Table(name="A1ABS")
@DiscriminatorValue("A1AB")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="ID")
public class EntityA1AB extends EntityA1A {

	// A1AB Nane ------------------------------------------------------------------------------
	@MetaField
	@Column(name="A1ABNAME", nullable=true, length=80)
	private String a1abName;
	public String getA1abName() { return a1abName; }
	public void setA1abName(String a1abName) { this.a1abName = a1abName; }

}
