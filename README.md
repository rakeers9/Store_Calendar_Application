# CS180 Project 5 README
## Authors
- Audrey Lupton
- Sruthi Lingam
- Christopher Lee
- Sreekar Gudipati

## Overview
This program implements a marketplace calendar. The calendar allows for booking and managing appointments between
sellers and customers.

This program can be run by first compiling and running Server.java and then Client.java. To have multiple users, run
Client.java again, but Server.java MUST always be running. It is recommended to create a Seller user before creating a Customer user.

* Program Functionality
  * The program saves all application data once the User exit and logs out of the program. 
  * The program achieves concurrency through the usage of threads, which allow the ability to handle multiple Users at once.
  * The program allows for Users to receive content updates as other Users make changes to the shared information. In order 
  to see the update, the User MUST navigate to a different page in their action ceneter then back to their original page. For
example, if the User is currently in the "View Store Calendars" option, they must go to a different page ("View statistics" or "View Pending Appointments", etc)
and then back to the "View Store Calendars" page.

Christopher Lee will submit the repository on Vocareum workspace, the project report, and the video presentation on Brightspace.

## Errors fixed from Project 4
* All invalid inputs are handled within the GUI with an appropriate error pop-up message.
* The Seller approve/decline appointment requests allow the Seller to choose whether to approve or decline an appointment.
It also asks for the Customer information before asking for the appointment.
* The deleted calendars are deleted from the Seller's view. If the Seller has one calendar, only the Seller's name, 
store name, and a time-stamped edit message will remain in the Seller's view once it has been deleted. 

## Client.java
The Client class handles the creation of the complex GUI for both the Seller and Customer and sending the appropriate
information to the Server class. For each Seller and Customer action options, a unique GUI will be displayed with
either fields to enter information or user-requested information. For every button click in the GUI, the Client
class will send the data to the Server class, where it is appropriately handled, and receive the requested information
from the server.

### Seller Options
1. View current calendars
2. Create new calendar
3. Edit calendar
4. Delete calendar
5. Approve/decline appointment requests
6. View currently approved appointments
7. View statistics
8. Logout

Option 1: The program prints out the seller's calendar

Option 2: The user can either choose to create a calendar in two ways:
1. Via file - The program first prints out an example of the file format, then asks the user for the filename, and imports the calendar within that file to the seller's unique seller file
2. Manually - The program asks the user for the calendar name and description. The user can then input any amount of appointments followings this format: appointment title, max amount of attendees, number of approved bookings (program will ensure the user inputs either a 0 or 1), start time, and end time.

Option 3: The program first prints out the seller's calendar. Then the program asks the user for the name of the calendar and title of the appointment they would like to edit. Then they are asked to input the new appointment information, including the new appointment title, max amount of attendees, number of approved bookings (program will ensure the user inputs either a 0 or 1), start time, and end time. The program will then edit the calendar as such.

Option 4: The program first prints out the seller's calendar. Then the program asks for the name of the calendar they would like to delete. The calendar is then deleted.

Option 5: If there are no appointments requests, the program will print out "No appointment requests". If there are appointment requests, the program will print the appointment requests and ask the user to input the appointment exactly as it appears in the appointment list printed above. The user will then be prompted to approve or decline the appointment request.

Option 6: The program prints out all currently approved appointments.

Option 7: The program first asks the user if they would like to sort the appointments by most popular windows, and then the program will return a list of the approved appointments depending on their answer to the previous question.

Option 8: The program logs the user out and they will be greeted with the starting menu.

### Customer Options
1. Make an appointment request
2. Cancel an appointment request
3. View store calendars
4. View currently approved appointments
5. View pending appointments
6. View store statistics
7. Export a file of your approved appointments
8. Exit and log out

Option 1: The user can create an appointment request by inputting a string of the appointment title. If the appointment is found and has space, the request will be made.

Option 2: The user can cancel an appointment by first choosing from a category of appointment requests: appointments awaiting approval or appoinments approved. Next, the user can input a string of the appointment title. If the appointment can be cancelled, the user is shown a confirmation message.

Option 3: The program prints out all store calendars available.

Option 4: If the user's appointment requests have been approved by the corresponding seller, the appointment and customer name will be added to the currently approved appointments file.

Option 5: All pending appointment requests that haven't been reviewed by the seller will be printed out.

Option 6: The user can view store statistics and choose to sort the dashboard as well. They can choose to sort by most to least popular sellers or least to most popular sellers. Alternatively, they can opt out of the sort function and a default version will be printed.

