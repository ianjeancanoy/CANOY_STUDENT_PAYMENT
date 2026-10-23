# Student Tuition Payment Reservation System

Database: `Student Tuition Payment Reservation System` (SQLite file `studentpayment.db`)

Layered Java console app connected to **SQLite** (via the `sqlite-jdbc` Maven dependency).

## Structure
- `Main.java` - app entry point
- `dao/DatabaseConnection.java` - SQLite connection + table creation
- `dao/UserDao.java` - SQL queries (PreparedStatement)
- `model/User.java` - Java Record
- `ui/MainMenu.java` - Register / Login / Exit
- `ui/UserMenu.java` - after-login menu (admin can approve users)
- `util/PasswordUtil.java` - SHA-256 password hashing

## Run
Requires JDK 17+ and Maven.

    mvn compile exec:java

Or build a runnable jar: `mvn clean package`, then `java -jar target/tuition-payment-reservation-1.0.0.jar`

The database file `studentpayment.db` is created automatically on first run.
Default admin: `admin` / `admin123`

## users table
id, username (unique), password (SHA-256 hash), roles, status (PENDING by default)
