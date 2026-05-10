package videostats.services.youtube;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.util.Collections;
import java.util.Optional;


public class YoutubeService {
    private final String apiKey;

    public YoutubeService(String apiKey) {
        this.apiKey = apiKey;
    }

    public Optional<YoutubeVideoViewsCountResponse> GetVideoViewsCount(String videoId) {
        try {
            YouTube youtube = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                request -> request.getUrl().set("key", apiKey)
            ).setApplicationName("VideoStatsAggregator").build();
            
            YouTube.Videos.List request = youtube.videos()
                .list(Collections.singletonList("statistics"));
            request.setId(Collections.singletonList(videoId));
            
            VideoListResponse response = request.execute();
            
            if (!response.getItems().isEmpty()) {
                Video video = response.getItems().get(0);
                long views = video.getStatistics().getViewCount().longValue();
                return Optional.of(new YoutubeVideoViewsCountResponse(views, videoId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
