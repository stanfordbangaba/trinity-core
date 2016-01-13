package zw.co.bangsoft.trinity.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Embeddable
@XmlRootElement
public @Data class Audit implements Serializable {

	/**
	 * Model for cross-cutting audit data
	 */
	private static final long serialVersionUID = 1L;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated;
	@Column
	private String createdBy;
	@Column
	private String lastUpdatedBy;

}