/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.main;

/**
 *
 * @author Caden Moodley - ST10537676  - code v3
 */
// ---------------------- PART 1 : LOGGING ----------------------

// import Scanner so we can get a user input
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// Create the main class
public class Main {

    // Create Scanner for user input
    static Scanner input = new Scanner(System.in);

    static String username;
    static String password;
    static String cellphone;

    // Stores the registered username
    static String registeredUsername;

    // Stores the registered Password
    static String registeredPassword;

    // Stores the registered Cellphone number
    static String registeredCellPhone;

    // Name of the file the messages are saved to
    static final String JSON_FILE = "messages.json";

    // Stores every message sent while the application runs
    static ArrayList<Message> sentMessages = new ArrayList<>();

    public static boolean checkUserName(String username){

        // username must have 5 characters
        // and must contain an underscore
        if(username.length() == 5 && username.contains("_")){
            return true;
        } else {
            return false;
        }
    }

    // Create the password checking method
    public static boolean checkPasswordComplexity(String password){

        // Password must be 8 characters long, contain a capital letter,
        // a small letter, a number and a special character
        if(password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^a-zA-Z0-9].*")) {
            return true;
        } else {
            return false;
        }
    }

    // Create the cellphone checking method
    public static boolean checkCellPhoneNumber(String cellphone){

        // Phone Number must have a country code (+27)
        if(cellphone.matches("\\+27[0-9]{9}$")){
            return true;
        } else {
            return false;
        }
    }

    // Create the registration method
    public static void registerUser(){

        // infinite loop for username
        while (true){
            System.out.print("Enter your Username: ");
            username = input.nextLine();
            // check username
            boolean usernameCorrect = checkUserName(username);
            if(usernameCorrect){
                break;
            } else {
                System.out.println("Username is incorrectly formatted. Please try again");
            }
        }

        // infinite loop for password
        while (true){
            System.out.print("Enter your Password: ");
            password = input.nextLine();
            // check password
            boolean passwordCorrect = checkPasswordComplexity(password);
            if(passwordCorrect){
                break;
            } else {
                System.out.println("Password is incorrectly formatted. Please try again");
            }
        }

        // infinite loop for cell phone number
        while (true){
            System.out.print("Enter your Cell Phone Number (+27): ");
            cellphone = input.nextLine();
            // check cell phone number
            boolean cellPhoneCorrect = checkCellPhoneNumber(cellphone);
            if(cellPhoneCorrect){
                break;
            } else {
                System.out.println("Cell Phone number is incorrectly formatted. Please try again");
            }
        }

        // save the valid username
        registeredUsername = username;
        // save the valid password
        registeredPassword = password;
        // save the valid cellphone number
        registeredCellPhone = cellphone;
        // display success message
        System.out.println("User registered successfully");
    }

    // Creates the log in method
    public static boolean loginUser(String username, String password){

        while (true){
            // Compare entered details with registered details
            if(username.equals(registeredUsername)
                    && password.equals(registeredPassword)) {
                return true;
            } else {
                System.out.println("Username or password incorrect, please try again");
                // Re-prompt the user for username
                System.out.print("Enter your username: ");
                username = input.nextLine();
                // Re-prompt the user for password
                System.out.print("Enter your password: ");
                password = input.nextLine();
            }
        }
    }

    // Creates the login status method
    public static String returnLoginStatus(boolean loginSuccessful){

        // Check if login was successful
        if(loginSuccessful){
            return "Welcome, it is great to see you again " + registeredUsername;
        } else {
            return "Username or password incorrect, please try again";
        }
    }

    // ---------------------- PART 2 : MESSAGING ----------------------

    // Displays the welcome message and the numeric menu
    public static void showMenu(){
        System.out.println("");
        System.out.println("Welcome to QuickChat.");
        System.out.println("Please choose an option (1-3):");
        System.out.println("1) Send Message");
        System.out.println("2) Show recently sent messages");
        System.out.println("3) Quit");
        System.out.print("Option: ");
    }

    // Reads a whole number from the user and keeps asking until it is valid
    public static int readNumber(){
        while (true){
            String text = input.nextLine().trim();
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    // Asks how many messages to send, then reads each one in a for loop
    public static void sendMessages(){

        System.out.print("How many messages do you want to enter? ");
        int totalMessages = readNumber();
        while (totalMessages < 1){
            System.out.print("Please enter 1 or more: ");
            totalMessages = readNumber();
        }

        // Loop counter doubles as the message number
        for (int messageNumber = 1; messageNumber <= totalMessages; messageNumber++){

            System.out.println("");
            System.out.println("--- Message " + messageNumber + " of " + totalMessages + " ---");

            // Recipient must be a valid +27 cell phone number
            String recipient;
            while (true){
                System.out.print("Enter recipient cell phone number (+27): ");
                recipient = input.nextLine();
                if(checkCellPhoneNumber(recipient)){
                    break;
                }
                System.out.println("Recipient number is incorrectly formatted. Please try again");
            }

            // Message text must be 250 characters or less and not empty
            String text;
            while (true){
                System.out.print("Enter your message (max 250 characters): ");
                text = input.nextLine();
                if(text.trim().isEmpty()){
                    System.out.println("Message cannot be empty.");
                    continue;
                }
                String lengthResult = Message.checkMessageLength(text);
                System.out.println(lengthResult);
                if(text.length() <= Message.MAX_MESSAGE_LENGTH){
                    break;
                }
            }

            // Create the message (ID and hash are built with string manipulation)
            Message message = new Message(messageNumber, recipient, text);
            sentMessages.add(message);

            // Display the details of the message
            System.out.println("");
            System.out.println(message.printMessage());
        }

        // ---------------------- PART 3 : STORING ----------------------
        // Store all the messages in the JSON file
        try {
            Message.saveToJsonFile(sentMessages, JSON_FILE);
            System.out.println("");
            System.out.println("Messages saved to " + JSON_FILE);
        } catch (IOException e) {
            System.out.println("Could not save messages: " + e.getMessage());
        }
    }

    // Displays all messages sent so far
    public static void showSentMessages(){
        if(sentMessages.isEmpty()){
            System.out.println("No messages have been sent yet.");
            return;
        }
        for (Message message : sentMessages){
            System.out.println("");
            System.out.println(message.printMessage());
        }
    }

    // Application loop - keeps running until the user chooses 3 (Quit)
    public static void runApplication(){

        boolean running = true;
        while (running){
            showMenu();
            int option = readNumber();

            switch (option){
                case 1:
                    sendMessages();
                    break;
                case 2:
                    showSentMessages();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2 or 3.");
            }
        }
    }

    // Main method where the program starts
    public static void main(String[] args) {

        // call registration method
        registerUser();

        System.out.println("");

        // ask user to login
        System.out.print("Enter your username to login: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter your password to login: ");
        String loginPassword = input.nextLine();

        System.out.println("");

        // call login method
        boolean loginSuccessful = loginUser(loginUsername, loginPassword);
        // call login status method
        System.out.println(returnLoginStatus(loginSuccessful));

        // start the messaging application
        runApplication();
    }
}