Option 7: A copy of the user's approved appointments will be exported to their desktop. If there are no approved appointments currently available, the program will export a blank text file.

Both the seller and customer users can execute one option at a time. After completing an option, the user can choose to select a different option or log out and quit the program.

### Sending Data To Server

The client class uses the method sendDataToServer, which connects the client to the server for each individual action and sends data to be processed and returns output 
if applicable. The parameters of this method include a JButton and a String of the data: (JButton button, String data). 
The button is the name of the button clicked, which includes the create account, login, customer proceed, and seller proceed button.
Once a user selects an option and clicks the proceed button, the user will be taken to a panel walking them through the option they chose. Once they select the final button
on that panel to submit their data, the program calls the sendDataToServer method with either the customerProceedButton or
sellerProceedButton as the first parameter, and the String of data, which varies from option to option but will overall
be multiple pieces of information separated by a comma, as the second parameter.

## Appointment.java
The Appointment class processes the name of the appointment, max number of slots available, current number of approved bookings, and the start and end times of the appointment.

## Customer.java
The customer class extends the User class and uses methods in the Calendar and Appointment classes
to handle all customer-side calendar/appointment functionality and customer requests.
* CORE:
    * Customers can view all the created calendars for stores.
    * Customers can make or cancel appointment requests.
    * Customers can view a list of their currently approved appointments, and the appointments waiting for approval.

* SELECTIONS:
    * Customers can view a dashboard with store and seller information.
    * Data will include a list of stores by number of customers and the most popular appointment windows by store.
    * Customers can choose to sort the dashboard.

## Seller.java
The seller class extends the User class and uses methods in the Calendar and Appointment classes to handle all the seller-side calendar/appointment functionality and customer requests.
* CORE:
    * Sellers can create, edit, and delete calendars for each of their stores. This includes modifying the title of an appointment, the maximum number of attendees, current number of approved bookings, and start and end time.
    * Sellers can approve or decline customer requests. Sellers will have access to all the customer appointment
      requests, and they will be approved/decline depending on the availability.
    * Sellers can also view the appointments they approved for a customer.


* SELECTIONS:
    * Sellers can import a csv file with a calendar and the program will automatically create a calendar and a  corresponding appointment list. This new calendar will appear in the personal .txt file that the program creates for each Seller.
    * Sellers can also view a dashboard that lists out the statistics related to their stores. Sellers can view the appointments that were approved and sort the most popular appointment windows of their stores.

## Server.java
The Server class implements the Runnable interface, and runs all its functionality inside the run() method.

Each thread begins with a validation of the user's login attempt until it's successful, followed by a separate while loop depending on
whether the user is a customer or a seller. Once in a while loop, the server receives the action the user has chosen through a number choice,
and the server performs the processing required for this choice and sends back the required data or message.

The thread runs until the user chooses to logout.

## User.java
The User class processes a user's login credentials and verifies whether they match with existing account information.

## Tests.md
This file contains test cases that simulate user interactions with our program. Each test case is labeled and numbered to ensure readability.

## .txt Files
* The program creates a .txt file called "userDatabase.txt" that stores if the User is a seller/customer, the User's name, username, and password. This allows the User to logout of their account and log back in.
    * Format of userDatabase.txt: seller/customer name,username,password.
* The program creates a .txt file for each User (Sellers and Customers) and they are named based on the User's username. For example if the
  User's username was "sally", then the .txt file for that User will be called "sally.txt".
    * If the User is a Seller, their .txt file will contain all of their store calendars and appointment windows.
        * Format of Seller .txt file:
            * Store name:
            * Calendar name:
            * Description:
            * Appointment List: (Calendar name-Appointment Title,Max Attendees,Approved Bookings,Start Time,End Time)
            * [List of appointments in the above format]
            * Edited:[Time Stamp]

    * If the User is a Customer, their .txt file will contain their appointment requests that were approved by the Seller.
        * Format of Customer .txt file:
            * [List of appointments in same format as Customer]-Customer username
* The program creates a .txt file called "hotel.txt" that contains every Seller's username and .txt file name. This file allows Customers to view the available appointment windows.
    * Format of hotel.txt: Seller username,Seller file name
* The program creates a .txt file called "customerDatabase.txt" that contains every Customer's username and .txt file name. This allows the Seller to access Customers and update their appointment request.
    * Format of customerDatabase.txt: customerUsername,customerFileName
* The program creates a .txt file called "awaitingApprovals.txt" that contains appointment requests by every Customer. This allows the Seller to approve/decline appointments.
    * Format of awaitingApprovals.txt: Calendar name-Appointment Info-Customer username
