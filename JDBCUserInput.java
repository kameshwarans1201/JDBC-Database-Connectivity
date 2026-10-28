
import java.sql.*;
import java.util.Scanner;

public class JDBCUserInput {

    static final String URL = "jdbc:mysql://localhost:3306/studentdb";
    static final String USER = "root";
    String password = "YOUR_MYSQL_PASSWORD";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD)) {

                System.out.println("Connected to MySQL database.");

                int choice;

                do {
                    System.out.println("\n----- STUDENT MENU -----");
                    System.out.println("1. Insert record");
                    System.out.println("2. Display records");
                    System.out.println("3. Update marks");
                    System.out.println("4. Delete record");
                    System.out.println("5. Exit");
                    System.out.print("Enter your choice: ");

                    choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {

                        case 1:
                            System.out.print("Enter ID: ");
                            int id = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Enter Name: ");
                            String name = sc.nextLine();

                            System.out.print("Enter Department: ");
                            String dept = sc.nextLine();

                            System.out.print("Enter Marks: ");
                            int marks = sc.nextInt();
                            sc.nextLine();

                            insertRecord(con, id, name, dept, marks);
                            break;

                        case 2:
                            displayRecords(con);
                            break;

                        case 3:
                            System.out.print("Enter ID to update: ");
                            int uid = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Enter new Marks: ");
                            int newMarks = sc.nextInt();
                            sc.nextLine();

                            updateRecord(con, uid, newMarks);
                            break;

                        case 4:
                            System.out.print("Enter ID to delete: ");
                            int did = sc.nextInt();
                            sc.nextLine();

                            deleteRecord(con, did);
                            break;

                        case 5:
                            System.out.println("Exiting program...");
                            break;

                        default:
                            System.out.println("Invalid choice. Try again.");
                    }

                } while (choice != 5);

            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: "
                    + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());

        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Enter numbers for ID, marks, and menu choice.");

        } finally {
            sc.close();
            System.out.println("Program closed.");
        }
    }

    // Insert student record
    static void insertRecord(Connection con, int id, String name,
                             String dept, int marks) {

        String sql = "INSERT INTO student (id, name, department, marks) "
                   + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, dept);
            ps.setInt(4, marks);

            ps.executeUpdate();

            System.out.println("Record inserted successfully.");

        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    // Display all student records
    static void displayRecords(Connection con) {

        String sql = "SELECT * FROM student";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nID\tName\tDepartment\tMarks");
            System.out.println("----------------------------------------");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println(
                    rs.getInt("id") + "\t" +
                    rs.getString("name") + "\t" +
                    rs.getString("department") + "\t" +
                    rs.getInt("marks")
                );
            }

            if (!found) {
                System.out.println("No records found.");
            }

        } catch (SQLException e) {
            System.out.println("Display failed: " + e.getMessage());
        }
    }

    // Update student marks
    static void updateRecord(Connection con, int id, int marks) {

        String sql = "UPDATE student SET marks = ? WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, marks);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("No record found with that ID.");
            }

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    // Delete student record
    static void deleteRecord(Connection con, int id) {

        String sql = "DELETE FROM student WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("No record found with that ID.");
            }

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }
}
