package com.berdibekov;

import com.berdibekov.menu.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskTrackerApplication implements CommandLineRunner {

    @Autowired
    private MenuService menuService;

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        menuService.lunchUserInterface();
    }
}
