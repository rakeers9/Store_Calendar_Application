# Test Cases

* Account Creation Tests
## Test 1: User Account Creation - Seller
### Steps:

User launches the application.
User clicks on the "Create Account" text box.
User selects the "Seller" option box.
User enters their name, email, and password via the keyboard.
User selects the "Enter" textbox.
### Expected result:

All necessary information (name, email, password) is filled.
No duplicate emails or passwords exist in the database.
Upon successful validation:
The application navigates the user back to the home page.
Test Status: Passed.

## Test 2: User Account Creation - Customer
### Steps:

User launches the application.
User clicks on the "Create Account" text box.
User selects the "Customer" option box.
User enters their name, email, and password via the keyboard.
User selects the "Enter" textbox.
### Expected result:

All necessary information (name, email, password) is filled.
No duplicate emails or passwords exist in the database.
Upon successful validation:
The application navigates the user back to the home page.
Test Status: Passed.

## Test 3: Navigation from Create Account to Home Page
### Steps:

User launches the application.
User selects the "Create Account" textbox.
User selects the "Go Back" textbox.
### Expected result:

The application is launched successfully.
The application navigates to the Create Account page.
Upon selecting "Go Back," the application returns to the home page.
Test Status: Passed.


* Seller Tests
## Test 4: Seller Login and Redirect to Seller Home Page
### Steps:

User opens the application.
User selects the login textbox.
User selects the Seller option box.
User fills valid information into the name, email, and password textboxes.
User selects the "Enter" textbox.
User selects the "OK" textbox on the login successful popup.
### Expected result:

The application loads the home page.
On selecting the login textbox, the application navigates to the login page.
After entering valid information, the application verifies the user's existence in the database.
A popup confirms successful login.
The application redirects to the seller's home page.
Test Status: Passed.

## Test 5: Seller Store Calendar Navigation
### Steps:

User enters the seller home page.
User fills in the appropriate store name in the textbox via the keyboard.
User selects "View Current Calendars" from the dropdown option box.
User selects the "Proceed" textbox.
User selects the "Go Back" textbox.
### Expected result:

Upon entering the seller home page, the user interface is loaded successfully.
The user enters the appropriate store name in the textbox.
The dropdown menu displays all available options upon selection.
On pressing "Proceed," the calendars are listed.
Upon selecting "Go Back," the user returns to the previous window displaying the store name and the dropdown menu.
Test Status: Passed.

## Test 6: Seller Calendar Creation and Import
### Steps:

User enters the seller home page.
User fills in the appropriate store name in the textbox using the keyboard.
User selects "Create New Calendar" from the dropdown option box.
User selects the "Proceed" textbox.
User selects "Import File" textbox.
User enters the filename using the keyboard and selects the "Import" textbox.
User presses the "OK" textbox.
### Expected result:

The seller home page loads successfully.
User inputs the appropriate store name in the textbox via the keyboard.
"Create new calendar" is selected from the dropdown menu.
On selecting "Proceed," a page with three textboxes is displayed.
Choosing "Import File" loads the import page.
Upon selecting "Import," a success message popup is displayed.
On pressing "OK," the application returns to the seller home page.
Test Status: Passed.


## Test 7: Seller Calendar Creation - Manual Input and Appointment Addition
### Steps:

User enters the seller home page.
User fills in the appropriate store name in the textbox using the keyboard.
User selects "Create New Calendar" from the dropdown option box.
User selects the "Proceed" textbox.
User selects "Manually Create Calendar" textbox.
User enters the calendar name and description via the keyboard.
User selects "Add Appointment" textbox.
User fills all five fields appropriately and selects "Create Appointment" textbox.
User selects "Create Calendar" textbox.
User selects the "OK" textbox.
### Expected result:

The seller home page loads successfully.
User inputs the appropriate store name in the textbox using the keyboard.
"Create New Calendar" is selected from the dropdown menu.
On selecting "Proceed," a page with three textboxes is displayed.
Upon selecting "Manually Create Calendar," the manual calendar creation page opens.
Selecting "Add Appointment" loads the appointment creation page.
After adding the appointment, the program returns to the manual calendar page and displays the new appointment.
On selecting "Create Calendar," a popup confirms successful calendar creation.
Pressing "OK" navigates the application back to the seller home page.
Test Status: Passed.


## Test 8: Seller Calendar Editing
### Steps:

User enters the seller home page.
User fills in the appropriate store name in the textbox using the keyboard.
User selects "Edit Calendar" from the dropdown option box.
User presses the "Proceed" textbox.
User fills all seven fields appropriately using the keyboard.
User selects the "Proceed" textbox.
User selects the "OK" textbox.
### Expected result:

The seller home page loads successfully.
User inputs the appropriate store name in the textbox using the keyboard.
"Edit Calendar" is selected from the dropdown menu.
On selecting "Proceed," the application opens the edit calendar page.
After proceeding, a success message popup is displayed.
Selecting "OK" navigates the application back to the seller home page.
Test Status: Passed.


