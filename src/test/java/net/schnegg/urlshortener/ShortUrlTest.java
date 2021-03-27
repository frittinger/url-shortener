package net.schnegg.urlshortener;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ShortUrlTest {

    public static final String ID = "l7NrDoGh";
    public static final String ORIGINAL_URL = "http://www.schnegg.net/foo?param1=foo&param2=boo";
    public static final int SHORTENING_COUNT = 1;
    public static final int REDIRECT_COUNT = 2;

    @Test
    void when_redirect_called_then_redirect_count_incremented() {
        // arrange
        ShortUrl shortUrl = new ShortUrl(ID, ORIGINAL_URL, SHORTENING_COUNT, REDIRECT_COUNT);
        // act
        shortUrl.incrementRedirectCount();
        // assert
        assertThat(shortUrl.getRedirectCount(), is(REDIRECT_COUNT+1));
    }

    @Test
    void when_shortening_called_then_shortening_count_incremented() {
        // arrange
        ShortUrl shortUrl = new ShortUrl(ID, ORIGINAL_URL, SHORTENING_COUNT, REDIRECT_COUNT);
        // act
        shortUrl.incrementShorteningCount();
        // assert
        assertThat(shortUrl.getShorteningCount(), is(SHORTENING_COUNT+1));
    }
}
