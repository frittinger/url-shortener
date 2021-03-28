package net.schnegg.urlshortener;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/s")
@RestController
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    // CORS config for react dev
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/shorturls")
    public ShortUrl newShortUrl(@RequestBody ShortUrl shortUrl) {
        String originalUrl = validateUrl(shortUrl.getOriginalUrl());
        return shortUrlService.createOrUpdateShortUrl(originalUrl);
    }

    /**
     * Validates the input URL for being a proper URL including http(s) prefix etc.
     * @param originalUrl
     * @return
     */
    private String validateUrl(String originalUrl) {
        // FIXME implementation missing
        return originalUrl;
    }

    @GetMapping("/{id}")
    public ResponseEntity handleRedirect(@PathVariable String id) {
        // Error handled with ShortUrlNotFoundAdvice
        ShortUrl shortUrl = shortUrlService.getShortUrlForRedirect(id);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, shortUrl.getOriginalUrl())
                .header(HttpHeaders.CACHE_CONTROL, "no-cache").build();
    }

}
