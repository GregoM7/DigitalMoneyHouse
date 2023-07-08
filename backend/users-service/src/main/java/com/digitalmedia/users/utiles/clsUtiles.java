package com.digitalmedia.users.utiles;

import com.digitalmedia.users.exceptions.BadRequestException;
import com.digitalmedia.users.model.dto.PasswordRequest;
import com.digitalmedia.users.model.dto.UserRequest;
import com.google.common.primitives.Chars;

import java.util.ArrayList;

public abstract class clsUtiles {

    public static void validarCampos(UserRequest user)throws Exception{

        //Valido los campos
        if(user.getName() == null || user.getName().isEmpty()){
            throw new BadRequestException("Campo Name vacio");
        }
        if(user.getLastname() == null || user.getLastname().isEmpty()){
            throw new BadRequestException("Campo LastName vacio");
        }
        if(user.getEmail() == null || user.getEmail().isEmpty()){
            throw new BadRequestException("Campo Email vacio");
        }
        if(user.getPassword() == null || user.getPassword().isEmpty()){
            throw new BadRequestException("Campo Password vacio");
        }
        if(user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()){
            throw new BadRequestException("Campo PhoneNumber vacio");
        }
        if(user.getDni() == null || user.getDni().equals(0)){
            throw new BadRequestException("Campo DNI vacio");
        }
        if(user.getDni() == null || user.getDni().equals(0)){
            throw new BadRequestException("Campo DNI vacio");
        }
    }

    public static void verificarPassword(PasswordRequest passwordRequest)throws Exception{

         if(passwordRequest.getPassword() == null || passwordRequest.getPasswordRepet() == null){
            throw new BadRequestException("Campo vacios");
        }
        if( !passwordRequest.getPassword().equals(passwordRequest.getPasswordRepet())){
            throw new BadRequestException("Las contraseñas no coinciden");
        }
    }

    public static void verificarAlias(String alias) throws BadRequestException {
        //alimento bandera y saco espacios
        String ls_alias = alias.toString().trim();
        String[] lv_palabras = ls_alias.split("\\.");

        if(ls_alias.startsWith(".") || ls_alias.endsWith(".")){
            throw new BadRequestException("El formato de alias ingresado no es valido");
        }

        if(lv_palabras.length != 3){
            throw new BadRequestException("El formato de alias ingresado no es valido");
            }else{
            for (String palabra:lv_palabras) {
                if(palabra.length()==0){
                    throw new BadRequestException("El formato de alias ingresado no es valido");
                }
            }
        }
    }
}
