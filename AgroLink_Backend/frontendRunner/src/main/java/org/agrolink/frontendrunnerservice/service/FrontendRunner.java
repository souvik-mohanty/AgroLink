package org.agrolink.frontendrunnerservice.service;


import org.springframework.stereotype.Service;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FrontendRunner {

    private Process process;

    public void run(String path) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "npm run dev");
            processBuilder.directory(new File(path));
            processBuilder.redirectErrorStream(true);

            process = processBuilder.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (process != null && process.isAlive()) {
                    killProcessTree();
                }
            }));

            new Thread(() -> {
                try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
                    String input;
                    while ((input = consoleReader.readLine()) != null) {
                        if (input.equalsIgnoreCase("q")) {
                            System.out.println("Stopping frontend server...");
                            killProcessTree();
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            Pattern viteUrlPattern = Pattern.compile("Local:\\s+(http://[^\\s]+)");

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            System.out.println("Frontend is starting... (press 'q' + Enter to stop)\n");

            while ((line = reader.readLine()) != null) {
                System.out.println(line);

                Matcher matcher = viteUrlPattern.matcher(line);
                if (matcher.find()) {
                    String url = matcher.group(1);
                    openBrowser(url);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("\nFrontend process exited with code: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openBrowser(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
                System.out.println("Opened browser at: " + url);
            } else {
                System.out.println("Please open browser manually: " + url);
            }
        } catch (Exception e) {
            System.out.println("Failed to open browser: " + e.getMessage());
        }
    }

    private void killProcessTree() {
        try {
            long pid = process.pid();
            System.out.println("Killing process tree for PID: " + pid);
            Process kill = new ProcessBuilder("cmd.exe", "/c", "taskkill /PID " + pid + " /T /F")
                    .inheritIO()
                    .start();
            kill.waitFor();
            System.out.println("Frontend process tree killed.");
        } catch (Exception e) {
            System.out.println("Failed to kill process tree: " + e.getMessage());
        }
    }
}

