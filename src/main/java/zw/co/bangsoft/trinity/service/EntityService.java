package zw.co.bangsoft.trinity.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.shiro.SecurityUtils;

import zw.co.bangsoft.trinity.annotation.UserLoggedIn;
import zw.co.bangsoft.trinity.auth.AccessRight;
import zw.co.bangsoft.trinity.auth.Role;
import zw.co.bangsoft.trinity.auth.RoleAccessRight;
import zw.co.bangsoft.trinity.auth.User;
import zw.co.bangsoft.trinity.iface.Auditable;
import zw.co.bangsoft.trinity.model.AuditTrail;
import lombok.val;

/**
 * Session Bean implementation class EntityService
 */
@Stateless
@LocalBean
public class EntityService {

    @PersistenceContext(unitName = "trinity-core")
    private EntityManager em;

    /**
     * Default constructor.
     */
    public EntityService() {
        // TODO Auto-generated constructor stub
    }

    //handle user logged in event
    public void onUserLoggedIn(@Observes @UserLoggedIn User user) {
        System.out.println("Caught userLoggedIn event: " + user.getUsername());
    }

    @Asynchronous
    public Future<String> addRoleAccessRights(Role role) {
      System.out.println(">> Adding access rights to role [" + role + "]" );
      try {
        this.getAllAccessRights().stream()
          .map(ar -> {
            return RoleAccessRight.builder()
                .accessRight(ar)
                .role(role)
                .granted(false)
                .build();
          }).forEach(rar -> em.persist(rar));

        return new AsyncResult<String>("Future result >> Access Rights added successfully to role");

      } catch(Exception e) {
        return new AsyncResult<String>("Future result >> Error adding access rights to role : " + e);
      }
    }

    public List<AccessRight> getAllAccessRights() {
      CriteriaQuery<AccessRight> criteria = em.getCriteriaBuilder().createQuery(AccessRight.class);
      return em.createQuery(criteria.select(criteria.from(AccessRight.class)))
          .getResultList();
    }

    public void addRoleAccessRights(AccessRight accessRight) {
      this.getAllRoles().stream()
        .map(role -> {
          return RoleAccessRight.builder()
              .accessRight(accessRight)
              .role(role)
              .granted(false)
              .build();
        }).forEach(rar -> em.persist(rar));
    }

    public List<Role> getAllRoles() {
      CriteriaQuery<Role> criteria = em.getCriteriaBuilder().createQuery(Role.class);
      return em.createQuery(criteria.select(criteria.from(Role.class)))
          .getResultList();
    }

    public User getUserByUsername(String username) {

      try {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.username = :username");
        query.setParameter("username", username);
        User user = (User) query.getSingleResult();
        return user;
      } catch(NoResultException ne) {

      }
      return null;
    }

    public void createAuditTrail(Auditable entity, String action) {
      try {
        System.out.println("Audit listener: postPersist/postUpdate");
        val auditTrail = AuditTrail.builder()
            .dateCreated(new Date())
            .entityClass(entity.getClass().getName())
            .entityId(entity.getId())
            .action(action)
            .trail(entity.getAuditTrail())
            .username(SecurityUtils.getSubject().getPrincipal().toString())
            .build();

        this.createAuditTrail(auditTrail);

      } catch(Exception e) {
        e.printStackTrace();
      }
    }

    public void createAuditTrail(AuditTrail auditTrail) {
      try {
        em.persist(auditTrail);
        System.out.println("Persisted audit trail event!");
      } catch(Exception e) {
        e.printStackTrace();
      }
    }

}
