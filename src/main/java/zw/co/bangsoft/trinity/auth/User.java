package zw.co.bangsoft.trinity.auth;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import zw.co.bangsoft.trinity.annotation.Email;
import zw.co.bangsoft.trinity.iface.Auditable;
import zw.co.bangsoft.trinity.listener.AuditListener;
import zw.co.bangsoft.trinity.model.Audit;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

@Entity
@Table(name = "user")
@EntityListeners(value = AuditListener.class)
@XmlRootElement
@ToString(of={"username"})
public @Data @Builder class User implements Serializable, Auditable {

	/**
	 * Model for system users
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
	@Size(min = 1, max = 50)
	private String firstName;

	@Column
	@NotNull
	@Size(min = 1, max = 50)
	private String lastName;

	@Column
	@NotNull
	@Size(min = 5, max = 50)
	private String username;

	@Column
	@Size(min = 8, max = 100)
	private String password;

	@Column
	@Email
	@Size(max = 100)
	private String email;

	@Column
	private boolean shouldChangePwd;

	@Column
	private boolean enabled;

	@Column
	private boolean accountExpired;

	@Column
	private boolean accountLocked;

	@Column
	private boolean passwordExpired;

	@Column
	private String status;

	@Embedded
	private Audit audit;

	@OneToMany(targetEntity = UserRole.class, mappedBy = "user")
	private List<UserRole> userRoles;

	@Override
	public String getAuditTrail() {
	  return "firstName=" + firstName + ", lastName=" + lastName + ", username=" + username +
	      ", email=" + email + ", shouldChangePwd=" + shouldChangePwd + ", enabled=" + enabled +
	        ", accountExpired=" + accountExpired + ", accountLocked=" + accountLocked + ", passwordExpired=" + passwordExpired + ", status" + status;

	}

	@Tolerate public User() {}

}