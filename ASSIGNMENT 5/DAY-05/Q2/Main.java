import java.sql.*;
import java.util.*;

// SQL Query:
// SELECT s.student_id, s.full_name
// FROM students s
// LEFT JOIN course_registrations cr ON s.student_id = cr.student_id
// WHERE cr.student_id IS NULL;
//
// Index to optimize this query:
// CREATE INDEX idx_course_registrations_student_id ON course_registrations (student_id);

interface RegistrationManager {
    void enrollAtRiskStudents();
}

abstract class DatabaseConnectionProvider {
    private String url = "jdbc:postgresql://localhost:5432/edixo";
    private String username = "dbuser";
    private String password = "dbpassword";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}

class EdixoRegistrationRepository extends DatabaseConnectionProvider implements RegistrationManager {
    private static final String AT_RISK_QUERY =
            "SELECT s.student_id, s.full_name " +
            "FROM students s " +
            "LEFT JOIN course_registrations cr ON s.student_id = cr.student_id " +
            "WHERE cr.student_id IS NULL";
    private static final String INSERT_ENROLLMENT_SQL =
            "INSERT INTO course_registrations (student_id, course_code, semester) VALUES (?, 'ORIENTATION 101', 'FALL2026')";

    @Override
    public void enrollAtRiskStudents() {
        try (Connection conn = getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(AT_RISK_QUERY);
             ResultSet rs = selectStmt.executeQuery();
             PreparedStatement insertStmt = conn.prepareStatement(INSERT_ENROLLMENT_SQL)) {

            int batchSize = 0;
            while (rs.next()) {
                long studentId = rs.getLong("student_id");
                insertStmt.setLong(1, studentId);
                insertStmt.addBatch();
                batchSize++;

                if (batchSize >= 1000) {
                    insertStmt.executeBatch();
                    batchSize = 0;
                }
            }
            if (batchSize > 0) {
                insertStmt.executeBatch();
            }
            System.out.println("At-risk students enrolled in Orientation 101.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        RegistrationManager manager = new EdixoRegistrationRepository();
        manager.enrollAtRiskStudents();
    }
}
