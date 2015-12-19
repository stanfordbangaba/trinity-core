package zw.co.bangsoft.trinity.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import zw.co.bangsoft.trinity.annotation.LoggedIn;
import zw.co.bangsoft.trinity.annotation.UserLoggedIn;
import zw.co.bangsoft.trinity.auth.User;

@Named
@SessionScoped
public class LoginBean implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Inject @UserLoggedIn Event<User> userLoggedIn;

  @Inject private EntityService entityService;

  private Subject subject;

  private User user;

  public LoginBean() {
    // TODO Auto-generated constructor stub
  }

  @Produces @LoggedIn
  public User getUser() {
    if (user == null && isLoggedIn()) {
        String username = (String) this.getSubject().getPrincipal();
        user = entityService.getUserByUsername(username);
    }
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Subject getSubject() {
    return SecurityUtils.getSubject();
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public ExternalContext getFacesContext() {
      return FacesContext.getCurrentInstance().getExternalContext();
  }

  public String logout() {
      System.out.println("Logging out...");
      user = null;
      SecurityUtils.getSubject().logout();
      this.getFacesContext().invalidateSession();
      try {
        this.getFacesContext().redirect("login/auth.xhtml");
      } catch(IOException e) {}
      return "";
  }

//  public void registerLoginToken(String username) {
//    this.getSessionMap().ifPresent(map -> map.put("username", username));
//    System.out.println("Login token put in session..");
//  }
//
//  public void removeLoginToken(String username) {
//    this.getSessionMap().ifPresent(map -> map.remove(username));
//    System.out.println("Login token removed from session..");
//  }

  public boolean isLoggedIn() {
    if (this.getSubject() != null) {
      return true;
    }
    return false;
  }

  public Optional<Map<String, Object>> getSessionMap() {
    Map<String, Object> sessionMap = null;
    try {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        sessionMap = externalContext.getSessionMap();
    } catch(Exception e) {
        System.out.println("Session map is null here..");
    }
    return Optional.ofNullable(sessionMap);
  }

}
