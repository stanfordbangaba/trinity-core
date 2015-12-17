package zw.co.bangsoft.trinity.service;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import zw.co.bangsoft.trinity.annotation.LoggedIn;
import zw.co.bangsoft.trinity.auth.User;

@Named
@SessionScoped
public class LoginBean implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private Credentials credentials = new Credentials();
  private User user;

  public LoginBean() {
    // TODO Auto-generated constructor stub
  }

  public Credentials getCredentials() {
    return credentials;
  }

  public void setCredentials(Credentials credentials) {
    this.credentials = credentials;
  }

  @Produces @LoggedIn
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String login() {

    System.out.println("Login credentials: "
        + credentials.getUsername() + " | " + credentials.getPassword());

    if ("admin".equals(credentials.getUsername()) && "admin".equals(credentials.getPassword())) {
      System.out.println("Authentication success!");

      user = new User();
      user.setLastName("Bangaba");
      user.setFirstName("Stan");
      user.setUsername("stanford");

      credentials.setUsername(null);
      credentials.setPassword(null);;

      return "/index.xhtml";
    }

    FacesContext.getCurrentInstance().addMessage("Authentication failed", null);

    return "failure";
  }

  public String logout() {
      System.out.println("Logging out...");
      user = null;
      return "/index.xhtml";
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
    System.out.println("Is loggeIn user: " +(user != null));
    return user != null;
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
