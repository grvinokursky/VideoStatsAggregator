package videostats.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import videostats.model.VideoStats;

public class VideoStatsRepository {
    private final DatabaseConnection db;

    public VideoStatsRepository(DatabaseConnection db) {
        this.db = db;
    }

    public void save(VideoStats videoStats) {
        String sql = """
            INSERT INTO video_stats (video_id, view_count)
            VALUES (?, ?)
            ON CONFLICT (video_id) DO UPDATE SET
                view_count = EXCLUDED.view_count,
        """;
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = db.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, videoStats.getVideoId());
            pstmt.setLong(2, videoStats.getViewCount());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save video stats", e);
        } finally {
            closeResources(rs, pstmt, conn);
        }
    }

    public List<VideoStats> findAll() {
        String sql = """
            SELECT video_id, view_count
            FROM video_stats
        """;
        
        List<VideoStats> videoStatsList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = db.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                VideoStats videoStats = mapRowToVideoStats(rs);
                videoStatsList.add(videoStats);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all video links", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return videoStatsList;
    }

    private VideoStats mapRowToVideoStats(ResultSet rs) throws SQLException {
        VideoStats videoStats = new VideoStats();
        videoStats.setVideoId(rs.getString("video_id"));
        videoStats.setViewCount(rs.getLong("view_count"));
        return videoStats;
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
