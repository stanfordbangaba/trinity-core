package zw.co.bangsoft.trinity.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * Entity implementation class for Entity: ClientProfile
 *
 */
@Entity
@XmlRootElement
@Table(name="client_profile", indexes = {
    @Index(columnList="profileId"),
    @Index(columnList="type")
})
public @Data @Builder class ClientProfile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Version
  @Column(name = "version")
  private int version;

  @ManyToOne(targetEntity = Client.class)
  @JoinColumn(name="client_id", referencedColumnName="id")
  private Client client;

  /**
   * ProfileId is mobile number, email, username etc
   */
  @Column(length=60)
  @NotNull
  private String profileId;

  /**
   * Type in MOBILE_NUMBER, WEB_PROFILE, CARD
   */
  @Column(length=60)
  @NotNull
  private String type;

  @Tolerate public ClientProfile() {}
}
