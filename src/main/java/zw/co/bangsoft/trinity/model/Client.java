package zw.co.bangsoft.trinity.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import zw.co.bangsoft.trinity.listener.AuditListener;

@Entity
@XmlRootElement
@Table(name="client")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="FROM_CLASS", discriminatorType=DiscriminatorType.STRING)
@EntityListeners(AuditListener.class)
public abstract @Data class Client implements Serializable {

	/**
	 * Abstract model for the client
	 */
	protected static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	protected Long id;
	@Version
	@Column(name = "version")
	protected int version;

	@ManyToOne(targetEntity = Branch.class)
	@JoinColumn(name="branch_id", referencedColumnName="id")
	private Branch branch;

	@OneToMany(targetEntity = ClientProfile.class)
	private List<ClientProfile> profiles;

	@NotNull
	@Column(length = 30)
	protected String type;

	@Embedded
	protected Address address;

	@Column(length=50)
  private String status;

	@Embedded
	protected Audit audit;

}