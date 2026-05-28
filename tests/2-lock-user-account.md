---
name: Lock User Account Test
id: 1
node: 2
required: 1
---
# Lock User Account Test

## Steps
1. Get `testuser` from `context-manager`
2. Open `TEST_URL`
3. Fill login form with `testuser.email` and a random 10 character string as the password, then click `Login` button.
4. Verify error message `Invalid email or password.` is displayed on the page.
5. Redo step 3.
6. Verify error message `Invalid email or password.` is displayed on the page.
7. Redo step 3.
8. Verify error message `Invalid email or password.` is displayed on the page.
9. Fill lgoin form with `testuser.email` and `testuser.password`, then click `Login` button.
10. Verify error message `This account has been locked.` is displayed on the page.