package zw.co.bangsoft.trinity.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import zw.co.bangsoft.trinity.listener.AuditListener;

@Entity
@EntityListeners(AuditListener.class)
@XmlRootElement
@Table(name="account", indexes = {
    @Index(columnList="accountNumber"),
    @Index(columnList="ownerId"),
    @Index(columnList="ownerType"),
    @Index(columnList="branch_id")
})
public @Data @Builder class Account implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(name = "version")
	private int version;

	@Column(length = 30)
	private String accountNumber;

	@Column(length = 150)
  private String accountName;

	@Column(length = 30)
  private String ownerId;

	@Column(length = 30)
  private String ownerType;

	@ManyToOne(targetEntity = Branch.class)
  @JoinColumn(name="branch_id", referencedColumnName="id")
  private Branch branch;


	@Tolerate public Account() {}

}