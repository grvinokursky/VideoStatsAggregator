package videostats.model;

public class Video {
    private String url;
    private String videoID;
    private String platform;
    private long userID;

    public Video() { }

    public Video(String url, String videoID, String platform, long userID) {
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
}
