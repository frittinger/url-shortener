package net.schnegg.urlshortener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
class UrlshortenerApplicationTests {

	@Autowired
	private ShortUrlController shortUrlController;

	@Autowired
	private ShortUrlService shortUrlService;

	@Test
	void when_application_starts_then_controller_is_non_null() {
		assertThat(shortUrlController, notNullValue());
	}

	@Test
	void when_application_starts_then_service_is_non_null() {
		assertThat(shortUrlService, notNullValue());
	}
}
