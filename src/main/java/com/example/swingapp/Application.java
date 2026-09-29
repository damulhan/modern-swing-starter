package com.example.swingapp;

import com.example.swingapp.view.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class Application {
    public static void main(String[] args) {
        // Set system properties for modern font rendering & HiDPI
        System.setProperty("sun.java2d.uiScale.enabled", "true");

        // 1. Setup FlatLaf Look and Feel before any GUI component is created
        try {
            FlatLightLaf.setup();
            // Global UI properties tweaking
            UIManager.put("Button.arc", 6);
            UIManager.put("Component.arc", 6);
            UIManager.put("ProgressBar.arc", 6);
            UIManager.put("TextComponent.arc", 6);
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf Look and Feel: " + e.getMessage());
        }

        // 2. Launch MainFrame on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
