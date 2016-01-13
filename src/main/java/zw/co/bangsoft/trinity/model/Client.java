package zw.co.bangsoft.trinity.model;

import java.io.Serializable;

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
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import lombok.Data;
import zw.co.bangsoft.trinity.listener.AuditListener;

@Entity
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

	@NotNull
	@Column(length = 30)
	protected String type;

	@Embedded
	protected Address address;

	@Embedded
	protected Audit audit;

}