import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The customer class extends the User class and uses methods in the Calendar and Appointment classes to handle all
 * customer-side calendar/appointment functionality and customer requests.
 * <p>
 * CORE:
 * Customers can view all the created calendars for stores.
 * Customers can make or cancel appointment requests.
 * Customers can view a list of their currently approved appointments, and the appointments waiting for approval.
 * <p>
 * SELECTIONS:
 * Customers can view a dashboard with store and seller information.
 * Data will include a list of stores by number of customers and the most popular appointment windows by store.
 * Customers can choose to sort the dashboard.
 *
 * @author Sreekar Gudipati
 * @version December 9, 2023
 */


public class Customer extends User {
    private String userName;
    private String fileName;

    //Customer constructor
    public Customer(String userName, String fileName) {
        this.userName = userName;
        this.fileName = fileName;
    }

    //Adds the customer's data to the customer database.
    public void createCustomer() {
        String customer = userName + "," + fileName;
        FileWriter writer = null;
        try {
            writer = new FileWriter("customerDatabase.txt", true);
            writer.write(customer + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Goes through all the hotel's calendars and prints them out for the customer to see.
    public String viewCalendars() {
        String calendars = "";
        ArrayList<String> sellerCalendars = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("hotels.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String user = bufferedReader.readLine();

            while (user != null) {
                sellerCalendars.add(user);
                user = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            return "No appointments made by the Seller.";
        }
        for (int i = 0; i < sellerCalendars.size(); i++) {
            String[] calendarFile = sellerCalendars.get(i).split(",");
            ArrayList<String> sellerFile = readFile(calendarFile[1]);

            for (int j = 0; j < sellerFile.size(); j++) {
                calendars += sellerFile.get(j) + "<br/> <br/>";
            }
        }
        return calendars;
    }

    //Takes the appointment as parameter, checks if the appointment exists in the hotel,
    //and requests it for the customer.
    public String makeAppointment(String appointment) {
        String appointmentList = viewCalendars();
        if (appointmentList.contains(appointment)) {
            FileWriter writer = null;
            try {
                writer = new FileWriter("awaitingApproval.txt", true);
                writer.write(appointment + "-" + userName + "\n");
                writer.close();
                return "Appointment request made.";
            } catch (Exception e) {
                return "ERROR";
            }
        } else {
            return "Sorry, not appointment.";
        }
    }

    //Checks through the customer's file and returns all the approved appointments.
    public String viewApprovedAppointments() {
        String appointment = "";
        ArrayList<String> userInformation = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(userName + ".txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String user = bufferedReader.readLine();

            while (user != null) {
                userInformation.add(user);
                user = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            return ("No calendar appointments have been approved!");
        }

        for (int i = 0; i < userInformation.size(); i++) {
            appointment += userInformation.get(i) + "<br> </br>";
        }
        return appointment;
    }

    //Checks through the awaitingApproval.txt file and returns all appointments belonging to the customer.
    public String viewAppointmentsWaitingApproval() {
        String appointment = "";

        ArrayList<String> lines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("awaitingApproval.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String user = bufferedReader.readLine();

            while (user != null) {
                lines.add(user);
                user = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            return "No appointments made by the Seller.";
        }

        for (int i = 0; i < lines.size(); i++) {
            String[] splitter = lines.get(i).split("-");
            if (splitter[2].equals(userName)) {
                appointment += lines.get(i) + "<br> </br>";
            }
        }

        if (appointment.length() == 0) {
            appointment = "No pending appointments.";
        }

        return appointment;
    }

    //Takes an input for whether the customer wants to cancel an approved or unapproved appointment,
    //and removes it from the file, and adds it to the cancelledAppointments.txt for the seller to cancel.
    public boolean cancelAppointment(String appointment, int response) {
        ArrayList<String> temp = new ArrayList<>();
        if (response == 1) { //awaiting approvals
            ArrayList<String> lines = readFile("awaitingApproval.txt");
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).equals(appointment)) {
                    found = true;
                    break;
                } else {
                    temp.add(lines.get(i));
                }
            }

            FileWriter writer = null;
            try {
                writer = new FileWriter("awaitingApproval.txt", false);
                for (int j = 0; j < temp.size(); j++) {
                    writer.write(temp.get(j) + "\n");
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return found;
        } else if (response == 2) { //customer file
            ArrayList<String> lines = readFile(userName + ".txt");
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).equals(appointment)) {
                    lines.remove(i);
                    found = true;
                    FileWriter writer = null;
                    try {
                        writer = new FileWriter(userName + ".txt");
                        for (int j = 0; j < lines.size(); j++) {
                            writer.write(lines.get(j) + "\n");
                        }
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        writer = new FileWriter("cancelledAppointments.txt", true);
                        writer.write(appointment + "-" + userName);
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return found;

        } else {
            System.out.println("Sorry, invalid response.");
        }
        return false;
    }

    //Goes through all the stores and sorts them depending on the parameter passed through,
    //and returns it for the customer to view.
    public String viewDashboard(int sort) {
        String dashboard = "";
        ArrayList<String> storeFiles = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader("hotels.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String user = bufferedReader.readLine();

            while (user != null) {
                storeFiles.add(user);
                user = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            return "No appointments made by the Seller.";
        }


        ArrayList<String> sellers = new ArrayList<String>();


        for (String storeFile : storeFiles) {
            String[] storeSplit = storeFile.split(",");
            ArrayList<String> sellerFile = readFile(storeSplit[1]);


            for (String s : sellerFile) {
                if (s.contains("Store name:")) {
                    sellers.add(s);
                }


                if (s.contains("-") && s.contains(",")) {
                    if (!s.contains("Appointment List:")) {
                        sellers.add(s);
                    }
                }
            }
        }


        ArrayList<String> temp = new ArrayList<>();
        if (sort == 1) { // Most to least
            for (String seller : sellers) {
                if (seller.contains("Store name:")) {
                    dashboard += seller + "<br> </br>";
                } else {
                    String[] dash = seller.split("-");
                    String[] comma = dash[1].split(",");
                    String approvedBooking = comma[2];
                    if (approvedBooking.equals("1")) {
                        dashboard += seller + "<br> </br>";
                    } else {
                        temp.add(seller);
                    }
                }
            }


            for (String s : temp) {
                dashboard += s + "<br> </br>";
            }
        } else if (sort == 2) { // Least to most
            for (String seller : sellers) {
                if (seller.contains("Store name:")) {
                    dashboard += seller + "<br> </br>";
                } else {
                    String[] dash = seller.split("-");
                    String[] comma = dash[1].split(",");
                    String approvedBooking = comma[2];
                    if (approvedBooking.equals("0")) {
                        dashboard += seller + "<br> </br>";
                    } else {
                        temp.add(seller);
                    }
                }
            }


            for (String s : temp) {
                dashboard += s + "<br> </br>";
            }
        }
        return dashboard;
    }
}