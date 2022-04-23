package StepDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.TakesScreenshot;

import Utils.ExcelReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestContext {
	
	public  RequestSpecification requestSpec = null;
	public  Response response = null;
	public List<Map<String, String>> responseBody;
	Scenario scn = null;
	public HashMap<String, String> responsecoll;
	String res = null;
	JsonPath jsonPath;
	//Hooks
	@Before
	public void beforeHook(Scenario s) {
		this.scn = s;
	}
	
	@After
	public void afterHook(Scenario s) {
		this.scn =s;
		
	}

}
