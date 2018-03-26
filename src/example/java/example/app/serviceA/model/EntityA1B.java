package example.app.serviceA.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.k2.MetaModel.annotations.MetaType;
import com.k2.MetaModel.annotations.MetaClass;
import com.k2.MetaModel.annotations.MetaEntity;
import com.k2.MetaModel.annotations.MetaField;
import com.k2.MetaModel.annotations.MetaVersion;

@MetaVersion(major=0, minor=0, point=1)
@MetaType
@MetaClass
@MetaEntity
@Entity
@Table(name="A1BS")
@DiscriminatorValue("A1B")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="ID")
public class EntityA1B extends EntityA1 {

	// A1B Name ------------------------------------------------------------------------------
	@MetaField
	@Column(name="A1BNAME", nullable=true, length=80)
	private String a1bName;
	public String getA1bName() { return a1bName; }
	public void setA1bName(String a1bName) { this.a1bName = a1bName; }
	
}
