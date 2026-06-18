import java.sql.*;
import java.util.*;

// SQL Query:
// SELECT h.asset_class, SUM(h.current_value) AS total_value
// FROM holdings h
// INNER JOIN investors i ON h.investor_id = i.investor_id
// WHERE h.investor_id = ?
// GROUP BY h.asset_class;
//
// Index to optimize this query:
// CREATE INDEX idx_holdings_investor_asset_value ON holdings (investor_id, asset_class, current_value);

interface PortfolioManager {
    void restructurePortfolio(long investorId);
}

abstract class FinancialDatabaseConfig {
    private String url = "jdbc:postgresql://localhost:5432/fire_portfolio";
    private String username = "dbuser";
    private String password = "dbpassword";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}

class PortfolioRepository extends FinancialDatabaseConfig implements PortfolioManager {
    private static final String AGGREGATION_SQL =
            "SELECT h.asset_class, SUM(h.current_value) AS total_value " +
            "FROM holdings h " +
            "INNER JOIN investors i ON h.investor_id = i.investor_id " +
            "WHERE h.investor_id = ? " +
            "GROUP BY h.asset_class";
    private static final String UPDATE_DEBT_SQL =
            "UPDATE holdings SET current_value = current_value - ? " +
            "WHERE investor_id = ? AND asset_class = 'Debt'";
    private static final String UPDATE_EQUITY_SQL =
            "UPDATE holdings SET current_value = current_value + ? " +
            "WHERE investor_id = ? AND asset_class = 'Equity'";

    @Override
    public void restructurePortfolio(long investorId) {
        double shiftAmount = 10000.0;

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement selectStmt = conn.prepareStatement(AGGREGATION_SQL)) {
                selectStmt.setLong(1, investorId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    Map<String, Double> totals = new HashMap<>();
                    while (rs.next()) {
                        totals.put(rs.getString("asset_class"), rs.getDouble("total_value"));
                    }

                    double debtTotal = totals.getOrDefault("Debt", 0.0);
                    if (debtTotal < shiftAmount) {
                        throw new SQLException("Insufficient debt value available for reallocation.");
                    }
                }
            }

            try (PreparedStatement updateDebt = conn.prepareStatement(UPDATE_DEBT_SQL);
                 PreparedStatement updateEquity = conn.prepareStatement(UPDATE_EQUITY_SQL)) {
                updateDebt.setDouble(1, shiftAmount);
                updateDebt.setLong(2, investorId);
                updateDebt.executeUpdate();

                updateEquity.setDouble(1, shiftAmount);
                updateEquity.setLong(2, investorId);
                updateEquity.executeUpdate();
            }

            conn.commit();
            System.out.println("Portfolio restructured successfully for investor " + investorId + ".");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        PortfolioManager manager = new PortfolioRepository();
        manager.restructurePortfolio(12345L);
    }
}
