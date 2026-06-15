# Secure Online Banking Management System

A basic full-stack virtual banking system built with Spring Boot, PostgreSQL, and React. It covers secure login, account viewing, fund transfers, transaction history, beneficiaries, admin account controls, PDF/CSV export, and email notifications.



## Tech Stack

| Layer | Technology |
| --- | --- |
| Backend | Java 17, Spring Boot 3, Spring Security, Spring Data JPA |
| Database | PostgreSQL 15 |
| Frontend | React 18, Vite, Tailwind CSS, Axios |
| Security | JWT, BCrypt |
| Export | iText PDF, OpenCSV |
| Email | JavaMailSender SMTP |
| DevOps | Docker, Docker Compose |

## Features

Customer:
- Register and login with JWT authentication
- View account details and balance
- Transfer money to another active account
- Manage beneficiaries
- View transaction history, mini statement, and search filters
- Download statement as PDF or CSV
- Update profile and change password

Admin:
- View all users and account details
- Block or unblock accounts
- View all transactions
- View audit logs

Note: New customer accounts start with a demo opening balance of 1000 so transfers can be tested immediately.

## Project Structure

```text
banking-app/
├── src/
│   └── main/
│       ├── java/com/banking/
│       │   ├── controller/
│       │   ├── service/
│       │   ├── entity/
│       │   ├── repository/
│       │   ├── dto/
│       │   ├── security/
│       │   └── exception/
│       └── resources/
│           └── application.properties
├── frontend/
│   ├── src/
│   │   ├── pages/
│   │   ├── components/
│   │   └── api/
│   └── package.json
├── Dockerfile
├── docker-compose.yml
└── README.md
```

## Database Tables

```text
users          -> id, full_name, email, password, phone, role, created_at
accounts       -> id, account_number, balance, status, user_id, created_at
transactions   -> id, sender_account, receiver_account, amount, type, description, created_at
beneficiaries  -> id, user_id, name, account_number, created_at
audit_logs     -> id, user_id, action, details, created_at
```

## Run With Docker

1. Clone the repository:

```bash
git clone <repository-url>
cd banking-app
```

2. Set environment variables for the backend:

```env
DB_URL=jdbc:postgresql://localhost:5432/banking_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=replace-with-a-long-random-secret
MAIL_USERNAME=youremail@gmail.com
MAIL_PASSWORD=your-gmail-app-password
FRONTEND_ORIGIN=http://localhost:5173
```

3. Start PostgreSQL and the Spring Boot app:

```bash
docker-compose up --build
```

Backend URL:

```text
http://localhost:8080
```

## Run Locally

Prerequisites:
- Java 17
- Maven
- Node.js 18+
- PostgreSQL 15

Create the database:

```sql
CREATE DATABASE banking_db;
```

Run backend:

```bash
cd banking-app
mvn spring-boot:run
```

Run frontend:

```bash
cd banking-app/frontend
npm install
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

## Important Environment Variables

```env
DB_URL=jdbc:postgresql://localhost:5432/banking_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=replace-with-a-long-random-secret
MAIL_USERNAME=youremail@gmail.com
MAIL_PASSWORD=your-gmail-app-password
FRONTEND_ORIGIN=http://localhost:5173
```

The backend reads sensitive values from environment variables, and the Docker compose file passes the same values into the app and database containers.

## API Endpoints

### Auth

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| POST | `/api/auth/register` | Register customer and create account | No |
| POST | `/api/auth/login` | Login and return JWT | No |

### Account

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| GET | `/api/account/balance` | Get logged-in user's balance | Yes |
| GET | `/api/account/details` | Get user and account details | Yes |

### Transfer

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| POST | `/api/transfer/send` | Transfer money to another account | Yes |

### Transactions

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| GET | `/api/transactions/history` | Last 50 transactions | Yes |
| GET | `/api/transactions/mini-statement` | Last 10 transactions | Yes |
| GET | `/api/transactions/search` | Filter by date, type, amount | Yes |
| GET | `/api/transactions/export/pdf` | Download PDF statement | Yes |
| GET | `/api/transactions/export/csv` | Download CSV statement | Yes |

### Beneficiary

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| POST | `/api/beneficiary/add` | Add beneficiary | Yes |
| GET | `/api/beneficiary/list` | List beneficiaries | Yes |
| DELETE | `/api/beneficiary/{id}` | Delete beneficiary | Yes |

### Profile

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| GET | `/api/profile/` | Get profile | Yes |
| PUT | `/api/profile/update` | Update email and phone | Yes |
| PUT | `/api/profile/change-password` | Change password | Yes |

### Admin

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| GET | `/api/admin/users` | List users with account details | ADMIN |
| GET | `/api/admin/transactions` | List all transactions | ADMIN |
| PUT | `/api/admin/account/{accountNumber}/block` | Block account | ADMIN |
| PUT | `/api/admin/account/{accountNumber}/unblock` | Unblock account | ADMIN |
| GET | `/api/admin/audit-logs` | View audit logs | ADMIN |

## Interview Talking Points

| Feature | Talking Point |
| --- | --- |
| BCrypt password encryption | Plain-text passwords are never stored |
| JWT stateless auth | Server does not need sessions |
| `@Transactional` transfer | Debit and credit succeed or roll back together |
| `@PreAuthorize` admin routes | Admin APIs are protected by role |
| `@ControllerAdvice` | Validation and runtime errors are handled centrally |
| Audit logs | Sensitive actions are logged with user ID and timestamp |
| PDF/CSV export | Users can download their statements |
| Input validation | Backend validates all request DTOs |

## Simple Testing Flow

1. Register two users.
2. Login as the first user.
3. Add the second user's account number as a beneficiary.
4. Transfer a small amount.
5. Check transaction history and mini statement.
6. Export the statement as CSV or PDF.
