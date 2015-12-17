package zw.co.bangsoft.trinity.service;

import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import zw.co.bangsoft.trinity.auth.AccessRight;
import zw.co.bangsoft.trinity.auth.Role;
import zw.co.bangsoft.trinity.auth.RoleAccessRight;

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

    @Asynchronous
    public void addRoleAccessRights(Role role) {
      this.getAllAccessRights().stream()
        .map(ar -> {
          RoleAccessRight rar = new RoleAccessRight();
          rar.setAccessRight(ar);
          rar.setRole(role);
          rar.setGranted(false);
          return rar;
        }).forEach(rar -> em.persist(rar));
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

}
