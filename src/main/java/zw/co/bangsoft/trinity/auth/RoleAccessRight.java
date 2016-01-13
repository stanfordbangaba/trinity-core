package zw.co.bangsoft.trinity.auth;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import java.io.Serializable;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import zw.co.bangsoft.trinity.iface.Auditable;
import zw.co.bangsoft.trinity.listener.AuditListener;
import zw.co.bangsoft.trinity.model.Audit;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

@Entity
@Table(name = "role_access_right")
@EntityListeners(value = AuditListener.class)
@XmlRootElement
public @Data @Builder class RoleAccessRight implements Serializable, Auditable {

	/**
	 * Many-to-many mapping for Role and AccessRight
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@NotNull
	@ManyToOne(targetEntity = Role.class)
	private Role role;

	@NotNull
	@ManyToOne(targetEntity = AccessRight.class)
	private AccessRight accessRight;

	@Column
	private boolean granted;

	@Embedded
	private Audit audit;

	@Override
	public String getAuditTrail() {
	  return "role=" + role + ", accessRight=" + accessRight + ", granted=" + granted;
	}

	@Tolerate public RoleAccessRight() {}

	public String toString() {
	  try {
	    return role.getName() + " " + accessRight.getUrl() + " " + granted;
	  } catch(Exception e) {
	    return "";
	  }
	}
}