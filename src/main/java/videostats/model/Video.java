package videostats.model;

import java.time.LocalDateTime;

public class Video {
    private String url;
    private String videoId;
    private String platform;
    private long userId;

    // Нужно включать этот объект в модель для удобства работы в сервисе.
    // Или можно перенести свойства 'VideoStats' в данный класс.
    // Сейчас реализовано для первого варианта, так что мне было бы удобнее остановится на первом варианте.
    private VideoStats videoStats;

    public Video() { }

    public Video(String url, String videoId, String platform,
                 long userId, Long viewsCount, LocalDateTime updateAt) {
        this.url = url;
        this.videoId = videoId;
        this.platform = platform;
        this.userId = userId;
        this.videoStats = new VideoStats(videoId, viewsCount, updateAt);
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
