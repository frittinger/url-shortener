package net.schnegg.urlshortener;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShortUrlRepository extends CrudRepository<ShortUrl, String> {
    List<ShortUrl> findByOriginalUrl(String originalUrl);
}
