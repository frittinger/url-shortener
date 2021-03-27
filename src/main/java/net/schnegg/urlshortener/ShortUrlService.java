package net.schnegg.urlshortener;

public interface ShortUrlService {
    ShortUrl getShortUrlForRedirect(String id);

    ShortUrl createOrUpdateShortUrl(String originalUrl);
}
