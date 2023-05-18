# BankApplication

A simple bank web application with deliberately insecure vulnerabilities.

Collaborators: Qihui Jian, Xiaowen Sun, Ying Che, Yi Zhang

## Build Instructions
### Installations:
- [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=mac)
- [Node.js and npm](https://nodejs.org/en/download)

### Build Database
1. Set up MySQL Workbench:
   - Open MySQL Workbench and connect to local instance 3306

2. Create schema:
   - Use the SQL script provided in [`bankAcct.sql`](https://github.com/isyizhang/BankApplication/blob/main/bankAcct.sql) to create database and table

### Build Project
   - Download and Open code of project in IntelliJ IDEA

#### Backend：
1. Set up database account:
   -  Open `src > main > resources`
    - Open the `application.properties` file, input your own MySQL username and password

2. Start the backend:
    - Run BankApplication:  src > main > java > com.swe266.bankapp

#### Frontend:
1. Change directory to frontend using the following command:
    - cd frontend

2. Install app required packages:
    - npm install

3. Start the application:
    - npm start

4. A new page will be rendered:
    - http://localhost:3000/
