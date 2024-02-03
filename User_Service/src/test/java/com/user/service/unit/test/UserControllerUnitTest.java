package com.user.service.unit.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.user.service.model.Address;
import com.user.service.model.User;
import com.user.service.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest
public class UserControllerUnitTest {

	@MockBean
	private UserRepository userRepo;
	
	@Autowired
	private WebTestClient wclient;
	
	static String USER_URL = "/v1/user";
	
	@Test
	void addUserTest() {
		User user = new User("a","venkat",new Address("bangalore","karnataka", Long.valueOf(58955)),Long.valueOf(978465455));
	
		when(userRepo.save(isA(User.class))).thenReturn(Mono.just(user));
		
		wclient
			.post()
			.uri(USER_URL)
			.bodyValue(user)
			.exchange()
			.expectStatus()
			.isCreated()
			.expectBody(User.class)
			.consumeWith(u->{
				var savedUser = u.getResponseBody();
				assert savedUser!=null;
				assertNotNull(savedUser.getId());
			});										
	}
	
	@Test
	void testGetUser() {
		var users = List.of(
				new User("a","venkat",new Address("bangalore","karnataka", Long.valueOf(58955)),Long.valueOf(978465455)),
				new User("b","vishnu",new Address("Kolkatha","kolkatha", Long.valueOf(58955)),Long.valueOf(978465455)),
				new User("c","pavan",new Address("pune","mumbai", Long.valueOf(58955)),Long.valueOf(978465455))
				);
		when(userRepo.findAll()).thenReturn(Flux.fromIterable(users));
		wclient
			.get()
			.uri(USER_URL)
			.exchange()
			.expectStatus()
			.is2xxSuccessful()
			.expectBodyList(User.class)
			.hasSize(3);
	}
	
	@Test
	void getUserByIdTest() {
		var user = new User("a","venkat",new Address("bangalore","karnataka", Long.valueOf(58955)),Long.valueOf(978465455));
		when(userRepo.findById(anyString())).thenReturn(Mono.just(user));
		
		wclient
			.get()
			.uri(USER_URL+"/a")
			.exchange()
			.expectStatus()
			.is2xxSuccessful()
			.expectBody(User.class)
			.consumeWith(u->{
				var userBody = u.getResponseBody();
				assertNotNull(userBody.getId());
			});
	}
	
	@Test
	void updateUserTest() {
		when(userRepo.findById(anyString())).thenReturn(Mono.just(new User("a","venkat",new Address("bangalore","karnataka", Long.valueOf(58955)),Long.valueOf(978465455))));
		when(userRepo.save(isA(User.class)))
			.thenReturn(Mono.just(new User("a","Pavan",new Address("bangalore","karnataka", Long.valueOf(58955)),Long.valueOf(978465455))));
		
		wclient
			.put()
			.uri(USER_URL+"/a")
			.exchange()
			.expectStatus()
			.is2xxSuccessful()
			.expectBody(User.class);
	}
	
	@Test
	void deleteUserTest() {
		when(userRepo.findById(anyString())).thenReturn(Mono.just(new User("a","venkat",new Address("bangalore","karnataka", Long.valueOf(58955)),Long.valueOf(978465455))));
		when(userRepo.deleteById(anyString()))
			.thenReturn(Mono.empty());
		wclient
			.delete()
			.uri(USER_URL+"/a")
			.exchange()
			.expectStatus()
			.isNoContent();
	}
}
