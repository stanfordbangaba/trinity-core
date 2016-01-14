package zw.co.bangsoft.trinity.listener;

import java.util.Date;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.shiro.SecurityUtils;

import zw.co.bangsoft.trinity.iface.Auditable;
import zw.co.bangsoft.trinity.model.Audit;
import zw.co.bangsoft.trinity.model.AuditTrail;
import zw.co.bangsoft.trinity.util.SystemConstants;

public class AuditListener {

  @PrePersist
  @PreUpdate
  public void beforeUpdate(Auditable entity) {
    try {
      if (entity.getAudit() == null) {
        System.out.println("Audit listener: prePersist ");
        Audit audit = new Audit();
        audit.setDateCreated(new Date());
        audit.setLastUpdated(new Date());
        if (SecurityUtils.getSubject() != null) {
          audit.setCreatedBy(SecurityUtils.getSubject().getPrincipal().toString());
          audit.setLastUpdatedBy(SecurityUtils.getSubject().getPrincipal().toString());
        }
        entity.setAudit(audit);
      } else {
        System.out.println("Audit listener: preUpdate ");
        Audit audit = entity.getAudit();
        audit.setLastUpdated(new Date());
        if (SecurityUtils.getSubject() != null) {
          audit.setLastUpdatedBy(SecurityUtils.getSubject().getPrincipal().toString());
        }
      }
      System.out.println("Audit listener: finish ");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @PostPersist
  public void afterPersist(Auditable entity) {
    this.fireAuditEvent(entity, SystemConstants.AUDIT_ACTION_CREATE);
  }

  @PostUpdate
  public void afterUpdate(Auditable entity) {
    //this.fireAuditEvent(entity, SystemConstants.AUDIT_ACTION_UPDATE);
  }

  public void fireAuditEvent(Auditable entity, String action) {
    try {
      System.out.println("Audit listener: post" + action);
      AuditTrail auditTrail = new AuditTrail();
      auditTrail.setDateCreated(new Date());
      auditTrail.setEntityClass(entity.getClass().getName());
      auditTrail.setEntityId(entity.getId());
      if (SecurityUtils.getSubject() != null) {
        auditTrail.setUsername(SecurityUtils.getSubject().getPrincipal().toString());
      }
      auditTrail.setAction(action);
      auditTrail.setTrail(entity.getAuditTrail());

      AuditListener.getBeanManager().fireEvent(auditTrail);

    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * CDI is not available for entity listeners so have to use JNDI
   * @return
   */
  public static BeanManager getBeanManager() {
    try {
        InitialContext initialContext = new InitialContext();
        return (BeanManager) initialContext.lookup("java:comp/BeanManager");
    } catch (NamingException e) {
        System.out.println("Error in EntityListener resolution of Bean Manager" + e);
        throw new IllegalStateException(e);
    }
  }
}
