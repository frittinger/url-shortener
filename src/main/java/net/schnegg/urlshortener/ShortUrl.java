package net.schnegg.urlshortener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ShortUrl {

    @Id
    String id;
    // note this will only handle URLs up to 255 chars
    String originalUrl;
    int shorteningCount;
    int redirectCount;

    public void incrementShorteningCount() {
        this.shorteningCount++;
    }

    public void incrementRedirectCount() {
        this.redirectCount++;
    }
}
