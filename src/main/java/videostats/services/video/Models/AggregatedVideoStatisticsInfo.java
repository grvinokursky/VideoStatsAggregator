package videostats.services.video.Models;

import java.math.BigInteger;

public class AggregatedVideoStatisticsInfo {
    private int videosCount;
    private BigInteger videosViewsCount;

    public AggregatedVideoStatisticsInfo(int videosCount, BigInteger videosViewsCount) {
        this.videosCount = videosCount;
        this.videosViewsCount = videosViewsCount;
    }

    public int getVideosCount() {
        return videosCount;
    }

    public void setVideosCount(int videosCount) {
        this.videosCount = videosCount;
    }

    public BigInteger getVideosViewsCount() {
        return videosViewsCount;
    }

    public void setVideosViewsCount(BigInteger videosViewsCount) {
        this.videosViewsCount = videosViewsCount;
    }
}
