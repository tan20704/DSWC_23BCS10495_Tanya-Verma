import java.sql.*;

// SQL Query:
// SELECT s.shipment_id, c.company_name, s.dispatch_date
// FROM shipments s
// INNER JOIN couriers c ON s.courier_id = c.courier_id
// WHERE s.status = 'DELAYED'
// ORDER BY s.dispatch_date DESC;
//
// Index to optimize this query:
// CREATE INDEX idx_shipments_status_dispatch_date ON shipments (status, dispatch_date DESC);

interface ReportGenerator {
    void printDelayedReport();
}

abstract class DatabaseRepository {
    private String url = "jdbc:postgresql://localhost:5432/cargologix";
    private String username = "dbuser";
    private String password = "dbpassword";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}

class LogisticsRepository extends DatabaseRepository implements ReportGenerator {
    private static final String DELAYED_REPORT_SQL =
            "SELECT s.shipment_id, c.company_name, s.dispatch_date " +
            "FROM shipments s " +
            "INNER JOIN couriers c ON s.courier_id = c.courier_id " +
            "WHERE s.status = 'DELAYED' " +
            "ORDER BY s.dispatch_date DESC";

    @Override
    public void printDelayedReport() {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELAYED_REPORT_SQL);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Delayed shipments report:");
            while (rs.next()) {
                long shipmentId = rs.getLong("shipment_id");
                String companyName = rs.getString("company_name");
                Timestamp dispatchTimestamp = rs.getTimestamp("dispatch_date");
                System.out.println(shipmentId + " | " + companyName + " | " + dispatchTimestamp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ReportGenerator reportGenerator = new LogisticsRepository();
        reportGenerator.printDelayedReport();
    }
}
