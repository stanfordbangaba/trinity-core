package zw.co.bangsoft.trinity.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.primefaces.context.RequestContext;

import zw.co.bangsoft.trinity.annotation.LoggedIn;
import zw.co.bangsoft.trinity.annotation.UserLoggedIn;
import zw.co.bangsoft.trinity.auth.User;

@Named
@RequestScoped
public class LoginBean implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private final String HOME_URL = "/index.xhtml";
  private final String LOGIN_URL = "login/auth.xhtml";

  @Inject @UserLoggedIn Event<User> userLoggedIn;

  @Inject private EntityService entityService;

  private String username;
  private String password;

  private User user;

  public LoginBean() {
    // TODO Auto-generated constructor stub
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void login(ActionEvent event) {

    String errorMsg = null;
    RequestContext context = RequestContext.getCurrentInstance();
    boolean loggedIn = false;

    try {

        System.out.println("Trying programmatic login..");
        SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
        System.out.println("Authentication success for user: " + username);

        System.out.println("Verifying if user is enabled...");
        user = this.getUser();
        if (!user.isEnabled()) {
          errorMsg = "User account disabled";
        }
        if (user.isAccountExpired()) {
          errorMsg = "User account expired";
        }
        if (user.isAccountLocked()) {
          errorMsg = "User account locked";
        }
        if (user.isPasswordExpired()) {
          errorMsg = "User password expired";
        }
        if (user.isShouldChangePwd()) {
          errorMsg = "User should change password";
        }

        if (errorMsg != null) {
          throw new Exception(errorMsg);
        }

        loggedIn = true;

        //roles and permissions debug
        getSubject().getPrincipals().asList()
          .forEach(p -> System.out.println("Principal item: " + p));

        System.out.println("Has role ADMIN: " + getSubject().hasRole("ADMIN"));

        System.out.println("Is permitted accessRight:create -> " + getSubject().isPermitted("accessRight:create"));
        System.out.println("Is permitted user:create -> " + getSubject().isPermitted("user:create"));

     //   this.getFacesContext().redirect(HOME_URL);
     //   return HOME_URL;
    } catch (AuthenticationException e) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown user, please try again"));
        System.out.println("AuthenticationException: " + e.getMessage()); // TODO: logger.
    } catch (Exception e) {
        errorMsg = (errorMsg != null)? errorMsg : "Authentication failed";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(errorMsg));
        System.out.println("Exception: " + e.getMessage()); // TODO: logger.
        if (isLoggedIn()) {
          SecurityUtils.getSubject().logout();
        }
    }
    context.addCallbackParam("loggedIn", loggedIn);
    //return LOGIN_URL;
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

  public ExternalContext getFacesContext() {
      return FacesContext.getCurrentInstance().getExternalContext();
  }

  public String logout() {
      System.out.println("Logging out...");
      user = null;
      SecurityUtils.getSubject().logout();
      this.getFacesContext().invalidateSession();
      try {
        this.getFacesContext().redirect(LOGIN_URL);
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
