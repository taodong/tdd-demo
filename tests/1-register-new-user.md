---
name: Register New User Test
id: 1
node: 1
---
# Register New User Test

## Steps
1. Create a `testuser` object with the following structure:
    ```json
    {
        "name": "${username}",
        "email": "${useremail}",
        "password": "${password}"
    }
    ```
`${username}` should be a random name, `${useremail}` should be a random email, and `${password}` is a 12 character random string
2. Store `testuser` in `context-manager` with key `testuser`
3. Open `TEST_URL` then click `Register` link
4. Verify the browser is on `/register` page
5. Use the `testuser` data to fill in the form and click `Register` button.
6. Verify the browser navigate to `/home` page and text `Welcome ${username}` is displayed on the page. 