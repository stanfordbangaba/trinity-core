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
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import zw.co.bangsoft.trinity.iface.Auditable;
import zw.co.bangsoft.trinity.listener.AuditListener;
import zw.co.bangsoft.trinity.model.Audit;

@Entity
@Table(name = "user_role")
@EntityListeners(value = AuditListener.class)
@XmlRootElement
public @Data @Builder class UserRole implements Serializable, Auditable {

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

	@NotNull
	@ManyToOne(targetEntity = User.class)
	private User user;

	@NotNull
	@ManyToOne(targetEntity = Role.class)
	private Role role;

	@Column
	@Digits(integer = 1, fraction = 0)
	private Integer approvalOrder;

	@Embedded private Audit audit;

	@Override
	public String getAuditTrail() {
	   return "user=" + user + ", role=" + role + ", approvalOrder=" + approvalOrder;
	}

  @Tolerate public UserRole() {}

  public String toString() {
    try {
      return user.getUsername() + " " + role.getName();
    } catch(Exception e) {
      return "";
    }
  }
}