/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newsystem;

public class StoredMessages {

    public static void runTests() {

        System.out.println("Running StoredMessages tests...\n");

        NewSystem.addMessageToArrays("0000000001", "+27834557896",
                "Did you get the cake?", "Sent");

        NewSystem.addMessageToArrays("0000000002", "+27838884567",
                "Where are you? You are late! I have asked you to be on time.", "Stored");

        NewSystem.addMessageToArrays("0000000003", "+27834484567",
                "Yohoooo, I am at your gate.", "Disregard");

        NewSystem.addMessageToArrays("0000000004", "0838884567",
                "It is dinner time!", "Sent");

        NewSystem.addMessageToArrays("0000000005", "+27838884567",
                "Ok, I am leaving without you.", "Stored");

        // Username Test
        if (NewSystem.checkUsername("ab_cd")) {
            System.out.println("Username Test: PASS");
        } else {
            System.out.println("Username Test: FAIL");
        }

        // Password Test
        if (NewSystem.checkPasswordComplexity("Password1!")) {
            System.out.println("Password Test: PASS");
        } else {
            System.out.println("Password Test: FAIL");
        }

        // Cell Number Test
        if (NewSystem.checkCellPhoneNumber("+27834567890")) {
            System.out.println("Cell Number Test: PASS");
        } else {
            System.out.println("Cell Number Test: FAIL");
        }

        // Login Test
        if (NewSystem.loginUser("user_", "Password1!",
                "user_", "Password1!")) {
            System.out.println("Login Test: PASS");
        } else {
            System.out.println("Login Test: FAIL");
        }

        System.out.println("\nAll tests completed.");
    }

    public static void main(String[] args) {
        runTests();
    }
}