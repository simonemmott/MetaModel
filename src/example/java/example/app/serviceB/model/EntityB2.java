package example.app.serviceB.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.k2.MetaModel.annotations.MetaClass;
import com.k2.MetaModel.annotations.MetaEntity;
import com.k2.MetaModel.annotations.MetaVersion;

import example.app.serviceA.model.EntityA1;

@MetaVersion(major=0, minor=0, point=1)
@MetaClass
@MetaEntity
@Entity
@Table(name="TYPEB2S")
public class EntityB2 {

	// Name ------------------------------------------------------------------------------
	@Id
	@Column(name="ALIAS", nullable=false, length=50)
	private String alias;
	public String getAlias() { return alias; }
	public void setAlias(String alias) { this.alias = alias; }

	// B1s -------------------------------------------------------------------------------
	@OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="ALIAS", referencedColumnName = "B2ALIAS")
	private Set<EntityB1> b1s;
	public Set<EntityB1> getA1s() { return b1s; }
	public void setB1s(Set<EntityB1> b1s) { this.b1s = b1s; }
	
	
}
