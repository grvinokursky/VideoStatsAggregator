package videostats.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String url;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "platform")
    private String platform;

    @Column(name = "user_id")
    private long userId;

    @Transient
    private VideoStats videoStats;

    public Video() { }

    public Video(String url, String videoId, String platform,
                 long userId, VideoStats videoStats) {
        this.url = url;
        this.videoId = videoId;
        this.platform = platform;
        this.userId = userId;
        this.videoStats = videoStats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public VideoStats getVideoStats() {
        return videoStats;
    }

    public void setVideoStats(VideoStats videoStats) {
        this.videoStats = videoStats;
    }
}
