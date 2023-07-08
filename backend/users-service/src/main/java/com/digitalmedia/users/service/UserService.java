package com.digitalmedia.users.service;

import com.digitalmedia.users.exceptions.BadRequestException;
import com.digitalmedia.users.exceptions.LocalNotFoundException;
import com.digitalmedia.users.model.AccessKeycloak;
import com.digitalmedia.users.model.EmailRequest;
import com.digitalmedia.users.model.User;
import com.digitalmedia.users.model.UserVerified;
import com.digitalmedia.users.model.dto.*;
import com.digitalmedia.users.repository.IAccountFeignRepository;
import com.digitalmedia.users.repository.IEmailFeignRepository;
import com.digitalmedia.users.repository.IUserRepository;
import com.digitalmedia.users.utiles.clsUtiles;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Service
@EnableAsync
public class UserService implements IUserService {
  @Autowired
  IUserRepository userRepository;

  @Autowired
  UserVerificationService userVerificationService;

  @Autowired
  IEmailFeignRepository emailFeignRepository;

  @Autowired
  IAccountFeignRepository accountFeignRepository;

  @Autowired
  KeyCloakService keyCloakService;

  @Autowired
  ObjectMapper mapper;

  public Optional<User> getUser(String username) {
    return userRepository.findByName(username);
  }
  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public UserResponse getUser(Long id) throws Exception{
    Optional<User> userDB = userRepository.findById(id);

    if (userDB.isEmpty()){
      throw  new LocalNotFoundException("Usuario no encontrado");
    }

    UserResponse userRet = new UserResponse();
    userRet.setName(userDB.get().getName());
    userRet.setLastname(userDB.get().getLastname());
    userRet.setDni(userDB.get().getDni());
    userRet.setPhoneNumber(userDB.get().getPhoneNumber());
    userRet.setCvu(userDB.get().getCvu());
    userRet.setAlias(userDB.get().getAlias());
    userRet.setEmail(userDB.get().getEmail());

    return userRet;
  }

  public void sendEmail(String toEmail, String subject,String body){
    emailFeignRepository.sendEmail(new EmailRequest(toEmail,subject,body));
  }

  public UserResponse saveUser(UserRequest user) throws Exception {

    Optional<User> userDB = userRepository.findByEmail(user.getEmail());

    //si existe en la DB retorno exception
    if (!userDB.isEmpty()){
      throw new BadRequestException("Ya existe un usuario con el email ingresado");
    }

    User user1 = new User();
    user1.setName(user.getName());
    user1.setLastname(user.getLastname());
    user1.setEmail(user.getEmail());
    user1.setPassword(user.getPassword());
    user1.setDni(user.getDni());
    user1.setPhoneNumber(user.getPhoneNumber());

    //asigno alias
    user1.setAlias(createAlias());

    //asigno CVU
    user1.setCvu(createCVU());

    //grabo en keycloak
    User userKeycloak = keyCloakService.createUser(user1);
    user1.setKeycloakId(userKeycloak.getKeycloakId());

    //Grabo en MYSQL
    User userRet = userRepository.save(user1);

    //Creo Cuenta
    accountFeignRepository.create(new CreateAccountRequest(Integer.valueOf(userRet.getId().intValue())));

    //Grabo en tabla de verificacion
    UserVerified userVerified = new UserVerified(userRet.getId(),"",userVerificationService.createCodeVerification());
    userVerificationService.save(userVerified);

    //Envio com.dmh.email.service.email con el codigo
    Thread t = new Thread(){
      public void run(){
        emailFeignRepository.sendEmail(new EmailRequest(userRet.getEmail(),"Verificacion de cuenta","Codigo de verificacion: " + userVerified.getCode()));
      }
    };
    t.start();

    UserResponse userResponse = mapper.convertValue(userRet, UserResponse.class);

   return userResponse;
  }

  private String createAlias() throws Exception{
    //creo variables
    ArrayList<String> lv_palabras = new ArrayList<>();
    File lo_file;
    FileInputStream lo_stream;
    Boolean lb_aliasOK = false;
    String ls_alias_ret = "";

    //Primero armo lista con las palabras del diccionario
    try{
      lo_file = new File("users-service/src/main/resources/static/diccionarioPalabras.txt");
      lo_stream = new FileInputStream(lo_file);
      Scanner lo_Scanner = new Scanner(lo_stream);
      while(lo_Scanner.hasNextLine()){
        String line = lo_Scanner.nextLine();
        lv_palabras.add(line.trim());
      }
    }catch (Exception e){
      throw new Exception("Error al intentar recuperar palabras para alias");
    }

    //chequeo que la lista tenga items
    if (!(lv_palabras.size()>0)){
      throw new Exception("Error al intentar recuperar palabras para alias");
    }

    //Armo alias y verifico contra la base si existe
    while (!lb_aliasOK){

      for (int i = 0; i < 3; i++) {
        ls_alias_ret += lv_palabras.get(new Random().nextInt(lv_palabras.size()));
        ls_alias_ret += ".";
      }

      ls_alias_ret = ls_alias_ret.substring(0,ls_alias_ret.length()-1);

      //verifico si existe un usuario con el alias generado
      Optional<User> userDB = userRepository.findByAlias(ls_alias_ret);

      //Si no encontro un usuario con el alias seteo a true la condicion
      if(userDB.isEmpty()){
        lb_aliasOK = true;
      }
    }

    return ls_alias_ret;
  }

