import java.sql.*;
import java.util.Scanner;

public class VideoStatsAggregator {
    
    private static Connection connection = null;
    
    public static void main(String[] args) {
        String url = System.getenv("POSTGRES_URL");
        String user = System.getenv("POSTGRES_USER");
        String password = System.getenv("POSTGRES_PASSWORD");
        
        try {
            connection = DriverManager.getConnection(url, user, password);
            createTable();
            insertVideo("akula", "youtube.com", 1);
            insertVideo("show", "vkvideo.ru", 2);
            getAllVideos();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void createTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS videos (
                id SERIAL PRIMARY KEY,
                video_id VARCHAR(100) NOT NULL,
                host VARCHAR(100) NOT NULL,
                user_id INTEGER NOT NULL
            )
        """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    private static void insertVideo(String videoID, String host, int userID) throws SQLException {
        String sql = "INSERT INTO videos (video_id, host, user_id) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, videoID);
            pstmt.setString(2, host);
            pstmt.setInt(3, userID);
            pstmt.executeUpdate();
        }
    }
    
    private static void getAllVideos() throws SQLException {
            String sql = "SELECT id, video_id, host, user_id FROM videos ORDER BY id";
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
        
            System.out.printf("%-3s | %-30s | %-20s | %-7s%n", "ID", "Video ID", "Host", "User ID");            
            while (rs.next()) {
                int id = rs.getInt("id");
                String videoId = rs.getString("video_id");
                String host = rs.getString("host");
                int userId = rs.getInt("user_id");
                System.out.printf("%-3d | %-30s | %-20s | %-7d%n", 
                    id, videoId, host, userId);
            }
        }
    }
}
