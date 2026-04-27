package videostats.services.youtube;

public class YoutubeVideoViewsCountResponse {
    private long viewsCount;
    private String videoId;

    public YoutubeVideoViewsCountResponse(long viewsCount, String videoId) {
        this.viewsCount = viewsCount;
        this.videoId = videoId;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
