package com.digitalmedia.users.service;

import com.digitalmedia.users.exceptions.BadRequestException;
import com.digitalmedia.users.model.User;
import com.digitalmedia.users.model.UserVerified;
import com.digitalmedia.users.repository.IUserRepository;
import com.digitalmedia.users.repository.IUserVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

@Service
public class UserVerificationService {

    @Autowired
    IUserVerificationRepository userVerificationRepository;

    @Autowired
    IUserRepository userRepository;

    public Optional<UserVerified> findByCode(String code){
        return userVerificationRepository.findByCode(code);
    }
    public String  findByEmail(String mail) throws BadRequestException {
        Optional<User> userDB =  userRepository.findByEmail(mail);

        if (userDB.isEmpty()){
            throw new NotFoundException("No existe usuario con el mail ingresado");
        }

        Optional<UserVerified> userVerified =  userVerificationRepository.findById(userDB.get().getId());

        if (userVerified.isEmpty()){
            throw new BadRequestException("No se pudo recuperar el codigo de validacion");
        }

        return userVerified.get().getCode();
    }

    public String createCodeVerification(){
        String ls_code = "";
        Boolean lb_codeOk = false;

        while(!lb_codeOk){
            ls_code = "";

            //armo cvu
            for (int i = 0; i < 6 ; i++) {
                ls_code += String.valueOf((int)(Math.random()*10));
            }

            //verifico si no existe
            Optional<UserVerified> userDB = userVerificationRepository.findByCode(ls_code);

            if(userDB.isEmpty()){
                lb_codeOk = true;
            }
        }

        return ls_code;
    }

    public Boolean userVerified(Long id){
       Optional<UserVerified> userVerified = userVerificationRepository.findById(id);

       if(userVerified.isEmpty()){
           return false;
       }

       if(userVerified.get().getFecha() == null || userVerified.get().getFecha().isEmpty()){
           return false;
       }

       return true;
    }

    public void save(UserVerified userVerified){
        userVerificationRepository.save(userVerified);
    }
}
