package zw.co.bangsoft.trinity.auth;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import java.io.Serializable;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;
import zw.co.bangsoft.trinity.iface.Auditable;
import zw.co.bangsoft.trinity.listener.AuditListener;
import zw.co.bangsoft.trinity.model.Audit;

@Entity
@Table(name = "access_right")
@EntityListeners(value = AuditListener.class)
@XmlRootElement
@ToString(of={"url", "description", "enabled"})
public @Data @Builder class AccessRight implements Serializable, Auditable {

	/**
	 * AccessRight is the model for user permissions
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@Column
	@NotNull
	@Size(min = 1, max = 150)
	private String url;

	@Column(length = 150, nullable = true)
	private String httpMethod;

	@Column(length = 150, nullable = true)
	private String description;

	@Column
	private boolean enabled = true;

	@Embedded
	private Audit audit;

  @Override
  public String getAuditTrail() {
    return "url=" + url + ", httpMethod=" + httpMethod + ", description=" + description + ", enabled=" + enabled;
  }

  @Tolerate public AccessRight() {}

}