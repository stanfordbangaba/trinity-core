package zw.co.bangsoft.trinity.auth;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import zw.co.bangsoft.trinity.annotation.Email;
import zw.co.bangsoft.trinity.model.Audit;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.shiro.crypto.hash.Sha256Hash;

@Entity
@Table(name = "user")
@XmlRootElement
public class User implements Serializable {

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

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getShouldChangePwd() {
		return shouldChangePwd;
	}

	public void setShouldChangePwd(boolean shouldChangePwd) {
		this.shouldChangePwd = shouldChangePwd;
	}

	public boolean getAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public boolean getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public boolean getPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Audit getAudit() {
		return audit;
	}

	public void setAudit(Audit audit) {
		this.audit = audit;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (username != null && !username.trim().isEmpty())
			result += "username: " + username;
		if (enabled)
			result += ", enabled: " + enabled;
		if (password != null && !password.trim().isEmpty())
			result += ", password: " + password;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

}