import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The Client class handles all user interaction with the program. The Client class handles the creation of the GUI,
 * sending information to the Server class to be processed, and receiving the necessary information from the Server
 * class to be displayed on the GUI.
 *
 * @author Audrey Lupton, Sruthi Lingam, Christopher Lee, and Sreekar Gudipati
 * @version December 9, 2023
 */

public class Client extends JComponent implements Runnable {
    private static final int port = 8008;
    private String createAccount;
    private String userType;    //"Seller" or "Customer"
    private String name;        //Name of the user
    private String email;       //Email of the user
    private String password;    //Password of the user
    private int viewCancelOption = 0;
    private int viewSortOption = 0;

    public boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String sendDataToServer(JButton button, String data) {
        try {
            Socket socket = new Socket("localhost", port);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            String clientMessage = "";
            String serverResponse = "";

            String output = ""; //What the method is returning
            if (button.getText().equals("Enter")) { //Creating an account
                createAccount = "true";
                pw.write("true");
                pw.println();

                String[] info = data.split(",");
                //Sends whether seller or customer
                pw.write(info[0]);
                pw.println();

                //Sends name of user
                pw.write(info[1]);
                pw.println();
                pw.flush();

                //Sends email
                pw.write(info[2]);
                pw.println();
                pw.flush();

                //Sends password
                pw.write(info[3]);
                pw.println();
                pw.flush();

                serverResponse = bfr.readLine();
                if (serverResponse.equals("true")) {
                    output = serverResponse;
                } else { //response equals "false"
                    //GUI displays error message
                }
            } else if (button.getText().equals("Login")) { //Button is "Login"
                createAccount = "false";
                pw.write("false");
                pw.println();

                String[] info = data.split(",");
                //Sends whether seller or customer
                userType = info[0];
                pw.write(info[0]);
                pw.println();

                //Sends name of user
                name = info[1];
                pw.write(info[1]);
                pw.println();
                pw.flush();

                //Sends email
                email = info[2];
                pw.write(info[2]);
                pw.println();
                pw.flush();

                //Sends password
                password = info[3];
                pw.write(info[3]);
                pw.println();
                pw.flush();

                serverResponse = bfr.readLine();
                if (serverResponse.equals("true")) {
                    output = serverResponse;
                } else { //response equals "false"
                    //GUI displays error message
                }
            } else if (button.getText().equals("Proceed")) { //When the user selects an option in their option menu
                //Sending to server that user has already logged in and their userType
                pw.println("false");
                pw.flush();
                pw.println(userType);
                pw.flush();

                if (userType.equals("Seller")) {
                    //Sending server name, email, and password
                    pw.println(name);
                    pw.flush();
                    pw.println(email);
                    pw.flush();
                    pw.println(password);
                    pw.flush();
                    bfr.readLine();

                    //Where options begin
                    String[] info = data.split(";");
                    //Sending storeName
                    pw.println(info[0]);
                    pw.flush();

                    //Sending choice number
                    pw.println(info[1]);
                    pw.flush();

                    switch (info[1]) {
                        case "calendars": {
                            pw.println("calendars");
                            pw.flush();
                            return bfr.readLine();
                        }

                        case ("requests"): {
                            pw.println("requests");
                            pw.flush();
                            return bfr.readLine();
                        }

                        case "1":
                            output = bfr.readLine();
                            break;
                        case "2": //Create new calendar
                            //Sending whether user chose "file" or "manual" option
                            pw.println(info[2]);
                            pw.flush();

                            if (info[2].equals("1")) { //File option
                                pw.println(info[3]);
                                pw.flush();

                                return bfr.readLine();
                            } else { //Manual option
                                pw.println(info[3]); //Num appointments
                                pw.flush();

                                pw.println(info[4]); //Calendar name
                                pw.flush();

                                pw.println(info[5]); //Calendar description
                                pw.flush();

                                //Sending appointment info
                                int numAppt = Integer.parseInt(info[3]);
                                int counter = 6;
                                for (int i = 0; i < numAppt; i++) {
                                    pw.println(info[counter]);
                                    pw.flush();
                                    pw.println(info[counter + 1]);
                                    pw.flush();
                                    pw.println(info[counter + 2]);
                                    pw.flush();
                                    pw.println(info[counter + 3]);
                                    pw.flush();
                                    pw.println(info[counter + 4]);
                                    pw.flush();
                                    counter += 5;
                                }
                            }
                            break;
                        case "3":
                            //  bfr.readLine();
                            pw.println(info[2]); //Calendar name
                            pw.flush();
                            pw.println(info[3]); //Old appt title
                            pw.flush();
                            pw.println(info[4]); //New appt title
                            pw.flush();
                            pw.println(info[5]); //New max attendees
                            pw.flush();
                            pw.println(info[6]); //New approved bookings
                            pw.flush();
                            pw.println(info[7]); //New start time
                            pw.flush();
                            pw.println(info[8]); //New end time
                            pw.flush();
                            bfr.readLine();
                            output = bfr.readLine();
                            break;
                        case "4":
                            pw.println(info[2]); //Name of calendar to be deleted
                            pw.flush();
                            output = bfr.readLine();
                            break;
                        case "5":
                            pw.println(info[2]); //Requested appointment
                            pw.flush();
                            pw.println(info[3]); //Seller action: approve/decline
                            pw.flush();
                            pw.println(info[4]); //Customer username
                            pw.flush();
                            output = bfr.readLine();
                            System.out.println(output);
                            break;
                        case "6":
                            if (viewSortOption == 1) {
                                pw.println("yes");
                                pw.flush();
                            } else {
                                pw.println("no");
                                pw.flush();
                            }
                            break;
                        case "7":
                            pw.println("8");
                            pw.flush();
                            break;
                    }
                } else { //User is a customer
                    //Sending server name, email, and password
                    pw.println(name);
                    pw.flush();
                    pw.println(email);
                    pw.flush();
                    pw.println(password);
                    pw.flush();
                    bfr.readLine();

                    //Where options begin
                    String[] temp = data.split(";");
                    switch (temp[0]) {
                        case ("calendars"): {
                            pw.println("calendars");
                            pw.flush();
                            return bfr.readLine();
                        }

                        case ("cancel"): {
                            pw.println("cancel");
                            pw.flush();
                            pw.println(temp[1]);
                            pw.flush();
                            return bfr.readLine();
                        }
                        case ("1"): {
                            pw.println("1");
                            pw.flush();
                            pw.println(temp[1]);
                            pw.flush();
                            return bfr.readLine();
                        }
                        case ("2"): {
                            pw.println("2");
                            pw.flush();
                            pw.println(temp[2]);
                            pw.flush();
                            pw.println(temp[1]);
                            pw.flush();
                            return bfr.readLine();
                        }
                        case ("4"): {
                            pw.println("4");
                            pw.flush();
                            return bfr.readLine();
                        }
                        case ("5"): {
                            pw.println("5");
                            pw.flush();
                            return bfr.readLine();
                        }
                        case ("6"): {
                            pw.println("6");
                            pw.flush();
                            pw.println(temp[1]);
                            pw.flush();
                            return bfr.readLine();
                        }
                        case ("7"): {
                            pw.println("7");
                            pw.flush();
                            return bfr.readLine();
                        }
                        case ("8"): {
                            pw.println("8");
                            pw.flush();
                            return bfr.readLine();
                        }
                    }
                }
            }
            pw.close();
            bfr.close();
            return output;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Client());
    }

