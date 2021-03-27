package net.schnegg.urlshortener;

public class ShortUrlNotFoundException extends RuntimeException {
    ShortUrlNotFoundException(String shortUrl) {
        super("Could not find short URL " + shortUrl);
    }
}
