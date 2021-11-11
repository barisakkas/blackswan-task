package com.blackswan.task;

import com.blackswan.task.model.Task;
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

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskTests {

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
	public void testCreateTask() {
		User user = new User();
		user.setUsername("username");
		user.setFirst_name("admin");
		user.setLast_name("admin");

		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/user", user, User.class);

		Task task = new Task();
		task.setName("task");
		task.setDescription("task desc");
		task.setDate_time(LocalDateTime.now());

		ResponseEntity<Task> taskResponse = restTemplate.postForEntity(getRootUrl() + "/user/" + postResponse.getBody().getId() + "/task", task, Task.class);
		System.out.println(taskResponse);

		Assert.assertNotNull(taskResponse);
		Assert.assertNotNull(taskResponse.getBody());
		Assert.assertEquals(taskResponse.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(taskResponse.getBody().getName(), task.getName());
	}

	@Test
	public void testGetTasks() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/user/1/task",
				HttpMethod.GET, entity, String.class);
		System.out.println(response);

		Assert.assertNotNull(response.getBody());
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void testUpdateTask() {
		User user = new User();
		user.setUsername("username");
		user.setFirst_name("admin");
		user.setLast_name("admin");

		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/user", user, User.class);

		Task task = new Task();
		task.setName("task");
		task.setDescription("task desc");
		task.setDate_time(LocalDateTime.now());

		ResponseEntity<Task> taskResponse = restTemplate.postForEntity(getRootUrl() + "/user/" + postResponse.getBody().getId() + "/task", task, Task.class);
		System.out.println(taskResponse);

		Task newTask = new Task();
		newTask.setName("updated task");

		restTemplate.put(getRootUrl() + "/user/" + postResponse.getBody().getId() + "/task/" + taskResponse.getBody().getId(), newTask);

		Task updatedTask = restTemplate.getForObject(getRootUrl() + "/user/" + postResponse.getBody().getId() + "/task/" + taskResponse.getBody().getId(), Task.class);
		System.out.println("updatedTask");
		System.out.println(updatedTask);
		Assert.assertNotNull(updatedTask);
		Assert.assertEquals(updatedTask.getName(), newTask.getName());
	}

	@Test
	public void testDeleteTaskNotFound() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> deleteResponse = restTemplate.exchange(getRootUrl() + "/user/1/task/1",
				HttpMethod.DELETE, entity, String.class);
		System.out.println("deleteResponse");
		System.out.println(deleteResponse);

		Assert.assertEquals(deleteResponse.getStatusCode(), HttpStatus.NOT_FOUND);

	}

}
