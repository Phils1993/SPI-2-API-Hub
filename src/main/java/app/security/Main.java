package app.security;

import app.config.HibernateConfig;
import app.entities.Role;
import app.entities.User;
import app.exceptions.EntityNotFoundException;
import app.exceptions.ValidationException;

public class Main {
    public static void main(String[] args) {
        // TODO: Make this more clean: maybe a loader?

        // FIXME Der skal laves en user først med create. Ændre til update

        ISecurityDAO dao = new SecurityDAO(HibernateConfig.getEntityManagerFactory());

        User user = dao.createUser("Gruppe18", "pass12345");
        System.out.println(user.getUsername()+": "+user.getPassword());
        Role role = dao.createRole("User");

        try {
            User updatedUser = dao.addUserRole("Gruppe18", "User");
            System.out.println(updatedUser);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        try {
            User validatedUser = dao.getVerifiedUser("Gruppe18", "pass12345");
            System.out.println("User was validated: "+validatedUser.getUsername());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
