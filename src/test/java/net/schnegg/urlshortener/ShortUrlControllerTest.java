package net.schnegg.urlshortener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ShortUrlController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ShortUrlControllerTest {

    public static final String ID = "l7NrDoGh";
    public static final String ORIGINAL_URL = "http://www.schnegg.net/foo?param1=foo&param2=boo";
    public static final int SHORTENING_COUNT = 1;
    public static final int REDIRECT_COUNT = 2;
    public static final String ID_NO_VALUE = "id_no_value";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ShortUrlService shortUrlService;

    @Test
    void when_existing_shortUrl_then_return_shortUrl() throws Exception {
        // arrange
        ShortUrl testShortUrl = new ShortUrl(ID, ORIGINAL_URL, SHORTENING_COUNT, REDIRECT_COUNT);
        Mockito.when(shortUrlService.getShortUrlForRedirect(testShortUrl.getId())).thenReturn(testShortUrl);
        // act
        this.mvc
                .perform(MockMvcRequestBuilders.get("/r/{id}", ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.originalUrl").value(ORIGINAL_URL));
        // assert
        verify(shortUrlService, VerificationModeFactory.times(1)).getShortUrlForRedirect(ID);
        reset(shortUrlService);
    }

}

