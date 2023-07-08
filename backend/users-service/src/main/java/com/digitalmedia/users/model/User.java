package com.digitalmedia.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.keycloak.representations.idm.UserRepresentation;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "Name", nullable = false)
  private String name;

  @Column(name = "last_name", nullable = false)
  private String lastname;

  @Column(name = "DNI", nullable = false)
  private Integer dni;

  @Column(name = "Email", nullable = false)
  private String email;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(name = "Password", nullable = false)
  private String password;

  @Column(name = "CVU", nullable = false)
  private String cvu;

  @Column(name = "Alias", nullable = false)
  private String alias;

  @JsonIgnore
  private String keycloakId;

  public User() {
  }

  public User(Long id, String name, String lastname, Integer dni, String email, String phoneNumber, String password, String cvu, String alias) {
    this.id = id;
    this.name = name;
    this.lastname = lastname;
    this.dni = dni;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.password = password;
    this.cvu = cvu;
    this.alias = alias;
  }

  public String getKeycloakId() {
    return keycloakId;
  }

  public void setKeycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public Integer getDni() {
    return dni;
  }

  public void setDni(Integer dni) {
    this.dni = dni;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCvu() {
    return cvu;
  }

  public void setCvu(String cvu) {
    this.cvu = cvu;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }


  public static User toUser(UserRepresentation userRepresentation) {
    User user = new User();
    user.setKeycloakId(userRepresentation.getId());
    user.setName(userRepresentation.getUsername());
    user.setName(userRepresentation.getFirstName());
    user.setLastname(userRepresentation.getLastName());
    user.setEmail(userRepresentation.getEmail());
        /*user.setDni(getAttribute(userRepresentation, "dni", String.class));
        user.setPhoneNumber(getAttribute(userRepresentation, "phoneNumber", String.class));
        user.setCvu(getAttribute(userRepresentation, "cvu", String.class));
        user.setAlias(getAttribute(userRepresentation, "alias", String.class));
*/
    return user;
  }
}
