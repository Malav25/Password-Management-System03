import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PasswordManagerCLI {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Connection conn;

    public PasswordManagerCLI() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            if (conn != null) {
                System.out.println("Connection success");
            } else {
                System.out.println("Connection failed");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    private String[] users = new String[100]; // Assuming a maximum of 100 users
    private String[] passwords = new String[100]; // Corresponding passwords
    private int userCount = 0;


    public static void main(String[] args) 
    {
        PasswordManagerCLI passwordManager = new PasswordManagerCLI();
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("_________________________________________");
        System.out.println("|                                       |");
        System.out.println("| Welcome To Password Management System |");
        System.out.println("|_______________________________________|");
      
        System.out.println();
        System.out.println();

        

       

        while (true) {
            System.out.println("Options:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    passwordManager.register(scanner);
                    break;
                case 2:
                    boolean loggedIn = passwordManager.login(scanner);
                    if (loggedIn) {
                        passwordManager.managePasswords(scanner);
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private void register(Scanner scanner) {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Store the user and password in the arrays
        users[userCount] = email;
        passwords[userCount] = password;
        userCount++;

        System.out.println("Registration successful. You can now login.");
    }

    private boolean login(Scanner scanner) {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        for (int i = 0; i < userCount; i++) {
            if (email.equals(users[i]) && password.equals(passwords[i])) {
                System.out.println("Login successful.");
                return true;
            }
        }

        System.out.println("Login failed. Incorrect email or password.");
        return false;
    }
    private void managePasswords(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        while (true) {
            System.out.println("Options:");
            System.out.println("1. Add Password");
            System.out.println("2. Retrieve Passwords");
            System.out.println("3. Update Password");
            System.out.println("4. Delete Password");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Implement the code to add passwords
                    int userId = getUserIDByUsername(username); // Replace with your code to get user_id
                    addPassword(scanner, userId);
                    break;

                case 2:
                    if (retrievePasswords(scanner)) {
                        System.out.println("Password retrieval successful.");
                    } else {
                        System.out.println("Password retrieval failed.");
                    }
                    break;

                    case 3:
                    updatePassword(scanner);
                    break;

                    case 4:
                    deletePassword(scanner);
                    break;

                case 5:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }



    
    private static int getUserIDByUsername(String username) {
        return 0;
    }

    private void addPassword(Scanner scanner, int userId) {
        // ... existing code ...
         System.out.print("Enter website: ");
        String website = scanner.nextLine();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Encrypt the password before storing it
        String encryptedPassword = encryptPassword(password);
        
        try {
            String insertQuery = "INSERT INTO passwords (user_id, website, username, password_encrypted) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setInt(1, userId);  // Set the user_id
            preparedStatement.setString(2, website);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, encryptedPassword);
    
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Password added successfully.");
            } else {
                System.out.println("Password addition failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private boolean retrievePasswords(Scanner scanner) {
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Check if the entered password matches the user's password
        for (int i = 0; i < userCount; i++) {
            if (password.equals(passwords[i])) {
                // Implement code to retrieve and display passwords for the logged-in user
                 System.out.print("Enter username: ");
        String username = scanner.nextLine();
    
        try {
            String selectQuery = "SELECT website, username, password_encrypted FROM passwords WHERE username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            System.out.println("Stored Passwords:");
            while (resultSet.next()) {
                String website = resultSet.getString("website");
                String storedUsername = resultSet.getString("username");
                String encryptedPassword = resultSet.getString("password_encrypted");
    
                String originalPassword = decryptPassword(encryptedPassword);
    
                System.out.println("Website: " + website);
                System.out.println("Username: " + storedUsername);
                System.out.println("Password: " + originalPassword);
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
                return true;
            }
        }

        System.out.println("Password retrieval failed. Incorrect password.");
        return false;
    }

   
    private String decryptPassword(String encryptedPassword) {
        StringBuilder originalPassword = new StringBuilder();
    
        for (int i = 0; i < encryptedPassword.length(); i++) {
            char encryptedChar = encryptedPassword.charAt(i);
            char originalChar = reverseMapCharacter(encryptedChar);
            originalPassword.append(originalChar);
        }
    
        return originalPassword.toString();
    }
    
    // Reverse the mapping for decryption
    private char reverseMapCharacter(char encryptedChar) {
        switch (encryptedChar) {
            case '*':
                return 'a';
            case '$':
                return 'b';
            case '#':
                return 'c';
            // Add more mappings as needed
            default:
                return encryptedChar; // Return as is if not found in mapping
        }
    }
    private void updatePassword(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter the website whose password you want to update: ");
        String website = scanner.nextLine();

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        // Encrypt the new password before updating it
        String encryptedPassword = encryptPassword(newPassword);

        try {
            String updateQuery = "UPDATE passwords SET password_encrypted = ? WHERE username = ? AND website = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
            preparedStatement.setString(1, encryptedPassword);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, website);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("Password update failed. Ensure the website and username are correct.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePassword(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter the website whose password you want to delete: ");
        String website = scanner.nextLine();

        try {
            String deleteQuery = "DELETE FROM passwords WHERE username = ? AND website = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, website);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Password deleted successfully.");
            } else {
                System.out.println("Password deletion failed. Ensure the website and username are correct.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String encryptPassword(String password) {
        StringBuilder encryptedPassword = new StringBuilder();
        
        for (int i = 0; i < password.length(); i++) {
            char originalChar = password.charAt(i);
            char encryptedChar = mapCharacter(originalChar);
            encryptedPassword.append(encryptedChar);
        }
        
        return encryptedPassword.toString();
    }

    // Map each letter to a special character (customize this mapping as needed)
    private char mapCharacter(char originalChar) {
        switch (originalChar) {
            case 'a':
                return '*';
            case 'b':
                return '$';
            case 'c':
                return '#';
            // Add more mappings as needed
            default:
                return originalChar;
        }
    }


    private void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}