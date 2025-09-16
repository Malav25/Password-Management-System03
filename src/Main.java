import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

class PasswordManagerConsole {
    Scanner sc = new Scanner(System.in);

    //Check Whether the user had entered the correct passwprd or not
    void checkPassword(String password) {
        
 boolean flag1 = true;
        String checkP = "";
        while(flag1){
            System.out.print("Enter your Email ID's Password = ");
            checkP=sc.nextLine();
            if(!checkP.equals(password)){ 
                System.out.println("Wrong Password ");
                System.out.println("Try Again");
                System.out.println();
                flag1=true;
            }
            else{
                flag1=false;
            }
        }
    }

    //Generates a random otp
    String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) { // You can adjust the OTP length as needed
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }


    //Writes or passes the otp to file
    void writeOTPToFile(String otp) throws IOException {
        FileWriter writer = new FileWriter("otp.txt");
        writer.write(otp);
        writer.close();
    }

    //reads the otp sent from the otp file
    String readOTPFromFile() throws IOException {
        Scanner fileScanner = new Scanner(new File("otp.txt"));
        String savedOTP = fileScanner.nextLine();
        fileScanner.close();
        return savedOTP;
    }

   
// resests the email id's Password
    String resetEmailPassword(Statement st) throws SQLException, IOException {
        System.out.print("Enter your registered Email ID: ");
        String EmailID = sc.nextLine();

        // Generate OTP
        String otp = generateOTP();

        // Write OTP to a text file
        writeOTPToFile(otp);

        System.out.println("An OTP has been sent to your registered email address.");
        System.out.print("Enter the OTP to reset your email ID password: ");
        String userOTP = sc.nextLine();

        // Read OTP from the file and verify
        String savedOTP = readOTPFromFile();
        String newPassword="";

        if (userOTP.equals(savedOTP)) {
            System.out.print("Enter your new email ID password: ");
             newPassword=sc.nextLine();
           
            System.out.println("Email ID password reset successfully.");
            
        } else {
            System.out.println("OTP verification failed. Please try again.");
        }
        return newPassword;
    }

    // Encryption Method

 String getRandomChar(String pass) {
    String ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*?";
    String result = "";
    for (int i = 0; i < pass.length(); i++) {
        int r = (int) (Math.random() * ch.length());
        result += ch.charAt(r);
    }
    return result;
   }

   // Checks the strength of password
String checkPasswordStrength(String Password_Data, String Username) {
    while (true) {
        if (Password_Data.length() < 8) {
            System.out.println("Your Password Must Have At Least 8 Characters ");
        } else if (Password_Data.equals(Username)) {
            System.out.println("Your Password Must Not Be the Same As the User Name You Entered ");
        } else {
            boolean hasUpperCase = false;
            boolean hasLowerCase = false;
            boolean hasDigit = false;

            for (int i = 0; i < Password_Data.length(); i++) {
                char c = Password_Data.charAt(i);
                if (Character.isUpperCase(c)) {
                    hasUpperCase = true;
                } else if (Character.isLowerCase(c)) {
                    hasLowerCase = true;
                } else if (Character.isDigit(c)) {
                    hasDigit = true;
                }
            }

            if (!hasUpperCase) {
                System.out.println("Your Password Must Have At Least 1 Capital Letter");
            } else if (!hasLowerCase) {
                System.out.println("Your Password Must Have At Least 1 Small Letter");
            } else if (!hasDigit) {
                System.out.println("Your Password Must Have At Least one Integral Number");
            } else {
                // All conditions are met, so we can exit the loop
                return Password_Data;
            }
        }

        // If any condition fails, we'll continue the loop
        System.out.println("Try Again");
        System.out.print("Enter a New Password: ");
        Password_Data = sc.nextLine();
    }
}
    //Adds the website name and it's password to the database

     void get(Statement st,HashMap<String,String> hm,String username)throws SQLException{
        System.out.print("Enter Website Name = ");
        String websiteName = sc.nextLine();
        System.out.println();
        System.out.print("Enter Website Password = ");
        String pass = sc.nextLine();
        pass = checkPasswordStrength(pass, username);
        String encrypted = getRandomChar(pass);
        String sql = "INSERT INTO password(website_name,password) VALUES(?,?);";
        PreparedStatement pst = st.getConnection().prepareStatement(sql);
        pst.setString(1, websiteName);
        pst.setString(2,encrypted );
        hm.put(encrypted, pass);
        int r = pst.executeUpdate();
        if(r>0){
            System.out.println();
            System.out.println("Data added to database succesfully");
            System.out.println();
        }else{
            System.out.println();
            System.out.println("Failed to add the data to database");
            System.out.println();
        }
    }

    //To change the website's name

