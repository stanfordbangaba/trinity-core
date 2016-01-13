package zw.co.bangsoft.trinity.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.omnifaces.util.Messages;

import zw.co.bangsoft.trinity.auth.AccessRight;
import zw.co.bangsoft.trinity.service.EntityService;

/**
 * Backing bean for AccessRight entities.
 * <p/>
 * This class provides CRUD functionality for all AccessRight entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class AccessRightBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving AccessRight entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private AccessRight accessRight;

	public AccessRight getAccessRight() {
		return this.accessRight;
	}

	public void setAccessRight(AccessRight accessRight) {
		this.accessRight = accessRight;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "trinity-core", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@Inject private EntityService entityService;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.accessRight = this.example;
		} else {
			this.accessRight = findById(getId());
		}
	}

	public AccessRight findById(Long id) {

		return this.entityManager.find(AccessRight.class, id);
	}

	/*
	 * Support updating and deleting AccessRight entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.accessRight);
				entityService.addRoleAccessRights(accessRight);
			//	entityService.createAuditTrail(accessRight, SystemConstants.AUDIT_ACTION_CREATE);
				Messages.addGlobalInfo("Access Right created");
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.accessRight);
        //entityService.createAuditTrail(accessRight, SystemConstants.AUDIT_ACTION_CREATE);
				Messages.addGlobalInfo("Access Right updated");
				return "view?faces-redirect=true&id="
						+ this.accessRight.getId();
			}
		} catch (Exception e) {
		  Messages.addGlobalError(e.getMessage());
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			AccessRight deletableEntity = findById(getId());

			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
		  Messages.addGlobalError(e.getMessage(), true);
			return null;
		}
	}

	/*
	 * Support searching AccessRight entities with pagination
	 */

	private int page;
	private long count;
	private List<AccessRight> pageItems;

	private AccessRight example = new AccessRight();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public AccessRight getExample() {
		return this.example;
	}

	public void setExample(AccessRight example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<AccessRight> root = countCriteria.from(AccessRight.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<AccessRight> criteria = builder
				.createQuery(AccessRight.class);
		root = criteria.from(AccessRight.class);
		TypedQuery<AccessRight> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<AccessRight> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String url = this.example.getUrl();
		if (url != null && !"".equals(url)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("url")),
					'%' + url.toLowerCase() + '%'));
		}
		String httpMethod = this.example.getHttpMethod();
		if (httpMethod != null && !"".equals(httpMethod)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("httpMethod")),
					'%' + httpMethod.toLowerCase() + '%'));
		}
		String description = this.example.getDescription();
		if (description != null && !"".equals(description)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("description")),
					'%' + description.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<AccessRight> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back AccessRight entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

//	public List<AccessRight> getAll() {
//	  CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
//		CriteriaQuery<AccessRight> criteria = cb.createQuery(AccessRight.class);
//    Root<AccessRight> root = criteria.from(AccessRight.class);
//
//		return entityManager.createQuery(
//				criteria.select(criteria.from(AccessRight.class))
//				.orderBy(cb.asc(root.get("url"))))
//				.getResultList();
//	}

	@SuppressWarnings("unchecked")
  public List<AccessRight> getAll() {
	  Query query = entityManager.createQuery("SELECT a FROM AccessRight a ORDER BY a.url ASC");
	  return (List<AccessRight>) query.getResultList();
  }

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final AccessRightBean ejbProxy = this.sessionContext
				.getBusinessObject(AccessRightBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return ejbProxy.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((AccessRight) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private AccessRight add = new AccessRight();

	public AccessRight getAdd() {
		return this.add;
	}

	public AccessRight getAdded() {
		AccessRight added = this.add;
		this.add = new AccessRight();
		return added;
	}

}
