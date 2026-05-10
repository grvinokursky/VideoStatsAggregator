package videostats.services.video;

import videostats.model.Video;
import videostats.model.VideoStats;
import videostats.repository.VideoRepository;
import videostats.repository.VideoStatsRepository;
import videostats.services.parsers.ParserFactory;
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
    private ParserFactory parserFactory;

    public VideoService(
            VideoRepository videoRepository,
            VideoStatsRepository videoStatsRepository,
            YoutubeService youtubeService,
            ParserFactory parserFactory) {
        this.videoRepository = videoRepository;
        this.videoStatsRepository = videoStatsRepository;
        this.youtubeService = youtubeService;
        this.parserFactory = parserFactory;
    }

    public void TrackVideo(long userId, String videoUrl) {
        var parsedUrl = parserFactory.parse(videoUrl);

        var videoStatistics = getVideoStatistics(
                parsedUrl.getVideoId(),
                parsedUrl.getPlatform());
        
        videoStatsRepository.save(videoStatistics);

        videoRepository.save(new Video(
                videoUrl,
                parsedUrl.getVideoId(),
                parsedUrl.getPlatform(),
                userId,
                videoStatistics
        ));
    }

    public List<VideoStatisticInfo> GetStatisticsForVideos(long userId) {
        var videos = videoRepository.findByUserId(userId, videoStatsRepository);

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
        var videos = videoRepository.findByUserId(userId, videoStatsRepository);

        var videosViewsCount = BigInteger.ZERO;
        for (Video video : videos) {
            videosViewsCount = videosViewsCount.add(BigInteger.valueOf(video.getVideoStats().getViewCount()));
        }

        return new AggregatedVideoStatisticsInfo(
                videos.size(),
                videosViewsCount);
    }

    public void RefreshVideosStatistics(long userId) {
        var videos = videoRepository.findByUserId(userId, videoStatsRepository);

        for (Video video : videos) {
            var videoStats = getVideoStatistics(
                    video.getVideoId(),
                    video.getPlatform()
            );
            videoStatsRepository.save(videoStats);
        }
    }

    private VideoStats getVideoStatistics(String videoId, String platform) {
        switch (platform) {
            case "youtube":
                var youtubeVideoViewsCountResponse = youtubeService.GetVideoViewsCount(videoId);
                if (youtubeVideoViewsCountResponse.isEmpty()) {
                    return videoStatsRepository.findByPlatformAndVideoId(platform, videoId).get();
                }

                return new VideoStats(
                        youtubeVideoViewsCountResponse.get().getVideoId(),
                        platform,
                        youtubeVideoViewsCountResponse.get().getViewsCount(),
                        LocalDateTime.now());
            default:
                throw new RuntimeException(String.format("Платформа '%s' не поддерживается.", platform));
        }
    }
}