void changeWebsiteName(Statement st) throws SQLException {
    ResultSet rs = st.executeQuery("SELECT website_name FROM password;");
    int i = 0;
    int choice = 0;
    String existingWeb = "";
    String newWeb = "";
    boolean flag = true;
    
    while (rs.next()) {
        i++;
        System.out.println(i + ". " + rs.getString(1));
    }
    
    while (flag) {
        System.out.print("Enter the number above matching to website name you want to change = ");
        choice = sc.nextInt();
        
        if (choice < 1 || choice > i) {
            System.out.println("Please Enter a Number from 1 to " + i);
            System.out.println();
        } else {
            flag = false;
        }
    }
    
    String sql = "SELECT website_name FROM password WHERE Sr_no = ?;";
    PreparedStatement pst = st.getConnection().prepareStatement(sql);
    pst.setInt(1, choice);
    ResultSet rst = pst.executeQuery();
    
    if (rst.next()) {
        existingWeb = rst.getString(1);
        System.out.print("Enter New Website Name = ");
        sc.nextLine(); // Consume the newline character left by nextInt()
        newWeb = sc.nextLine();
        
        String sql1 = "UPDATE password SET website_name = ? WHERE website_name = ?";
        PreparedStatement pst1 = st.getConnection().prepareStatement(sql1);
        pst1.setString(1, newWeb);
        pst1.setString(2, existingWeb);
        
        int r = pst1.executeUpdate();
        
        if (r > 0) {
            System.out.println("Data updated in database");
        } else {
            System.out.println("Failed to update database");
        }
    } else {
        System.out.println("Invalid selection. No matching record found.");
    }
}

  //To change website's Password

void changePassword(Statement st, HashMap<String, String> hm, String username) throws SQLException {
    ResultSet rs = st.executeQuery("SELECT website_name FROM password;");
    int i = 0;
    int choice = 0;
    String existingPass = "";
    String newPass = "";
    boolean flag = true;

    while (rs.next()) {
        i++;
        System.out.println(i + ". " + rs.getString(1));
    }

    while (flag) {
        System.out.print("Enter the number above matching to the website name you want to change = ");
        choice = sc.nextInt();

        if (choice < 1 || choice > i) {
            System.out.println("Please Enter a Number from 1 to " + i);
            System.out.println();
        } else {
            flag = false;
        }
    }

    String sql = "SELECT password FROM password WHERE Sr_no = ?;";
    PreparedStatement pst = st.getConnection().prepareStatement(sql);
    pst.setInt(1, choice);
    ResultSet rst = pst.executeQuery();

    if (rst.next()) {
        String hashPass = rst.getString(1);
        existingPass = hm.get(hashPass);
        System.out.print("Enter New Password = ");
        sc.nextLine(); // Consume the newline character left by nextInt()
        newPass = sc.nextLine();
        newPass = checkPasswordStrength(newPass, username);
        String newHashPass = getRandomChar(newPass);
        String sql1 = "UPDATE password SET password = ? WHERE password = ?";
        PreparedStatement pst1 = st.getConnection().prepareStatement(sql1);
        pst1.setString(1, newHashPass);
        pst1.setString(2, hashPass);

        int r = pst1.executeUpdate();

        if (r > 0) {
            System.out.println("Password Updated successfully");
            hm.remove(hashPass); // Remove the old password entry
            hm.put(newHashPass, newPass); // Update the storePass HashMap
        } else {
            System.out.println("Failed to update");
        }
    } else {
        System.out.println("Invalid selection. No matching record found.");
    }
}
  //Deletes the saved Password

