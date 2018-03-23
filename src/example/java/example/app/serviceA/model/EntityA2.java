package example.app.serviceA.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.k2.MetaModel.annotations.MetaClass;
import com.k2.MetaModel.annotations.MetaEntity;
import com.k2.MetaModel.annotations.MetaVersion;

@MetaVersion(major=0, minor=0, point=1)
@MetaClass
@MetaEntity
@Entity
@Table(name="A2S")
public class EntityA2 {
	
	// Name ------------------------------------------------------------------------------
	@Id
	@Column(name="ALIAS", nullable=false, length=50)
	private String alias;
	public String getAlias() { return alias; }
	public void setAlias(String alias) { this.alias = alias; }

	// A1s -------------------------------------------------------------------------------
	@OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="ALIAS", referencedColumnName = "A2ALIAS")
	private Set<EntityA1> a1s;
	public Set<EntityA1> getA1s() { return a1s; }
	public void setA1s(Set<EntityA1> a1s) { this.a1s = a1s; }
	
	

}
