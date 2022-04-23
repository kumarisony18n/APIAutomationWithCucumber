package StepDefinition;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Utils.ExcelReader;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.hamcrest.Matchers;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.*;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

public class UserApiSteps {
	
	TestContext testContext;
	int statuscode;
	String user_id;
	String  name;
	String location;
	String message_response;
	
	public UserApiSteps(TestContext testContext) {
		this.testContext = testContext;
	}
	
		
	@Given("LMS API is up and running for authorized user")
	public void lms_api_is_up_and_running_for_authorized_user() {
		testContext.requestSpec = given().baseUri(BaseClass.BASE_URL_LMS).auth().basic(BaseClass.USERNAME, BaseClass.PASSWORD);
	}

	@When("User sends Get request with endpoint as {string}")
	public void user_sends_get_request_with_endpoint_as(String endpoint) {
		testContext.response = testContext.requestSpec.when().get(endpoint);
	}

	@Then("API should return all users data with success status code")
	public void api_should_return_all_users_data_with_success_status_code() {
		
		testContext.response.then().assertThat()
		    .statusCode(200);
	}
	
	@When("User sends Get request to fetch specific user with endpoint as {string} from given sheetname {string} and rownumber {int}")
	public void user_sends_get_request_to_fetch_specific_user_with_endpoint_as_from_given_sheetname_and_rownumber(String endpoint, String sheetname, Integer rownumber) throws InvalidFormatException, IOException {
		
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.USERS_EXCEL_Path, sheetname);
		testContext.response = testContext.requestSpec.when().get("/Users/" + testData.get(rownumber).get("user_id"));
	}
	
	@Then("API should return data for specific user with success status code")
	public void api_should_return_data_for_specific_user_with_success_status_code() {
	    
		testContext.response.then().assertThat()
		 	.statusCode(200)
		    .body("name",equalTo("John,Doe"))
		    .body("time_zone", equalTo("EST"))
		    .body("location", equalTo("Charlotte"))
		 	.body("user_id",equalTo("U01"));
	}
	
	@When("User sends the Post request with valid inputs from given sheetname {string} and rownumber {int} and endpoints as {string}")
	public void user_sends_the_post_request_with_valid_inputs_from_given_sheetname_and_rownumber_and_endpoints_as(String sheetname, Integer rownumber, String endpoint) throws InvalidFormatException, IOException {
	    
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.USERS_EXCEL_Path, sheetname);
		JSONObject body_json = new JSONObject();
		body_json.put("name",testData.get(rownumber).get("name"));
		body_json.put("phone_number",testData.get(rownumber).get("phone_number"));
    	body_json.put("location",testData.get(rownumber).get("location"));
    	body_json.put("time_zone",testData.get(rownumber).get("time_zone"));
    	body_json.put("linkedin_url",testData.get(rownumber).get("linkedin_url"));
    	body_json.put("education_ug",testData.get(rownumber).get("education_ug"));
    	body_json.put("education_pg",testData.get(rownumber).get("education_pg"));
    	body_json.put("visa_status",testData.get(rownumber).get("visa_status"));
    	body_json.put("comments",testData.get(rownumber).get("comments"));

    	//Headers
    	Map<String,String> headers = new HashMap<String,String>();
 		headers.put("Content-type","application/json");
   		testContext.response = testContext.requestSpec.headers(headers).body(body_json).when().post(endpoint);
    	System.out.println(testContext.response.getBody().asString());
    			
	}

	@Then("New user record is successfully created with success status code")
	public void new_user_record_is_successfully_created_with_success_status_code() {
	   
		statuscode= testContext.response.getStatusCode();
		assertEquals(statuscode, 201);
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response:" +testContext.res);
		testContext.jsonPath = new JsonPath(testContext.res);
		user_id  = testContext.jsonPath.getString("user_id");
		 name = testContext.jsonPath.getString("name");
		 location = testContext.jsonPath.getString("location");
		 message_response = testContext.jsonPath.getString("message_response");
		//checking for the new user_id created by calling Get method for that user_id
		Response response_get = given().baseUri(BaseClass.BASE_URL_LMS)
								.auth().basic(BaseClass.USERNAME, BaseClass.PASSWORD)
								.when()
								.get(BaseClass.BASE_URL_LMS +"Users/"+user_id);
		System.out.println("Response from get Request:" +response_get.asString());
		//making assertions to check response has request fields 
		assertEquals(response_get.asString().contains(user_id), true);
		assertEquals(testContext.res.contains(name), true);
		assertEquals(testContext.res.contains(location), true);
		assertEquals(testContext.res.contains(message_response), true);
		
		assertThat(testContext.res,
				JsonSchemaValidator.matchesJsonSchema(new File(".\\src\\test\\resources\\JsonSchemas\\UsersPostSchema.json")));
		
	}

	@When("User sends Put request with endpoint as {string} and from given sheetname {string} and rownumber {int}")
	public void user_sends_put_request_with_endpoint_as_and_from_given_sheetname_and_rownumber(String endpoint, String sheetname, Integer rownumber) throws InvalidFormatException, IOException {
	   
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.USERS_EXCEL_Path, sheetname);
		JSONObject body_json = new JSONObject();
		body_json.put("user_id",testData.get(rownumber).get("user_id"));
		body_json.put("name",testData.get(rownumber).get("name"));
		body_json.put("phone_number",testData.get(rownumber).get("phone_number"));
    	body_json.put("location",testData.get(rownumber).get("location"));
    	body_json.put("time_zone",testData.get(rownumber).get("time_zone"));
    	body_json.put("linkedin_url",testData.get(rownumber).get("linkedin_url"));
    	body_json.put("education_ug",testData.get(rownumber).get("education_ug"));
    	body_json.put("education_pg",testData.get(rownumber).get("education_pg"));
    	body_json.put("visa_status",testData.get(rownumber).get("visa_status"));
    	body_json.put("comments",testData.get(rownumber).get("comments"));
    	
    	//Headers
    	Map<String,String> headers = new HashMap<String,String>();
   		headers.put("Content-type","application/json");
  		testContext.response = testContext.requestSpec.headers(headers).body(body_json.toJSONString()).when().put("/Users/" +testData.get(rownumber).get("user_id"));
    	System.out.println(testContext.response.getBody().asString());
    			
	}

	@Then("User record is successfully updated with success status code")
	public void user_record_is_successfully_updated_with_success_status_code() {
		
		System.out.println(testContext.response.getBody().asString());
		statuscode= testContext.response.getStatusCode();
		assertEquals(statuscode, 201);
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response:" +testContext.res);
		testContext.jsonPath = new JsonPath(testContext.res);
		user_id  = testContext.jsonPath.getString("user_id");
		 name = testContext.jsonPath.getString("name");
		 location = testContext.jsonPath.getString("location");
		 message_response = testContext.jsonPath.getString("message_response");
		//checking for the user_id created by calling Get method for that user_id
		 Response response_get = given().baseUri(BaseClass.BASE_URL_LMS)
					.auth().basic(BaseClass.USERNAME, BaseClass.PASSWORD)
					.when()
					.get(BaseClass.BASE_URL_LMS +"Users/"+user_id);
		 System.out.println("Response from get Request:" +response_get.asString());
		//making asertions for input fields from response field	
		assertEquals(response_get.asString().contains(user_id), true);
		assertEquals(testContext.res.contains(name), true);
		assertEquals(testContext.res.contains(location), true);
		assertEquals(testContext.res.contains(message_response), true);
		assertEquals(message_response, "Successfully Updated !!");
		assertThat(testContext.res,
				JsonSchemaValidator.matchesJsonSchema(new File(".\\src\\test\\resources\\JsonSchemas\\UsersPostSchema.json")));

	}
	
	@When("User sends the Delete request with valid input and endpoints as {string} from given sheetname {string} and rownumber {int}")
	public void user_sends_the_delete_request_with_valid_input_and_endpoints_as_from_given_sheetname_and_rownumber(String endpoint, String sheetname, Integer rownumber) throws InvalidFormatException, IOException {
	    
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.USERS_EXCEL_Path, sheetname);
		JSONObject body_json = new JSONObject();
		body_json.put("user_id",testData.get(rownumber).get("user_id"));
		
		//Add header to request body stating it is JSON
		testContext.requestSpec.header("Content-Type","application/json");
		//Add JSON to the request body
		testContext.requestSpec.body(body_json.toJSONString());
		//DELETE request
		testContext.response = testContext.requestSpec.when().delete("/Users/"+testData.get(rownumber).get("user_id"));
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response Body:" +testContext.res);
	}
	
	@Then("user record is successfully deleted with success status code")
	public void user_record_is_successfully_deleted_with_success_status_code() {
		statuscode = testContext.response.getStatusCode();
		
		testContext.requestSpec.header("Content-Type","application/json");
		testContext.res = testContext.response.getBody().asString();
		testContext.jsonPath = new JsonPath(testContext.res);
		
		assertEquals(statuscode, 200);
		message_response = testContext.jsonPath.getString("message_response");
		assertEquals(testContext.res.contains(message_response), true);
		assertEquals(message_response, "The record has been deleted !!");
		
	}

}