void delete(Statement st, HashMap<String, String> hm) throws SQLException {
    ResultSet rs = st.executeQuery("SELECT website_name FROM password;");
    int choice = 0;
    int i = 0;
    boolean flag = true;

    while (rs.next()) {
        i++;
        System.out.println(i + ". " + rs.getString(1));
    }

    while (flag) {
        System.out.print("Enter the number corresponding to the website name you want to delete = ");
        choice = sc.nextInt();

        if (choice < 1 || choice > i) {
            System.out.println("Please Enter a Number between 1 and " + i);
        } else {
            flag = false;
        }
    }

    String sql = "SELECT website_name FROM password WHERE Sr_no = ?";
    PreparedStatement pst = st.getConnection().prepareStatement(sql);
    pst.setInt(1, choice);
    ResultSet rst = pst.executeQuery();

    if (rst.next()) {
        String webName = rst.getString(1);
        String sql1 = "DELETE FROM password WHERE website_name = ?";
        PreparedStatement pst1 = st.getConnection().prepareStatement(sql1);
        pst1.setString(1, webName);
        int r = pst1.executeUpdate();

if (r > 0) {
    String deletedPassword = hm.remove(webName); // Remove the password from storePass
    System.out.println("Website's data deleted successfully from the database");
    System.out.println("Deleted Password: " + deletedPassword);
} else {
    System.out.println("Failed to delete website's data");
}

    } else {
        System.out.println("Invalid selection. No matching record found.");
    }
}

 /*No need to display all passwords you can individually search for a 
  Particular password*/

void search(Statement st, HashMap<String, String> hm) throws SQLException {
    String sql = "SELECT website_name FROM password;";
    ResultSet rs = st.executeQuery(sql);
    int i = 0;

    while (rs.next()) {
        i++;
        System.out.println(i + ". " + rs.getString(1));
    }

    System.out.print("Enter the number corresponding to the website you want to search = ");
    int choice = sc.nextInt();

    if (choice >= 1 && choice <= i) {
        String sql1 = "SELECT * FROM password WHERE Sr_no = ?";
        PreparedStatement pst = st.getConnection().prepareStatement(sql1);
        pst.setInt(1, choice);
        ResultSet rst = pst.executeQuery();

        if (rst.next()) {
            System.out.println("-----------------------------");
            System.out.println("Website Name: " + rst.getString(2));
            System.out.println();
            System.out.println("Password: " + hm.get(rst.getString(3)));
            System.out.println("-----------------------------");
        } else {
            System.out.println("No matching website found.");
        }
    } else {
        System.out.println("Please enter a valid number between 1 and " + i);
    }
}

// For displaying the saved Passwords

void display(Statement st, HashMap<String, String> hm) throws SQLException {
    ResultSet rs = st.executeQuery("SELECT * FROM password;");

    try{

        if(rs.getString(2)==null&&hm.get(rs.getString(3))==null)
        {
          System.out.println("No Passwords To Show");
        }
    }
    catch(SQLException e)
    {
         System.out.println("No Passwords To show");
         System.out.println();
    }


    while (rs.next()) {
        System.out.println("-----------------------------");
        System.out.println("Website Name: " + rs.getString(2));
        System.out.println();
        System.out.println("Password: " + hm.get(rs.getString(3)));
        System.out.println("-----------------------------");
    }

}

// For Changing both website's name and password

