package zw.co.bangsoft.trinity.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import zw.co.bangsoft.trinity.listener.AuditListener;

/**
 * Entity implementation class for Entity: Bank
 *
 */

@Entity
@EntityListeners(value=AuditListener.class)
@XmlRootElement
@Table(name="bank", indexes = {
    @Index(columnList="code")
})
public @Data @Builder class Bank implements Serializable {

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

  @Column
  @OneToMany(targetEntity = Branch.class, mappedBy = "bank")
  private List<Branch> branches;

  @Column(length = 60)
  private String status;

  @Embedded
  private Audit audit;


  @Tolerate public Bank() {}
}
