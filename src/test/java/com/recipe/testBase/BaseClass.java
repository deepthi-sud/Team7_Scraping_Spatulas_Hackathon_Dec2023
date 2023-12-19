package com.recipe.testBase;

import java.time.Duration;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;
import org.testng.annotations.Parameters;
import com.recipe.testBase.BaseClass.Comorbidity;
import com.recipe.testBase.BaseClass.FoodCategory;
import com.recipe.testBase.BaseClass.RecipeCategory;

public class BaseClass {
	
	public static WebDriver driver;
	public static ResourceBundle rb;
	public Logger logger;
		
	@BeforeClass
	@Parameters("browser")
	public void setup(String br) throws Exception
	{
		rb=ResourceBundle.getBundle("config");
		logger=LogManager.getLogger(this.getClass()); 
		ChromeOptions options=new ChromeOptions();
		
		//disabled downloading images 
		options.addArguments("--blink-settings=imagesEnabled=false");
		
		if(br.equalsIgnoreCase("chrome"))
		{
			//driver=new ChromeDriver();
			options.addArguments("--headless");
			driver=new ChromeDriver(options);
		}
		else if(br.equalsIgnoreCase("edge"))
		{
			driver=new EdgeDriver();
		}
		else if(br.equalsIgnoreCase("firefox"))
				{
					driver=new FirefoxDriver();
				}
		else
		{
			throw new Exception("Browser is not correct");
		}
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		
		driver.get(rb.getString("baseURL"));
		logger.info("read app url");

		driver.manage().window().maximize();
		logger.info("window maximized");
		}
	
	
	@AfterClass
	public void tearDown()
	{
		driver.quit();
	}

	public enum FoodCategory {
		VEGETARIAN , VEGAN,EGGETARIAN, JAIN

	}
	
	public enum RecipeCategory {
		NONE, BREAKFAST ,LUNCH, SNACK , DINNER
		}
	
	public enum Comorbidity {
		DIABETES , PCOS , HYPOTHYROIDISM , HYPERTENSION
		}
	
	public Comorbidity SetPCOSMorbidity() {
		return Comorbidity.PCOS;
		}	
	public Comorbidity SetDiabetesMorbidity() {
		return Comorbidity.DIABETES;
		}	
	public Comorbidity SetHyperTensionMorbidity() {
		return Comorbidity.HYPERTENSION;
		}	
	public Comorbidity SetHypoThyroidismMorbidity() {
		return Comorbidity.HYPOTHYROIDISM;
		}	
	public RecipeCategory getRecipeCategory() {
		String tags = driver.findElement(By.xpath("//div[@id = 'recipe_tags']")).getText();
		
		if (tags.toLowerCase().contains("breakfast")) {
			return RecipeCategory.BREAKFAST;

		} else if (tags.toLowerCase().contains("lunch")) {
			return RecipeCategory.LUNCH;

		} else if (tags.toLowerCase().contains("snack")) {
			return RecipeCategory.SNACK;
		} else if (tags.toLowerCase().contains("dinner")) {
			return RecipeCategory.DINNER;
		}
		return RecipeCategory.NONE;
	}
	
	public FoodCategory getFoodCategory() {
		
		String tags = driver.findElement(By.xpath("//div[@id = 'recipe_tags']")).getText();
		String ingredients = driver.findElement(By.xpath("//div[@id='rcpinglist']")).getText();
		
		if (tags.toLowerCase().contains("jain")) {
			return FoodCategory.JAIN;

		} else if (ingredients.toLowerCase().contains("egg")) {
			return FoodCategory.EGGETARIAN;
		}
		Pattern p= Pattern.compile("(milk|cheese|yogurt|icecream|buttermilk|paneer|ghee|chocolate|butter|dahi)");
		Matcher regexMatcher = p.matcher(ingredients.toLowerCase());
		if(!regexMatcher.find()) {
			return FoodCategory.VEGAN;                
		}
		return FoodCategory.VEGETARIAN;
	}
}
