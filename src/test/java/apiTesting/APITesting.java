package apiTesting;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.*;


public class APITesting {

	@Test
	public void test_NumberOfCircuitsFor2017Season_ShouldBe20() {
        
	    RestAssured.given().
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.circuitId",hasSize(20));
	}
	@Test
	public void test_ResponseHeaderData_ShouldBeCorrect() {
	        
		 RestAssured.given().
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json").
	    then().
	        assertThat().
	        statusCode(200).
	    and().
	        contentType(ContentType.JSON).
	    and().
	        header("Content-Length",equalTo("4551"));
	}
	@Test
	public void test_Md5CheckSumForTest_ShouldBe098f6bcd4621d373cade4e832627b4f6() {
	    
	    String originalText = "test";
	    String expectedMd5CheckSum = "098f6bcd4621d373cade4e832627b4f6";
	        
	    RestAssured.given().
	        param("text",originalText).
	    when().
	        get("http://md5.jsontest.com").
	    then().
	        assertThat().
	        body("md5",equalTo(expectedMd5CheckSum));
	}
	@Test(enabled=false)
	public void test_NumberOfCircuits_ShouldBe20_Parameterized() {
	        
	    String season = "2017";//if will give 2016 ,will produce error because 21 race was there in 2016
	    int numberOfRaces = 20;
	        
	    RestAssured.given().
	        pathParam("raceSeason",season).
	    when().
	        get("http://ergast.com/api/f1/{raceSeason}/circuits.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.circuitId",hasSize(numberOfRaces));
	}
	@DataProvider(name="seasonsAndNumberOfRaces")
	public Object[][] createTestDataRecords() {
	    return new Object[][] {
	        {"2017",20},
	        {"2016",21},
	        {"1966",9}
	    };
	}
	@Test(dataProvider="seasonsAndNumberOfRaces")
	public void test_NumberOfCircuits_ShouldBe_DataDriven(String season, int numberOfRaces) {
	                
	    RestAssured.given().
	        pathParam("raceSeason",season).
	    when().
	        get("http://ergast.com/api/f1/{raceSeason}/circuits.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.circuitId",hasSize(numberOfRaces));
	}
	@Test
	public void test_ScenarioRetrieveFirstCircuitFor2017SeasonAndGetCountry_ShouldBeAustralia() {
	        
	    // First, retrieve the circuit ID for the first circuit of the 2017 season
	    String circuitId = RestAssured.given().
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json").
	    then().
	        extract().
	        path("MRData.CircuitTable.Circuits.circuitId[0]");
	        
	    // Then, retrieve the information known for that circuit and verify it is located in Australia
	    RestAssured.given().
	        pathParam("circuitId",circuitId).
	    when().
	        get("http://ergast.com/api/f1/circuits/{circuitId}.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.Location[0].country",equalTo("Australia"));
	}
	ResponseSpecification checkStatusCodeAndContentType = 
		    new ResponseSpecBuilder().
		        expectStatusCode(200).
		        expectContentType(ContentType.JSON).
		        build();
	@Test
	public void test_NumberOfCircuits_ShouldBe20_UsingResponseSpec() {
	        
		 RestAssured.given().
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json").
	    then().
	        assertThat().
	        spec(checkStatusCodeAndContentType).
	    and().
	        body("MRData.CircuitTable.Circuits.circuitId",hasSize(20));
	}
}
