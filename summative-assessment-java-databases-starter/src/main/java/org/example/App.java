package org.example;

import org.example.controller.MenuController;
import org.example.view.IO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class App implements CommandLineRunner
{
    IO io;
    MenuController menu;
    Environment environment;

    public App(IO io, MenuController menu, Environment environment) {
        this.io = io;
        this.menu = menu;
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Prevent from running during unit tests
        for (String env : environment.getActiveProfiles()) {
            if (env.equals("test")) {
                return;
            }
        }

        //Kick off the menu
        io.displayTitle("Bistro Management Application");
        menu.run();
    }
}
