package StepDefinition;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import Utils.ExcelReader;
import io.cucumber.java.en.*;
import io.restassured.module.jsv.JsonSchemaValidator;

public class UserSkillMapGetApi {
	
	TestContext testContext;
	
	public UserSkillMapGetApi(TestContext testContext) {
		this.testContext = testContext;
	}
	
	@When("User sends Get request to fetch List of all users with all skill details with endpoint as {string}")
	public void user_sends_get_request_to_fetch_list_of_all_users_with_all_skill_details_with_endpoint_as(String endpoint) {
	    
		testContext.response = testContext.requestSpec.when().get(endpoint);
		
	}
	
	@Then("API should return List of all users with all skill details with success status code")
	public void api_should_return_list_of_all_users_with_all_skill_details_with_success_status_code() {
	    
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response:" +testContext.res);
		//Assertions
		testContext.response.then().assertThat()
	    .statusCode(200);
		
	}
	
	@When("User sends Get request to fetch List of user with the skill details by USER_ID with endpoint as {string} from given sheetname {string} and rownumber {int}")
	public void user_sends_get_request_to_fetch_list_of_user_with_the_skill_details_by_user_id_with_endpoint_as_from_given_sheetname_and_rownumber(String endpoint, String sheetname, Integer rownumber) throws InvalidFormatException, IOException {
	   
		
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.USERSKILLSMAPPING_EXCEL_Path, sheetname);
		testContext.response = testContext.requestSpec.when().get("/UserSkillsMap/" + testData.get(rownumber).get("user_id"));
	}
	
	@Then("API should return List of user with the skill details by USER_ID with success status code")
	public void api_should_return_list_of_user_with_the_skill_details_by_user_id_with_success_status_code() {
	    
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response:" +testContext.res);
		//Assertions
		testContext.response.then().assertThat()
	    .statusCode(200);
		assertThat(testContext.res,JsonSchemaValidator.matchesJsonSchema(new File(".\\src\\test\\resources\\JsonSchemas\\UserSkillMappingGetByUserId.json")));
	}
	
	@When("User sends Get request to fetch List of all users details by SKILL_ID with endpoint as {string}")
	public void user_sends_get_request_to_fetch_list_of_all_users_details_by_skill_id_with_endpoint_as(String endpoint) {
	   
		testContext.response = testContext.requestSpec.when().get(endpoint);
	}
	
	@When("User sends Get request to fetch List of all users details by SKILL_ID with endpoint as {string} from given sheetname {string} and rownumber {int}")
	public void user_sends_get_request_to_fetch_list_of_all_users_details_by_skill_id_with_endpoint_as_from_given_sheetname_and_rownumber(String endpoint, String sheetname, Integer rownumber) throws InvalidFormatException, IOException {
	    
		ExcelReader reader = new ExcelReader();
		List<Map<String,String>> testData = reader.getData(BaseClass.USERSKILLSMAPPING_EXCEL_Path, sheetname);
		testContext.response = testContext.requestSpec.when().get("/UsersSkillsMap/" + testData.get(rownumber).get("skill_id"));
	}
	
	@Then("API should return List of all users details by SKILL_ID with success status code")
	public void api_should_return_list_of_all_users_details_by_skill_id_with_success_status_code() {
	    
		testContext.res = testContext.response.getBody().asString();
		System.out.println("Response:" +testContext.res);
		//Assertions
		testContext.response.then().assertThat()
	    .statusCode(200);
		
	}



}
