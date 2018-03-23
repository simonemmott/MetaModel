package example.app.serviceA.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.k2.MetaModel.annotations.MetaClass;
import com.k2.MetaModel.annotations.MetaEntity;
import com.k2.MetaModel.annotations.MetaVersion;

@MetaVersion(major=0, minor=0, point=1)
@MetaClass
@MetaEntity
@Entity
@Table(name="A1AAS")
@DiscriminatorValue("A1AA")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="ID")
public class EntityA1AA extends EntityA1A {

	// A1AA Nane ------------------------------------------------------------------------------
	@Column(name="A1AANAME", nullable=true, length=80)
	private String a1aaName;
	public String getA1aaName() { return a1aaName; }
	public void setA1aaName(String a1aaName) { this.a1aaName = a1aaName; }
	
}
