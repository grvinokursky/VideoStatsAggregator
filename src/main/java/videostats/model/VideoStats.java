package videostats.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "video_stats")
public class VideoStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "platform")
    private String platform;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public VideoStats() { }

    public VideoStats(String videoId, String platform,
                      Long viewCount, LocalDateTime updatedAt) {
        this.videoId = videoId;
        this.platform = platform;
        this.viewCount = viewCount;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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
