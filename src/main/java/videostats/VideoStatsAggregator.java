package videostats;

import java.sql.*;
import java.util.Scanner;
import java.util.List;

import videostats.model.Video;
import videostats.repository.DatabaseConnection;
import videostats.repository.VideoRepository;

public class VideoStatsAggregator {
    private static DatabaseConnection db;

    public static void main(String[] args) {
        String url = System.getenv("POSTGRES_URL");
        String user = System.getenv("POSTGRES_USER");
        String password = System.getenv("POSTGRES_PASSWORD");

        db = DatabaseConnection.getInstance(url, user, password);
        VideoRepository repository = new VideoRepository(db);
        
        try {
            createTable();
            Video video = new Video("https://youtube.com/akula", "akula", "youtube.com", 1);
            repository.save(video);
            video = new Video("https://vkvideo.ru/show", "show", "vkvideo.ru", 2);
            repository.save(video);
            getAllVideos(repository);
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void createTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS videos (
                id SERIAL PRIMARY KEY,
                url VARCHAR(100) NOT NULL UNIQUE,
                video_id VARCHAR(100) NOT NULL,
                platform VARCHAR(100) NOT NULL,
                user_id BIGINT NOT NULL
            )
        """;
        Connection conn = db.getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
        finally {
            db.releaseConnection(conn);
        }
    }
    
    private static void getAllVideos(VideoRepository repository) throws SQLException {
        List<Video> videos = repository.findAll();
        System.out.printf("%-3s | %-30s | %-20s | %-7s%n", "Url", "Video ID", "Platform", "User ID");            
        for (Video video : videos) {
            System.out.printf("%-30s | %-30s | %-20s | %-7s%n", 
                video.getUrl(), 
                video.getVideoID(), 
                video.getPlatform(), 
                video.getUserID()
            );
        }
    }
}
