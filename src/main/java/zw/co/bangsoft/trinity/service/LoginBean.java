package zw.co.bangsoft.trinity.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

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

  private final String HOME_URL = "/index.xhtml";
  private final String LOGIN_URL = "login/auth.xhtml";

  @Inject @UserLoggedIn Event<User> userLoggedIn;

  @Inject private EntityService entityService;

  private String username;
  private String password;

  private Subject subject;

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

  public String login() throws IOException {

    String errorMsg = null;

    try {
        System.out.println("Trying programmatic login..");
        SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
        System.out.println("Authentication success for user: " + username);
        System.out.println("Clearing sensitive fields ...");
        password = null;

        System.out.println("Verifying if user is enabled...");
        user = this.getUser();
        if (!user.getEnabled()) {
          errorMsg = "User account disabled";
        }
        if (user.getAccountExpired()) {
          errorMsg = "User account expired";
        }
        if (user.getAccountLocked()) {
          errorMsg = "User account locked";
        }
        if (user.getPasswordExpired()) {
          errorMsg = "User password expired";
        }
        if (user.getShouldChangePwd()) {
          errorMsg = "User should change password";
        }

        if (errorMsg != null) {
          throw new Exception(errorMsg);
        }
     //   this.getFacesContext().redirect(HOME_URL);
        return HOME_URL;
    } catch (AuthenticationException e) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown user, please try again"));
        System.out.println("AuthenticationException: " + e.getMessage()); // TODO: logger.
    } catch (Exception e) {
        errorMsg = (errorMsg != null)? errorMsg : "Authentication failed";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(errorMsg));
        System.out.println("Exception: " + e.getMessage()); // TODO: logger.
        if (isLoggedIn()) {
            this.logout();
        }
    }
    return LOGIN_URL;
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
