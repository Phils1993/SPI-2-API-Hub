package app;

import app.applicationConfig.ApplicationConfig;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");


        ApplicationConfig.startServer(7076);
    }
}
