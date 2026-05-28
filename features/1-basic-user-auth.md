# Basic User Auth

## Objectives
1. Implement basic user auth through three pages: `register`, `login`, `home`
    - `register` page (path `/register`): it includes a form with title `Register Account`, a `Register` button and the following fields:
        a. `Name`: the user's real name, required
        b. `Email`: email address, required
        c. `Password`: password, required, min length 8
        d. `Confirm Password`: required the same text as the password
    If all input fields are valid, click `Register` button will create a new user account in database then navigate to `login` page. Otherwise, display the proper error message on `register` page.
    - `login` page (path `/login`): it includes a form with title `Login`, a `Login` button and the following fields:
        a. `Email`: registered email address, required
        b. `Password`: user's password, required
    When `Login` button is clicked, if the email and password value matches a database record, user will be navigate to `home` page. If no matched record found, display error message `Invalid email or password.`
    - `home` page (path `/home`): The page display a title `Welcome ${Name}`. The ${Name} is the name of the logged in user. 

2. Implement top menu for all three pages to enable following the navigation flow:
    - the default page of the web application should be `login`. There should be a `Register` item on the top menu to navigate to `register` page.
    - The top menu on `register` page should have `Login` item to navigate to `login` page.
    - The top menu on `home` page should have `Logout` item which will logout current user and redirect page to `login`

3. Implement validation rules. The following genral validation rules are implemented:
    - Validation should be performed before any form is submitted. If any required field is missing, display error message listed all mising required field. 
    - If a field length requirement isn't meet, display message `${field} requires at least ${lengh_required} characters`
    - In the case of `password` and `confirmed password` don't match, display error message `Password fields don't match`
    - Any form field value change will clear the existing error message.

4. Implement account lock. If a user typed in correct email address but incorrect password for three consective times, lock the account in database. If an account is locked, a correct email and password combination will result in error message `This account has been locked.`

## Should Not Do
- Don't store password value as plain text in the database

## Tasks
Implement the followig tasks:
1. Design and implement database schema to store user account
2. Design and implement proper front end pages, back end services and configure security.
3. Create unit tests for both front and back end. 
4. make sure `mvn clean install` succeed.
5. After the coding is done, confirm QA environment then perform acceptance test up to 3 times. In the case of the acceptance test fails, use feature debug to identify bugs and fix them.