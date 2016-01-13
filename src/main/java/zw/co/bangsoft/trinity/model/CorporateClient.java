package zw.co.bangsoft.trinity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper=false)
public @Data class CorporateClient extends Client implements Serializable {

	/**
   * Client type Corporate class
   */
  private static final long serialVersionUID = 1L;

  @Column(length=150)
	private String name;

  public String getType() {
    return CorporateClient.class.getSimpleName();
  }

}