package net.schnegg.urlshortener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class ShortUrlServiceTest {

    public static final String ID = "l7NrDoGh";
    public static final String ORIGINAL_URL = "http://www.schnegg.net/foo?param1=foo&param2=boo";
    public static final int SHORTENING_COUNT = 1;
    public static final int REDIRECT_COUNT = 2;
    public static final String ID_NO_VALUE = "id_no_value";
    @Autowired
    private ShortUrlService shortUrlService;
    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Test
    void when_creating_new_short_url_then_id_is_created_and_counts_are_0() {
        // arrange
        Mockito.when(shortUrlRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Collections.emptyList());
        // act
        ShortUrl shortUrl = shortUrlService.createOrUpdateShortUrl(ORIGINAL_URL);
        // assert
        assertThat(shortUrl.getId(), notNullValue());
        assertThat(shortUrl.getOriginalUrl(), is(ORIGINAL_URL));
        assertThat(shortUrl.getRedirectCount(), is(0));
        assertThat(shortUrl.getShorteningCount(), is(0));
    }

    @Test
    void when_creating_existing_short_url_then_shortening_count_is_incremented_and_id_kept() {
        // arrange
        ShortUrl testShortUrl = new ShortUrl(ID, ORIGINAL_URL, SHORTENING_COUNT, 0);
        List<ShortUrl> testList = Arrays.asList(testShortUrl);
        Mockito.when(shortUrlRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(testList);
        // act
        ShortUrl shortUrl = shortUrlService.createOrUpdateShortUrl(ORIGINAL_URL);
        // assert
        assertThat(shortUrl.getShorteningCount(), is(SHORTENING_COUNT + 1));
        assertThat(shortUrl.getId(), is(ID));
    }

    @Test
    void when_redirect_called_for_existing_short_url_short_url_returned_and_redirect_counter_is_incremented() {
        // arrange
        ShortUrl testShortUrl = new ShortUrl(ID, ORIGINAL_URL, SHORTENING_COUNT, REDIRECT_COUNT);
        Mockito.when(shortUrlRepository.findById(testShortUrl.getId())).thenReturn(Optional.of(testShortUrl));
        // act
        ShortUrl shortUrl = shortUrlService.getShortUrlForRedirect(ID);
        // assert
        assertThat(shortUrl.getRedirectCount(), is(REDIRECT_COUNT + 1));
        assertThat(shortUrl.getOriginalUrl(), is(ORIGINAL_URL));
        assertThat(shortUrl.getId(), is(ID));
    }

    @Test
    void when_redirect_called_for_nonexistent_shorturl_exception_is_thrown() {
        // arrange
        ShortUrl testShortUrl = new ShortUrl(ID, ORIGINAL_URL, SHORTENING_COUNT, REDIRECT_COUNT);
        Mockito.when(shortUrlRepository.findById(testShortUrl.getId())).thenReturn(Optional.of(testShortUrl));
        // act
        // assert
        Throwable exception = assertThrows(ShortUrlNotFoundException.class, () -> {
            shortUrlService.getShortUrlForRedirect(ID_NO_VALUE);
        });
        assertThat(exception.getMessage(), is("Could not find short URL " + ID_NO_VALUE));
    }

    @TestConfiguration
    static class ShortUrlServiceImplTestContextConfiguration {
        @Bean
        @Primary
        public ShortUrlRepository shortUrlRepository() {
            return Mockito.mock(ShortUrlRepository.class);
        }

        @Bean
        public ShortUrlService shortUrlService() {
            return new ShortUrlServiceImpl(shortUrlRepository());
        }
    }
}
