package zw.co.bangsoft.trinity.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import zw.co.bangsoft.trinity.listener.AuditListener;

/**
 * Entity implementation class for Entity: Branch
 *
 */

@Entity
@XmlRootElement
@EntityListeners(value=AuditListener.class)
@Table(name="branch", indexes = {
    @Index(columnList="code"),
    @Index(columnList="bank_id")
})
public @Data @Builder class Branch implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Version
  @Column(name = "version")
  private int version;

  @Column(length = 60)
  @NotNull
  private String name;

  @Column(length = 15)
  @NotNull
  private String code;

  @ManyToOne(targetEntity = Bank.class)
  @JoinColumn(name="bank_id", referencedColumnName="id")
  private Bank bank;

  @Column(length = 60)
  private String status;

  @Embedded
  private Audit audit;


  @Tolerate public Branch() {}
}
