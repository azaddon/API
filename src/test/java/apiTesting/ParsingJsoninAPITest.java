package apiTesting;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import java.util.List;
import static io.restassured.RestAssured.given;

public class ParsingJsoninAPITest {
	public static Response doGetRequest(String endpoint) {
		RestAssured.defaultParser = Parser.JSON;
		return given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when().get(endpoint).then()
				.contentType(ContentType.JSON).extract().response();
	}
// testing merge Feature1 changes to master
	
	public static void main(String[] args) {
		Response response = doGetRequest("https://jsonplaceholder.typicode.com/users");
		List<String> jsonResponse = response.jsonPath().getList("$");
		System.out.println(jsonResponse.size());
		System.out.print("testing merge Feature1 changes to master");
	}
}
