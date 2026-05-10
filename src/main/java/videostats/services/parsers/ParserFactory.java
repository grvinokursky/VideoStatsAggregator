package videostats.services.parsers;

import java.util.ArrayList;
import java.util.List;

import videostats.services.parsers.model.ParsedTokens;

public class ParserFactory {
    private final List<Parser> parsers;

    public ParserFactory() {
        parsers = new ArrayList<>();
        parsers.add(new YoutubeParser());
    }

    public ParsedTokens parse(String url) {
        for (Parser parser : parsers) {
            var res = parser.parse(url);
            if (res.isPresent()) {
                return res.get();
            }
        }
        throw new RuntimeException("ссылка имеет неподдерживаемый формат, попробуйте задать по-другому");
    }
}
