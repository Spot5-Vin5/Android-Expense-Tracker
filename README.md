# Android-Expense-Tracker
**_My first Android project, that is to track my daily expenses and its named as Expense Tracker._**

_Feature: NewUser_
```
 1. Login New user.
 2. Details like Name, Email, Password need to enter by user.
```
_Feature: ExistingUser_
```
 1. Login existing user, feature development.
 2. Save the New User email as file name, store password inside the profile page.
 3. Steps to validate the existing user
    a. User should enter email, this should validate all the file names in the folder.
    b. Once file name is matched, load the password page and ask for password.
    c. Click on sign in button and now validate password and do login.   
    d. Load Home page.
```
_Feature: ViewAllButton_
```
 1. There will be 2 buttons on top section of screen
 2. By default Transactions button is clicked, so all transaction list will be displayed below.
 3. When Payments button is clicked, list of payment types will be displayed.
 4. Each Transaction show details like 
    a. Date -> mandatory
    b. Amount -> mandatory
    c. category type -> mandatory
    d. Category Subtype -> if it there [Bug fix needed]
    e. Payment type -> mandatory
    f. Payment Subtype -> if it there [Bug fix needed]
    g. Note -> if it there [Bug fix needed]
 5. Each Payment type show details like
    a. Payment Type
    b. Total Amount 
```
_Feature: Logout Button_
```
 1. Introduce Logout button in Profile page.
```
_Upcoming Features_
```
 1. Logout button. // Success
 2. Sort button in Transactions and Payment screens.
 3. Month wise Piechart,Transactions and Payments.
 4. Back button in all screens.
 5. Login screen improvement.
 6. Apply Multithreading for excel reading and writing operations.
 7. When there is no data to display, tell to user nothing to display Ex: No Transactions availble to show.
 8. More colors in Pie chart and Display text in center of Pie chart.
 9. Auto logout from app after 5min if user don't logout.
 10. Default profile picture.
 11. Check delete button functionality in excel file row.
```
_Git Commands_
```
1. Merge feature branch  to Main in GitHub.
2. Create new feature in GitHub.
```
3. **"git fetch origin"** in local to sync all branches from remote to local.
4. **"git branch -a"** to see all the branches in local and remote.
5. **"git checkout -b new-feature-branch-name remote/origin/new-feature-branch-name"** to create same new feature branch in local and establish connection to same origin branch.


