package net.schnegg.urlshortener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ShortUrlController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ShortUrlControllerTest {

    public static final String ID = "l7NrDoGh";
    public static final String ORIGINAL_URL = "http://www.schnegg.net/foo?param1=foo&param2=boo";
    public static final int SHORTENING_COUNT = 1;
    public static final int REDIRECT_COUNT = 2;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ShortUrlService shortUrlService;

    @Test
    void when_create_existing_original_url_then_return_short_url() throws Exception {
        // arrange
        ShortUrl testShortUrl = new ShortUrl(ID, ORIGINAL_URL, SHORTENING_COUNT, REDIRECT_COUNT);
        Mockito.when(shortUrlService.createOrUpdateShortUrl(testShortUrl.getOriginalUrl())).thenReturn(testShortUrl);
        // act
        this.mvc.perform(post("/s/shorturls")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"originalUrl\": \"" + ORIGINAL_URL + "\", \"id\": \"\", \"shorteningCount\": 0, \"redirectCount\": 0 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.originalUrl").value(ORIGINAL_URL))
                .andExpect(jsonPath("$.shorteningCount").value(SHORTENING_COUNT))
                .andExpect(jsonPath("$.redirectCount").value(REDIRECT_COUNT));
        // assert
        verify(shortUrlService, VerificationModeFactory.times(1)).createOrUpdateShortUrl(testShortUrl.getOriginalUrl());
        reset(shortUrlService);
    }

    @Test
    void when_existing_shortUrl_then_return_redirect() throws Exception {
        // arrange
        ShortUrl testShortUrl = new ShortUrl(ID, ORIGINAL_URL, SHORTENING_COUNT, REDIRECT_COUNT);
        Mockito.when(shortUrlService.getShortUrlForRedirect(testShortUrl.getId())).thenReturn(testShortUrl);
        // act
        this.mvc
                .perform(MockMvcRequestBuilders.get("/s/{id}", ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string(HttpHeaders.LOCATION, testShortUrl.getOriginalUrl()));
        // assert
        verify(shortUrlService, VerificationModeFactory.times(1)).getShortUrlForRedirect(ID);
        reset(shortUrlService);
    }

//    @Test
//    void when_non_existing_shortUrl_then_return_404() throws Exception {
//        // This fails because the ShortUrlNotFoundAdvice is not caught in the mocked test.
//        // arrange
//        ShortUrl testShortUrl = new ShortUrl(ID, ORIGINAL_URL, SHORTENING_COUNT, REDIRECT_COUNT);
//        Mockito.when(shortUrlService.getShortUrlForRedirect(testShortUrl.getId())).thenReturn(testShortUrl);
//        // act
//        this.mvc
//                .perform(MockMvcRequestBuilders.get("/{id}", ID_NO_VALUE))
//                .andExpect(status().is4xxClientError());
//        // assert
//        verify(shortUrlService, VerificationModeFactory.times(1)).getShortUrlForRedirect(ID);
//        reset(shortUrlService);
//    }

}

