package com.digitalmedia.users.controller;

import com.digitalmedia.users.exceptions.LocalNotFoundException;
import com.digitalmedia.users.model.*;
import com.digitalmedia.users.model.dto.AliasRequest;
import com.digitalmedia.users.model.dto.PasswordRequest;
import com.digitalmedia.users.model.dto.UserRequest;
import com.digitalmedia.users.model.dto.UserResponse;
import com.digitalmedia.users.service.UserService;
import com.digitalmedia.users.service.UserVerificationService;
import com.digitalmedia.users.utiles.clsUtiles;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private final UserService userService ;

  @Autowired
  UserVerificationService userVerificationService;

  @GetMapping("/test")
  public String getInfo() {
    return "Te has conectado al microservicio de Usuarios.";
  }

  @GetMapping("/passwordRecover/{mail}")
  public ResponseEntity<?> passwordRecover(@PathVariable String mail) throws Exception {
    Optional<User> userDB = userService.getUserByEmail(mail);

    if (userDB.isEmpty()) {
      ResponseEntity.notFound().build();
    }

    String code = userVerificationService.findByEmail(mail);

    userService.sendEmail(mail,"Recuperacion de Contraseña","www.unLink.com.ar/"+code);

    return ResponseEntity.ok("");
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> GetUser(@Valid @PathVariable Long id) throws Exception {
    UserResponse userRet = userService.getUser(id);

    if(userRet == null){
      throw new LocalNotFoundException("Usuario no encontrado");
    }

    return ResponseEntity.ok(userRet);
  }

  @PatchMapping("/alias")
  public  ResponseEntity<?> PatchAlias(@Valid @RequestBody AliasRequest aliasRequest) throws Exception {
    String userId = SecurityContextHolder.getContext().getAuthentication().getName();
    String ret = userService.updateAlias(userId,aliasRequest);

    return ResponseEntity.ok("Actualizado con exito");
  }

  @PatchMapping()
  public  ResponseEntity<?> PatchUser(@Valid @RequestBody UserRequest userRequest) throws Exception {
    String userId = SecurityContextHolder.getContext().getAuthentication().getName();

    UserResponse ret = userService.update(userId,userRequest);

    if(ret==null){
      ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(ret );
  }

  @PatchMapping("/password/{code}")
  public  ResponseEntity<?> PatchUserPassword( @PathVariable String code ,@Valid @RequestBody PasswordRequest passwordRequest) throws Exception {
   Optional<UserVerified> userVerified = userVerificationService.findByCode(code);

    if (userVerified.isEmpty()){
      throw new NotFoundException("No se pudo recuperar el usuario con el codigo ingresado");
    }

    String ret = userService.updatePassword(userVerified.get().getIdUser(),passwordRequest);

    if(ret == ""){
      throw  new Exception();
    }

    return ResponseEntity.ok(ret);
  }

  @PostMapping("/register")
  public ResponseEntity<?> save(@Valid @RequestBody UserRequest user) throws Exception {

    //valido campos
    clsUtiles.validarCampos(user);

    //inserto usuario
    UserResponse userRet = userService.saveUser(user);

    //valido insert
    if (userRet == null){
      return ResponseEntity.internalServerError().build();
    }

    //retorno response
    return ResponseEntity.ok(userRet);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody Login login) throws Exception {
    AccessKeycloak credenciales = userService.login(login.getEmail(),login.getPassword());

    if (credenciales.getAccessToken().isEmpty()){
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(credenciales);
  }

  @PostMapping("/EmailVerified/{code}")
  public ResponseEntity<?> emailVerication(@PathVariable String code) throws Exception {

    Optional<UserVerified> userVerifiedRet = userVerificationService.findByCode(code);

    if(userVerifiedRet.isEmpty()){
    return  ResponseEntity.notFound().build();
    }

    userVerifiedRet.get().setFecha(LocalDate.now().toString());

    userVerificationService.save(userVerifiedRet.get());

    return ResponseEntity.ok("Verificado con exito");
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout() throws Exception {
    String userId = SecurityContextHolder.getContext().getAuthentication().getName();
    System.out.println(userId);

    if (userId.isEmpty()) {
      ResponseEntity.notFound().build();
    }

    userService.logout(userId);

    return ResponseEntity.ok("Cerrado con exito");
  }
}
