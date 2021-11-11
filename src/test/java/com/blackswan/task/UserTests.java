package com.blackswan.task;

import com.blackswan.task.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port + "/api";
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testCreateUser() {
		User user = new User();
		user.setUsername("username");
		user.setFirst_name("admin");
		user.setLast_name("admin");

		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/user", user, User.class);
		System.out.println(postResponse);

		Assert.assertNotNull(postResponse);
		Assert.assertNotNull(postResponse.getBody());
		Assert.assertEquals(postResponse.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(postResponse.getBody().getUsername(), user.getUsername());
	}

	@Test
	public void testGetAllUsers() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/user",
				HttpMethod.GET, entity, String.class);
		System.out.println(response);

		Assert.assertNotNull(response.getBody());
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void testGetUserById() {
		User user = new User();
		user.setUsername("username");
		user.setFirst_name("admin");
		user.setLast_name("admin");

		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/user", user, User.class);
		System.out.println(postResponse);
		User getUser = restTemplate.getForObject(getRootUrl() + "/user/" + postResponse.getBody().getId(), User.class);
		Assert.assertNotNull(getUser);
		Assert.assertEquals(user.getUsername(),getUser.getUsername());
	}

	@Test
	public void testCreateUserMissingFields() {
		User user = new User();
		user.setUsername("username");
		user.setFirst_name("admin");
		//user.setLast_name("admin");

		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/user", user, User.class);
		System.out.println(postResponse);

		Assert.assertNotNull(postResponse);
		Assert.assertNotNull(postResponse.getBody());
		Assert.assertEquals(postResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void testUpdateUser() {
		User user = new User();
		user.setUsername("username");
		user.setFirst_name("admin");
		user.setLast_name("admin");

		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/user", user, User.class);
		System.out.println("postResponse1");
		System.out.println(postResponse.getBody());

		User newUser = new User();
		newUser.setUsername("updatedUserName");

		restTemplate.put(getRootUrl() + "/user/" + postResponse.getBody().getId(), newUser);

		User updatedUser = restTemplate.getForObject(getRootUrl() + "/user/" + postResponse.getBody().getId(), User.class);
		System.out.println("updatedUser");
		System.out.println(updatedUser);
		Assert.assertNotNull(updatedUser);
		Assert.assertEquals(updatedUser.getUsername(), newUser.getUsername());
	}

	@Test
	public void testDeleteUser() {
		User user = new User();
		user.setUsername("username");
		user.setFirst_name("admin");
		user.setLast_name("admin");

		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/user", user, User.class);
		System.out.println("postResponse1");
		System.out.println(postResponse);

		restTemplate.delete(getRootUrl() + "/user/" + postResponse.getBody().getId());

		try {
			user = restTemplate.getForObject(getRootUrl() + "/user/" + postResponse.getBody().getId(), User.class);
			System.out.println("user after");
			System.out.println(user);
		} catch (final HttpClientErrorException e) {
			Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}

	@Test
	public void testDeleteUserNotFound() {
		User user = new User();
		user.setId(-1);
		user.setUsername("username");
		user.setFirst_name("admin");
		user.setLast_name("admin");
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> deleteResponse = restTemplate.exchange(getRootUrl() + "/user/" + user.getId(),
				HttpMethod.DELETE, entity, String.class);
		System.out.println("deleteResponse");
		System.out.println(deleteResponse);

		Assert.assertEquals(deleteResponse.getStatusCode(), HttpStatus.NOT_FOUND);

	}

}
