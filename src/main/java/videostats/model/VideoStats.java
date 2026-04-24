package videostats.model;

public class VideoStats {
    private String videoID;
    private long viewCount;

    public VideoStats() { }

    public VideoStats(String videoID, long viewCount) {
        this.videoID = videoID;
        this.viewCount = viewCount;
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
}
