import java.sql.*;

// SQL Query:
// SELECT bj.job_id, d.dept_name, bj.created_at
// FROM background_jobs bj
// INNER JOIN departments d ON bj.dept_id = d.dept_id
// WHERE bj.status = 'PENDING' AND d.dept_name = 'Engineering'
// ORDER BY bj.created_at ASC
// FOR UPDATE SKIP LOCKED
// LIMIT 1;
//
// Index to optimize this query:
// CREATE INDEX idx_background_jobs_pending_dept_time ON background_jobs (dept_id, created_at) WHERE status = 'PENDING';

interface QueueWorker {
    void processNextJob();
}

abstract class EnterpriseConnectionFactory {
    private String url = "jdbc:postgresql://localhost:5432/enterprise_jobs";
    private String username = "dbuser";
    private String password = "dbpassword";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}

class JobQueueWorker extends EnterpriseConnectionFactory implements QueueWorker {
    private static final String FETCH_NEXT_JOB_SQL =
            "SELECT bj.job_id " +
            "FROM background_jobs bj " +
            "INNER JOIN departments d ON bj.dept_id = d.dept_id " +
            "WHERE bj.status = 'PENDING' AND d.dept_name = 'Engineering' " +
            "ORDER BY bj.created_at ASC " +
            "FOR UPDATE SKIP LOCKED " +
            "LIMIT 1";
    private static final String UPDATE_JOB_STATUS_SQL =
            "UPDATE background_jobs SET status = 'PROCESSING' WHERE job_id = ?";

    @Override
    public void processNextJob() {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement fetchStmt = conn.prepareStatement(FETCH_NEXT_JOB_SQL);
                 ResultSet rs = fetchStmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("No pending Engineering job found.");
                    conn.commit();
                    return;
                }

                long jobId = rs.getLong("job_id");

                try (PreparedStatement updateStmt = conn.prepareStatement(UPDATE_JOB_STATUS_SQL)) {
                    updateStmt.setLong(1, jobId);
                    updateStmt.executeUpdate();
                }
                conn.commit();
                System.out.println("Job " + jobId + " locked and moved to PROCESSING.");
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        QueueWorker worker = new JobQueueWorker();
        worker.processNextJob();
    }
}
