import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * CORE:
 * The Seller class handles all the calendar/appointment functionality and customer requests. Sellers can create,
 * edit, and delete calendars for each of their stores. This includes modifying the title of an appointment,
 * the maximum number of attendees, current number of approved bookings, and start and end time.
 * <p>
 * Sellers can approve or decline customer requests. Sellers will have access to all the customer appointment
 * requests, and they will be approved/decline depending on the availability. Sellers can also view the
 * appointments they approved for a customer.
 * <p>
 * SELECTIONS:
 * Sellers can import a csv file with a calendar and the program will automatically create a calendar and a
 * corresponding appointment list. This new calendar will appear in the personal .txt file that the program
 * creates for each Seller.
 * <p>
 * Sellers can also view a dashboard that lists out the statistics related to their stores. Sellers can view the
 * appointments that were approved and sort the most popular appointment windows by store.
 *
 * @author Christopher Lee
 * @version December 9, 2023
 */

public class Seller extends User {
    private String username;
    private String storeName;
    private String fileName;

    // Parameterized constructor.
    public Seller(String username, String storeName, String fileName) {
        this.username = username; // Instantiates username to parameter.
        this.storeName = storeName; // Instantiates store name to parameter.
        this.fileName = fileName; // Instantiates file name to parameter.
    }

