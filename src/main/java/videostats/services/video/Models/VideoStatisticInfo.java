package videostats.services.video.Models;

import java.time.LocalDateTime;

public class VideoStatisticInfo {
    private String url;
    private String platform;
    private long viewCount;
    private LocalDateTime updatedAt;

    public VideoStatisticInfo(String url, String platform, long viewCount, LocalDateTime updatedAt) {
        this.url = url;
        this.platform = platform;
        this.viewCount = viewCount;
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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
