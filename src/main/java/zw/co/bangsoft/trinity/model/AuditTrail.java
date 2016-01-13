package zw.co.bangsoft.trinity.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * Entity implementation class for Entity: AuditTrail
 *
 */
@Entity
@Table(name="audit_trail")
@XmlRootElement
public @Data @Builder class AuditTrail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;
  @Version
  @Column(name = "version")
  private int version;

  @Column
  private Long entityId;

  @Column(length=40)
  private String entityClass;

  @Column(length=30)
  private String username;

  @Column(length=30)
  private String action;

  @Column(length=500)
  private String trail;

  @Column
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateCreated;


  @Tolerate public AuditTrail() {}

}
