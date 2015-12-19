package zw.co.bangsoft.trinity.service;

import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import zw.co.bangsoft.trinity.annotation.LoggedIn;
import zw.co.bangsoft.trinity.annotation.UserLoggedIn;
import zw.co.bangsoft.trinity.auth.AccessRight;
import zw.co.bangsoft.trinity.auth.Role;
import zw.co.bangsoft.trinity.auth.RoleAccessRight;
import zw.co.bangsoft.trinity.auth.User;

/**
 * Session Bean implementation class EntityService
 */
@Stateless
@LocalBean
public class EntityService {

    @PersistenceContext(unitName = "trinity-core-persistence-unit")
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
            RoleAccessRight rar = new RoleAccessRight();
            rar.setAccessRight(ar);
            rar.setRole(role);
            rar.setGranted(false);
            return rar;
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

    @Asynchronous
    public void addRoleAccessRights(AccessRight accessRight) {
      this.getAllRoles().stream()
        .map(role -> {
          RoleAccessRight rar = new RoleAccessRight();
          rar.setAccessRight(accessRight);
          rar.setRole(role);
          rar.setGranted(false);
          return rar;
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
}
