package zw.co.bangsoft.trinity.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import zw.co.bangsoft.trinity.auth.RoleAccessRight;
import zw.co.bangsoft.trinity.auth.AccessRight;
import zw.co.bangsoft.trinity.auth.Role;

/**
 * Backing bean for RoleAccessRight entities.
 * <p/>
 * This class provides CRUD functionality for all RoleAccessRight entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class RoleAccessRightBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving RoleAccessRight entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private RoleAccessRight roleAccessRight;

	public RoleAccessRight getRoleAccessRight() {
		return this.roleAccessRight;
	}

	public void setRoleAccessRight(RoleAccessRight roleAccessRight) {
		this.roleAccessRight = roleAccessRight;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "trinity-core-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

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
			this.roleAccessRight = this.example;
		} else {
			this.roleAccessRight = findById(getId());
		}
	}

	public RoleAccessRight findById(Long id) {

		return this.entityManager.find(RoleAccessRight.class, id);
	}

	/*
	 * Support updating and deleting RoleAccessRight entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.roleAccessRight);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.roleAccessRight);
				return "view?faces-redirect=true&id="
						+ this.roleAccessRight.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			RoleAccessRight deletableEntity = findById(getId());
			Role role = deletableEntity.getRole();
			role.getRoleAccessRights().remove(deletableEntity);
			deletableEntity.setRole(null);
			this.entityManager.merge(role);
			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching RoleAccessRight entities with pagination
	 */

	private int page;
	private long count;
	private List<RoleAccessRight> pageItems;

	private RoleAccessRight example = new RoleAccessRight();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public RoleAccessRight getExample() {
		return this.example;
	}

	public void setExample(RoleAccessRight example) {
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
		Root<RoleAccessRight> root = countCriteria.from(RoleAccessRight.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<RoleAccessRight> criteria = builder
				.createQuery(RoleAccessRight.class);
		root = criteria.from(RoleAccessRight.class);
		TypedQuery<RoleAccessRight> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<RoleAccessRight> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Role role = this.example.getRole();
		if (role != null) {
			predicatesList.add(builder.equal(root.get("role"), role));
		}
		AccessRight accessRight = this.example.getAccessRight();
		if (accessRight != null) {
			predicatesList.add(builder.equal(root.get("accessRight"),
					accessRight));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<RoleAccessRight> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back RoleAccessRight entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<RoleAccessRight> getAll() {

		CriteriaQuery<RoleAccessRight> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(RoleAccessRight.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(RoleAccessRight.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final RoleAccessRightBean ejbProxy = this.sessionContext
				.getBusinessObject(RoleAccessRightBean.class);

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

				return String.valueOf(((RoleAccessRight) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private RoleAccessRight add = new RoleAccessRight();

	public RoleAccessRight getAdd() {
		return this.add;
	}

	public RoleAccessRight getAdded() {
		RoleAccessRight added = this.add;
		this.add = new RoleAccessRight();
		return added;
	}
}
