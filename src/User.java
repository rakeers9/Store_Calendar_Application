import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The User class has several methods that help with the login and reading .txt files. It has a validator
 * that ensures that User's username and password match and also if the User already exists in the database.
 * User is the parent class of Seller and Customer.
 *
 * @author Christopher Lee
 * @version November 12, 2023
 */

public class User {
    public User() {
    }

    public ArrayList<String> readFile(String fileName) {
        ArrayList<String> userInformation = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String user = bufferedReader.readLine();

            while (user != null) {
                userInformation.add(user);
                user = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            userInformation.add("error");
        }
        return userInformation;
    }

    public boolean validator(String fileName, String userName, String password) {
        ArrayList<String> userInformation = readFile(fileName);
        for (int i = 0; i < userInformation.size(); i++) {
            String[] user = userInformation.get(i).split(",");
            if (user[2].equals(userName) && user[3].equals(password)) {
                return true;
            }

        }
        return false;
    }

    public boolean usernameExists(String fileName, String username) {
        File f = new File(fileName);
        boolean checker = false;
        try (BufferedReader bfr = new BufferedReader(new FileReader(f))) {
            String line = bfr.readLine();
            while (line != null) {
                String[] splitter = line.split(",");
                if (username.equals(splitter[2])) {
                    checker = true;
                }
                line = bfr.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return checker;
    }

    public boolean checkUserType(String fileName, String username, String userType) {
        File f = new File(fileName);
        boolean checker = false;
        try (BufferedReader bfr = new BufferedReader(new FileReader(f))) {
            String line = bfr.readLine();
            while (line != null) {
                String[] splitter = line.split(",");
                if (username.equals(splitter[2])) {
                    if (splitter[0].equals(userType)) {
                        checker = true;
                    }
                }
                line = bfr.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return checker;
    }
}