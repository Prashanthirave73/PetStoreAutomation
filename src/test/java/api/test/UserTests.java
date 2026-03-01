package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndpoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {

	Faker faker;
	User userPayload;
	// org.apache.logging.log4j.Logger
	Logger logger;

	@BeforeClass
	public void setupData() {
		faker = new Faker();
		userPayload = new User();

		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());

		// logs
		logger = LogManager.getLogger(this.getClass());
	}

	@Test(priority = 1)
	public void testPostUser() {
		logger.info("************Creating user***************");
		Response response = UserEndpoints.createUser(userPayload);
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(), 200);

		logger.info("************User is Created***************");
	}

	@Test(priority = 2)
	public void testGetUserByName() {
		logger.info("************Reading user info***************");
		Response response = UserEndpoints.getUser(this.userPayload.getUsername());
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("************User info is displayed***************");
	}

	@Test(priority = 3)
	public void testUpdateUserByName() {

		logger.info("************Updating user ***************");

		// Update firstname,lastname and email
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());

		Response response = UserEndpoints.updateUser(this.userPayload.getUsername(), userPayload);
		// response.then().log().all();
		// response.then().log().body().statusCode(200); //Restassured Assertion
		response.then().log().body();
		Assert.assertEquals(response.getStatusCode(), 200); // TestNg Assertion

		logger.info("************User is Updated***************");

		// checking data after update
		Response responseAfterUpdate = UserEndpoints.getUser(this.userPayload.getUsername());
		Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
	}

	@Test(priority = 4)
	public void testDeleteUserByName() {

		logger.info("************Deleting User***************");

		Response response = UserEndpoints.deleteUser(this.userPayload.getUsername());
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(), 200);

		logger.info("************User Deleted***************");
	}

}
