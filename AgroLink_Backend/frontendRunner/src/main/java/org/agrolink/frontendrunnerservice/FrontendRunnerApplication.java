package org.agrolink.frontendrunnerservice;

import org.agrolink.frontendrunnerservice.service.FrontendRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class FrontendRunnerApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(FrontendRunnerApplication.class, args);

        FrontendRunner frontendRunner = context.getBean(FrontendRunner.class);

        // Resolve frontend path relative to backend location
        Path currentDir = Paths.get("").toAbsolutePath();
        Path frontendPath = currentDir.getParent().resolve("AgroLink_FrontEnd").resolve("AgroLink");
        if (!frontendPath.toFile().exists()) {
            System.err.println("Frontend path does not exist: " + frontendPath);
            return;
        }

        System.out.println("Resolved frontend path: " + frontendPath);

        new Thread(() -> frontendRunner.run(frontendPath.toString())).start();
    }
}