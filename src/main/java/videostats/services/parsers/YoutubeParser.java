package videostats.services.parsers;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import videostats.services.parsers.model.ParsedTokens;

public class YoutubeParser implements Parser {
    
    private static final Pattern[] URL_PATTERNS = {
        Pattern.compile("(?:youtube\\.com\\/watch\\?v=|youtu\\.be\\/)([\\w-]+)(?:[?&].*)?$"),
        Pattern.compile("youtu\\.be\\/([\\w-]+)(?:[?&].*)?$"),
        Pattern.compile("youtube\\.com\\/embed\\/([\\w-]+)(?:[?&].*)?$"),
        Pattern.compile("youtube\\.com\\/v\\/([\\w-]+)(?:[?&].*)?$"),
        Pattern.compile("youtube\\.com\\/shorts\\/([\\w-]+)(?:[?&].*)?$"),
        Pattern.compile("youtube\\.com\\/live\\/([\\w-]+)(?:[?&].*)?$")
    };
    
    @Override
    public Optional<ParsedTokens> parse(String url) {
        if (url == null || url.isEmpty()) {
            return Optional.empty();
        }
        String videoId = extractVideoId(url);
        if (videoId == null) {
            return Optional.empty();
        }
        ParsedTokens tokens = new ParsedTokens("youtube", videoId);
        return Optional.of(tokens);
    }
    
    private String extractVideoId(String url) {
        for (Pattern pattern : URL_PATTERNS) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }
}