## Test 9: Seller Calendar Deletion
### Steps:

User enters the seller home page.
User fills in the appropriate store name in the textbox using the keyboard.
User selects "Delete Calendar" from the dropdown option box.
User presses the "Proceed" textbox.
User fills the calendar name appropriately via keyboard.
User selects the "Proceed" textbox.
User selects the "OK" textbox.
### Expected result:

The seller home page loads successfully.
User inputs the appropriate store name in the textbox using the keyboard.
"Delete Calendar" is selected from the dropdown menu.
On selecting "Proceed," the application opens the delete calendar page displaying all calendars.
After proceeding, a success message popup confirms the deletion.
Selecting "OK" navigates the application back to the seller home page.
Test Status: Passed.


## Test 10: Seller Appointment Approval
### Steps:

User enters the seller home page.
User fills in the appropriate store name in the textbox using the keyboard.
User selects "Approve/decline appointment requests" from the dropdown option box.
User selects "Proceed" textbox.
User fills fields appropriately using the keyboard.
User selects "Proceed" textbox.
User selects "Approve appointment" from the dropdown option box.
User selects "Proceed" textbox.
User selects the "OK" textbox.
### Expected result:

The seller home page loads successfully.
User inputs the appropriate store name in the textbox using the keyboard.
"Approve/decline appointment requests" is selected from the dropdown menu.
On selecting "Proceed," the application opens the appointment approval page displaying appointment requests, fields for approval, and a dropdown for selections.
After proceeding, a success message popup confirms the approval.
Selecting "OK" navigates the application back to the seller home page.
Test Status: Passed.


## Test 11: Seller Statistics - Appointments Sorted Most to Least
### Steps:

User enters the seller home page.
User fills in the appropriate store name in the textbox using the keyboard.
User selects "View statistics" from the dropdown option box.
User selects "Proceed" textbox.
User selects "Most to Least Appointments" textbox.
User selects "Proceed" textbox.
User selects "Go back" textbox.
### Expected result:

The seller home page loads successfully.
User inputs the appropriate store name in the textbox using the keyboard.
"View statistics" is selected from the dropdown menu.
On selecting "Proceed," the application opens the sort selection page displaying sorting options.
After selecting the sorting option and proceeding, the statistics page displays appointments in descending order of popularity.
Selecting "Go back" navigates the application back to the seller home page.
Test Status: Passed.


## Test 12: Seller Statistics - Appointments Sorted Least to Most
### Steps:

User enters the seller home page.
User fills in the appropriate store name in the textbox using the keyboard.
User selects "View statistics" from the dropdown option box.
User selects "Proceed" textbox.
User selects "Least to Most Appointments" textbox.
User selects "Proceed" textbox.
User selects "Go back" textbox.
### Expected result:

The seller home page loads successfully.
User inputs the appropriate store name in the textbox using the keyboard.
"View statistics" is selected from the dropdown menu.
On selecting "Proceed," the application opens the sort selection page displaying sorting options.
After selecting the sorting option and proceeding, the statistics page displays appointments in ascending order of popularity.
Selecting "Go back" navigates the application back to the seller home page.
Test Status: Passed.

## Test 13: Seller Logout
### Steps:

User enters the seller home page.
User fills in the appropriate store name in the textbox using the keyboard.
User selects "Exit and log out" from the dropdown option box.
User presses the "Proceed" textbox.
User presses the "OK" textbox.
### Expected result:

The seller home page loads successfully.
User inputs the appropriate store name in the textbox using the keyboard.
"Exit and log out" is selected from the dropdown menu.
On selecting "OK," the application opens the logout page with a thank you message.
Pressing "OK" navigates the application to the home page.
Test Status: Passed.


* Customer Tests
## Test 14: Customer Login and Redirect to Customer Home Page
### Steps:

User opens the application.
User selects the login textbox.
User selects the Customer option box.
User fills valid information into the name, email, and password textboxes.
User selects the "Enter" textbox.
User selects the "OK" textbox on the login successful popup.
### Expected result:

The application loads the home page.
On selecting the login textbox, the application navigates to the login page.
After entering valid information, the application verifies the user's existence in the database.
A popup confirms successful login.
The application redirects to the customer's home page.
Test Status: Passed.


## Test 15: Customer Appointment Request
### Steps:

User enters the customer home page.
User selects "Make an appointment request" from the dropdown option box.
User presses the "Proceed" textbox.
User fills the appointment name in the correct format.
User presses the "Proceed" textbox.
User presses the "OK" textbox.
### Expected result:

The customer home page loads successfully.
"Make an appointment request" is selected from the dropdown menu.
On filling the appointment name in the correct format, the application opens the make appointment page displaying all appointments.
After proceeding, a success message popup confirms the request.
Selecting "OK" navigates the application back to the customer home page.
Test Status: Passed.


## Test 16: Customer Awaiting Approval Appointment Cancellation
### Steps:

