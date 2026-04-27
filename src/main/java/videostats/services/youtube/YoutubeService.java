package videostats.services.youtube;

public class YoutubeService {
    // Заглушка.
    public YoutubeVideoViewsCountResponse GetVideoViewsCount(String url) {
        return new YoutubeVideoViewsCountResponse(
                100,
                "1");
    }
}
