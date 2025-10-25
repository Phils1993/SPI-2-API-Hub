package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();



        //TODO: Remember to fill the DB, put in a user from secrurity.main
        //TODO: then change from create to update in hibernateConfig and run main
        ApplicationConfig.startServer(7070,emf);

        //DELETE THIS COMMENT LATER, IT IS NOT IMPORTANT AT ALL.
    }
}
