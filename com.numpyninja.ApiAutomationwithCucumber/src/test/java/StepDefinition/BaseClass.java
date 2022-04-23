package StepDefinition;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseClass {
	
	public static final String BASE_URL_JOBS = "https://jobs123.herokuapp.com";
	public static final String BASE_URL_LMS = "https://springboot-lms-userskill.herokuapp.com/";
	public static final String USERNAME = "APIPROCESSING";
	public static final String PASSWORD = "2xx@Success";
	public static final String USERS_EXCEL_Path = "./src/test/java/Utils/UsersApi.xlsx";
	public static final String USERSKILLS_EXCEL_Path = "./src/test/java/Utils/UserSkills.xlsx";
	public static final String SKILLS_EXCEL_Path = "./src/test/java/Utils/SkillsApi.xlsx";
	public static final String USERSKILLSMAPPING_EXCEL_Path = "./src/test/java/Utils/UserSkillMappingGetApi.xlsx";
}
