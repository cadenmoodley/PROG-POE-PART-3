package com.mycompany.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Represents one message sent through the application.
 *
 * @author Caden Moodley - ST10537676 - code v3
 */
public class Message {

    // Maximum number of characters allowed in a message
    public static final int MAX_MESSAGE_LENGTH = 250;

    // Number of digits in a message ID
    public static final int MESSAGE_ID_LENGTH = 10;

    private final String messageId;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    // Creates a message and generates its ID and hash automatically
    public Message(int messageNumber, String recipient, String messageText) {
        this(createMessageId(), messageNumber, recipient, messageText);
    }

    // Creates a message with a supplied ID (used by the unit tests)
    public Message(String messageId, int messageNumber, String recipient, String messageText) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash(messageId, messageNumber, messageText);
    }

    // Builds a 10 digit ID from the last digits of the current time (substring)
    public static String createMessageId() {
        String time = String.valueOf(System.currentTimeMillis());
        return time.substring(time.length() - MESSAGE_ID_LENGTH);
    }

    // A message ID is valid when it is exactly 10 characters long
    public static boolean checkMessageId(String messageId) {
        return messageId != null && messageId.length() == MESSAGE_ID_LENGTH;
    }

    // Hash format = first two digits of ID : message number : FIRSTWORDLASTWORD
    public static String createMessageHash(String messageId, int messageNumber, String messageText) {
        // Split the message into words
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        // First two characters of the ID using substring
        String idStart = messageId.substring(0, 2);

        return (idStart + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // Returns the length check result shown to the user
    public static String checkMessageLength(String messageText) {
        if (messageText.length() <= MAX_MESSAGE_LENGTH) {
            return "Message ready to send.";
        }
        int extra = messageText.length() - MAX_MESSAGE_LENGTH;
        return "Message exceeds " + MAX_MESSAGE_LENGTH + " characters by " + extra
                + ", please reduce size.";
    }

    // Getters
    public String getMessageId() { return messageId; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }

    // Message details in the required display order
    public String printMessage() {
        return "Message ID: " + messageId + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText;
    }

    // Escapes characters that are not allowed raw inside a JSON string
    public static String escapeJson(String value) {
        StringBuilder sb = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }

    // Converts this message to a JSON object string
    public String toJson() {
        return "  {\n"
                + "    \"messageId\": \"" + escapeJson(messageId) + "\",\n"
                + "    \"messageNumber\": " + messageNumber + ",\n"
                + "    \"messageHash\": \"" + escapeJson(messageHash) + "\",\n"
                + "    \"recipient\": \"" + escapeJson(recipient) + "\",\n"
                + "    \"message\": \"" + escapeJson(messageText) + "\"\n"
                + "  }";
    }

    // Converts a list of messages to a JSON array string
    public static String toJsonArray(List<Message> messages) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < messages.size(); i++) {
            sb.append(messages.get(i).toJson());
            if (i < messages.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    // Writes all messages to a JSON file
    public static void saveToJsonFile(List<Message> messages, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(toJsonArray(messages));
        }
    }
}
