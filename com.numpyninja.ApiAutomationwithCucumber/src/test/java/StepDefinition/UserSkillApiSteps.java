package StepDefinition;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.JSONObject;

import Utils.ExcelReader;
import io.cucumber.java.en.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import junit.framework.Assert;

public class UserSkillApiSteps {
	String user_skill_id;
	String user_id;
	Integer skill_id;
	Integer months_of_exp;
	Integer statuscode;
	String userskill_id;
	String message_response;
	
	TestContext testContext;
	
	public UserSkillApiSteps(TestContext testContext) {
		this.testContext = testContext;
	}
	
	@When("User sends the Get request with endpoints as {string}")
	public void user_sends_the_get_request_with_endpoints_as(String endpoint) {
	    
		testContext.response = testContext.requestSpec.when().get(endpoint);
		
	}
	
	@Then("API should return all Userskill mappings with success status code")
	public void api_should_return_all_userskill_mappings_with_success_status_code() {
	    
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response:" +testContext.res);
		//Assertions
		testContext.response.then().assertThat()
	    .statusCode(200);
		assertThat(testContext.res,
				JsonSchemaValidator.matchesJsonSchema(new File(".\\src\\test\\resources\\JsonSchemas\\UserskillsSchema.json")));
	
	}
	
	@When("User sends the Get request for specific userskill with endpoints as {string} from given sheetname {string} and rownumber {int}")
	public void user_sends_the_get_request_for_specific_userskill_with_endpoints_as_from_given_sheetname_and_rownumber(String endpoint, String sheetname, Integer rownumber) throws InvalidFormatException, IOException {
	    
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.USERSKILLS_EXCEL_Path, sheetname);
		testContext.response = testContext.requestSpec.when().get("/UserSkills/" + testData.get(rownumber).get("user_skill_id"));
	}
	
	@Then("API should return specific userskill mapping with success status code")
	public void api_should_return_specific_userskill_mapping_with_success_status_code() {
	   
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response:" +testContext.res);
		testContext.jsonPath = new JsonPath(testContext.res);
		//fetch the value from response body fields
		user_skill_id = testContext.jsonPath.getString("user_skill_id");
		user_id = testContext.jsonPath.getString("user_id");
		skill_id = testContext.jsonPath.getInt("skill_id");
		months_of_exp = testContext.jsonPath.getInt("months_of_exp");
		//Assertions
		testContext.response.then().assertThat()
	    .statusCode(200)
	    .body("user_skill_id",equalTo(user_skill_id))
	    .body("user_id",equalTo(user_id))
	    .body("skill_id",equalTo(skill_id))
	    .body("months_of_exp",equalTo(months_of_exp));
		assertThat(testContext.res,
				JsonSchemaValidator.matchesJsonSchema(new File(".\\src\\test\\resources\\JsonSchemas\\UserSkillGetByIdSchema.json")));
	
	}
	@When("User sends the Post request for new Userskill mapping with valid inputs from given sheetname {string} and rownumber {int} and endpoints as {string}")
	public void user_sends_the_post_request_for_new_userskill_mapping_with_valid_inputs_from_given_sheetname_and_rownumber_and_endpoints_as(String sheetname, Integer rownumber, String endpoint) throws InvalidFormatException, IOException {
		
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.USERSKILLS_EXCEL_Path, sheetname);
		JSONObject body_json = new JSONObject();
		
		body_json.put("user_id",testData.get(rownumber).get("user_id"));
		body_json.put("skill_id",testData.get(rownumber).get("skill_id"));
		body_json.put("months_of_exp",testData.get(rownumber).get("months_of_exp"));
		
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Content-type","application/json");
		testContext.response = testContext.requestSpec.headers(headers).body(body_json).when().post(endpoint);
		System.out.println(testContext.response.getBody().asString());
	}
	
	@Then("New UsersSkill record is successfully created with success status code")
	public void new_users_skill_record_is_successfully_created_with_success_status_code() {
	    
		statuscode= testContext.response.getStatusCode();
		
		assertEquals(statuscode, 201);
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response:" +testContext.res);
		testContext.jsonPath = new JsonPath(testContext.res);
		//fetch the value from response body fields
		user_skill_id = testContext.jsonPath.getString("user_skill_id");
		user_id = testContext.jsonPath.getString("user_id");
		skill_id = testContext.jsonPath.getInt("skill_id");
		months_of_exp = testContext.jsonPath.getInt("months_of_exp");
		message_response = testContext.jsonPath.getString("message_response");
		Response response_get = given().baseUri(BaseClass.BASE_URL_LMS)
				.auth().basic(BaseClass.USERNAME, BaseClass.PASSWORD)
				.when()
				.get(BaseClass.BASE_URL_LMS +"UserSkills/" +user_skill_id);
		System.out.println("Response from get Request:" +response_get.asString()); 
		
		//making asertions for input fields from response field	
		assertEquals(response_get.asString().contains(user_skill_id), true);
		 testContext.response.then().assertThat()
		 .body("months_of_exp", equalTo(months_of_exp))
		 .body("skill_id", equalTo(skill_id));
		//Assert.assertEquals(res.contains(userskill_id), true);
		assertEquals(testContext.res.contains(user_id), true);
		assertEquals(message_response, "Successfully Created !!");
		assertThat(testContext.res,JsonSchemaValidator.matchesJsonSchema(new File(".\\src\\test\\resources\\JsonSchemas\\UserSkillPostSchema.json")));
	
	}
	
	@When("User sends the Put request to modify the Userskill record with valid inputs with endpoints as {string} from given sheetname {string} and rownumber {int}")
	public void user_sends_the_put_request_to_modify_the_userskill_record_with_valid_inputs_with_endpoints_as_from_given_sheetname_and_rownumber(String endpoint, String sheetname, Integer rownumber) throws InvalidFormatException,NumberFormatException, IOException {
	    
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.USERSKILLS_EXCEL_Path, sheetname);
		JSONObject body_json = new JSONObject();
		
		body_json.put("user_skill_id",testData.get(rownumber).get("user_skill_id"));
		body_json.put("user_id",testData.get(rownumber).get("user_id"));
		body_json.put("skill_id",testData.get(rownumber).get("skill_id"));
		body_json.put("months_of_exp",testData.get(rownumber).get("months_of_exp"));
		
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Content-type","application/json");
		testContext.response = testContext.requestSpec.headers(headers).body(body_json).when().put("/UserSkills/"+testData.get(rownumber).get("user_skill_id"));
		System.out.println(testContext.response.getBody().asString());
	}
	
	
	@Then("record is successfully updated with success status code")
	public void record_is_successfully_updated_with_success_status_code() {
		
		System.out.println(testContext.response.getBody().asString());
		statuscode= testContext.response.getStatusCode();
		assertEquals(statuscode, 201);
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response:" +testContext.res);
		testContext.jsonPath = new JsonPath(testContext.res);
		user_skill_id = testContext.jsonPath.getString("user_skill_id");
		user_id = testContext.jsonPath.getString("user_id");
		skill_id = testContext.jsonPath.getInt("skill_id");
		months_of_exp = testContext.jsonPath.getInt("months_of_exp");
		message_response = testContext.jsonPath.getString("message_response");
		 
		Response response_get = given().baseUri(BaseClass.BASE_URL_LMS)
					.auth().basic(BaseClass.USERNAME, BaseClass.PASSWORD)
					.when()
					.get(BaseClass.BASE_URL_LMS +"UserSkills/" +user_skill_id);
		System.out.println("Response from get Request:" +response_get.asString());
		//making asertions for input fields from response field	
		assertEquals(response_get.asString().contains(user_skill_id), true);
		testContext.response.then().assertThat()
				.body("months_of_exp", equalTo(months_of_exp))
				.body("skill_id", equalTo(skill_id));
		assertEquals(testContext.res.contains(user_skill_id), true);
		assertEquals(testContext.res.contains(user_id), true);
		
		assertEquals(testContext.res.contains(message_response), true);
		assertEquals(message_response, "Successfully Updated !!");
		assertThat(testContext.res,JsonSchemaValidator.matchesJsonSchema(new File(".\\src\\test\\resources\\JsonSchemas\\UserSkillPostSchema.json")));
	
	}
	
	@When("User sends the Delete request with valid inputs with endpoints as {string} from given sheetname {string} and rownumber {int}")
	public void user_sends_the_delete_request_with_valid_inputs_with_endpoints_as_from_given_sheetname_and_rownumber(String endpoint, String sheetname, Integer rownumber) throws InvalidFormatException, IOException {
	   
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.USERSKILLS_EXCEL_Path, sheetname);
		JSONObject body_json = new JSONObject();
		
		body_json.put("user_skill_id",testData.get(rownumber).get("user_skill_id"));
		//Add header to request body stating it is JSON
		testContext.requestSpec.header("Content-Type","application/json");
		//Add JSON to the request body
		testContext.requestSpec.body(body_json.toJSONString());
		//DELETE request
		testContext.response = testContext.requestSpec.when().delete("/UserSkills/" +testData.get(rownumber).get("user_skill_id"));
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response :" +testContext.res);
		
	}
	
	@Then("record is successfully deleted with success status code")
	public void record_is_successfully_deleted_with_success_status_code() {
	   
		statuscode = testContext.response.getStatusCode();
		//Add header to request body stating it is JSON
		testContext.requestSpec.header("Content-Type","application/json");
		testContext.res = testContext.response.getBody().asString();
		testContext.jsonPath = new JsonPath(testContext.res);
		
		assertEquals(200, statuscode);
		message_response = testContext.jsonPath.getString("message_response");
		Assert.assertEquals(message_response, "The record has been deleted !!");
	}
	
	
}
