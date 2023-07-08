package com.digitalmedia.users.service;


import com.digitalmedia.users.config.KeycloakConfiguration;
import com.digitalmedia.users.model.AccessKeycloak;
import com.digitalmedia.users.model.User;
import lombok.SneakyThrows;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeyCloakService {

    @Value("${dh.keycloak.realm}")
    private String realm;

    @Value("${dh.keycloak.serverUrl}")
    private String serverUrl;

    @Value("${dh.keycloak.clientId}")
    private String clientId;

    @Value("${dh.keycloak.clientSecret}")
    private String clientSecret;

    @Value("${dh.keycloak.tokenEndpoint}")
    private String tokenEndpoint;

    @Autowired
    private KeycloakConfiguration keyCloakConfiguration;

    public RealmResource getRealm() {
        return keyCloakConfiguration.buildClient().realm(realm);
    }

    public User createUser(User user) throws Exception {

        UserRepresentation userRepresentation = new UserRepresentation();
        Map<String, List<String>> attributes = new HashMap<>();

        userRepresentation.setUsername(user.getName());
        userRepresentation.setFirstName(user.getName());
        userRepresentation.setLastName(user.getLastname());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEmailVerified(false);
        attributes.put("phoneNumber", Collections.singletonList(String.valueOf(user.getPhoneNumber())));
        attributes.put("dni", Collections.singletonList(String.valueOf(user.getDni())));
        attributes.put("cvu", Collections.singletonList(String.valueOf(user.getCvu())));
        attributes.put("alias", Collections.singletonList(String.valueOf(user.getAlias())));
        userRepresentation.setAttributes(attributes);
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(Collections.singletonList(newCredential(user.getPassword())));

        Response response = getRealm().users().create(userRepresentation);

        if (response.getStatus() == 409) {
            throw new Exception("El usuario o com.dmh.email.service.email ingresado ya existen");
        }
        if (response.getStatus() >= 400) {
            throw new BadRequestException("Error al intentar registrar usuario");
        }

        userRepresentation.setId(CreatedResponseUtil.getCreatedId(response));
        User userSaved = User.toUser(userRepresentation);

        return userSaved;
    }

    private CredentialRepresentation newCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(password);
        return credential;
    }

    private UserRepresentation updateUserRepresentation(UserRepresentation userRepresentation, User user) {
        if (user.getName() != null && userRepresentation.getUsername() != user.getName()) {
            userRepresentation.setUsername(user.getName());
        }
        if (user.getName() != null && user.getName() != userRepresentation.getFirstName()) {
            userRepresentation.setFirstName(user.getName());
        }
        if (user.getLastname() != null && userRepresentation.getLastName() != user.getLastname()) {
            userRepresentation.setLastName(user.getLastname());
        }
        if (user.getEmail() != null && userRepresentation.getEmail() != user.getEmail()) {
            userRepresentation.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            userRepresentation.setCredentials(Collections.singletonList(newCredential(user.getPassword())));
        }
        if (user.getPhoneNumber() != null && !userRepresentation.getAttributes().get("phoneNumber").equals(user.getPhoneNumber())){
            userRepresentation.getAttributes().put("phoneNumber", Collections.singletonList(user.getPhoneNumber()));
        }
        if (user.getDni() != null && !userRepresentation.getAttributes().get("dni").equals(user.getDni())) {
            userRepresentation.getAttributes().put("dni", Collections.singletonList(String.valueOf(user.getDni())));
        }
        return userRepresentation;
    }

    @SneakyThrows
    public User updateDataUser(User userSaved, User user) {
        UserResource userResource = getRealm().users().get(userSaved.getKeycloakId());

        UserRepresentation userRepresentationUpdated = updateUserRepresentation(userResource.toRepresentation(), user);
        User userUpdated = User.toUser(userRepresentationUpdated);

        getRealm().users().get(userSaved.getKeycloakId()).update(userRepresentationUpdated);
        return userUpdated;
    }

    public AccessKeycloak login(String email, String password) throws Exception {
        try{
            AccessKeycloak tokenAccess = null;

            Keycloak keycloakClient = null;
            TokenManager tokenManager = null;

            keycloakClient = Keycloak.getInstance(serverUrl, realm, email,
                    password, clientId, clientSecret);

            tokenManager = keycloakClient.tokenManager();

            tokenAccess = AccessKeycloak.builder()
                    .accessToken(tokenManager.getAccessTokenString())
                    .expiresIn(tokenManager.getAccessToken().getExpiresIn())
                    .refreshToken(tokenManager.refreshToken().getRefreshToken())
                    .scope(tokenManager.getAccessToken().getScope())
                    .build();

            return tokenAccess;

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void logout(String userId) {
        getRealm().users().get(userId).logout();
    }
}
