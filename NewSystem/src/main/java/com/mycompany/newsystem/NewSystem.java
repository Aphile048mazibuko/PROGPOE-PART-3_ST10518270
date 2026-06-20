/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.newsystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
/**
 *
 * @author mnqob
 */
public class NewSystem {

    private static boolean exit = false;
    private static int maxMessages = 10;
    private static int totalMessages = 0;
    private static int messageCounter = 0;
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<HashMap<String, String>> messagesList = new ArrayList<>();
    static Random rand = new Random();

    // Stored login details
    static String storedUsername = "";
    static String storedPassword = "";

    // -------Username Validation-------
    public static boolean checkUsername(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // -------------- Password Validation -------------------
    public static boolean checkPasswordComplexity(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.matches(regex, password);
    }

    // -------------- Cellphone Validation ------------------
    public static boolean checkCellPhoneNumber(String number) {
        String regex = "^\\+27\\d{9}$";
        return Pattern.matches(regex, number);
    }

    // -------------- Register User -------------------
    public static String registerUser(String username, String password) {
        if (!checkUsername(username)) {
            return "Username is incorrect. Must contain '_' and be max 5 characters.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is incorrect. Must have 8+ characters, a capital letter, a number, and a special character.";
        }
        return "Username and Password successfully captured. User registered!";
    }

    // -------------- Login User -------------------
    public static boolean loginUser(String username, String password,
            String storedUser, String storedPass) {
        return username.equals(storedUser) && password.equals(storedPass);
    }

    // -------------- Login Status Message -------------------
    public static String returnLoginStatus(boolean status) {
        if (status) {
            return "Login successful! Welcome back!";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    // ------- Recipient Validation -------
    private static String checkRecipient(String recipient) {
        if (recipient == null || !recipient.matches("^\\+\\d{9,12}$")) {
            System.out.println("Invalid number. Must start with + and have 9-12 digits. Example: +27123456789");
            return null;
        }
        return recipient;
    }

    // -------------- Helper: escape strings for JSON -------
    private static String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // -------------- Helper: convert HashMap to JSON string -------
    private static String toJsonString(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder("{");
        int i = 0;
        for (String key : map.keySet()) {
            sb.append("\"").append(escapeJson(key)).append("\":\"")
              .append(escapeJson(map.get(key))).append("\"");
            if (++i < map.size()) sb.append(",");
        }
        sb.append("}");
        return sb.toString();
    }

    // -------------- Send Message -------------------
    public static void sendMessage() {
        long messageID = 10000000000L + rand.nextInt(900000000);
        messageCounter++;

        System.out.print("Enter recipient number (+CCXXXXXXXXXX): ");
        String recipient = scanner.nextLine();
        recipient = checkRecipient(recipient);

        if (recipient == null) {
            return;
        }

        System.out.print("Enter your message (max 250 characters): ");
        String messageText = scanner.nextLine();

        if (messageText.length() > 250) {
            System.out.println("Message exceeds 250 characters. Please shorten your message.");
            return;
        }

        String[] words = messageText.trim().split("\\s+");
        String counterStr = Long.toString(messageCounter);
        String hash = String.format("%02d:%d:%s%s",
                Integer.parseInt(counterStr.substring(0, Math.min(2, counterStr.length()))),
                messageCounter,
                words[0].toUpperCase(),
                words.length > 1 ? words[words.length - 1].toUpperCase() : "");

        System.out.println("\nSelect action:");
        System.out.println("1. Send Message");
        System.out.println("2. Cancel Message");
        System.out.println("3. Archive");
        System.out.print("Choice: ");

        int action;
        try {
            action = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice. Message cancelled.");
            return;
        }

        if (action == 2) {
            System.out.println("Message cancelled.");
            return;
        }

        HashMap<String, String> message = new HashMap<>();
        message.put("MessageID", Long.toString(messageID));
        message.put("Recipient", recipient);
        message.put("Message", messageText);
        message.put("Hash", hash);
        message.put("Action", action == 1 ? "Send" : "Archive");

        if (action == 1) {
            messagesList.add(message);
            totalMessages++;
            System.out.println("\nMessage sent!");
            System.out.println("Message ID: " + messageID);
            System.out.println("Message Hash: " + hash);
            System.out.println("Recipient: " + recipient);
            System.out.println("Message: " + messageText);
        } else if (action == 3) {
            messagesList.add(message);
            System.out.println("Message archived.");
        } else {
            System.out.println("Invalid choice. Message cancelled.");
        }
    }

    // ------------ Show Messages -----------------
    private static void showRecentlySentMessages() {
        if (messagesList.isEmpty()) {
            System.out.println("No stored messages.");
        } else {
            System.out.println("\n--- Stored Messages ---");
            for (int i = 0; i < messagesList.size(); i++) {
                System.out.println(toJsonString(messagesList.get(i)));
            }
        }
    }

    // ------------ Discard Last Message -----------------
    private static void discardLastMessage() {
        if (messagesList.isEmpty()) {
            System.out.println("No messages to discard.");
        } else {
            HashMap<String, String> removed = messagesList.remove(messagesList.size() - 1);
            System.out.println("Discarded message: " + toJsonString(removed));
        }
    }

    // ------------ Save To JSON ----------------
    static void saveMessagesToJSON() {
        try (FileWriter file = new FileWriter("storedMessages.json")) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < messagesList.size(); i++) {
                sb.append(toJsonString(messagesList.get(i)));
                if (i < messagesList.size() - 1) sb.append(",");
            }
            sb.append("]");
            file.write(sb.toString());
            file.flush();
            System.out.println("Stored messages saved to storedMessages.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        // ------------ Quick Chat Menu -----------------
public static void displayQuickChatMenu(String username) {

    while (true) {

        System.out.println("\n========== QUICK CHAT MENU ==========");
        System.out.println("a. Display sender and recipient of all stored messages");
        System.out.println("b. Display the longest stored message");
        System.out.println("c. Search for a message by Message ID");
        System.out.println("d. Search messages by recipient number");
        System.out.println("e. Delete a message using message hash");
        System.out.println("f. Display full message report");
        System.out.println("0. Back to Main Menu");
        System.out.print("Select an option: ");

        String choice = scanner.nextLine().toLowerCase();

        switch (choice) {

            case "a":
                showRecentlySentMessages();
                break;

            case "b":
                System.out.println("Longest stored message feature not yet implemented.");
                break;

            case "c":
                System.out.print("Enter Message ID: ");
                String id = scanner.nextLine();
                System.out.println("Searching for Message ID: " + id);
                break;

            case "d":
                System.out.print("Enter Recipient Number: ");
                String recipient = scanner.nextLine();
                System.out.println("Searching messages for recipient: " + recipient);
                break;

            case "e":
                System.out.print("Enter Message Hash: ");
                String hash = scanner.nextLine();
                System.out.println("Deleting message with hash: " + hash);
                break;

            case "f":
                System.out.println("\n========== FULL MESSAGE REPORT ==========");
                showRecentlySentMessages();
                break;

            case "0":
                return;

            default:
                System.out.println("Invalid option.");
        }
    }
}
    
    // ------ Main Method -------
    public static void main(String[] args) {
        System.out.println("Welcome to ChatII!");
        Scanner input = new Scanner(System.in);

        // -------- Registration Section --------
        System.out.println("\n--- Register ---");

        String userName;
        while (true) {
            System.out.print("Enter Username (must contain '_' and be max 5 characters): ");
            userName = input.nextLine();
            if (checkUsername(userName)) break;
            System.out.println("Invalid username. Example: us_er");
        }

        String password;
        while (true) {
            System.out.print("Enter Password (8+ characters, 1 capital, 1 number, 1 special character): ");
            password = input.nextLine();
            if (checkPasswordComplexity(password)) break;
            System.out.println("Invalid password. Example: Password1!");
        }

        System.out.println(registerUser(userName, password));
        storedUsername = userName;
        storedPassword = password;

        while (true) {
            System.out.print("Enter Cell Phone (+27 followed by 9 digits): ");
            String cellPhone = input.nextLine();
            if (checkCellPhoneNumber(cellPhone)) {
                System.out.println("Cell phone number successfully added.");
                break;
            } else {
                System.out.println("Invalid number. Must start with +27 and have 9 digits. Example: +27123456789");
            }
        }

        // -------- Login Section --------
        System.out.println("\n--- Login ---");
        while (true) {
            System.out.print("Enter Username: ");
            String loginUser = input.nextLine();

            System.out.print("Enter Password: ");
            String loginPass = input.nextLine();

            boolean loginStatus = loginUser(loginUser, loginPass, storedUsername, storedPassword);
            System.out.println(returnLoginStatus(loginStatus));

            if (loginStatus) break;
        }

        // -------- Set Message Limit --------
        System.out.println("\nHow many messages would you like to be able to send?");
        try {
            maxMessages = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            maxMessages = 5;
        }

       // -------- Main Menu --------
while (true) {

    System.out.println("\n==============================");
    System.out.println("          MAIN MENU");
    System.out.println("==============================");
    System.out.println("1. Send Message");
    System.out.println("2. Menu");
    System.out.println("3. Exit");
    System.out.print("Enter your choice: ");

    String option = input.nextLine();

    switch (option) {

        case "1":
            if (totalMessages < maxMessages) {
                sendMessage();
            } else {
                System.out.println("Maximum message limit reached.");
            }
            break;

        case "2":
            displayQuickChatMenu(storedUsername);
            break;

        case "3":
            saveMessagesToJSON();
            System.out.println("Thank you for using QuickChat.");
            input.close();
            scanner.close();
            return;

        default:
            System.out.println("Invalid option. Please try again.");
    }
}
    }
static void addMessageToArrays(String id, String recipient,
                               String message, String status) {

    System.out.println("Added:");
    System.out.println("ID: " + id);
    System.out.println("Recipient: " + recipient);
    System.out.println("Message: " + message);
    System.out.println("Status: " + status);
}
}

