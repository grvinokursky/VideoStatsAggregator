package videostats.model;

import java.time.LocalDateTime;

public class Video {
    private String url;
    private String videoID;
    private String platform;
    private long userID;

    // Нужно включать этот объект в модель для удобства работы в сервисе.
    // Или можно перенести свойства 'VideoStats' в данный класс.
    // Сейчас реализовано для первого варианта, так что мне было бы удобнее остановится на первом варианте.
    private VideoStats videoStats;

    public Video() { }

    public Video(String url, String videoID, String platform, long userID, Long viewsCount, LocalDateTime updateAt) {
        this.url = url;
        this.videoID = videoID;
        this.platform = platform;
        this.userID = userID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public VideoStats getVideoStats() {
        return videoStats;
    }

    public void setVideoStats(VideoStats videoStats) {
        this.videoStats = videoStats;
    }
}
