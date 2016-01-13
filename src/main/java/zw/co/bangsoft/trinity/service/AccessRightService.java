package zw.co.bangsoft.trinity.service;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import zw.co.bangsoft.trinity.auth.AccessRight;

/**
 * Session Bean implementation class AccessRightService
 */
@Stateless
@LocalBean
public class AccessRightService {

  @PersistenceContext(unitName = "trinity-core")
  private EntityManager em;

    /**
     * Default constructor.
     */
    public AccessRightService() {
        // TODO Auto-generated constructor stub
    }

    public AccessRight create(AccessRight entity) {
      em.persist(entity);
      return entity;
    }

    public void deleteById(Long id) {
      AccessRight entity = em.find(AccessRight.class, id);
      em.remove(entity);
    }

    public AccessRight findById(Long id) {
      TypedQuery<AccessRight> findByIdQuery = em
          .createQuery(
              "SELECT DISTINCT a FROM AccessRight a WHERE a.id = :entityId ORDER BY a.id",
              AccessRight.class);
      findByIdQuery.setParameter("entityId", id);
      AccessRight entity;
      try {
        entity = findByIdQuery.getSingleResult();
      } catch (NoResultException nre) {
        entity = null;
      }

      return entity;
    }

    public List<AccessRight> listAll() {
      return this.listAll(null, null);
    }

    public List<AccessRight> listAll(Integer startPosition, Integer maxResult) {
      TypedQuery<AccessRight> findAllQuery = em.createQuery(
          "SELECT DISTINCT a FROM AccessRight a ORDER BY a.url",
          AccessRight.class);
      if (startPosition != null) {
        findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null) {
        findAllQuery.setMaxResults(maxResult);
      }
      final List<AccessRight> results = findAllQuery.getResultList();
      return results;
    }

    public AccessRight update(AccessRight entity) {
      entity = em.merge(entity);
      return entity;
    }

}
