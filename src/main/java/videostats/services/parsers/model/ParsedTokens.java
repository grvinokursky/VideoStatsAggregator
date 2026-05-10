package videostats.services.parsers.model;

public class ParsedTokens {
    private String plarform;
    private String videoId;

    public ParsedTokens(String platform, String videoId) {
        this.plarform = platform;
        this.videoId = videoId;
    }
    
    public String getPlatform() {
        return plarform;
    }

    public void setPlatform(String platform) {
        this.plarform = platform;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