    /**
     * Method creates a new txt file "hotels.txt" that saves the username and unique file name for
     * each Seller.
     */
    public void createHotel() {
        String seller = username + "," + fileName;
        FileWriter writer = null;
        try {
            writer = new FileWriter("hotels.txt", true);
            writer.write(seller + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method allows the Seller to create a calendar and appointment list by importing a csv file. The
     * calendar/appointment list will appear in the Seller's unique txt file. If the Seller has multiple appointments,
     * they would keep adding to the same line in the txt file.
     * <p>
     * If the Seller wants to add multiple stores/calendars, they would add another line in the txt file in the
     * specified format.
     * <p>
     * FORMAT OF IMPORTED CSV:
     * Store Name,Calendar name,Calendar description,List of Appointments(Appointment Title,Max Attendees,Approved
     * Bookings,Start Time,End Time)
     *
     * @param importFileName file path to Seller CSV file.
     */
    public String createCalendarWithFile(String importFileName) {
        ArrayList<String> userInformation = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(importFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String user = bufferedReader.readLine();

            while (user != null) {
                userInformation.add(user);
                user = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            return "Unsuccessful";
        }

        ArrayList<String> createCalendar = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        for (int i = 0; i < userInformation.size(); i++) {
            String[] sellerInput = userInformation.get(i).split(",");
            createCalendar.add("Store name:" + sellerInput[0] + "\n");
            createCalendar.add("Calendar name:" + sellerInput[1] + "\n");
            createCalendar.add("Description:" + sellerInput[2] + "\n");
            createCalendar.add("Appointment List: (Calendar name-Appointment Title,Max Attendees,Approved Bookings,"
                    + "Start Time,End Time)" + "\n");

            for (int j = 3; j < sellerInput.length; j += 5) {
                if (j + 4 < sellerInput.length) {
                    Appointment appointment = new Appointment(sellerInput[j], Integer.parseInt(sellerInput[j + 1]),
                            Integer.parseInt(sellerInput[j + 2]), sellerInput[j + 3], sellerInput[j + 4]);
                    createCalendar.add(sellerInput[1] + "-" + appointment + "\n");
                }
            }
        }
        createCalendar.add("Edited: " + formatter.format(date) + "\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (String line : createCalendar) {
                writer.write(line);
            }
            return "Successful";
        } catch (IOException e) {
            return "Unsuccessful";
        }
    }

    /**
     * Method creates a calendar based on the user inputs.
     *
     * @param calendarName        user-inputted.
     * @param calendarDescription user-inputted.
     * @param appointments        user-inputted.
     */
    public void createCalendar(String calendarName, String calendarDescription,
                               ArrayList<Appointment> appointments) {
        FileOutputStream fileOutputStream = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();

        try {
            fileOutputStream = new FileOutputStream(fileName, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileOutputStream);
        String sellerCalendar = "";

        sellerCalendar += "Store name:" + storeName + "\n";
        sellerCalendar += "Calendar name:" + calendarName + "\n";
        sellerCalendar += "Description:" + calendarDescription + "\n";
        sellerCalendar += "Appointment List: (Calendar name-Appointment Title,Max Attendees,Approved Bookings,"
                + "Start Time,End Time)" + "\n";

        for (int i = 0; i < appointments.size(); i++) {
            sellerCalendar += calendarName + "-" + appointments.get(i).toString() + "\n";
        }

        sellerCalendar += "Edited: " + formatter.format(date);
        sellerCalendar += "\n" + "\n";

        printWriter.print(sellerCalendar);
        printWriter.flush();
        printWriter.close();
    }

    /**
     * Method prints the Seller's calendar.
     *
     * @return String representing a Seller's calendar.
     */
    public String printCalendar() {
        if (handleCancel()) {
            updateCancelRequest();
        }

        ArrayList<String> calendar = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String user = bufferedReader.readLine();

            while (user != null) {
                calendar.add(user);
                user = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            return "";
        }

        String list = "";
        for (int i = 0; i < calendar.size(); i++) {
            list += calendar.get(i) + "<br> </br>";
        }
        return list;
    }

    /**
     * Method allows the Seller to edit a calendar of their store. They will be prompted to enter the calendar
     * name and appointment title of the appointment they want to edit. They then will be asked the updated
     * appointment.
     *
     * @param calendarName       represents the calendar the Seller is editing.
     * @param appointmentTitle   represents the appointment the Seller is editing.
     * @param updatedAppointment represents the new appointment for a Seller.
     */
    public String editCalendar(String calendarName, String appointmentTitle, String updatedAppointment) {
        ArrayList<String> calendarFile = readFile(fileName);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        String list = "";
        String appointmentDetails = "";
        boolean edited = false;

        for (int i = 0; i < calendarFile.size(); i++) {
            list += calendarFile.get(i) + "\n";
        }

        for (int i = 0; i < calendarFile.size(); i++) {
            if (calendarFile.get(i).contains("-") || calendarFile.get(i).contains(",")) {
                String[] temp = calendarFile.get(i).split("-");
                String[] appointmentString = temp[1].split(",");
                if (temp[0].equals(calendarName) && appointmentString[0].equals(appointmentTitle)) {
                    appointmentDetails = calendarFile.get(i);
                    edited = true;
                    break;
                }
            }
        }

        if (edited) {
            String newAppointment = list.replaceAll(appointmentDetails, calendarName + "-" +
                    updatedAppointment);
            newAppointment += "Edited: " + formatter.format(date);

            FileWriter writer = null;
            try {
                writer = new FileWriter(fileName);
                writer.write(newAppointment);
                writer.close();
                return "Success";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Unsuccessful";
    }

    /**
     * Method allows the Seller to delete one of their calendars. The Seller must specify the name of the calendar
     * they want to delete.
     *
     * @param calendarName represents the calendar to be deleted.
     */
    public String deleteCalendar(String calendarName) {
        ArrayList<String> calendarFile = readFile(fileName);
        ArrayList<String> newCalendarFile = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        boolean deleteMode = false;
        String message = "Calendar not deleted";

        for (String line : calendarFile) {
            if (line.equals("Calendar name:" + calendarName)) {
                deleteMode = true;
                message = "Calendar deleted";
            } else if (deleteMode && line.equals("\n")) {
                // Stop deleting when a new line is encountered
                deleteMode = false;
            } else if (!deleteMode) {
                // Add the lines to be kept
                newCalendarFile.add(line);
            }
        }
        newCalendarFile.add("Edited: " + formatter.format(date) + "\n");

        // Write the new content back to the file
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String line : newCalendarFile) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * Compiles the customer appointment requests into a String.
     *
     * @return String that represents all customers.
     */
    public String getCustomerRequest() {
        ArrayList<String> customerRequest = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("awaitingApproval.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String user = bufferedReader.readLine();

            while (user != null) {
                customerRequest.add(user);
                user = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            return "No appointment requests";
        }

        if (customerRequest.isEmpty()) {
            return "No appointment requests";
        }

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String user = bufferedReader.readLine();

            while (user != null) {
                temp.add(user);
                user = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            return "No appointment requests";
        }

        String customers = "";

        for (int i = 0; i < customerRequest.size(); i++) {
            String[] customerArr = customerRequest.get(i).split("-");
            for (int j = 0; j < temp.size(); j++) {
                if ((customerArr[0] + "-" + customerArr[1]).equals(temp.get(j))) {
                    if (!customers.contains(customerArr[0] + "-" + customerArr[1])) {
                        customers += customerRequest.get(i) + "<br> </br>";
                    }
                }
            }
        }
        return customers;
    }

    /**
     * Method allows Seller to handle customer requests. If the appointment is successfully approved,
     * the approved bookings field in the appointment info description will increase to 1. Seller must enter the
     * appointment title they want to handle.
     *
     * @param appointment      represents the appointment they want to handle.
     * @param customerUsername represents the customer who is making the appointment request.
     */
    public String handleCustomerRequests(String appointment, String customerUsername, String action) {
        ArrayList<String> calendarFile = readFile(fileName);
        ArrayList<String> customerRequest = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        boolean approved = false;
        String appointmentStatus = "";

        if (action.equals("1")) {
            for (int i = 0; i < calendarFile.size(); i++) {
                if (calendarFile.get(i).contains("-") && calendarFile.get(i).contains(",")) {
                    String[] formatAppointment = appointment.split("-");
                    if (calendarFile.get(i).equals(formatAppointment[0] + "-" + formatAppointment[1])) {
                        String[] dash = calendarFile.get(i).split("-");
                        String[] comma = dash[1].split(",");

                        String update = dash[0] + "-" + comma[0] + "," + comma[1] + "," + "1" + "," + comma[3] + ","
                                + comma[4];
                        customerRequest.add(appointment);
                        calendarFile.set(i, update);
                        approved = true;
                        appointmentStatus = "Approved";
                    }
                }
                if (approved) {
                    break;
                }
            }
        } else if (action.equals("2")) {
            appointmentStatus = "Declined";
            try (FileWriter writer = new FileWriter("awaitingApproval.txt", false)) {
                for (int i = 0; i < customerRequest.size(); i++) {
                    if (!customerRequest.get(i).equals(appointment)) {
                        writer.write(customerRequest.get(i) + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (approved) {
            try (FileWriter writer = new FileWriter(customerUsername + ".txt", false)) {
                for (String update : customerRequest) {
                    writer.write(update + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            calendarFile.add("Edited: " + formatter.format(date));

            try (FileWriter writer = new FileWriter("awaitingApproval.txt", false)) {
                for (int i = 0; i < customerRequest.size(); i++) {
                    if (!customerRequest.get(i).equals(appointment)) {
                        writer.write(customerRequest.get(i) + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            BufferedWriter writer1 = null;
            try {
                writer1 = new BufferedWriter(new FileWriter(fileName, false));
                for (String s : calendarFile) {
                    writer1.write(s + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer1 != null) {
                        writer1.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return appointmentStatus;
    }

    /**
     * Method allows the Seller to view a list of the appointments that were approved for their store.
     *
     * @return String representing the approved appointment list.
     */
    public String viewApprovedAppointments() {
        String appointments = "";
        ArrayList<String> calendarList = readFile(fileName);
        for (int i = 0; i < calendarList.size(); i++) {
            if (calendarList.get(i).split(",").length == 5 &&
                    !calendarList.get(i).contains("Appointment List:")) {
                String[] list = calendarList.get(i).split("-");
                if (list.length >= 2) {
                    String[] approvedAppointments = list[1].split(",");
                    if (approvedAppointments.length >= 3 && Integer.parseInt(approvedAppointments[2]) > 0) {
                        appointments += calendarList.get(i) + "\n";
                    }
                }
            }
        }
        if (appointments.isEmpty()) {
            return "No approved appointments";
        }
        return appointments;
    }

    /**
     * Method allows the Seller to view statistics for each of their stores. It also allows them to view
     * popular appointment windows for their stores.
     *
     * @param sortDashboard represents if the Seller wants to sort the appointment list of their stores.
     * @return String representing the statistics for the Seller's indicated store.
     */
    public String viewStatistics(String sortDashboard) {
        ArrayList<String> calendarList = readFile(fileName);
        String stats = "";
        String popularAppointments = "";
        String standardAppointments = "";

        stats += "Approved appointments: <br> </br>" + viewApprovedAppointments() + "<br> </br>";

        if (sortDashboard.equalsIgnoreCase("yes")) {
            for (int i = 0; i < calendarList.size(); i++) {
                if (calendarList.get(i).split(",").length == 5 &&
                        !calendarList.get(i).contains("Appointment List:")) {
                    String[] list = calendarList.get(i).split("-");

                    String[] approvedAppointments = list[1].split(",");
                    if (Integer.parseInt(approvedAppointments[2]) > 0) {
                        popularAppointments += "Popular: " + calendarList.get(i) + "<br> </br>";
                    } else {
                        standardAppointments += "Standard: " + calendarList.get(i) + "<br> </br>";
                    }
                }
            }
        }
        return stats + popularAppointments + standardAppointments;
    }

    /**
     * Method reads the cancelled appointments to determine if the calendar needs to be updated. The method will
     * remove the appointment from the Seller's appointment list.
     *
     * @return boolean representing if there are cancelled appointments.
     */
    public boolean handleCancel() {
        ArrayList<String> cancel = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("cancelledAppointments.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String user = bufferedReader.readLine();

            while (user != null) {
                cancel.add(user);
                user = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            return false;
        }

        if (cancel.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Method handles the cancel request by updating the Seller's calendar.
     */
    public void updateCancelRequest() {
        String cancelled = "";
        ArrayList<String> calendar = readFile(fileName);
        ArrayList<String> updateFile = readFile("cancelledAppointments.txt");
        ArrayList<String> newFile = new ArrayList<>();

        for (int i = 0; i < updateFile.size(); i++) {
            String[] user = updateFile.get(i).split("-");
            String[] temp = user[1].split(",");
            cancelled += user[0] + "-" + temp[0] + "," + temp[1] + "," + "1" + "," + temp[3] + "," + temp[4];
        }

        for (int i = 0; i < calendar.size(); i++) {
            if (!calendar.get(i).contains(cancelled)) {
                newFile.add(calendar.get(i));
            }
        }

        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (String appointments : newFile) {
                writer.write(appointments + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}