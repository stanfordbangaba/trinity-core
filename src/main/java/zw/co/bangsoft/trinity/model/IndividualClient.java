package zw.co.bangsoft.trinity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper=false)
public @Data class IndividualClient extends Client implements Serializable {

	/**
   *
   */
  private static final long serialVersionUID = 1L;

  @Column(length=30)
  private String lastName;
  @Column(length=30)
  private String firstName;
  @Column(length=30)
  private String nationalId;
  @Column(length=30)
  private String dateOfBirth;

  public String getType() {
    return IndividualClient.class.getSimpleName();
  }
}