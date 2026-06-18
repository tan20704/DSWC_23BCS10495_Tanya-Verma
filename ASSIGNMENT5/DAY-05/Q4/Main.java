import java.sql.*;
import java.time.*;

// SQL Query:
// SELECT r.rider_id, r.rider_name, r.bike_model, g.latitude, g.longitude, g.recorded_at
// FROM riders r
// INNER JOIN (
//   SELECT rider_id, latitude, longitude, recorded_at,
//          ROW_NUMBER() OVER (PARTITION BY rider_id ORDER BY recorded_at DESC) AS rn
//   FROM gps_pings
// ) g ON r.rider_id = g.rider_id
// WHERE g.rn = 1;
//
// Index to optimize this query:
// CREATE INDEX idx_gps_pings_rider_time_desc ON gps_pings (rider_id, recorded_at DESC);

interface TelemetryService {
    void printLatestLocations();
}

abstract class FleetDatabaseConnection {
    private String url = "jdbc:postgresql://localhost:5432/fleet_tracker";
    private String username = "dbuser";
    private String password = "dbpassword";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}

class FleetTelemetryRepository extends FleetDatabaseConnection implements TelemetryService {
    private static final String LATEST_LOCATION_SQL =
            "SELECT r.rider_id, r.rider_name, r.bike_model, g.latitude, g.longitude, g.recorded_at " +
            "FROM riders r " +
            "INNER JOIN ( " +
            "  SELECT rider_id, latitude, longitude, recorded_at, " +
            "         ROW_NUMBER() OVER (PARTITION BY rider_id ORDER BY recorded_at DESC) AS rn " +
            "  FROM gps_pings " +
            ") g ON r.rider_id = g.rider_id " +
            "WHERE g.rn = 1";

    @Override
    public void printLatestLocations() {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(LATEST_LOCATION_SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                long riderId = rs.getLong("rider_id");
                String riderName = rs.getString("rider_name");
                String bikeModel = rs.getString("bike_model");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                LocalDateTime recordedAt = rs.getObject("recorded_at", LocalDateTime.class);

                System.out.println(riderId + " | " + riderName + " | " + bikeModel + " | " +
                        latitude + ", " + longitude + " | " + recordedAt);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        TelemetryService service = new FleetTelemetryRepository();
        service.printLatestLocations();
    }
}