  private String createCVU(){
    String ls_cvu = "";
    Boolean lb_cvuOk = false;

    while(!lb_cvuOk){
      ls_cvu = "";

      //armo cvu
      for (int i = 0; i < 22 ; i++) {
        ls_cvu += String.valueOf((int)(Math.random()*10));
      }

      //verifico si no existe
      Optional<User> userDB = userRepository.findByCvu(ls_cvu);

      if(userDB.isEmpty()){
        lb_cvuOk = true;
      }
    }

    return ls_cvu;
  }

  public AccessKeycloak login(String email, String password) throws Exception{
    Optional<User> userDb = userRepository.findByEmail(email);

    //Valido contraseña y password
    if(userDb.isEmpty()){
      throw new BadRequestException("Usuario inexistente");
    }else if(!userDb.get().getPassword().equals(password)){
      throw new BadRequestException("Contraseña incorrecta");
    }

   //Valido si esta verificado
    if (!userVerificationService.userVerified(userDb.get().getId())){
      throw new BadRequestException("Email no verificado");
    };

    return keyCloakService.login(userDb.get().getName(),password);
  }

  public void logout(String userId) {
    keyCloakService.logout(userId);
  }

  public UserResponse update(String id,UserRequest userUpdate) throws Exception {
    //busco en la db el usuario a actualizar
    Optional<User> optionalUserDB = userRepository.findByKeycloakId(id);

    if(optionalUserDB.isEmpty()){
      throw new NotFoundException("No existe usuario con el id ingresado");
    }

    User userDB = optionalUserDB.get();

    //Valido campos
    clsUtiles.validarCampos(userUpdate);

    userDB.setName(userUpdate.getName());
    userDB.setLastname(userUpdate.getLastname());
    userDB.setPhoneNumber(userUpdate.getPhoneNumber());
    userDB.setPassword(userUpdate.getPassword());
    userDB.setEmail(userUpdate.getEmail());
    userDB.setDni(userUpdate.getDni());

    if(userUpdate.getAlias() != null) {
      if (userDB.getAlias() != userUpdate.getAlias()) {
        //Verifico si esta disponible
        Optional<User> userVerificationAlias = userRepository.findByAlias(userUpdate.getAlias());
        if (userVerificationAlias.isEmpty()) {
          userDB.setAlias(userUpdate.getAlias());
        } else {
          throw new BadRequestException("El alias ingresado no se encuentra disponible");
        }
      }
    }

    userRepository.save(userDB);
    keyCloakService.updateDataUser(optionalUserDB.get(),userDB);

    UserResponse userRet = new UserResponse();
    userRet.setName(userDB.getName());
    userRet.setLastname(userDB.getLastname());
    userRet.setDni(userDB.getDni());
    userRet.setPhoneNumber(userDB.getPhoneNumber());
    userRet.setCvu(userDB.getCvu());
    userRet.setAlias(userDB.getAlias());
    userRet.setEmail(userDB.getEmail());

    return userRet;
  }

  public String updatePassword(Long id, PasswordRequest passwordRequest) throws Exception {
    //busco en la db el usuario a actualizar
    Optional<User> optionalUserDB = userRepository.findById(id);

    if(optionalUserDB.isEmpty()){
      throw new NotFoundException("No existe usuario con el id ingresado");
    }

    clsUtiles.verificarPassword(passwordRequest);

    User userOld = optionalUserDB.get();

    User userSave = new User();
    userSave.setId(userOld.getId());
    userSave.setName(userOld.getName());
    userSave.setLastname(userOld.getLastname());
    userSave.setEmail(userOld.getEmail());
    userSave.setAlias(userOld.getAlias());
    userSave.setCvu(userOld.getCvu());
    userSave.setDni(userOld.getDni());
    userSave.setPassword(passwordRequest.getPassword());
    userSave.setPhoneNumber(userOld.getPhoneNumber());
    userSave.setKeycloakId(userOld.getKeycloakId());

    userRepository.save(userSave);
    keyCloakService.updateDataUser(userOld,userSave);

   return "Contraseña cambiada con exito";
  }

  public String updateAlias(String id, AliasRequest aliasRequest) throws Exception {

    //verifico formato alias
    clsUtiles.verificarAlias(aliasRequest.getAlias());

    //busco en la db el usuario a actualizar
    Optional<User> optionalUserDB = userRepository.findByKeycloakId(id);

    if(optionalUserDB.isEmpty()){
      throw new NotFoundException("No existe usuario con el id ingresado");
    }

    User userSave = optionalUserDB.get();

    //Verifico si esta disponible
    Optional<User> userVerificationAlias = userRepository.findByAlias(aliasRequest.getAlias());
      if(userVerificationAlias.isEmpty()){
        userSave.setAlias(aliasRequest.getAlias());
      }else{
        throw new BadRequestException("El alias ingresado no se encuentra disponible");
      }

    userRepository.save(userSave);

    return "Contraseña cambiada con exito";
  }

  public User getUserIDKeycloak(String id) throws Exception {
    Optional<User> userDB = userRepository.findByKeycloakId(id);

    if(userDB.isEmpty()){
      throw new Exception();
    }

    return userDB.get();
  }
}