void changeBoth(Statement st, HashMap<String, String> hm, String username) throws SQLException {
    ResultSet rs = st.executeQuery("SELECT website_name FROM password;");
    int i = 0;
    int choice = 0;
    boolean flag = true;

    while (flag) {
        while (rs.next()) {
            i++;
            System.out.println(i + ". " + rs.getString(1));
        }

        System.out.print("Enter the number which you want to change = ");
        choice = sc.nextInt();

        if (choice < 1 || choice > i) {
            System.out.println("Please Enter a Number between 1 and " + i);
        } else {
            flag = false;
        }
    }

    String sql = "SELECT website_name FROM password WHERE Sr_no = ?;";
    PreparedStatement pst = st.getConnection().prepareStatement(sql);
    pst.setInt(1, choice);
    ResultSet rst = pst.executeQuery();

    if (rst.next()) {
        String existingWeb = rst.getString(1);
        System.out.print("Enter New Website Name = ");
        sc.nextLine(); // Consume the newline character left by nextInt()
        String newWeb = sc.nextLine();

        String sql1 = "UPDATE password SET website_name = ? WHERE website_name = ?";
        PreparedStatement pst1 = st.getConnection().prepareStatement(sql1);
        pst1.setString(1, newWeb);
        pst1.setString(2, existingWeb);
        int r1 = pst1.executeUpdate();

        if (r1 > 0) {
            System.out.println("Website name updated in database");

            System.out.print("Enter Old Password = ");
            String oldPass = sc.nextLine();
            boolean flag1 = true;

            while (flag1) {
                if (checkExistingPass(st, hm, oldPass, choice)) {
                    System.out.print("Enter New Password = ");
                    String newPass = sc.nextLine();
                    newPass = checkPasswordStrength(newPass, username);

                    String sql2 = "UPDATE password SET password = ? WHERE website_name = ?";
                    PreparedStatement pst2 = st.getConnection().prepareStatement(sql2);
                    pst2.setString(1, newPass);
                    pst2.setString(2, newWeb);
                    int r2 = pst2.executeUpdate();

                    if (r2 > 0) {
                        System.out.println("Password Updated successfully");
                        flag1 = false;
                        hm.remove(oldPass); // Remove the old password entry from the HashMap
                        hm.put(newPass, newWeb); // Add the new password entry to the HashMap
                    } else {
                        System.out.println("Failed to update password");
                    }
                } else {
                    System.out.println("Your Entered Password doesn't match with the existing password in the database");
                    System.out.print("Please Enter your password again = ");
                    oldPass = sc.nextLine();
                    flag1 = true;
                }
            }
        } else {
            System.out.println("Invalid selection. No matching record found.");
        }
    } else {
        System.out.println("Invalid selection. No matching record found.");
    }
}

// Checks whether a particular Password Exists Or Not
 
boolean checkExistingPass(Statement st,HashMap<String,String> hm,String oldPass,int choice)throws SQLException{
    String sql = "SELECT password FROM password WHERE Sr_no = ?;";
    PreparedStatement pst = st.getConnection().prepareStatement(sql);
    pst.setInt(1, choice);
    ResultSet rs = pst.executeQuery();
    if(hm.get(rs.getString(3))!=oldPass){
        return false;
    }
    return true;
}
}

// Main Class

public class Main {
    static{
        System.out.println("--------------------------------------------------------------------");
        System.out.println("               Your Personalized Password Manager                   ");
        System.out.println("--------------------------------------------------------------------");
    }

    // Main Method

