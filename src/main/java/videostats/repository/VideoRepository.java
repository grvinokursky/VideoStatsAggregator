package videostats.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import videostats.model.Video;

public class VideoRepository {
    private final DatabaseConnection db;

    public VideoRepository(DatabaseConnection db) {
        this.db = db;
    }

    public void save(Video video) {
        String sql = """
            INSERT INTO videos (url, video_id, platform, user_id)
            VALUES (?, ?, ?, ?)
            ON CONFLICT (url) DO UPDATE SET
                video_id = EXCLUDED.video_id,
                platform = EXCLUDED.platform,
                user_id = EXCLUDED.user_id
        """;
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = db.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, video.getUrl());
            pstmt.setString(2, video.getVideoID());
            pstmt.setString(3, video.getPlatform());
            pstmt.setLong(4, video.getUserID());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save video", e);
        } finally {
            closeResources(rs, pstmt, conn);
        }
    }

    public List<Video> findAll() {
        String sql = """
            SELECT url, video_id, platform, user_id
            FROM videos
        """;
        
        List<Video> videos = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = db.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Video video = mapRowToVideo(rs);
                videos.add(video);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all video links", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return videos;
    }

    private Video mapRowToVideo(ResultSet rs) throws SQLException {
        Video video = new Video();
        video.setUrl(rs.getString("url"));
        video.setVideoID(rs.getString("video_id"));
        video.setPlatform(rs.getString("platform"));
        video.setUserID(rs.getLong("user_id"));
        return video;
    }

    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) db.releaseConnection(conn);
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
