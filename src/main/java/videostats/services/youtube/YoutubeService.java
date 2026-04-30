package videostats.services.youtube;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.util.Collections;


public class YoutubeService {
    private final String apiKey;

    public YoutubeService(String apiKey) {
        this.apiKey = apiKey;
    }
    // Заглушка.
    public YoutubeVideoViewsCountResponse GetVideoViewsCount(String videoId) {
        try {
            YouTube youtube = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                request -> request.getUrl().set("key", apiKey)
            ).setApplicationName("VideoStatsAggregator").build();
            
            YouTube.Videos.List request = youtube.videos()
                .list(Collections.singletonList("statistics"));
            request.setId(Collections.singletonList(videoId));
            
            VideoListResponse response = request.execute();
            
            if (!response.getItems().isEmpty()) {
                Video video = response.getItems().get(0);
                long views = video.getStatistics().getViewCount().longValue();
                System.out.println("Просмотров: " + views);
                return new YoutubeVideoViewsCountResponse(views, videoId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
