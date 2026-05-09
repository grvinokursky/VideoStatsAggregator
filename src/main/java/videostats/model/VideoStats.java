package videostats.model;

import java.time.LocalDateTime;

public class VideoStats {
    private String videoId;
    private long viewCount;
    private LocalDateTime updatedAt;

    public VideoStats() { }

    public VideoStats(String videoId, long viewCount, LocalDateTime updatedAt) {
        this.videoId = videoId;
        this.viewCount = viewCount;
        this.updatedAt = updatedAt;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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
