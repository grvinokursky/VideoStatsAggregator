package videostats.model;

import java.time.LocalDateTime;

public class VideoStats {
    private String videoID;
    private long viewCount;
    private LocalDateTime updatedAt;

    public VideoStats() { }

    public VideoStats(String videoID, long viewCount, LocalDateTime updatedAt) {
        this.videoID = videoID;
        this.viewCount = viewCount;
        this.updatedAt = updatedAt;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String VideoID) {
        this.videoID = videoID;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
