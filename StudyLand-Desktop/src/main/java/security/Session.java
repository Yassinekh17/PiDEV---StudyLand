package security;

import entities.User;
import services.ServiceUser;

public class Session {
    private static Session instance;
     public UserInfo userInfo;

    private ServiceUser serviceUser;

    private Session() {
        serviceUser = new ServiceUser();
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void login(String email, String password) {
        try {
            User user = serviceUser.connexion(email, password);
            if (user != null) {
                userInfo = new UserInfo(user.getId(), user.getNom(), user.getPrenom(), user.getRole(), user.getEmail(), user.getPassword(),user.getImage());
            } else {
                userInfo = null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void logout() {
        userInfo = null;
    }
//Rester en ligne
public boolean isLoggedIn() {
    return userInfo != null;
}
}
