package net.schnegg.urlshortener;

import org.springframework.web.bind.annotation.*;

@RestController
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @PostMapping("/shorturls")
    public ShortUrl newShortUrl(@RequestBody ShortUrl shortUrl) {
        // FIXME error handling
        String originalUrl = shortUrl.getOriginalUrl();
        return shortUrlService.createOrUpdateShortUrl(originalUrl);
    }

    @GetMapping("/r/{id}")
    public ShortUrl redirect(@PathVariable String id) {
        // FIXME send redirect
        return shortUrlService.getShortUrlForRedirect(id);
    }
}