User enters the customer home page.
User selects "Cancel an appointment request" from the dropdown option box.
User selects "Proceed" textbox.
User selects "Appointments Awaiting Approval" from the dropdown option box.
User selects "Proceed" textbox.
User fills the appointment name in the correct format.
User selects "Proceed" textbox.
User presses the "OK" textbox.
### Expected result:

The customer home page loads successfully.
"Make an appointment request" is selected from the dropdown menu.
On proceeding, the application opens the cancel appointment selection page with a dropdown menu.
After proceeding, the app opens the appointment deletion page.
Upon proceeding, a success message popup confirms the cancellation.
Selecting "OK" navigates the application back to the customer home page.
Test Status: Passed.


## Test 17: Customer Approved Appointment Cancellation
### Steps:

User enters the customer home page.
User selects "Cancel an appointment request" from the dropdown option box.
User selects "Proceed" textbox.
User selects "Appointments Approved" from the dropdown option box.
User selects "Proceed" textbox.
User fills the appointment name in the correct format.
User selects "Proceed" textbox.
User presses the "OK" textbox.
### Expected result:

The customer home page loads successfully.
"Make an appointment request" is selected from the dropdown menu.
On proceeding, the application opens the cancel appointment selection page with a dropdown menu.
After proceeding, the app opens the appointment deletion page.
Upon proceeding, a success message popup confirms the cancellation.
Selecting "OK" navigates the application back to the customer home page.
Test Status: Passed.


## Test 18: Customer View Store Calendars
### Steps:

User enters the customer home page.
User selects "View store calendars" from the dropdown option box.
User selects "Proceed" textbox.
User presses the "Go Back" textbox.
### Expected result:

The customer home page loads successfully.
"View store calendars" is selected from the dropdown menu.
On selecting "Proceed," the application opens the store calendars page displaying all store calendars.
Pressing "Go Back" navigates the application back to the customer home page.
Test Status: Passed.


## Test 19: Customer View Approved Appointments
### Steps:

User enters the customer home page.
User selects "View currently approved appointments" from the dropdown option box.
User selects "Proceed" textbox.
User presses the "Go Back" textbox.
### Expected result:

The customer home page loads successfully.
"View currently approved appointments" is selected from the dropdown menu.
On selecting "Proceed," the application opens the approved appointments page displaying all the approved appointments.
Pressing "Go Back" navigates the application back to the customer home page.
Test Status: Passed.


## Test 20: Customer View Pending Appointments
### Steps:

User enters the customer home page.
User selects "View pending appointments" from the dropdown option box.
User selects "Proceed" textbox.
User presses the "Go Back" textbox.
### Expected result:

The customer home page loads successfully.
"View pending appointments" is selected from the dropdown menu.
On selecting "Proceed," the application opens the unapproved appointments page displaying all the pending appointments.
Pressing "Go Back" navigates the application back to the customer home page.
Test Status: Passed.


## Test 21: Customer Store Statistics Sored Most to Least
### Steps:

User enters the customer home page.
User selects "View store statistics" from the dropdown option box.
User selects "Proceed" textbox.
User selects "Most to Least Popular Stores" from the dropdown option box.
User selects "Proceed" textbox.
User presses the "Go back" textbox.
### Expected result:

The customer home page loads successfully.
"View store statistics" is selected from the dropdown menu.
On selecting "Proceed," the application opens the sort selection page displaying the dropdown menu for sorting options.
After proceeding, the app opens the statistics page with stores listed in descending order of popularity.
Pressing "Go Back" navigates the application back to the customer home page.
Test Status: Passed.


## Test 22: Customer Store Statistics - Ascending Order
### Steps:

User enters the customer home page.
User selects "View store statistics" from the dropdown option box.
User selects "Proceed" textbox.
User selects "Least to Most Popular Stores" from the dropdown option box.
User selects "Proceed" textbox.
User presses the "Go back" textbox.
### Expected result:

The customer home page loads successfully.
"View store statistics" is selected from the dropdown menu.
On selecting "Proceed," the application opens the sort selection page displaying the dropdown menu for sorting options.
After proceeding, the app opens the statistics page with stores listed in ascending order of popularity.
Pressing "Go Back" navigates the application back to the customer home page.
Test Status: Passed.


## Test 23: Customer Export Approved Appointments
### Steps:

User enters the customer home page.
User selects "Export file of your approved appointments" from the dropdown option box.
User selects "Proceed" textbox.
User presses the "OK" textbox.
### Expected result:

The customer home page loads successfully.
"Export file of your approved appointments" is selected from the dropdown menu.
On selecting "Proceed," a popup with a success message is presented.
After pressing "OK," the application navigates back to the customer home page.
Test Status: Passed.


## Test 24: Customer Logout
### Steps:

User enters the customer home page.
User selects "Exit and log out" from the dropdown option box.
User presses the "Proceed" textbox.
User presses the "OK" textbox.
### Expected result:

The seller home page loads successfully.
"Exit and log out" is selected from the dropdown menu.
On selecting "OK," the application opens the logout page with a thank you message.
Pressing "OK" navigates the application to the home page.
Test Status: Passed.