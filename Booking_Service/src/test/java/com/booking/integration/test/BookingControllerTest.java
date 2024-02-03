package com.booking.integration.test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.booking.service.model.Booking;
import com.github.tomakehurst.wiremock.client.WireMock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8084)
@TestPropertySource(
			properties = {
					"rest.client.user.url=http://localhost:8081/v1/user",
					"rest.client.restaurant.url=http://localhost:8080/v1/restaurant"
			}
		)
public class BookingControllerTest {

	@Autowired
	WebTestClient wclient;
	
	@BeforeEach
	void setUp() {
		WireMock.reset();
	}
	
	@Test
	void testRetrieveBookindDataById() {
		var bookingId = "a";
		stubFor(get(urlEqualTo("/v1/user/"+bookingId))
				.willReturn(aResponse()
						.withHeader("content-Type", "application/json")
						.withBodyFile("userData.json")));
		
		stubFor(get(urlEqualTo("/v1/restaurant/name/"+bookingId))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withBodyFile("restaurant.json")));
		
		wclient
			.get()
			.uri("/v1/booking/id/{id}"+bookingId)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Booking.class)
			.consumeWith(b->{
				var booking = b.getResponseBody();
				assertNotNull(booking.getId());
			});
			
	}
	
	@Test
	void testRetrieveBooking_whenInvalidId_return404Response() {
		var bookId = "a";
		var invalidId = "d";
		
		stubFor(get(urlEqualTo("/v1/user/"+bookId))
				.willReturn(aResponse()
						.withHeader("content-Type", "application/json")
						.withBodyFile("userData.json")));
		
		wclient
        .get()
        .uri("/v1/booking/id/{id}", invalidId)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(String.class)
        .isEqualTo("No such Booking exist for the ID: " + invalidId);
	}
}
