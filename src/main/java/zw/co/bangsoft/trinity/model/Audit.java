package zw.co.bangsoft.trinity.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

@Embeddable
@XmlRootElement
public class Audit implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@Column
	private Date dateCreated;
	@Column
	private Date lastUpdated;
	@Column
	private String createdBy;
	@Column
	private String lastUpdatedBy;

	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (dateCreated != null)
			result += "dateCreated: " + dateCreated;
		return result;
	}

	 @Override
	  public boolean equals(Object obj) {
	    if (this == obj) {
	      return true;
	    }
	    if (!(obj instanceof Client)) {
	      return false;
	    }

	    return true;
	  }

	  @Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
	    return result;
	  }

}