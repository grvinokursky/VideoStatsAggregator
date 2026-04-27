package videostats.services.video;

import videostats.model.Video;
import videostats.model.VideoStats;
import videostats.repository.VideoRepository;
import videostats.repository.VideoStatsRepository;
import videostats.services.video.Models.AggregatedVideoStatisticsInfo;
import videostats.services.video.Models.VideoStatisticInfo;
import videostats.services.youtube.YoutubeService;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public class VideoService {
    private VideoRepository videoRepository;
    private VideoStatsRepository videoStatsRepository;
    private YoutubeService youtubeService;

    public VideoService(
            VideoRepository videoRepository,
            VideoStatsRepository videoStatsRepository,
            YoutubeService youtubeService) {
        this.videoRepository = videoRepository;
        this.videoStatsRepository = videoStatsRepository;
        this.youtubeService = youtubeService;
    }

    public void TrackVideo(long userId, String videoUrl) {
        var platform = getPlatform(videoUrl);

        var videoStatistics = getVideoStatistics(
                videoUrl,
                platform,
                userId);

        videoRepository.save(new Video(
                videoUrl,
                videoStatistics.getVideoID(),
                platform,
                userId,
                videoStatistics.getViewCount(),
                LocalDateTime.now()));
    }

    public List<VideoStatisticInfo> GetStatisticsForVideos(long userId) {
        var videos = videoRepository.findByUserId(userId);

        return videos
                .stream()
                .map(x -> new VideoStatisticInfo(
                        x.getUrl(),
                        x.getPlatform(),
                        x.getVideoStats().getViewCount(),
                        x.getVideoStats().getUpdatedAt()))
                .toList();
    }

    public AggregatedVideoStatisticsInfo GetAggregatedVideoStatisticsInfo(long userId) {
        var videos = videoRepository.findByUserId(userId);

        var videosViewsCount = BigInteger.ZERO;
        for (Video video : videos) {
            videosViewsCount = videosViewsCount.add(BigInteger.valueOf(video.getVideoStats().getViewCount()));
        }

        return new AggregatedVideoStatisticsInfo(
                videos.size(),
                videosViewsCount);
    }

    public void RefreshVideosStatistics(long userId) {
        var videos = videoRepository.findByUserId(userId);

        for (Video video : videos) {
            var videoStats = getVideoStatistics(
                    video.getUrl(),
                    video.getPlatform(),
                    video.getUserID());

            videoStatsRepository.save(videoStats);
        }
    }

    private VideoStats getVideoStatistics(String url, String platform, long userId) {
        switch (platform) {
            case "youtube":
                var youtubeVideoViewsCountResponse = youtubeService.GetVideoViewsCount(url);

                return new VideoStats(
                        youtubeVideoViewsCountResponse.getVideoId(),
                        youtubeVideoViewsCountResponse.getViewsCount(),
                        LocalDateTime.now());
            default:
                throw new RuntimeException(String.format("Платформа '%s' не поддерживается.", platform));
        }
    }

    private String getPlatform(String videoUrl) {
        if (videoUrl.startsWith("https://www.youtube.com")) {
            return "youtube";
        } else {
            throw new RuntimeException("Не поддерживается источник видео");
        }
    }
}