    public void run() {
        //Create JFrame
        JFrame frame = new JFrame("Hotel Manager");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        //Create components
        JLabel welcomeMessageLabel = new JLabel("Welcome to the Hotel Manager!");
        JLabel selectOptionLabel = new JLabel("Please select an option.");
        JButton createAccountButton = new JButton("Create Account");
        JButton loginButton = new JButton("Login");

        //Login info components - used for both create account and login
        JComboBox<String> sellerOrCustomer = new JComboBox();
        sellerOrCustomer.addItem("Seller");
        sellerOrCustomer.addItem("Customer");
        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameText = new JTextField("", 20);
        JLabel loginInfoLabel = new JLabel("Please enter your email and password.");
        JLabel emailLabel = new JLabel("Email: ");
        JTextField emailText = new JTextField("", 20);
        JLabel createPassLabel = new JLabel("Password: ");
        JLabel loginPassLabel = new JLabel("Password: ");
        JTextField createPassText = new JTextField("", 20);
        JPasswordField loginPassText = new JPasswordField();
        JButton createEnterButton = new JButton("Enter");
        JButton loginEnterButton = new JButton("Login");
        JButton createBackButton = new JButton("Go Back");
        JButton loginBackButton = new JButton("Go Back");

        //Customer options components
        JLabel customerOptionsLabel = new JLabel("Select an option from the customer menu below");
        JComboBox<String> customerOptions = new JComboBox<>();
        JButton customerProceedButton = new JButton("Proceed");
        JLabel exitMessage = new JLabel("Thank you for using the Hotel Manager. You have successfully " +
                "logged out.");
        JLabel closingMessage = new JLabel("This message will close in 5 seconds.");
        JLabel customerAppointmentRequest = new JLabel("<html>Enter the appointment you would like to request " +
                "exactly as " +
                "it appears in the appointment list. <br/> <br/> Format: [Calendar name]-[Appointment Title]," +
                "[Max Attendees],[Approved Bookings],[Start Time],[End Time]</html>", SwingConstants.CENTER);
        JLabel getAppointments = new JLabel();
        JTextField customerAppointmentText = new JTextField("", 45);
        JButton customerAppointmentButton = new JButton("Proceed");

        JLabel customerCancelAppointmentRequest = new JLabel("<html>Enter the appointment you would like to " +
                "cancel " + "exactly as it appears in the appointment list. <br/> <br/> Format: [Calendar name]-" +
                "[Appointment Title]," + "[Max Attendees],[Approved Bookings],[Start Time],[End Time]-" +
                "[Username]</html>");
        JTextField customerCancelText = new JTextField("", 45);
        JButton customerCancelButton = new JButton("Proceed");
        JLabel customerCancelLabel = new JLabel();
        JLabel customerViewCancelLabel = new JLabel("Choose an appointment category to delete from:");
        JButton customerViewCancelButton = new JButton("Proceed");
        JComboBox<String> customerCancelOptions = new JComboBox<>();

        JLabel customerViewCalendarsLabel = new JLabel();
        JLabel customerViewApprovedAppointmentsLabel = new JLabel();
        JLabel customerViewPendingAppointmentsLabel = new JLabel();

        JLabel customerSort = new JLabel("Choose a method to sort statistics dashboard");
        JComboBox<String> customerSortOptions = new JComboBox<>();
        JButton customerSortButton = new JButton("Proceed");
        JLabel customerViewStatsLabel = new JLabel();

        JButton customerAppointmentBackButton = new JButton("Go Back");
        JButton customerCancelBackButton = new JButton("Go Back");
        JButton customerViewCalendarsBackButton = new JButton("Go Back");
        JButton customerViewApprovedAppointmentsBackButton = new JButton("Go Back");
        JButton customerViewAppointmentsAwaitingBackButton = new JButton("Go Back");
        JButton customerExportFileBackButton = new JButton("Go Back");
        JButton customerViewStatisticsBackButton = new JButton("Go Back");
        JButton customerViewStatsBackButton = new JButton("Go Back");
        JButton customerExitButton = new JButton("OK");

        //Creates main panel
        welcomeMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel initialOptionPanel = new JPanel();
        initialOptionPanel.setLayout(new BoxLayout(initialOptionPanel, BoxLayout.PAGE_AXIS));
        initialOptionPanel.add(Box.createRigidArea(new Dimension(1, 20)));
        initialOptionPanel.add(welcomeMessageLabel);
        initialOptionPanel.add(Box.createRigidArea(new Dimension(1, 20)));
        initialOptionPanel.add(createAccountButton);
        initialOptionPanel.add(Box.createRigidArea(new Dimension(1, 5)));
        initialOptionPanel.add(loginButton);

        //Adds initial option panel to frame
        content.add(initialOptionPanel, BorderLayout.CENTER);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        //Creates create account panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoPanel.add(Box.createRigidArea(new Dimension(1, 10)));
        selectOptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(selectOptionLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(1, 10)));
        sellerOrCustomer.setMaximumSize(new Dimension(100, 25));
        sellerOrCustomer.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(sellerOrCustomer);
        infoPanel.add(Box.createRigidArea(new Dimension(1, 20)));

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.LINE_AXIS));
        namePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        namePanel.add(nameLabel);
        namePanel.add(Box.createRigidArea(new Dimension(50, 0)));
        nameText.setMaximumSize(new Dimension(200, 25));
        namePanel.add(nameText);

        JPanel loginInfoPanel = new JPanel();
        loginInfoPanel.add(loginInfoLabel);

        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.LINE_AXIS));
        emailPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        emailPanel.add(emailLabel);
        emailPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        emailText.setMaximumSize(new Dimension(300, 25));
        emailPanel.add(emailText);

        JPanel createPassPanel = new JPanel();
        createPassPanel.setLayout(new BoxLayout(createPassPanel, BoxLayout.LINE_AXIS));
        createPassPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        createPassPanel.add(createPassLabel);
        createPassPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        createPassText.setMaximumSize(new Dimension(200, 25));
        createPassPanel.add(createPassText);

        JPanel loginPassPanel = new JPanel();
        loginPassPanel.setLayout(new BoxLayout(loginPassPanel, BoxLayout.LINE_AXIS));
        loginPassPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        loginPassPanel.add(loginPassLabel);
        loginPassPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        loginPassText.setMaximumSize(new Dimension(200, 25));
        loginPassPanel.add(loginPassText);
        JPanel createEnterPanel = new JPanel();
        createEnterPanel.setLayout(new BoxLayout(createEnterPanel, BoxLayout.LINE_AXIS));
        createEnterPanel.add(Box.createRigidArea(new Dimension(200, 0)));
        createEnterPanel.add(createBackButton);
        createEnterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        createEnterPanel.add(createEnterButton);
        createEnterPanel.add(Box.createRigidArea(new Dimension(1, 10)));

        JPanel loginEnterPanel = new JPanel();
        loginEnterPanel.setLayout(new BoxLayout(loginEnterPanel, BoxLayout.LINE_AXIS));
        loginEnterPanel.add(Box.createRigidArea(new Dimension(200, 0)));
        loginEnterPanel.add(loginBackButton);
        loginEnterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        loginEnterPanel.add(loginEnterButton);
        loginEnterPanel.add(Box.createRigidArea(new Dimension(1, 10)));

        //Customer panel
        JPanel customerPanel = new JPanel();
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.PAGE_AXIS));
        customerPanel.add(Box.createRigidArea(new Dimension(200, 10)));
        customerOptionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerPanel.add(customerOptionsLabel);
        customerPanel.add(Box.createRigidArea(new Dimension(1, 10)));
        customerOptions.setMaximumSize(new Dimension(400, 25));
        customerOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerOptions.addItem("Make an appointment request");
        customerOptions.addItem("Cancel an appointment request");
        customerOptions.addItem("View store calendars");
        customerOptions.addItem("View currently approved appointments");
        customerOptions.addItem("View pending appointments");
        customerOptions.addItem("View store statistics");
        customerOptions.addItem("Export a file of your approved appointments");
        customerOptions.addItem("Exit and log out");
        customerPanel.add(customerOptions);
        customerPanel.add(Box.createRigidArea(new Dimension(1, 20)));
        customerProceedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerPanel.add(customerProceedButton);
        customerPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        // Customer make appointment request panel.
        JPanel customerMakeAppointment = new JPanel();
        customerMakeAppointment.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.insets = new Insets(5, 5, 5, 5);
        customerMakeAppointment.add(getAppointments, constraints);
        customerMakeAppointment.add(customerAppointmentRequest, constraints);
        customerMakeAppointment.add(customerAppointmentText, constraints);
        customerMakeAppointment.add(customerAppointmentButton, constraints);
        customerMakeAppointment.add(customerAppointmentBackButton, constraints);
        JScrollPane customerMakeAppointmentScroll = new JScrollPane(customerMakeAppointment);
        customerMakeAppointmentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Customer view cancel appointments panel.
        JPanel customerViewCancelAppointmentsPanel = new JPanel();
        customerViewCancelAppointmentsPanel.setLayout(new BoxLayout(customerViewCancelAppointmentsPanel,
                BoxLayout.PAGE_AXIS));
        customerViewCancelAppointmentsPanel.add(Box.createRigidArea(new Dimension(200, 10)));
        customerViewCancelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerViewCancelAppointmentsPanel.add(customerViewCancelLabel);
        customerViewCancelAppointmentsPanel.add(Box.createRigidArea(new Dimension(1, 10)));
        customerCancelOptions.setMaximumSize(new Dimension(400, 25));
        customerCancelOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerCancelOptions.addItem("Appointments Awaiting Approval");
        customerCancelOptions.addItem("Appointments Approved");
        customerViewCancelAppointmentsPanel.add(customerCancelOptions);
        customerViewCancelAppointmentsPanel.add(Box.createRigidArea(new Dimension(1, 20)));
        customerViewCancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerViewCancelAppointmentsPanel.add(customerViewCancelButton);
        customerViewCancelAppointmentsPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        // Customer cancel appointment panel.
        JPanel customerCancelAppointment = new JPanel();
        customerCancelAppointment.add(customerCancelAppointmentRequest);
        customerCancelAppointment.add(customerCancelLabel, SwingConstants.CENTER);
        customerCancelAppointment.add(customerCancelText);
        customerCancelAppointment.add(customerCancelButton);
        customerCancelAppointment.add(customerCancelBackButton);

        // Customer view calendars panel.
        JPanel customerViewCalendars = new JPanel();
        customerViewCalendars.add(customerViewCalendarsLabel);
        customerViewCalendars.add(customerViewCalendarsBackButton);

        // Customer view approved appointments panel.
        JPanel customerViewApprovedAppointments = new JPanel();
        customerViewApprovedAppointments.add(customerViewApprovedAppointmentsLabel);
        customerViewApprovedAppointments.add(customerViewApprovedAppointmentsBackButton);

        // Customer view appointments awaiting approval panel.
        JPanel customerViewAppointmentsAwaiting = new JPanel();
        customerViewAppointmentsAwaiting.add(customerViewPendingAppointmentsLabel);
        customerViewAppointmentsAwaiting.add(customerViewAppointmentsAwaitingBackButton);

        // Customer view statistics panel.
        JPanel customerViewStatistics = new JPanel();
        customerViewStatistics.setLayout(new BoxLayout(customerViewStatistics, BoxLayout.PAGE_AXIS));
        customerViewStatistics.add(Box.createRigidArea(new Dimension(200, 10)));
        customerSort.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerViewStatistics.add(customerSort);
        customerViewStatistics.add(Box.createRigidArea(new Dimension(1, 10)));
        customerSortOptions.setMaximumSize(new Dimension(400, 25));
        customerSortOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerSortOptions.addItem("Most to Least Popular Stores");
        customerSortOptions.addItem("Least to Most Popular Stores");
        customerViewStatistics.add(customerSortOptions);
        customerPanel.add(Box.createRigidArea(new Dimension(1, 20)));
        customerSortButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerViewStatistics.add(customerSortButton);
        customerPanel.add(Box.createRigidArea(new Dimension(1, 20)));
        customerViewStatisticsBackButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerViewStatistics.add(customerViewStatisticsBackButton);

        // Customer view appointment stats sorted.
        JPanel customerViewStats = new JPanel();
        customerViewStats.add(customerViewStatsLabel);
        customerViewStats.add(customerViewStatsBackButton);

        //Exit panel
        JPanel exitLogOutPanel = new JPanel();
        exitLogOutPanel.setLayout(new BoxLayout(exitLogOutPanel, BoxLayout.PAGE_AXIS));
        exitLogOutPanel.add(Box.createRigidArea(new Dimension(200, 60)));
        exitMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitLogOutPanel.add(exitMessage);
        exitLogOutPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        closingMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitLogOutPanel.add(customerExitButton);
        // exitLogOutPanel.add(closingMessage);
        exitLogOutPanel.add(Box.createRigidArea(new Dimension(200, 10)));

        //Seller panels here

        //Options panel
        JLabel sellerOptionsLabel = new JLabel("Select an option from the seller menu below");
        JComboBox<String> sellerOptions = new JComboBox<>();
        JButton sellerProceedButton = new JButton("Proceed");
        JPanel sellerPanel = new JPanel();
        sellerPanel.setLayout(new BoxLayout(sellerPanel, BoxLayout.PAGE_AXIS));
        sellerPanel.add(Box.createRigidArea(new Dimension(200, 10)));
        JPanel storeNamePanel = new JPanel();
        JLabel storeNameLabel = new JLabel("Please enter the name of your store you would like to manage: ");
        JTextField storeNameText = new JTextField("", 20);
        storeNamePanel.add(storeNameLabel);
        storeNamePanel.add(storeNameText);
        sellerPanel.add(storeNamePanel);
        sellerPanel.add(Box.createRigidArea(new Dimension(1, 10)));
        sellerOptionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellerPanel.add(sellerOptionsLabel);
        sellerPanel.add(Box.createRigidArea(new Dimension(1, 10)));
        sellerOptions.setMaximumSize(new Dimension(400, 25));
        sellerOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellerOptions.addItem("[Choose an option]");
        sellerOptions.addItem("View current calendars");
        sellerOptions.addItem("Create new calendar");
        sellerOptions.addItem("Edit calendar");
        sellerOptions.addItem("Delete calendar");
        sellerOptions.addItem("Approve/decline appointment requests");
        sellerOptions.addItem("View statistics");
        sellerOptions.addItem("Exit and log out");
        sellerPanel.add(sellerOptions);
        sellerPanel.add(Box.createRigidArea(new Dimension(1, 20)));
        sellerProceedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel sellerProceedAndLogoutPanel = new JPanel();
        sellerProceedAndLogoutPanel.add(sellerProceedButton);
        sellerProceedAndLogoutPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        sellerPanel.add(sellerProceedAndLogoutPanel);

        //View current calendars panel
        JLabel viewCalendarsLabel = new JLabel("");
        JButton viewBackButton = new JButton("Go Back");
        JPanel viewCalendarsPanel = new JPanel();
        viewCalendarsPanel.setLayout(new BoxLayout(viewCalendarsPanel, BoxLayout.PAGE_AXIS));
        viewCalendarsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        viewCalendarsLabel.setAlignmentX(CENTER_ALIGNMENT);
        viewCalendarsPanel.add(viewCalendarsLabel);
        viewBackButton.setMaximumSize(new Dimension(100, 25));
        viewCalendarsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        viewBackButton.setAlignmentX(CENTER_ALIGNMENT);
        viewCalendarsPanel.add(viewBackButton);
        JScrollPane jsp = new JScrollPane(viewCalendarsPanel);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //Create new calendar panel
        JButton importFileButton = new JButton("Import File");
        JButton manualButton = new JButton("Manually Create Calendar");
        JButton createCalendarBackButton = new JButton("Go Back");
        JPanel createCalendarPanel = new JPanel();
        createCalendarPanel.add(importFileButton);
        createCalendarPanel.add(manualButton);
        createCalendarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JPanel calendarBackButtonPanel = new JPanel();
        calendarBackButtonPanel.add(createCalendarBackButton);
        createCalendarPanel.add(calendarBackButtonPanel);

        //Import file panel
        JLabel importFileFormatLabel = new JLabel("Please ensure your file is in this format: ",
                SwingConstants.CENTER);
        JLabel exampleFormatLabel1 = new JLabel("[Store Name],[Calendar Name],[Calendar description]," +
                "[Appointment Title],[Max Attendees]," + "[Approved Bookings],[Start Time],[End Time]");
        JLabel exampleFormatLabel2 = new JLabel("Multiple appointments can be on the same line separated by " +
                "commas.");
        JLabel exampleFormatLabel3 = new JLabel("Max Attendees can only be an integer value 0 or 1.");
        JLabel importFileLabel = new JLabel("Please enter the filename: ");
        JTextField importFileText = new JTextField("", 20);
        JButton importButton = new JButton("Import");
        JButton importBackButton = new JButton("Go Back");
        JPanel importFilePanel = new JPanel();
        importFilePanel.setLayout(new BoxLayout(importFilePanel, BoxLayout.PAGE_AXIS));
        importFilePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        importFileFormatLabel.setAlignmentX(CENTER_ALIGNMENT);
        importFilePanel.add(importFileFormatLabel);
        importFilePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        exampleFormatLabel1.setAlignmentX(CENTER_ALIGNMENT);
        exampleFormatLabel2.setAlignmentX(CENTER_ALIGNMENT);
        exampleFormatLabel3.setAlignmentX(CENTER_ALIGNMENT);
        importFilePanel.add(exampleFormatLabel1);
        importFilePanel.add(exampleFormatLabel2);
        importFilePanel.add(exampleFormatLabel3);
        importFilePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JPanel importDataPanel = new JPanel();
        importDataPanel.add(importFileLabel);
        importDataPanel.add(importFileText);
        importDataPanel.add(importButton);
        importFilePanel.add(importDataPanel);
        importFilePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        importBackButton.setAlignmentX(CENTER_ALIGNMENT);
        importBackButton.setMaximumSize(new Dimension(100, 25));
        importFilePanel.add(importBackButton);
        importFilePanel.add(Box.createRigidArea(new Dimension(0, 30)));

        //Manually create calendar panel
        JLabel calendarNameLabel = new JLabel("   Calendar name: ");
        JTextField calendarNameText = new JTextField("", 20);
        JLabel calendarDescriptionLabel = new JLabel("Calendar description: ");
        JTextField calendarDescriptionText = new JTextField("", 20);
        JButton addAppointmentButton = new JButton("Add Appointment");
        JLabel appointmentTitleLabel = new JLabel("   Appointment Title");
        JTextField appointmentTitleText = new JTextField("", 20);
        JLabel maxAttendeesLabel = new JLabel("Maximum amount of attendees: ");
        JTextField maxAttendeesText = new JTextField("", 20);
        JLabel approvedBookingsLabel = new JLabel("Number of approved bookings (value can only be a 0 or 1): ");
        JTextField approvedBookingsText = new JTextField("", 20);
        JLabel startTimeLabel = new JLabel("Start time of the appointment (in the form hh:mm): ");
        JTextField startTimeText = new JTextField("", 20);
        JLabel endTimeLabel = new JLabel("End time of the appointment (in the form hh:mm): ");
        JTextField endTimeText = new JTextField("", 20);
        JButton createAppointmentButton = new JButton("Create Appointment");
        JLabel appointmentListLabel = new JLabel("Appointments: None");
        JLabel appointmentListLabel1 = new JLabel("");
        JButton createCalendarButton = new JButton("Create Calendar");
        JButton anotherCalendarBackButton = new JButton("Go Back");
        JButton appointmentBackButton = new JButton("Go Back");
        JPanel manualCalendarPanel = new JPanel();
        manualCalendarPanel.setLayout(new BoxLayout(manualCalendarPanel, BoxLayout.PAGE_AXIS));
        calendarNameLabel.setAlignmentX(LEFT_ALIGNMENT + 0.1f);
        manualCalendarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        manualCalendarPanel.add(calendarNameLabel);
        calendarNameText.setAlignmentX(LEFT_ALIGNMENT);
        calendarNameText.setMaximumSize(new Dimension(200, 25));
        manualCalendarPanel.add(calendarNameText);
        calendarDescriptionLabel.setAlignmentX(LEFT_ALIGNMENT);
        manualCalendarPanel.add(calendarDescriptionLabel);
        calendarDescriptionText.setAlignmentX(LEFT_ALIGNMENT);
        calendarDescriptionText.setMaximumSize(new Dimension(500, 25));
        manualCalendarPanel.add(calendarDescriptionText);
        manualCalendarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        appointmentListLabel.setAlignmentX(LEFT_ALIGNMENT);
        manualCalendarPanel.add(appointmentListLabel);
        appointmentListLabel1.setAlignmentX(LEFT_ALIGNMENT);
        manualCalendarPanel.add(appointmentListLabel1);
        manualCalendarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JPanel appointmentAndCalendarPanel = new JPanel();
        appointmentAndCalendarPanel.add(anotherCalendarBackButton);
        appointmentAndCalendarPanel.add(addAppointmentButton);
        appointmentAndCalendarPanel.add(createCalendarButton);
        appointmentAndCalendarPanel.setAlignmentX(LEFT_ALIGNMENT);
        manualCalendarPanel.add(appointmentAndCalendarPanel);

        //Appointment info panel
        JPanel appointmentInfoPanel = new JPanel();
        appointmentInfoPanel.setLayout(new BoxLayout(appointmentInfoPanel, BoxLayout.PAGE_AXIS));
        appointmentInfoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        appointmentTitleLabel.setAlignmentX(LEFT_ALIGNMENT + 0.1f);
        appointmentInfoPanel.add(appointmentTitleLabel);
        appointmentTitleText.setAlignmentX(LEFT_ALIGNMENT);
        appointmentTitleText.setMaximumSize(new Dimension(200, 25));
        appointmentInfoPanel.add(appointmentTitleText);
        maxAttendeesLabel.setAlignmentX(LEFT_ALIGNMENT);
        appointmentInfoPanel.add(maxAttendeesLabel);
        maxAttendeesText.setAlignmentX(LEFT_ALIGNMENT);
        maxAttendeesText.setMaximumSize(new Dimension(50, 25));
        appointmentInfoPanel.add(maxAttendeesText);
        approvedBookingsLabel.setAlignmentX(LEFT_ALIGNMENT);
        appointmentInfoPanel.add(approvedBookingsLabel);
        approvedBookingsText.setAlignmentX(LEFT_ALIGNMENT);
        approvedBookingsText.setMaximumSize(new Dimension(50, 25));
        appointmentInfoPanel.add(approvedBookingsText);
        startTimeLabel.setAlignmentX(LEFT_ALIGNMENT);
        appointmentInfoPanel.add(startTimeLabel);
        startTimeText.setAlignmentX(LEFT_ALIGNMENT);
        startTimeText.setMaximumSize(new Dimension(50, 25));
        appointmentInfoPanel.add(startTimeText);
        endTimeLabel.setAlignmentX(LEFT_ALIGNMENT);
        appointmentInfoPanel.add(endTimeLabel);
        endTimeText.setAlignmentX(LEFT_ALIGNMENT);
        endTimeText.setMaximumSize(new Dimension(50, 25));
        appointmentInfoPanel.add(endTimeText);
        appointmentInfoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JPanel createAndBackPanel = new JPanel();
        createAndBackPanel.add(appointmentBackButton);
        createAndBackPanel.add(createAppointmentButton);
        createAndBackPanel.setAlignmentX(LEFT_ALIGNMENT);
        appointmentInfoPanel.add(createAndBackPanel);

        //Seller edit calendar panel
        JLabel editCalendarTitleLabel = new JLabel("Name of calendar to edit: ");
        JTextField editCalendarTitleField = new JTextField("", 20);
        JLabel editCalendarApptLabel = new JLabel("Name of appointment: ");
        JTextField editCalendarApptField = new JTextField("", 20);
        JLabel editCalendarNewApptLabel = new JLabel("Enter the name of the new appointment: ");
        JTextField editCalendarNewApptField = new JTextField("", 20);
        JLabel editCalendarMaxLabel = new JLabel("Enter the new number of max attendees: ");

        JLabel editCalendarApprovedBookingsLabel = new JLabel("Enter the new max bookings: ");
        JTextField editCalendarApprovedBookingsField = new JTextField("", 20);

        JLabel editCalendarNewStartTimeLabel = new JLabel("Enter the new appointment start time: ");
        JTextField editCalendarNewStartTimeField = new JTextField("", 20);

        JLabel editCalendarNewEndTimeLabel = new JLabel("Enter the new appointment end time: ");
        JTextField editCalendarNewEndTimeField = new JTextField("", 20);

        JTextField editCalendarMaxField = new JTextField("", 20);
        JButton editCalendarProceedButton = new JButton("Proceed");
        JButton editCalendarBackButton = new JButton("Go Back");
        JLabel editCalendarViewCalendars = new JLabel();

        JPanel editCalendarPanel = new JPanel();
        editCalendarPanel.setLayout(new BoxLayout(editCalendarPanel, BoxLayout.PAGE_AXIS));
        editCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        editCalendarViewCalendars.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarPanel.add(editCalendarViewCalendars);
        editCalendarTitleLabel.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarPanel.add(editCalendarTitleLabel);
        editCalendarPanel.add(editCalendarTitleField);
        editCalendarTitleField.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarTitleField.setMaximumSize(new Dimension(200, 25));
        editCalendarApptLabel.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        editCalendarPanel.add(editCalendarApptLabel);
        editCalendarPanel.add(editCalendarApptField);
        editCalendarApptField.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarApptField.setMaximumSize(new Dimension(200, 25));
        editCalendarNewApptLabel.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        editCalendarPanel.add(editCalendarNewApptLabel);
        editCalendarPanel.add(editCalendarNewApptField);
        editCalendarNewApptField.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarNewApptField.setMaximumSize(new Dimension(200, 25));
        editCalendarMaxLabel.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        editCalendarPanel.add(editCalendarMaxLabel);
        editCalendarPanel.add(editCalendarMaxField);
        editCalendarMaxField.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarMaxField.setMaximumSize(new Dimension(200, 25));
        editCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));

        editCalendarPanel.add(editCalendarApprovedBookingsLabel);
        editCalendarPanel.add(editCalendarApprovedBookingsField);
        editCalendarApprovedBookingsLabel.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarApprovedBookingsField.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarApprovedBookingsField.setMaximumSize(new Dimension(200, 25));
        editCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));

        editCalendarPanel.add(editCalendarNewStartTimeLabel);
        editCalendarPanel.add(editCalendarNewStartTimeField);
        editCalendarNewStartTimeLabel.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarNewStartTimeField.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarNewStartTimeField.setMaximumSize(new Dimension(200, 25));
        editCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));

        editCalendarPanel.add(editCalendarNewEndTimeLabel);
        editCalendarPanel.add(editCalendarNewEndTimeField);
        editCalendarNewEndTimeLabel.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarNewEndTimeField.setAlignmentX(LEFT_ALIGNMENT);
        editCalendarNewEndTimeField.setMaximumSize(new Dimension(200, 25));
        editCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        editCalendarProceedButton.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.add(editCalendarProceedButton);
        editCalendarBackButton.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.add(editCalendarBackButton);
        editCalendarPanel.add(buttonPanel);
        editCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        JScrollPane editCalendarScroll = new JScrollPane(editCalendarPanel);
        editCalendarScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //Seller delete panel
        JLabel deleteCalendarTitleLabel = new JLabel("     Name of calendar to delete: ");
        JTextField deleteCalendarTitleField = new JTextField();
        JButton deleteCalendarProceedButton = new JButton("Proceed");
        JButton deleteCalendarBackButton = new JButton("Go Back");
        JLabel deleteCalendarViewCalendars = new JLabel();
        JPanel deleteCalendarPanel = new JPanel();
        deleteCalendarPanel.setLayout(new BoxLayout(deleteCalendarPanel, BoxLayout.PAGE_AXIS));
        deleteCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        deleteCalendarViewCalendars.setAlignmentX(CENTER_ALIGNMENT);
        deleteCalendarPanel.add(deleteCalendarViewCalendars);
        editCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        deleteCalendarTitleLabel.setAlignmentX(LEFT_ALIGNMENT + 0.1f);
        deleteCalendarPanel.add(deleteCalendarTitleLabel);
        deleteCalendarTitleField.setAlignmentX(LEFT_ALIGNMENT);
        deleteCalendarPanel.add(deleteCalendarTitleField);
        deleteCalendarTitleField.setAlignmentX(LEFT_ALIGNMENT);
        deleteCalendarTitleField.setMaximumSize(new Dimension(200, 25));
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        deleteCalendarProceedButton.setAlignmentX(LEFT_ALIGNMENT);
        buttonPanel2.add(deleteCalendarProceedButton);
        deleteCalendarBackButton.setAlignmentX(LEFT_ALIGNMENT);
        buttonPanel2.add(deleteCalendarBackButton);
        deleteCalendarPanel.add(buttonPanel2);
        deleteCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        JScrollPane deleteCalendarScroll = new JScrollPane(deleteCalendarPanel);
        deleteCalendarScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //Seller approve/decline appointment requests panel (pt 1 - choose calendar)
        JLabel selectApprovalCalendarLabel = new JLabel("<html> <br> </br> Please select an appointment " +
                "above to approve/decline: " + "<br> </br> Enter the appointment exactly as it appears in the above " +
                "appointment list. [Calendar name]-" + "[Appointment Title],[Max Attendees],[Approved Bookings]," +
                "[Start Time],[End Time]-[Customer username]</html>");
        JLabel selectApprovalCustomerUsernameLabel = new JLabel("Enter the customer's username of the " +
                "requested appointment: ");
        JLabel actionResponse = new JLabel("Select an option:");
        JLabel selectApprovalRequestsLabel = new JLabel();
        JTextField selectApprovalCalendarField = new JTextField("", 400);
        JTextField selectApprovalCustomerUsernameField = new JTextField("", 400);
        JButton selectApprovalProceedButton = new JButton("Proceed");
        JButton selectApprovalBackButton = new JButton("Go Back");
        JComboBox<String> sellerActionOptions = new JComboBox<>();
        JPanel selectApprovalCalendarPanel = new JPanel();
        selectApprovalCalendarPanel.setLayout(new BoxLayout(selectApprovalCalendarPanel, BoxLayout.PAGE_AXIS));
        selectApprovalCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        selectApprovalRequestsLabel.setAlignmentX(LEFT_ALIGNMENT);
        selectApprovalCalendarPanel.add(selectApprovalRequestsLabel);
        selectApprovalCalendarLabel.setAlignmentX(LEFT_ALIGNMENT);
        selectApprovalCalendarPanel.add(selectApprovalCalendarLabel);
        selectApprovalCalendarField.setMaximumSize(new Dimension(500, 25));
        selectApprovalCalendarField.setAlignmentX(LEFT_ALIGNMENT);
        selectApprovalCalendarPanel.add(selectApprovalCalendarField);
        selectApprovalCalendarPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        selectApprovalCustomerUsernameLabel.setAlignmentX(LEFT_ALIGNMENT);
        selectApprovalCalendarPanel.add(selectApprovalCustomerUsernameLabel);
        selectApprovalCustomerUsernameField.setMaximumSize(new Dimension(200, 25));
        selectApprovalCustomerUsernameField.setAlignmentX(LEFT_ALIGNMENT);
        selectApprovalCalendarPanel.add(selectApprovalCustomerUsernameField);
        // Add space under the text field
        selectApprovalCalendarPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        actionResponse.setAlignmentX(LEFT_ALIGNMENT);
        selectApprovalCalendarPanel.add(actionResponse);
        // Add space under the text field
        selectApprovalCalendarPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        sellerActionOptions.setMaximumSize(new Dimension(400, 25));
        sellerActionOptions.setAlignmentX(LEFT_ALIGNMENT);
        sellerActionOptions.addItem("Approve appointment.");
        sellerActionOptions.addItem("Decline appointment.");
        selectApprovalCalendarPanel.add(sellerActionOptions);

        // Add space under the text field
        selectApprovalCalendarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(selectApprovalProceedButton);
        buttonBox.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonBox.add(selectApprovalBackButton);
        buttonBox.add(Box.createHorizontalGlue());
        selectApprovalCalendarPanel.add(buttonBox);
        selectApprovalCalendarPanel.add(Box.createRigidArea(new Dimension(20, 20)));

        //Seller approve/decline appointment requests panel (pt 2 - checkbox requests)
        JLabel approveLabel = new JLabel("Check the box beside an appointment request to approve it.");
        JPanel approvePanel = new JPanel();
        JButton approveProceedButton = new JButton("Confirm Approvals");
        JButton approveBackButton = new JButton("Go Back");
        approvePanel.setLayout(new BoxLayout(approvePanel, BoxLayout.PAGE_AXIS));
        approvePanel.add(Box.createRigidArea(new Dimension(20, 20)));
        approvePanel.add(approveLabel);
        approvePanel.add(Box.createRigidArea(new Dimension(20, 20)));
        int numAppointments = 2; //update to reflect actual number of seller appointment requests pending approval
        for (int i = 0; i < numAppointments; i++) {
            JCheckBox checkBox = new JCheckBox(String.valueOf(i));
            checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Perform actions based on checkbox state
                    JCheckBox source = (JCheckBox) e.getSource();
                    //let program know that the appointment selected has been approved here

                }
            });
            approvePanel.add(checkBox);
            checkBox.setAlignmentX(LEFT_ALIGNMENT + 0.1f);
        }
        approvePanel.add(Box.createRigidArea(new Dimension(20, 20)));
        approvePanel.add(approveProceedButton);
        approvePanel.add(approveBackButton);
        approveLabel.setAlignmentX(LEFT_ALIGNMENT + 0.1f);

        //Seller view statistics panel
        JLabel sellerSort = new JLabel("Choose a method to sort statistics dashboard");
        JComboBox<String> sellerSortOptions = new JComboBox<>();
        JButton sellerSortButton = new JButton("Proceed");
        JButton sellerSortBackButton = new JButton("Go Back");
        JPanel sellerViewStatistics = new JPanel();
        sellerViewStatistics.setLayout(new BoxLayout(sellerViewStatistics, BoxLayout.PAGE_AXIS));
        sellerViewStatistics.add(Box.createRigidArea(new Dimension(200, 10)));
        sellerSort.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellerViewStatistics.add(sellerSort);
        sellerViewStatistics.add(Box.createRigidArea(new Dimension(1, 10)));
        sellerSortOptions.setMaximumSize(new Dimension(400, 25));
        sellerSortOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellerSortOptions.addItem("Most to Least Popular Appointments");
        sellerSortOptions.addItem("Least to Most Popular Appointments");
        sellerViewStatistics.add(sellerSortOptions);
        sellerPanel.add(Box.createRigidArea(new Dimension(1, 20)));
        sellerSortButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellerViewStatistics.add(sellerSortButton);
        sellerPanel.add(Box.createRigidArea(new Dimension(1, 20)));
        sellerSortBackButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellerViewStatistics.add(sellerSortBackButton);

        JPanel sellerSortedStatisticsPanel = new JPanel();
        JLabel sellerSortedStatsLabel = new JLabel();
        JButton sellerViewSortedStatsBackButton = new JButton("Go back");
        sellerSortedStatisticsPanel.add(sellerSortedStatsLabel);
        sellerSortedStatisticsPanel.add(sellerViewSortedStatsBackButton);

        //Seller log out panel = customer log out panel

        //Action listeners
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == createAccountButton) {
                    content.removeAll(); //Removes initial option menu

                    //Adding create account panels
                    content.setLayout(new GridLayout(6, 1));
                    content.add(infoPanel);
                    content.add(namePanel);
                    content.add(loginInfoPanel);
                    content.add(emailPanel);
                    content.add(createPassPanel);
                    content.add(createEnterPanel);
                    frame.setSize(600, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == loginButton) {
                    content.removeAll(); //Removes initial option menu

                    //Adding login panels
                    content.setLayout(new GridLayout(6, 1));
                    content.add(infoPanel);
                    content.add(namePanel);
                    content.add(loginInfoPanel);
                    content.add(emailPanel);
                    content.add(loginPassPanel);
                    content.add(loginEnterPanel);
                    frame.setSize(600, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == createEnterButton) {

                    boolean passCheck = false;

                    //Presence check on all fields
                    if (nameText.getText().isEmpty() || emailText.getText().isEmpty() ||
                            createPassText.getText().isEmpty()) {
                        //Tells user that all fields need to be filled
                        JOptionPane.showMessageDialog(null, "All fields need to be filled!",
                                "Create Account", JOptionPane.ERROR_MESSAGE);
                    } else {
                        passCheck = true;
                    }

                    if (passCheck) {
                        //Gathering data to send to Server
                        String userOrSeller = (String) sellerOrCustomer.getSelectedItem();
                        String name = nameText.getText();
                        String email = emailText.getText();
                        String pass = createPassText.getText();

                        //Sends info to Server
                        String validEmail = sendDataToServer(createEnterButton, userOrSeller + "," + name
                                + "," + email + "," + pass);
                        if (validEmail.equals("true")) {
                            //Tells user account has been created
                            JOptionPane.showMessageDialog(null, "Account created!",
                                    "Create Account", JOptionPane.INFORMATION_MESSAGE);

                            //Resets all text fields
                            nameText.setText("");
                            emailText.setText("");
                            createPassText.setText("");

                            content.removeAll(); //Removes current panel

                            //Returning user to initial option menu
                            content.setLayout(new BorderLayout());
                            content.add(initialOptionPanel);
                            frame.setSize(400, 200);
                            frame.setLocationRelativeTo(null);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            frame.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Email already exists! " +
                                            "Please choose a different email",
                                    "Create Account", JOptionPane.ERROR_MESSAGE); //Error message

                            //Resets text fields so user can try again
                            emailText.setText("");
                            createPassText.setText("");
                        }
                    }
                } else if (e.getSource() == loginEnterButton) {
                    boolean passCheck = false;
                    //Presence check on all fields
                    if (nameText.getText().isEmpty() || emailText.getText().isEmpty() ||
                            loginPassText.getText().isEmpty()) {
                        //Tells user that all fields need to be filled
                        JOptionPane.showMessageDialog(null, "All fields need to be filled!",
                                "Create Account", JOptionPane.ERROR_MESSAGE);
                    } else {
                        passCheck = true;
                    }

                    if (passCheck) {
                        //Gathering data to send to Server
                        String userOrSeller = (String) sellerOrCustomer.getSelectedItem();
                        String name = nameText.getText();
                        String email = emailText.getText();
                        String pass = loginPassText.getText();

                        //Sends info to Server
                        String validLogin = sendDataToServer(loginEnterButton, userOrSeller + "," + name +
                                "," + email + "," + pass);
                        if (validLogin.equals("true")) {
                            JOptionPane.showMessageDialog(null, "Login successful!",
                                    "Login", JOptionPane.INFORMATION_MESSAGE); //Tells user login was successful

                            //Resets text fields
                            nameText.setText("");
                            emailText.setText("");
                            loginPassText.setText("");

                            //code below is nested inside login check after verification
                            if (userOrSeller.equals("Seller")) {
                                //TODO: Where Seller screen pops up
                                content.removeAll();
                                frame.repaint();
                                content.setLayout(new GridLayout(2, 1));
                                content.add(sellerPanel);
                                frame.setSize(750, 400);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                updateUI();
                                frame.setVisible(true);
                            } else if (userOrSeller.equals("Customer")) {
                                content.removeAll();
                                frame.repaint();
                                content.setLayout(new GridLayout(2, 1));
                                content.add(customerPanel);
                                frame.setSize(750, 400);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                updateUI();
                                frame.setVisible(true);
                            }
                        } else { //Invalid login
                            JOptionPane.showMessageDialog(null, "Either you did not " +
                                            "select your correct user type (Seller or Customer)" +
                                            " or your email or password is incorrect!",
                                    "Login", JOptionPane.ERROR_MESSAGE); //Error message
                        }

                        //Resets text fields
                        nameText.setText("");
                        emailText.setText("");
                        loginPassText.setText("");
                    }
                } else if (e.getSource() == createBackButton) {
                    content.removeAll(); //Clears the frame

                    //Takes user back to initial option screen
                    content.setLayout(new BorderLayout());
                    content.add(initialOptionPanel);
                    frame.setSize(400, 200);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == loginBackButton) {
                    content.removeAll(); //Clears the frame

                    //Takes user back to initial option screen
                    content.setLayout(new BorderLayout());
                    content.add(initialOptionPanel);
                    frame.setSize(400, 200);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == customerProceedButton) {
                    int customerMenuSelection = customerOptions.getSelectedIndex();
                    switch (customerMenuSelection) {
                        case 0:
                            String temp = sendDataToServer(customerProceedButton, "calendars;temp");
                            getAppointments.setText("<html>" + temp + "</html");
                            content.removeAll();
                            frame.repaint();
                            content.setLayout(new GridLayout(1, 1));
                            customerAppointmentText.setText("");
                            content.add(customerMakeAppointmentScroll);
                            frame.setSize(750, 1300);
                            frame.setLocationRelativeTo(null);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            updateUI();
                            frame.setVisible(true);
                            break;
                        case 1: //Cancel an appointment request
                            content.removeAll();
                            frame.repaint();
                            content.setLayout(new GridLayout(2, 1));
                            content.add(customerViewCancelAppointmentsPanel);
                            frame.setSize(750, 300);
                            frame.setLocationRelativeTo(null);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            updateUI();
                            frame.setVisible(true);
                            break;
                        case 2:
                            String temp2 = sendDataToServer(customerProceedButton, "calendars;temp");
                            customerViewCalendarsLabel.setText("<html>" + temp2 + "</html>");
                            content.removeAll();
                            frame.repaint();
                            content.setLayout(new GridLayout(1, 1));
                            content.add(customerViewCalendars);
                            frame.setSize(750, 1300);
                            frame.setLocationRelativeTo(null);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            updateUI();
                            frame.setVisible(true);
                            break;
                        case 3:
                            String temp3 = sendDataToServer(customerProceedButton, "4;temp");
                            customerViewApprovedAppointmentsLabel.setText("<html>" + temp3 + "</html>");
                            content.removeAll();
                            frame.repaint();
                            content.setLayout(new GridLayout(2, 1));
                            content.add(customerViewApprovedAppointments);
                            frame.setSize(750, 300);
                            frame.setLocationRelativeTo(null);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            updateUI();
                            frame.setVisible(true);
                            break;
                        case 4:
                            String temp4 = sendDataToServer(customerProceedButton, "5;temp");
                            customerViewPendingAppointmentsLabel.setText("<html>" + temp4 + "</html>");
                            content.removeAll();
                            frame.repaint();
                            content.setLayout(new GridLayout(2, 1));
                            content.add(customerViewAppointmentsAwaiting);
                            frame.setSize(750, 300);
                            frame.setLocationRelativeTo(null);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            updateUI();
                            frame.setVisible(true);
                            break;
                        case 5:
                            content.removeAll();
                            frame.repaint();
                            content.setLayout(new GridLayout(2, 1));
                            content.add(customerViewStatistics);
                            frame.setSize(750, 300);
                            frame.setLocationRelativeTo(null);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            updateUI();
                            frame.setVisible(true);
                            break;
                        case 6:
                            String temp6 = sendDataToServer(customerProceedButton, "7;temp");
                            if (temp6.equals("Your file has been exported! Please check your desktop " +
                                    "to view the text file.")) {
                                JOptionPane.showMessageDialog(null,
                                        "Your file has been exported! Please check your desktop to " +
                                                "view the text file.", "File Export",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "You must make an appointment to export your file.",
                                        "File Export", JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 7:
                            sendDataToServer(customerProceedButton, "8;temp");
                            content.removeAll();
                            content.setLayout(new GridLayout(1, 1));
                            content.add(exitLogOutPanel);
                            frame.setSize(600, 200);
                            frame.setLocationRelativeTo(null);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            updateUI();
                            frame.setVisible(true);
                            break;
                        default:
                            break;
                    }
                } else if (e.getSource() == customerAppointmentButton) {

                    boolean passCheck = false;

                    String appointmentText = customerAppointmentText.getText();

                    if (customerAppointmentText.getText().isEmpty()) {
                        //Tells user that all fields need to be filled
                        JOptionPane.showMessageDialog(null, "Appointment needs to be filled!",
                                "Appointment", JOptionPane.ERROR_MESSAGE);
                    } else if (customerAppointmentText.getText().split("-").length != 2 ||
                            customerAppointmentText.getText().split("-")[1].split(",").length != 5) {
                        //Tells user that all fields need to be filled
                        JOptionPane.showMessageDialog(null, "Appointment needs to be in the " +
                                        "right format!",
                                "Appointment", JOptionPane.ERROR_MESSAGE);
                    } else {
                        passCheck = true;
                    }

                    if (passCheck) {
                        String getAppointmentStatus = sendDataToServer(customerAppointmentButton, "1;"
                                + appointmentText);
                        if (getAppointmentStatus.equals("Appointment request made.")) {
                            JOptionPane.showMessageDialog(null, "Appointment request made.",
                                    "Status", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Appointment request " +
                                            "unsuccessful.",
                                    "Status", JOptionPane.ERROR_MESSAGE);
                        }
                        content.removeAll(); //Clears the frame
                        frame.repaint();
                        content.setLayout(new BorderLayout());
                        content.add(customerPanel);
                        frame.setSize(900, 400);
                        frame.setLocationRelativeTo(null);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setVisible(true);
                    }
                } else if (e.getSource() == customerViewCancelButton) {
                    viewCancelOption = customerCancelOptions.getSelectedIndex();
                    String getCancelCalendar = "";
                    if (viewCancelOption == 0) {
                        getCancelCalendar = sendDataToServer(customerAppointmentButton, "cancel;1");
                    } else {
                        getCancelCalendar = sendDataToServer(customerAppointmentButton, "cancel;2");
                    }

                    customerCancelLabel.setText("<html>" + getCancelCalendar + "</html>");
                    content.removeAll(); //Clears the frame
                    frame.repaint();
                    content.setLayout(new BorderLayout());
                    content.add(customerCancelAppointment);
                    frame.setSize(900, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateUI();
                    frame.setVisible(true);
                } else if (e.getSource() == customerCancelButton) {
                    boolean passCheck = false;

                    String getCancelAppointment = customerCancelText.getText();

                    if (customerCancelText.getText().isEmpty()) {
                        //Tells user that all fields need to be filled
                        JOptionPane.showMessageDialog(null, "Appointment needs to " +
                                        "be filled!",
                                "Appointment", JOptionPane.ERROR_MESSAGE);
                    } else if (customerCancelText.getText().split("-").length != 3 ||
                            customerCancelText.getText().split("-")[1].split(",").length != 5) {
                        //Tells user that all fields need to be filled
                        JOptionPane.showMessageDialog(null, "Appointment needs to be in " +
                                        "the right format!",
                                "Appointment", JOptionPane.ERROR_MESSAGE);
                    } else {
                        passCheck = true;
                    }

                    if (passCheck) {
                        if (viewCancelOption == 0) {
                            viewCancelOption = 1;
                        } else {
                            viewCancelOption = 2;
                        }

                        String getCancelCalendar = sendDataToServer(customerCancelButton, "2;" +
                                viewCancelOption + ";" + getCancelAppointment);
                        if (getCancelCalendar.equals("Appointment cancelled successfully.")) {
                            JOptionPane.showMessageDialog(null, "Appointment cancelled " +
                                            "successfully.",
                                    "Status", JOptionPane.INFORMATION_MESSAGE);
                            content.removeAll(); //Clears the frame
                            frame.repaint();
                            content.setLayout(new BorderLayout());
                            content.add(customerPanel);
                            frame.setSize(900, 400);
                            frame.setLocationRelativeTo(null);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            updateUI();
                            frame.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Appointment " +
                                            "request unsuccessful.",
                                    "Status", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else if (e.getSource() == customerSortButton) {
                    viewSortOption = customerCancelOptions.getSelectedIndex();
                    if (viewSortOption == 0) {
                        viewSortOption = 1;
                    }
                    String tempSort = sendDataToServer(customerSortButton, "6;" + viewSortOption);
                    customerViewStatsLabel.setText("<html>" + tempSort + "</html>");

                    content.removeAll(); //Clears the frame
                    frame.repaint();
                    content.setLayout(new BorderLayout());
                    content.add(customerViewStats);
                    frame.setSize(900, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == customerAppointmentBackButton) {
                    content.removeAll(); //Clears the frame
                    frame.repaint();
                    content.setLayout(new BorderLayout());
                    content.add(customerPanel);
                    frame.setSize(900, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == customerCancelBackButton) {
                    content.removeAll(); //Clears the frame
                    frame.repaint();
                    content.setLayout(new BorderLayout());
                    content.add(customerPanel);
                    frame.setSize(900, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == customerExitButton) {
                    content.removeAll(); //Clears the frame
                    frame.repaint();
                    content.setLayout(new BorderLayout());
                    content.add(initialOptionPanel);
                    frame.setSize(900, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == customerViewCalendarsBackButton) {
                    content.removeAll(); //Clears the frame
                    frame.repaint();
                    content.setLayout(new BorderLayout());
                    content.add(customerPanel);
                    frame.setSize(900, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == customerViewStatsBackButton) {
                    content.removeAll(); //Clears the frame
                    frame.repaint();
                    content.setLayout(new BorderLayout());
                    content.add(customerPanel);
                    frame.setSize(900, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == customerViewApprovedAppointmentsBackButton) {
                    content.removeAll(); //Clears the frame
                    frame.repaint();
                    content.setLayout(new BorderLayout());
                    content.add(customerPanel);
                    frame.setSize(900, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == customerViewAppointmentsAwaitingBackButton) {
                    content.removeAll(); //Clears the frame
                    frame.repaint();
                    content.setLayout(new BorderLayout());
                    content.add(customerPanel);
                    frame.setSize(900, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == customerExportFileBackButton) {
                    content.removeAll(); //Clears the frame
                    frame.repaint();
                    content.setLayout(new BorderLayout());
                    content.add(customerPanel);
                    frame.setSize(900, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == customerViewStatisticsBackButton) {
                    content.removeAll(); //Clears the frame
                    frame.repaint();
                    content.setLayout(new BorderLayout());
                    content.add(customerPanel);
                    frame.setSize(900, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == importFileButton) {
                    content.removeAll(); //Clears the frame
                    content.setLayout(new BorderLayout());
                    content.add(importFilePanel);
                    frame.setSize(900, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == manualButton) {
                    content.removeAll(); //Clears the frame
                    content.setLayout(new BorderLayout());
                    content.add(manualCalendarPanel);
                    frame.setSize(700, 500);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == importButton) {

                    boolean passCheck = false;

                    if (importFileText.getText().isEmpty()) {
                        //Tells user that all fields need to be filled
                        JOptionPane.showMessageDialog(null, "Store name needs to " +
                                        "be filled!",
                                "Seller", JOptionPane.ERROR_MESSAGE);
                    } else if (importFileText.getText().split(",").length != 8) {
                        //Tells user that all fields need to be in right format
                        JOptionPane.showMessageDialog(null, "Ensure file name is in " +
                                        "the right format!",
                                "Seller", JOptionPane.ERROR_MESSAGE);
                    } else {
                        passCheck = true;
                    }

                    if (passCheck) {
                        String sendToServer = storeNameText.getText() + ";2;1;" + importFileText.getText();
                        if (sendDataToServer(sellerProceedButton, sendToServer).equals("Unsuccessful")) {
                            JOptionPane.showMessageDialog(null, "File was unable to import! " +
                                            "Please ensure your file exists.",
                                    "Status", JOptionPane.ERROR_MESSAGE);
                        } else { //Was successful
                            JOptionPane.showMessageDialog(null, "File imported successfully!",
                                    "Status", JOptionPane.INFORMATION_MESSAGE);

                            importFileText.setText("");
                            content.removeAll();
                            frame.repaint();
                            content.setLayout(new GridLayout(2, 1));
                            content.add(sellerPanel);
                            frame.setSize(750, 400);
                            frame.setLocationRelativeTo(null);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            updateUI();
                            frame.setVisible(true);
                        }
                    }
                } else if (e.getSource() == addAppointmentButton) {

                    boolean passCheck = false;

                    if (calendarNameText.getText().isEmpty() || calendarDescriptionText.getText().isEmpty()) {
                        //Tells user that all fields need to be filled
                        JOptionPane.showMessageDialog(null, "Calendar details need to " +
                                        "be filled!",
                                "Calendar", JOptionPane.ERROR_MESSAGE);
                    } else {
                        passCheck = true;
                    }

                    if (passCheck) {
                        content.removeAll(); //Clears the frame
                        frame.repaint();
                        content.setLayout(new BorderLayout());
                        content.add(appointmentInfoPanel);
                        frame.setSize(700, 500);
                        frame.setLocationRelativeTo(null);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setVisible(true);
                    }

                } else if (e.getSource() == createAppointmentButton) {

                    boolean passCheck = false;

                    //Presence check
                    if (appointmentTitleText.getText().isEmpty() || maxAttendeesText.getText().isEmpty()
                            || approvedBookingsText.getText().isEmpty()
                            || startTimeText.getText().isEmpty() || endTimeText.getText().isEmpty()) {
                        //Tells user that all fields need to be filled
                        JOptionPane.showMessageDialog(null, "All fields need to be filled!",
                                "Appointment", JOptionPane.ERROR_MESSAGE);
                    } else if (!isInteger(maxAttendeesText.getText()) || !isInteger(approvedBookingsText.getText())) {
                        //Tells user that all field needs to be right data type
                        JOptionPane.showMessageDialog(null, "Ensure max attendees and " +
                                        "approved bookings are integers",
                                "Appointment", JOptionPane.ERROR_MESSAGE);
                        maxAttendeesText.setText("");
                        approvedBookingsText.setText("");
                        //Range check on approved bookings text
                    } else if (Integer.parseInt(approvedBookingsText.getText()) < 0 ||
                            Integer.parseInt(approvedBookingsText.getText()) > 1) {
                        JOptionPane.showMessageDialog(null, "Approved bookings must be 0 or 1!",
                                "Appointment", JOptionPane.ERROR_MESSAGE); //Tells user about range
                        approvedBookingsText.setText("");

                        //Format check on start and end time
                    } else if (!startTimeText.getText().matches("\\d{2}:\\d{2}")) {
                        JOptionPane.showMessageDialog(null, "Ensure the time format is correct!",
                                "Appointment", JOptionPane.ERROR_MESSAGE); //Tells user about format
                        startTimeText.setText("");

                    } else if (!endTimeText.getText().matches("\\d{2}:\\d{2}")) {
                        JOptionPane.showMessageDialog(null, "Ensure the time format is correct!",
                                "Appointment", JOptionPane.ERROR_MESSAGE); //Tells user about format
                        endTimeText.setText("");
                    } else {
                        passCheck = true;
                    }

                    //Clearing the textfields
                    if (!passCheck) {
                        appointmentTitleText.setText("");
                        maxAttendeesText.setText("");
                        approvedBookingsText.setText("");
                        startTimeText.setText("");
                        endTimeText.setText("");
                    }

                    if (passCheck) {
                        //Getting all the info
                        String apptTitle = appointmentTitleText.getText();
                        int maxAttendees = Integer.parseInt(maxAttendeesText.getText());
                        int approvedBookings = Integer.parseInt(approvedBookingsText.getText());
                        String startTime = startTimeText.getText();
                        String endTime = endTimeText.getText();

                        //Clearing the textfields
                        appointmentTitleText.setText("");
                        maxAttendeesText.setText("");
                        approvedBookingsText.setText("");
                        startTimeText.setText("");
                        endTimeText.setText("");

                        //Creating appointment object
                        Appointment newAppt = new Appointment(apptTitle, maxAttendees, approvedBookings, startTime, endTime);

                        //Adding appointment to calendar page
                        if (appointmentListLabel.getText().length() > 14) {
                            appointmentListLabel.setText(appointmentListLabel.getText().substring(0, 13));
                        }

                        if (appointmentListLabel1.getText().length() > 1) {
                            appointmentListLabel1.setText(appointmentListLabel1.getText().substring(0,
                                    appointmentListLabel1.getText().length() - 7) + "<br/>" + newAppt + "</html>");
                        } else {
                            appointmentListLabel1.setText(appointmentListLabel1.getText() + "<html><br/>" +
                                    newAppt + "</html>");
                        }

                        //Bringing user back to calendar page
                        content.removeAll(); //Clears the frame
                        frame.repaint();
                        content.setLayout(new BorderLayout());
                        content.add(manualCalendarPanel);
                        frame.setSize(700, 500);
                        frame.setLocationRelativeTo(null);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setVisible(true);
                    }

                } else if (e.getSource() == sellerProceedButton) {

                    boolean passCheck = false;

                    if (sellerOptions.getSelectedIndex() == 7) {
                        passCheck = true;
                    } else if (storeNameText.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Store name needs to be filled!",
                                "Seller", JOptionPane.ERROR_MESSAGE); //Tells user that all fields need to be filled
                    } else {
                        passCheck = true;
                    }

                    if (passCheck) {
                        int sellerMenuSelection = sellerOptions.getSelectedIndex();
                        switch (sellerMenuSelection) {
                            case 1: //View current calendars
                                viewCalendarsLabel.setText("<html>" + sendDataToServer(sellerProceedButton,
                                        storeNameLabel.getText() + ";1") + "</html>");
                                content.removeAll(); //Clears the frame
                                frame.repaint();
                                content.setLayout(new BorderLayout());
                                content.add(jsp);
                                frame.setSize(900, 400);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                frame.setVisible(true);
                                break;
                            case 2: //Create new calendar
                                content.removeAll(); //Clears the frame
                                content.setLayout(new BorderLayout());
                                content.add(createCalendarPanel);
                                frame.setSize(400, 200);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                frame.setVisible(true);
                                break;
                            case 3: //Edit calendar
                                String temp = sendDataToServer(sellerProceedButton, "temp;calendars");
                                editCalendarViewCalendars.setText("<html>" + temp + "</html>");
                                content.removeAll();
                                content.setLayout(new BorderLayout());
                                content.add(editCalendarScroll);
                                frame.setSize(1100, 1300);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                frame.setVisible(true);
                                break;
                            case 4: //Delete calendar
                                String temp1 = sendDataToServer(sellerProceedButton, "temp;calendars");
                                deleteCalendarViewCalendars.setText("<html>" + temp1 + "</html>");
                                content.removeAll();
                                content.setLayout(new BorderLayout());
                                content.add(deleteCalendarScroll);
                                frame.setSize(900, 500);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                frame.setVisible(true);
                                break;
                            case 5: //Approve or decline requests
                                String temp2 = sendDataToServer(sellerProceedButton, "temp;requests");
                                selectApprovalRequestsLabel.setText("<html>" + temp2 + "</html>");
                                content.removeAll();
                                content.setLayout(new BorderLayout());
                                content.add(selectApprovalCalendarPanel);
                                frame.setSize(900, 500);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                frame.setVisible(true);
                                break;
                            case 6: //View stats
                                String temp3 = sendDataToServer(sellerProceedButton, "temp;6");
                                sellerSortedStatsLabel.setText("<html>" + temp3 + "</html>");
                                content.removeAll();
                                content.setLayout(new BorderLayout());
                                content.add(sellerViewStatistics);
                                frame.setSize(1000, 800);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                frame.setVisible(true);
                                break;
                            case 7: //exit and log out
                                content.removeAll();
                                content.setLayout(new GridLayout(1, 1));
                                content.add(exitLogOutPanel);
                                frame.setSize(600, 200);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                updateUI();
                                frame.setVisible(true);
                                break;
                        }

                    }
                } else if (e.getSource() == createCalendarButton) {

                    boolean passCheck = false;

                    if (calendarNameText.getText().isEmpty() || calendarDescriptionText.getText().isEmpty()) {
                        //Tells user that all fields need to be filled
                        JOptionPane.showMessageDialog(null, "Calendar details need to " +
                                        "be filled!",
                                "Calendar", JOptionPane.ERROR_MESSAGE);
                    } else {
                        passCheck = true;
                    }

                    if (passCheck) {
                        String appointmentText = appointmentListLabel1.getText();

                        if (appointmentText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter an appointment!",
                                    "Status", JOptionPane.ERROR_MESSAGE);
                        } else {
                            appointmentText = appointmentText.substring(11, appointmentText.length() - 7);
                            String[] appointments = appointmentText.split("<br/>");

                            String sendToServer = storeNameText.getText() + ";2;2;" + appointments.length + ";" +
                                    calendarNameText.getText() + ";" + calendarDescriptionText.getText();

                            for (int i = 0; i < appointments.length; i++) {
                                appointments[i] = appointments[i].replace(',', ';');
                                sendToServer += ";" + appointments[i];
                            }

                            sendDataToServer(sellerProceedButton, sendToServer);
                            JOptionPane.showMessageDialog(null, "Calendar created " +
                                            "successfully!",
                                    "Status", JOptionPane.INFORMATION_MESSAGE);

                            //Clearing text fields
                            calendarNameText.setText("");
                            calendarDescriptionText.setText("");
                            appointmentListLabel1.setText("");
                            appointmentListLabel.setText("Appointments: None");

                            //Going back to main option panel
                            content.removeAll();
                            frame.repaint();
                            content.setLayout(new GridLayout(2, 1));
                            content.add(sellerPanel);
                            frame.setSize(750, 400);
                            frame.setLocationRelativeTo(null);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            updateUI();
                            frame.setVisible(true);
                        }
                    }
                } else if (e.getSource() == appointmentBackButton) {
                    //Clearing the textfields
                    appointmentTitleText.setText("");
                    maxAttendeesText.setText("");
                    approvedBookingsText.setText("");
                    startTimeText.setText("");
                    endTimeText.setText("");

                    //Going back to manually create calendar page
                    content.removeAll(); //Clears the frame
                    frame.repaint();
                    content.setLayout(new BorderLayout());
                    content.add(manualCalendarPanel);
                    frame.setSize(700, 500);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                } else if (e.getSource() == viewBackButton) {
                    content.removeAll();
                    frame.repaint();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(sellerPanel);
                    frame.setSize(750, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateUI();
                    frame.setVisible(true);
                } else if (e.getSource() == importBackButton) {
                    importFileText.setText("");
                    content.removeAll();
                    frame.repaint();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(createCalendarPanel);
                    frame.setSize(750, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateUI();
                    frame.setVisible(true);
                } else if (e.getSource() == selectApprovalProceedButton) {
                    boolean passCheck = false;

                    if (selectApprovalCalendarField.getText().isEmpty() ||
                            selectApprovalCustomerUsernameField.getText().isEmpty()) {
                        //Tells user that all fields need to be filled
                        JOptionPane.showMessageDialog(null, "All fields need to be filled!",
                                "Calendar", JOptionPane.ERROR_MESSAGE);
                    } else {
                        passCheck = true;
                    }

                    if (passCheck) {
                        String data = storeNameText.getText() + ";5;" + selectApprovalCalendarField.getText() + ";" +
                                (sellerActionOptions.getSelectedIndex() + 1) + ";" +
                                selectApprovalCustomerUsernameField.getText();
                        if (sendDataToServer(sellerProceedButton, data).equals("Appointment approved!")) {
                            JOptionPane.showMessageDialog(null, "Appointment approved!",
                                    "Appointment", JOptionPane.INFORMATION_MESSAGE);

                        } else { //Appointment declined
                            JOptionPane.showMessageDialog(null, "Appointment declined!",
                                    "Appointment", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

                    //Reset text fields
                    selectApprovalCalendarField.setText("");
                    selectApprovalCustomerUsernameField.setText("");

                    //Take user back to main screen
                    content.removeAll();
                    frame.repaint();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(sellerPanel);
                    frame.setSize(750, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateUI();
                    frame.setVisible(true);
                } else if (e.getSource() == selectApprovalBackButton) {
                    importFileText.setText("");
                    content.removeAll();
                    frame.repaint();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(sellerPanel);
                    frame.setSize(750, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateUI();
                    frame.setVisible(true);
                } else if (e.getSource() == editCalendarProceedButton) {

                    boolean passCheck = false;


                    if (editCalendarTitleField.getText().isEmpty() || editCalendarApptField.getText().isEmpty() ||
                            editCalendarNewApptField.getText().isEmpty() || editCalendarMaxField.getText().isEmpty() ||
                            editCalendarApprovedBookingsField.getText().isEmpty() ||
                            editCalendarNewStartTimeField.getText().isEmpty()
                            || editCalendarNewEndTimeField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "All details need to be filled!",
                                "Calendar", JOptionPane.ERROR_MESSAGE); //Tells user that all fields need to be filled
                    } else if (!isInteger(editCalendarMaxField.getText()) ||
                            !isInteger(editCalendarApprovedBookingsField.getText())) {
                        JOptionPane.showMessageDialog(null, "Ensure max attendees and " +
                                        "approved bookings are integers!",
                                "Calendar", JOptionPane.ERROR_MESSAGE); //Tells user that all fields need to be
                        // right data type

                    } else if (!editCalendarNewStartTimeField.getText().matches("\\d{2}:\\d{2}")) {
                        JOptionPane.showMessageDialog(null, "Ensure the time format " +
                                        "is correct!",
                                "Appointment", JOptionPane.ERROR_MESSAGE); //Tells user about format
                        editCalendarNewStartTimeField.setText("");

                    } else if (!editCalendarNewEndTimeField.getText().matches("\\d{2}:\\d{2}")) {
                        JOptionPane.showMessageDialog(null, "Ensure the time format " +
                                        "is correct!",
                                "Appointment", JOptionPane.ERROR_MESSAGE); //Tells user about format
                        editCalendarNewEndTimeField.setText("");
                    } else {
                        passCheck = true;
                    }

                    if (passCheck) {
                        String data = storeNameText.getText() + ";3;" + editCalendarTitleField.getText() + ";" +
                                editCalendarApptField.getText() + ";" +
                                editCalendarNewApptField.getText() + ";" + editCalendarMaxField.getText() + ";" +
                                editCalendarApprovedBookingsField.getText() + ";" +
                                editCalendarNewStartTimeField.getText() + ";" + editCalendarNewEndTimeField.getText();
                        String customerEditedConfirmation = sendDataToServer(sellerProceedButton, data);

                        if (customerEditedConfirmation.equals("Success")) {
                            JOptionPane.showMessageDialog(null, "Calendar has been edited!",
                                    "Calendar", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Calendar has not been edited!",
                                    "Calendar", JOptionPane.INFORMATION_MESSAGE);
                        }


                        //Resetting all text fields
                        editCalendarTitleField.setText("");
                        editCalendarApptField.setText("");
                        editCalendarNewApptField.setText("");
                        editCalendarMaxField.setText("");
                        editCalendarApprovedBookingsField.setText("");
                        editCalendarNewStartTimeField.setText("");
                        editCalendarNewEndTimeField.setText("");

                        //Taking user back to main panel
                        content.removeAll();
                        frame.repaint();
                        content.setLayout(new GridLayout(2, 1));
                        content.add(sellerPanel);
                        frame.setSize(750, 400);
                        frame.setLocationRelativeTo(null);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        updateUI();
                        frame.setVisible(true);
                    }
                } else if (e.getSource() == editCalendarBackButton) {
                    importFileText.setText("");
                    content.removeAll();
                    frame.repaint();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(sellerPanel);
                    frame.setSize(750, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateUI();
                    frame.setVisible(true);
                } else if (e.getSource() == deleteCalendarProceedButton) {

                    boolean passCheck = false;

                    if (deleteCalendarTitleField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Calendar title needs to be " +
                                        "filled!",
                                "Calendar", JOptionPane.ERROR_MESSAGE);
                    } else {
                        passCheck = true;
                    }

                    if (passCheck) {
                        String data = storeNameText.getText() + ";4;" + deleteCalendarTitleField.getText();
                        if (sendDataToServer(sellerProceedButton, data).equals("Calendar deleted!")) {
                            JOptionPane.showMessageDialog(null, "Calendar has been deleted!",
                                    "Calendar", JOptionPane.INFORMATION_MESSAGE);

                            //Reset text fields
                            deleteCalendarTitleField.setText("");

                            //Take user back to main screen
                            content.removeAll();
                            frame.repaint();
                            content.setLayout(new GridLayout(2, 1));
                            content.add(sellerPanel);
                            frame.setSize(750, 400);
                            frame.setLocationRelativeTo(null);
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            updateUI();
                            frame.setVisible(true);

                        } else { //Calendar was unable to delete - display error message
                            JOptionPane.showMessageDialog(null, "Calendar was unable " +
                                            "to delete! " +
                                            "Please ensure the calendar you entered exists!",
                                    "Calendar", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else if (e.getSource() == deleteCalendarBackButton) {
                    content.removeAll();
                    frame.repaint();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(sellerPanel);
                    frame.setSize(750, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateUI();
                    frame.setVisible(true);
                } else if (e.getSource() == approveProceedButton) {


                    boolean passCheck = false;

                    if (selectApprovalCalendarField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Appointment needs to be filled!",
                                "Appointment", JOptionPane.ERROR_MESSAGE);
                    } else if (selectApprovalCalendarField.getText().split("-").length != 3 ||
                            customerCancelText.getText().split("-")[1].split(",").length != 5) {
                        JOptionPane.showMessageDialog(null, "Appointment needs to be in " +
                                        "the right format!",
                                "Appointment", JOptionPane.ERROR_MESSAGE);
                    } else {
                        passCheck = true;
                    }

                    if (passCheck) {

                    }
                    //after requests have been approved, next screen + confirmation message

                    //TODO
                } else if (e.getSource() == approveBackButton) {
                    content.removeAll();
                    frame.repaint();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(selectApprovalCalendarPanel);
                    frame.setSize(750, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateUI();
                    frame.setVisible(true);
                    //add all seller button options here!!!
                } else if (e.getSource() == sellerSortButton) {
                    viewSortOption = sellerSortOptions.getSelectedIndex();
                    if (viewSortOption == 0) {
                        viewSortOption = 1;
                    }
                    String tempSort = sendDataToServer(sellerSortButton, "7;" + viewSortOption);
                    sellerSortedStatsLabel.setText("<html>" + tempSort + "</html>");
                    content.removeAll();
                    frame.repaint();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(sellerSortedStatisticsPanel);
                    frame.setSize(1000, 800);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateUI();
                    frame.setVisible(true);
                    //show sorted dashboard here
                } else if (e.getSource() == sellerSortBackButton) {
                    content.removeAll();
                    frame.repaint();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(sellerPanel);
                    frame.setSize(750, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateUI();
                    frame.setVisible(true);
                } else if (e.getSource() == createCalendarBackButton) {
                    content.removeAll();
                    frame.repaint();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(sellerPanel);
                    frame.setSize(750, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateUI();
                    frame.setVisible(true);
                } else if (e.getSource() == sellerViewSortedStatsBackButton) {
                    content.removeAll();
                    frame.repaint();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(sellerPanel);
                    frame.setSize(750, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    updateUI();
                    frame.setVisible(true);
                } else if (e.getSource() == anotherCalendarBackButton) {
                    calendarNameText.setText("");
                    calendarDescriptionText.setText("");
                    content.removeAll(); //Clears the frame
                    content.setLayout(new BorderLayout());
                    content.add(createCalendarPanel);
                    frame.setSize(400, 200);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                }
            }
        };

        // Add action listeners to buttons
        createAccountButton.addActionListener(actionListener);
        createEnterButton.addActionListener(actionListener);
        loginButton.addActionListener(actionListener);
        loginEnterButton.addActionListener(actionListener);
        createBackButton.addActionListener(actionListener);
        loginBackButton.addActionListener(actionListener);
        customerProceedButton.addActionListener(actionListener);
        customerAppointmentBackButton.addActionListener(actionListener);
        customerCancelBackButton.addActionListener(actionListener);
        customerViewCalendarsBackButton.addActionListener(actionListener);
        customerViewApprovedAppointmentsBackButton.addActionListener(actionListener);
        customerViewAppointmentsAwaitingBackButton.addActionListener(actionListener);
        customerExportFileBackButton.addActionListener(actionListener);
        customerViewStatisticsBackButton.addActionListener(actionListener);
        importFileButton.addActionListener(actionListener);
        manualButton.addActionListener(actionListener);
        importButton.addActionListener(actionListener);
        addAppointmentButton.addActionListener(actionListener);
        createAppointmentButton.addActionListener(actionListener);
        sellerProceedButton.addActionListener(actionListener);
        createCalendarButton.addActionListener(actionListener);
        appointmentBackButton.addActionListener(actionListener);
        viewBackButton.addActionListener(actionListener);
        importBackButton.addActionListener(actionListener);
        customerAppointmentButton.addActionListener(actionListener);
        customerViewCancelButton.addActionListener(actionListener);
        customerCancelButton.addActionListener(actionListener);
        customerSortButton.addActionListener(actionListener);
        customerViewStatsBackButton.addActionListener(actionListener);
        customerExitButton.addActionListener(actionListener);
        customerExitButton.addActionListener(actionListener);
        selectApprovalProceedButton.addActionListener(actionListener);
        selectApprovalBackButton.addActionListener(actionListener);
        editCalendarProceedButton.addActionListener(actionListener);
        editCalendarBackButton.addActionListener(actionListener);
        deleteCalendarProceedButton.addActionListener(actionListener);
        deleteCalendarBackButton.addActionListener(actionListener);
        approveProceedButton.addActionListener(actionListener);
        approveBackButton.addActionListener(actionListener);
        createCalendarBackButton.addActionListener(actionListener);
        anotherCalendarBackButton.addActionListener(actionListener);
        sellerSortButton.addActionListener(actionListener);
        sellerSortBackButton.addActionListener(actionListener);
        sellerViewSortedStatsBackButton.addActionListener(actionListener);
    }
}
