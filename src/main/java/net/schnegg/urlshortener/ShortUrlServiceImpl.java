package net.schnegg.urlshortener;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlServiceImpl(ShortUrlRepository shorturlRepository) {
        this.shortUrlRepository = shorturlRepository;
    }

    public ShortUrl getShortUrlForRedirect(String id) {

        ShortUrl shortUrl = shortUrlRepository.findById(id).orElseThrow(() -> new ShortUrlNotFoundException(id));
        shortUrl.incrementRedirectCount();
        shortUrlRepository.save(shortUrl);
        return shortUrl;
    }

    public ShortUrl createOrUpdateShortUrl(String originalUrl) {
        if (null == originalUrl) {
            throw new ShortUrlNotFoundException("");
        }

        List<ShortUrl> shortUrls = shortUrlRepository.findByOriginalUrl(originalUrl);

        ShortUrl shortUrl;
        if (shortUrls.isEmpty()) {
            String id = generateUniqueShortId();
            shortUrl = new ShortUrl(id, originalUrl, 0, 0);
        } else {
            shortUrl = shortUrls.get(0);
            shortUrl.incrementShorteningCount();
        }
        shortUrlRepository.save(shortUrl);
        return shortUrl;
    }

    private String generateUniqueShortId() {
        String id = RandomStringUtils.randomAlphanumeric(8);
        id = checkUniqueness(id);
        return id;
    }

    private String checkUniqueness(String id) {
        // FIXME
        // could end up endless if repeated
        // have a fixed number of retries and give up afterwards ...
        //Optional<ShortUrl> shortUrl = shortUrlRepository.findById(id);
        return id;
    }
}
