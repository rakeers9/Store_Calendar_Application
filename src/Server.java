import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The Server class handles the Client requests and sends the requested information back to the Client. The Server
 * handles the creation of the users, calling the methods from the Seller/Customer class, and verifying the
 * appropriate information is sent.
 *
 * @author Christopher Lee and Sreekar Gudipati
 * @version December 2, 2023
 */

public class Server implements Runnable {
    private Socket socket;

    public Server(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream())
        ) {
            // Creates a new user (seller or customer).
            User user = new User();

            String email = "";
            String userType = "";

            String createAccount = bufferedReader.readLine(); // Receives input on creating or logging in to account.

            if (createAccount != null && createAccount.equals("true")) {
                boolean runAgain = true;

                while (runAgain) {
                    userType = bufferedReader.readLine(); // Client sends "Seller" or "Customer".
                    String name = bufferedReader.readLine(); // Name of the user.
                    email = bufferedReader.readLine(); // Email of the user.
                    String password = bufferedReader.readLine(); // Password of the user.
                    if (!user.usernameExists("userDatabase.txt", email)) {
                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("userDatabase.txt",
                                true)));

                        // Writes User info to userDatabase.txt file.
                        pw.println(userType + "," + name + "," + email + "," + password);
                        pw.flush();
                        pw.close();

                        writer.println("true"); // Sends "true" to Client if username is accepted.
                        writer.flush();
                        runAgain = false;
                    } else {
                        writer.println("false"); // Sends "false" to Client if username is not accepted.
                        writer.flush();
                    }
                }
            } else { // User login option.
                boolean runAgain = true;
                while (runAgain) {
                    userType = bufferedReader.readLine(); // Client sends "Client" or "Customer".
                    String name = bufferedReader.readLine(); // Name of the user.
                    email = bufferedReader.readLine(); // Email of the user.
                    String loginPassword = bufferedReader.readLine(); // Password of the user.

                    if (user.validator("userDatabase.txt", email, loginPassword) &&
                            user.checkUserType("userDatabase.txt", email, userType)) {
                        writer.println("true"); // Successful login.
                        writer.flush();

                        runAgain = false;
                    } else {
                        writer.println("false"); // Unsuccessful login.
                        writer.flush();
                    }
                }

                if (userType.equals("Seller")) {
                    String storeName = bufferedReader.readLine();

                    Seller seller = new Seller(email, storeName, email + ".txt");
                    ArrayList<String> temp = user.readFile("hotels.txt");
                    boolean add = true;
                    for (int i = 0; i < temp.size(); i++) {
                        if (temp.get(i).equals("error")) {
                            break;
                        }
                        if (temp.get(i).contains(email + ".txt")) {
                            add = false;
                            break;
                        }
                    }

                    if (add) {
                        seller.createHotel();
                    }
                    String choice = "";
                    boolean runMenuAgain = true;
                    while (runMenuAgain) {
                        choice = bufferedReader.readLine(); // Receives choice for action menu.

                        if (choice != null && choice.equals("1")) { // Handles view current calendars.
                            String currentCalendars = seller.printCalendar();

                            if (currentCalendars.isEmpty()) { // Handles case where no calendars were created.
                                writer.println("No current calendars!");
                                writer.flush();
                            } else {
                                writer.println(currentCalendars); // Sends current calendars to Client.
                                writer.flush();
                            }
                        } else if (choice != null && choice.equals("2")) { // Handles create new calendar.
                            String importChoice = bufferedReader.readLine(); // Indicates to create calendar with
                            // file or manually.
                            if (importChoice.equals("1")) { // Import file
                                String fileName = bufferedReader.readLine();
                                // Creates calendar with file.
                                writer.println(seller.createCalendarWithFile(
                                        System.getProperty("user.home") + "/Desktop/" + fileName));
                                writer.flush();
                            } else if (importChoice.equals("2")) { // Manually create file.
                                ArrayList<Appointment> apptList = new ArrayList<>(); // Holds the appointments.

                                String numAppointments = bufferedReader.readLine();
                                String calendarName = bufferedReader.readLine(); // Receives calendar name.
                                String description = bufferedReader.readLine(); // Receives calendar description.

                                for (int i = 0; i < Integer.parseInt(numAppointments); i++) {
                                    String appointmentTile = bufferedReader.readLine(); // Receives appointment title
                                    int maxAttendees = Integer.parseInt(bufferedReader.readLine()); // Client needs
                                    // to check if valid input before sending to Server.
                                    int approvedBookings = Integer.parseInt(bufferedReader.readLine()); // Client
                                    // needs to check if valid input before sending to Server.
                                    String startTime = bufferedReader.readLine(); // Receives start time.
                                    String endTime = bufferedReader.readLine(); // Receives end time.

                                    Appointment newAppt = new Appointment(appointmentTile, maxAttendees,
                                            approvedBookings, startTime, endTime);
                                    apptList.add(newAppt);
                                }
                                seller.createCalendar(calendarName, description, apptList); // Creates the new calendar.
                            }
                        } else if (choice != null && choice.equals("3")) { // Handles edit calendar.
                            writer.print(seller.printCalendar()); // Sends created calendars to client.
                            writer.println();
                            writer.flush();

                            String calendarName = bufferedReader.readLine();
                            String oldApptTitle = bufferedReader.readLine();
                            String apptTitle = bufferedReader.readLine();
                            int maxAttendee = Integer.parseInt(bufferedReader.readLine()); // Client needs to check
                            // if valid before sending to Server.
                            int approvedBookings = Integer.parseInt(bufferedReader.readLine()); // Client needs to
                            // check if valid before sending to Server.
                            String startTime = bufferedReader.readLine();
                            String endTime = bufferedReader.readLine();

                            Appointment editedAppt = new Appointment(apptTitle, maxAttendee, approvedBookings,
                                    startTime, endTime);
                            String temp1 = seller.editCalendar(calendarName, oldApptTitle, editedAppt.toString());
                            writer.println(temp1);
                            writer.flush();
                        } else if (choice != null && choice.equals("calendars")) { // Sends created calendars to client.
                            writer.println(seller.printCalendar());
                            writer.flush();
                        } else if (choice != null && choice.equals("4")) { // Handles delete calendar.
                            String deletedCalendarName = bufferedReader.readLine();
                            if (seller.deleteCalendar(deletedCalendarName).equals("Calendar deleted")) {
                                writer.println("Calendar deleted!");
                                writer.flush();
                            } else {
                                writer.println("Calendar not deleted!");
                                writer.flush();
                            }
                        } else if (choice != null && choice.equals("requests")) { // Sends pending appointment
                            // requests to Client.
                            writer.println(seller.getCustomerRequest()); // Sends message to Client.
                            writer.flush();
                        } else if (choice != null && choice.equals("5")) { // Handles approve/decline appointments.
                            String requestedAppointment = bufferedReader.readLine();
                            String action = bufferedReader.readLine();
                            String requestUsername = bufferedReader.readLine();
                            if (seller.handleCustomerRequests(requestedAppointment, requestUsername, action).
                                    equals("Approved")) {
                                writer.println("Appointment approved!");
                                writer.flush();
                            } else {
                                writer.println("Appointment declined!");
                                writer.flush();
                            }
                        } else if (choice != null && choice.equals("6")) { // Handles view currently approved
                            // appointments.
                            String approvedAppointments = seller.viewApprovedAppointments();
                            writer.println(approvedAppointments);
                            writer.flush();
                        } else if (choice != null && choice.equals("7")) { // Handles view statistics.
                            String sort = bufferedReader.readLine();
                            writer.println(seller.viewStatistics(sort));
                            writer.flush();
                        } else {
                            writer.println("You have successfully logged out.");
                            writer.flush();
                            runMenuAgain = false; // Exits the loop.
                        }
                    }
                } else {
                    boolean runner = true;

                    Customer customer = new Customer(email, email + ".txt");
                    String choice = "";
                    String response1 = "";

                    while (runner) {
                        choice = bufferedReader.readLine();

                        if (choice != null && choice.equals("calendars")) {
                            String viewCalendars = customer.viewCalendars();
                            writer.println(viewCalendars);
                            writer.flush();
                        }

                        if (choice != null && choice.equals("cancel")) {
                            response1 = (bufferedReader.readLine());

                            if (response1.equals("1")) {
                                writer.println(customer.viewAppointmentsWaitingApproval());
                                writer.flush();

                            } else if (response1.equals("2")) {
                                writer.println(customer.viewApprovedAppointments());
                                writer.flush();
                            }
                        }

                        if (choice != null && choice.equals("1")) {
                            String appointment = bufferedReader.readLine();
                            String message = customer.makeAppointment(appointment);

                            writer.println(message);
                            writer.flush();

                        } else if (choice != null && choice.equals("2")) {
                            String appointment = bufferedReader.readLine();
                            String response = bufferedReader.readLine();

                            boolean cancelled = customer.cancelAppointment(appointment, Integer.parseInt(response));
                            String message = "";
                            if (cancelled) {
                                message = "Appointment cancelled successfully.";
                            } else {
                                message = "Appointment cancellation failed. " +
                                        "Sorry, not appointment.";
                            }

                            writer.println(message);
                            writer.flush();

                        } else if (choice != null && choice.equals("3")) {
                            String message = customer.viewCalendars();
                            writer.println(message);
                            writer.flush();
                        } else if (choice != null && choice.equals("4")) {
                            String message = customer.viewApprovedAppointments();
                            writer.println(message);
                            writer.flush();
                        } else if (choice != null && choice.equals("5")) {
                            String message = customer.viewAppointmentsWaitingApproval();
                            writer.println(message);
                            writer.flush();
                        } else if (choice != null && choice.equals("6")) {
                            String message = "";
                            int yesNo = Integer.parseInt(bufferedReader.readLine());

                            if (yesNo == 1) {
                                message = customer.viewDashboard(yesNo);
                            } else {
                                message = customer.viewDashboard(2);
                            }

                            writer.println(message);
                            writer.flush();
                        } else if (choice != null && choice.equals("7")) {
                            String outFile = (email + ".txt");
                            String message = "";
                            try (BufferedReader reader = new BufferedReader(new FileReader(outFile))) {
                                String desktop = System.getProperty("user.home") + "/Desktop/";
                                String exportPath = desktop + outFile;
                                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(exportPath))) {
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        bufferedWriter.write(line);
                                        bufferedWriter.newLine();
                                    }
                                    message = "Your file has been exported! Please check your desktop to view the " +
                                            "text file.";

                                    writer.println(message);
                                    writer.flush();
                                } catch (IOException e) {
                                    message = "unsuccessful";
                                    writer.println(message);
                                    writer.flush();
                                }
                            } catch (IOException e) {
                                message = "unsuccessful";
                                writer.println(message);
                                writer.flush();
                            }
                        } else if (choice != null && choice.equals("8")) {
                            writer.println("Thank you for using our app.");
                            writer.flush();
                            runner = false;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8008);

        while (true) { // Server is in a continuous loop.
            Socket socket = serverSocket.accept();
            Server server = new Server(socket);
            new Thread(server).start();
        }
    }
}