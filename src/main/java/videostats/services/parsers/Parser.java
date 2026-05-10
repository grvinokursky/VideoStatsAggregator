package videostats.services.parsers;

import java.util.Optional;

import videostats.services.parsers.model.ParsedTokens;

public interface Parser {
    public Optional<ParsedTokens> parse(String url);
}
