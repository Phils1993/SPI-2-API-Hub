package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        ApplicationConfig.startServer(7076,emf);

        //DELETE THIS COMMENT LATER, IT IS NOT IMPORTANT AT ALL.
    }
}