    public static void main(String[] args)throws ClassNotFoundException,SQLException,InterruptedException, IOException {
        Scanner sc = new Scanner(System.in);
        String driverName = "com.mysql.cj.jdbc.Driver";
        String dburl = "jdbc:mysql://localhost:3306/lju";
        String dbuser = "root";
        String dpass = "";
        Class.forName(driverName);
        Connection con = DriverManager.getConnection(dburl, dbuser, dpass);
        if(con==null){
            System.out.println("Connection Failed");
        }
        Statement st = con.createStatement();
        boolean flag = true;
        String EmailID="";
        HashMap<Integer,String> validEmailID = new HashMap<>();
        validEmailID.put(1, "gmail.com");
        validEmailID.put(2, "yahoo.com");
        validEmailID.put(3, "rediffmail.com");
        validEmailID.put(4, "outlook.com");
        validEmailID.put(5, "protonmail.com");
        flag=true;
        
        while(flag){
            System.out.print("Enter your Email ID = ");
            EmailID = sc.nextLine();
            String check[] = EmailID.split("@");
            int count=0;
            for(int i=0;i<EmailID.length();i++){
                    if((EmailID.charAt(i))=='@'){
                        count++;
                    }
            }
            if(count==0){
                System.out.println("Please Enter a valid Email ID");
                flag=true;
            }
            else{
            if(!validEmailID.containsValue(check[1])){
                System.out.println();
                System.out.println();
                System.out.println("Error");
                System.out.println();
                System.out.println("Please enter a valid Email ID ");
                flag=true;
            }
            else{
                flag=false;
            }
        } 
        }
        System.out.print("Enter your Email ID's Password = ");
        String emailIDPassword = sc.nextLine();
        System.out.print("Enter your User name = ");
        String username = sc.nextLine();
        String permission = "yes";
        String userInput = "yes";
        String sql="";
        sql="TRUNCATE TABLE password;";
        st.executeUpdate(sql);
        HashMap<String,String> storePass = new HashMap<>();
        PasswordManagerConsole pmc = new PasswordManagerConsole();
        while((userInput.toLowerCase()).equals(permission)){
            System.out.println("1. Add Passwords ");
            System.out.println("2. Edit The Existing Passwords ");
            System.out.println("3. Delete The Existing Passwords ");
            System.out.println("4. To Search For The Specific Website's Password");
            System.out.println("5. Display the Existing Passwords");
            System.out.println("6. Exit");
System.out.println("7. For Reset Email's Password");
            System.out.print("Enter The Number Displayed Below Corresponding to Your Desired Operation = ");            
            switch(sc.nextInt()){
                case 1:
                pmc.checkPassword(emailIDPassword);
                pmc.get(st,storePass,username);
                break;
                case 2:
                sc.nextLine();
                pmc.checkPassword(emailIDPassword);
                boolean flag1 = true;
                while(flag1){
                    System.out.println("Select your following desired option");
                    System.out.println("1. Change only Website name");
                    System.out.println("2. Change only Specific Website's password");
                    System.out.println("3. Change Website and it's Password both");
                    System.out.print("Which you would like to go with ? =  ");
                    switch(sc.nextInt()){
                        case 1:
                        pmc.checkPassword(emailIDPassword);
                        pmc.changeWebsiteName(st);
                        flag1=false;
                        break;
                        case 2:
                        pmc.checkPassword(emailIDPassword);
                        pmc.changePassword(st, storePass, username);
                        flag1=false;
                        break;
                        case 3:
                        pmc.checkPassword(emailIDPassword);
                        pmc.changeWebsiteName(st);
                        pmc.changePassword(st, storePass, username);
                        System.out.println();
                        System.out.println("Website name and password both updated in database");
                        flag1=false;
                        break;
                        default:
                        System.out.println("Invalid Input");
                        System.out.println("Enter number between 1 and 3");
                        flag1=true;
                        break;
                    }
                }
                break;
                case 3:
                pmc.checkPassword(emailIDPassword);
                pmc.delete(st, storePass);
                break;
                case 4:
                pmc.checkPassword(emailIDPassword);
                pmc.search(st, storePass);
                break;
                case 5:
                pmc.checkPassword(emailIDPassword);;
                pmc.display(st, storePass);
                break;
                case 6:
                System.out.print("Exiting");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);                                
                sc.close();
                st.close();
                con.close();
                System.exit(0);
                break;
                    case 7:
                   
                   emailIDPassword= pmc.resetEmailPassword(st);
                    break;
                default :
                System.out.println("Invalid Input");
                System.out.println("Please Enter number between 1 and 6");
                break;
            }
        }
    }
}
