package com.project.service;

import com.project.database.DatabaseManager;
import com.project.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Base64;

public class MessageService {

    // Generate random code
    private String generateCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        StringBuilder code = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return code.toString();
    }

    // Create and store message
    public String createMessage(String text) {
        String code = generateCode();

        try {
            Connection conn = DatabaseManager.getConnection();
            String sql = "INSERT INTO messages (code, text, viewed) VALUES (?, ?, false)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, code);
           String encoded = Base64.getEncoder().encodeToString(text.getBytes());
            stmt.setString(2, encoded);

            stmt.executeUpdate();

            return code;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve message (one-time)
    public String getMessage(String code) {
        try {
            Connection conn = DatabaseManager.getConnection();

            String sql = "SELECT * FROM messages WHERE code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, code);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                boolean viewed = rs.getBoolean("viewed");

                if (viewed) {
                    return "Message already viewed!";
                }

                String encodedText = rs.getString("text");
                String messageText = new String(Base64.getDecoder().decode(encodedText));
                // Mark as viewed
                String deleteSql = "DELETE FROM messages WHERE code = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                deleteStmt.setString(1, code);
                deleteStmt.executeUpdate();

                return messageText;
            } else {
                return "Message expired or already viewed!";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving message!";
        }
    }
}