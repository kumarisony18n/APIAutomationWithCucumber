package StepDefinition;

import static io.restassured.RestAssured.given;
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

public class SkillApiSteps {
	
	int skill_id;
	String skill_name;
	int statuscode;
	String  message_response;
	TestContext testContext;
	
	public SkillApiSteps(TestContext testContext) {
		this.testContext = testContext;
	}
	
	
	@When("User sends Get request to fetch all skill data with endpoint as {string}")
	public void user_sends_get_request_to_fetch_all_skill_data_with_endpoint_as(String endpoint) {
	    
		testContext.response = testContext.requestSpec.when().get(endpoint);
	}
	@Then("API should return all skill data with success status code")
	public void api_should_return_all_skill_data_with_success_status_code() {
		testContext.response.then().assertThat()
	    .statusCode(200);
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response:" +testContext.res);
		testContext.jsonPath = new JsonPath(testContext.res);
		
		skill_name = testContext.jsonPath.getString("skill_name");
		assertEquals(testContext.res.contains("Java"), true);
		assertThat(testContext.res,
				JsonSchemaValidator.matchesJsonSchema(new File(".\\src\\test\\resources\\JsonSchemas\\SkillsSchema.json")));
	}

	
	@When("User sends Get request to fetch specific skill data with endpoint as {string} from given sheetname {string} and rownumber {int}")
	public void user_sends_get_request_to_fetch_specific_skill_data_with_endpoint_as_from_given_sheetname_and_rownumber(String endpoint, String sheetname, Integer rownumber) throws InvalidFormatException, IOException {
	    
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.SKILLS_EXCEL_Path, sheetname);
		testContext.response = testContext.requestSpec.when().get("/Skills/" + testData.get(rownumber).get("skill_id"));
	}
	
	@Then("API should return record for specific skill with success status code")
	public void api_should_return_record_for_specific_skill_with_success_status_code() {
	    
		testContext.response.then().assertThat()
	 	.statusCode(200);
	   
		assertThat(testContext.response.asString(),
				JsonSchemaValidator.matchesJsonSchema(new File(".\\src\\test\\resources\\JsonSchemas\\SkillGetByIdSchema.json")));
	
	}

	@When("User sends the Post request with valid inputs from given sheetname {string} and rownumber {int} and and endpoints as {string}")
	public void user_sends_the_post_request_with_valid_inputs_from_given_sheetname_and_rownumber_and_and_endpoints_as(String sheetname, Integer rownumber, String endpoint) throws InvalidFormatException, IOException {
	    
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.SKILLS_EXCEL_Path, sheetname);
		JSONObject body_json = new JSONObject();
		body_json.put("skill_name",testData.get(rownumber).get("skill_name"));
		
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Content-type","application/json");
		testContext.response = testContext.requestSpec.headers(headers).body(body_json).when().post(endpoint);
		System.out.println(testContext.response.getBody().asString());
	}
		
	@Then("New skill record is successfully created with success status code")
	public void new_skill_record_is_successfully_created_with_success_status_code() {
	    
		statuscode= testContext.response.getStatusCode();
		
		assertEquals(statuscode, 201);
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response:" +testContext.res);
		testContext.jsonPath = new JsonPath(testContext.res);
		skill_id  = testContext.jsonPath.getInt("skill_id");
		skill_name = testContext.jsonPath.getString("skill_name");
		message_response = testContext.jsonPath.getString("message_response");
		//checking for the user_id created by calling Get method for that user_id
		Response response_get = given().baseUri(BaseClass.BASE_URL_LMS)
					.auth().basic(BaseClass.USERNAME, BaseClass.PASSWORD)
					.when()
					.get(BaseClass.BASE_URL_LMS +"Skills/"+skill_id);
		System.out.println("Response from get Request:" +response_get.asString());
		 //making assertions to check response has request fields 
		response_get.then().assertThat()
		 .body("skill_id", equalTo(skill_id));
		//making asertions for input fields from response field	
		assertEquals(testContext.res.contains(skill_name), true);		
		assertEquals(message_response, "Successfully Created !!");
		assertThat(testContext.response.asString(),
		JsonSchemaValidator.matchesJsonSchema(new File(".\\src\\test\\resources\\JsonSchemas\\SkillsPostSchema.json")));
		
	}
	
	@When("User sends Put request for Skill Api with endpoint as {string} and from given sheetname {string} and rownumber {int}")
	public void user_sends_put_request_for_skill_api_with_endpoint_as_and_from_given_sheetname_and_rownumber(String endpoint, String sheetname, Integer rownumber) throws InvalidFormatException, IOException {
		
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.SKILLS_EXCEL_Path, sheetname);
		JSONObject body_json = new JSONObject();
		body_json.put("skill_id",testData.get(rownumber).get("skill_id"));
		body_json.put("skill_name",testData.get(rownumber).get("skill_name"));
		
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Content-type","application/json");
		testContext.response = testContext.requestSpec.headers(headers).body(body_json).when().put("/Skills/" +testData.get(rownumber).get("skill_id"));
		System.out.println(testContext.response.getBody().asString());
		
	}
	
	@Then("Skill record is successfully updated with success status code")
	public void skill_record_is_successfully_updated_with_success_status_code() {
	   
		statuscode= testContext.response.getStatusCode();
		assertEquals(statuscode, 201);
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response:" +testContext.res);
		testContext.jsonPath = new JsonPath(testContext.res);
		skill_id  = testContext.jsonPath.getInt("skill_id");
		message_response = testContext.jsonPath.getString("message_response");
		//checking for the user_id created by calling Get method for that user_id
		Response response_get = given().baseUri(BaseClass.BASE_URL_LMS)
							.auth().basic(BaseClass.USERNAME, BaseClass.PASSWORD)
							.when()
							.get(BaseClass.BASE_URL_LMS +"Skills/"+skill_id);
		System.out.println("Response from get Request:" +response_get.asString());
		//making assertions to check response has request fields 
		response_get.then().assertThat()
				 .body("skill_id", equalTo(skill_id));
		//making asertions for input fields from response field	
		assertEquals(testContext.res.contains(message_response), true);
		//Schema Validation	
		assertThat(testContext.response.asString(),JsonSchemaValidator.matchesJsonSchema(new File(".\\src\\test\\resources\\JsonSchemas\\SkillsPostSchema.json")));
				
		}
	
	@When("User sends the Delete request to SkillApi with valid input and endpoints as {string} from given sheetname {string} and rownumber {int}")
	public void user_sends_the_delete_request_to_skill_api_with_valid_input_and_endpoints_as_from_given_sheetname_and_rownumber(String endpoint, String sheetname, Integer rownumber) throws InvalidFormatException, IOException {
	   
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.SKILLS_EXCEL_Path, sheetname);
		JSONObject body_json = new JSONObject();
		body_json.put("skill_id",testData.get(rownumber).get("skill_id"));
		//Add header to request body stating it is JSON
		testContext.requestSpec.header("Content-Type","application/json");
		//Add JSON to the request body
		testContext.requestSpec.body(body_json.toJSONString());
		//DELETE request
		testContext.response = testContext.requestSpec.when().delete("/Skills/" +testData.get(rownumber).get("skill_id"));
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response :" +testContext.res);
	}
	
	@Then("skill record is successfully deleted with success status code")
	public void skill_record_is_successfully_deleted_with_success_status_code() {
	    
		statuscode = testContext.response.getStatusCode();
		//Add header to request body stating it is JSON
		testContext.requestSpec.header("Content-Type","application/json");
		testContext.res = testContext.response.getBody().asString();
		testContext.jsonPath = new JsonPath(testContext.res);
		
		Assert.assertEquals(200, statuscode);
		message_response = testContext.jsonPath.getString("message_response");
		Assert.assertEquals(message_response, "The record has been deleted !!");
		
	}
	
}
