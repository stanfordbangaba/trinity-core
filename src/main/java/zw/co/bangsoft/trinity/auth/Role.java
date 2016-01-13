package zw.co.bangsoft.trinity.auth;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;
import zw.co.bangsoft.trinity.iface.Auditable;
import zw.co.bangsoft.trinity.listener.AuditListener;
import zw.co.bangsoft.trinity.model.Audit;

@Entity
@Table(name = "role")
@EntityListeners(value = AuditListener.class)
@XmlRootElement
@ToString(of="name")
public @Data @Builder class Role implements Serializable, Auditable {

	/**
	 * Model for user roles
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@Column(length = 50, unique = true)
	@NotNull
	private String name;

	@OneToMany(targetEntity = RoleAccessRight.class, mappedBy = "role")
	private List<RoleAccessRight> roleAccessRights;

	@OneToMany(targetEntity = UserRole.class, mappedBy = "role")
	private List<UserRole> roleUsers;

  @Embedded private Audit audit;

	@Override
	public String getAuditTrail() {
	   return "name=" + name;
	}

	@Tolerate public Role() {}

}