package com.recipe.testCases;

import com.recipe.utilities.ExcelUtility;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.time.Duration;
import java.time.Instant;

import java.util.List;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.recipe.testBase.BaseClass;


public class TC_001_HyperTension extends BaseClass {

	static int flag, allergyFlag = 0;
	private com.recipe.utilities.ExcelUtility excel;
	int eliminated_recipe_count =  0;
	int goodRecipes_count=0;
	Instant timer_start, timer_end;
	String allergy_name;
	int recipeLinksSize;

	public String RecipeID;
	public String RecipeName;
	public String Ingredients;
	public String prep_method;
	public String PrepTime;
	public String CookTime;
	public String recipe_url;
	public String nutrient;
	public String recipeCategory;
	public String targetmorbidity;
	public String foodCategory;

	@Test
	public void hypertension() throws Exception, InterruptedException  

	{
		try {
		ExcelUtility  xlutil = new ExcelUtility(".\\TestData\\Team7_Scraping_Spatulas_ScrapedRecipes.xlsx");

		ArrayList<String> EliminateList = excel.readDataFromSheet(0, 4);
		ArrayList<String> toAddList = excel.readDataFromSheet(0, 5);
		ArrayList<String> allergyList = excel.readDataFromSheet(1, 0);


		xlutil.setCellData("HYPERTENSION", 0, 0, "RecipeID");
		xlutil.setCellData("HYPERTENSION", 0, 1, "RecipeName");
		xlutil.setCellData("HYPERTENSION", 0, 2, "RecipeCategory");
		xlutil.setCellData("HYPERTENSION", 0, 3, "FoodCategory");
		xlutil.setCellData("HYPERTENSION", 0, 4, "Preparation Time");
		xlutil.setCellData("HYPERTENSION", 0, 5, "Cooking Time");
		xlutil.setCellData("HYPERTENSION", 0, 6, "Ingredients");
		xlutil.setCellData("HYPERTENSION", 0, 7, "RecipeMethod");
		xlutil.setCellData("HYPERTENSION", 0, 8, "Targetted morbidity");
		xlutil.setCellData("HYPERTENSION", 0, 9, "Recipe URL");				
		xlutil.setCellData("HYPERTENSION", 0, 10, "Nutrients");
		xlutil.setCellData("HYPERTENSION", 0, 11, "Allergy_Info");
		xlutil.setCellData("TOADD_HYPERTENSION", 0, 0,"Recipe_Name");


		timer_start = Instant.now();
		int eliminated_recipe_count =  0;
		int total_recipes = 0,goodRecipes_count=0;
		int i=0;
		//Click on Recipes A to Z

		WebElement recipes=driver.findElement(By.xpath("//div[@id = 'toplinks']/a[5]"));
		recipes.click();
		logger.info("clicked on Recipe A to Z");

		List<WebElement> alphabets = driver.findElements(By.xpath("//table[@id = 'ctl00_cntleftpanel_mnuAlphabets']//td[position()>=3 and position()<last()]//a"));
		int alphacount = alphabets.size();
		System.out.println("Number of alphabets: " +alphacount);
		if(alphacount >0) {
			for(int a=0;a<alphacount;a++)
			{
				WebElement alphab = driver.findElement(By.xpath("//table[@id = 'ctl00_cntleftpanel_mnuAlphabets']//td[position()=last()]//a[text()='"+alphabets.get(a).getText()+"']"));
				System.out.println("Active Alphabet: "+alphab.getText());
				alphab.click();

				List <WebElement> recipe_link_pages = driver.findElements(By.xpath("//a[@class='respglink']"));
				int num =recipe_link_pages.size();

				String total_pages_to_scrape = recipe_link_pages.get(num-1).getText();
				int total_pages = (Integer.parseInt(total_pages_to_scrape));
				System.out.println("Total pages to Scrape: "+total_pages);

				//Traverse through pages

				for(int p=1; p<=total_pages;p++) 
				{

					String current_url = driver.getCurrentUrl();


					WebElement active_page=driver.findElement(By.xpath("//div[@style='text-align:right;padding-bottom:15px;'][1]/a[text()='"+p+"']"));
					System.out.println("active page: "+active_page.getText());        
					active_page.click();

					List <WebElement> recipeLinks=driver.findElements(By.xpath("//div[@class='rcc_recipecard']//div[3]/span/a"));

					recipeLinksSize=recipeLinks.size();

					System.out.println("Total Recipe cards in Page"+p+":"+recipeLinksSize);

					//Traverse through recipes in each page
					try {
						for_loop:
							for(int j=0;j<recipeLinksSize; j++ )
							{


								String Recipe_name="";


								JavascriptExecutor js = (JavascriptExecutor) driver;
								js.executeScript("window.scrollBy(0,350)", "");
								Recipe_name = recipeLinks.get(j).getText();

								recipeLinks.get(j).click();
								String ingredients = driver.findElement(By.xpath("//div[@id='rcpinglist']")).getText();

								flag = 0;
								outer:
									for(int k=0;k<EliminateList.size();k++)
									{
										String s1 =EliminateList.get(k).toLowerCase();
										String s2 = ingredients.toLowerCase();
										if(s2.contains(s1))
										{
											eliminated_recipe_count++;
											flag = 1;
											break outer;
										}
									}




								if(flag==0)
								{
									i++;
									String nutrientVal = "";
									String allergy_name = "";
									String CookTime = "";
									String prepTime = " ";

									System.out.println("********************************************************");
									System.out.println("Recipe name: " +Recipe_name);
									String recipe_url = driver.getCurrentUrl();
									String recipe_id = recipe_url.replaceAll("[^0-9]", "");
									System.out.println("Recipe ID: "+ recipe_id);
									System.out.println("****Ingredients****");

									System.out.println(ingredients);
									System.out.println("------------------------");

									String prepMethod = driver.findElement(By.xpath("//div[@id = 'ctl00_cntrightpanel_pnlRcpMethod']")).getText();
									System.out.println("PreparationMethod :");
									System.out.println(prepMethod);


									System.out.println("****************************");

									System.out.println("------------------------");
									outer1:
										for (String betterIngrediants : toAddList) 
										{
											if (ingredients.toLowerCase().contains(betterIngrediants.trim())) 
											{
												goodRecipes_count++;
												System.out.println("GOOD TO HAVE RECIPE: " + Recipe_name);

												xlutil.setCellData("TOADD_HYPERTENSION", i, 0,Recipe_name);
												break outer1;
											}
										}
									outer2:
										for(int k=0;k<allergyList.size();k++)
										{
											String s1 = allergyList.get(k).toLowerCase();	                   	  
											String s2 = ingredients.toLowerCase();
											if(s2.contains(s1))	                   	  {

												allergyFlag = 1;	                   		
												allergy_name = s1;
												//  System.out.println("ALLERGY TYPE: "+allergy_name+"            RECIPE NAME:       " + Recipe_name);
												xlutil.setCellData("HYPERTENSION", i, 11,allergy_name);

												break outer2;
											}
										}



									try {

										prepTime = driver.findElement(By.xpath("//time[@itemprop = 'prepTime']")).getText();
										System.out.println("Preparation Time: "+prepTime);
										xlutil.setCellData("HYPERTENSION", i, 4, PrepTime);
									}
									catch(NoSuchElementException e)
									{
										prepTime = "";
										xlutil.setCellData("HYPERTENSION", i, 4, PrepTime);
									}
									try {

										CookTime = driver.findElement(By.xpath("//time[@itemprop = 'cookTime']")).getText();
										System.out.println("CookingTime: "+CookTime);
										xlutil.setCellData("HYPERTENSION", i, 5, CookTime);
									}
									catch(NoSuchElementException e)
									{
										CookTime = "";
										xlutil.setCellData("HYPERTENSION", i, 5, CookTime);
									}

									try {
										nutrientVal = driver.findElement(By.xpath("//table[@id='rcpnutrients']")).getText();

									}catch( NotFoundException e) {
										nutrientVal=" nutrients list not found";
										logger.info(current_url, Recipe_name,  recipe_url, recipe_id, e);
									}


									System.out.println("************************");

									xlutil.setCellData("HYPERTENSION", i, 0, recipe_id);
									xlutil.setCellData("HYPERTENSION", i, 1, Recipe_name);
									xlutil.setCellData("HYPERTENSION", i, 2, getRecipeCategory().toString() );
									xlutil.setCellData("HYPERTENSION", i, 3, getFoodCategory().toString());
									xlutil.setCellData("HYPERTENSION", i, 6, ingredients);
									xlutil.setCellData("HYPERTENSION", i, 7, prepMethod);
									xlutil.setCellData("HYPERTENSION", i, 8, SetHyperTensionMorbidity().toString());
									xlutil.setCellData("HYPERTENSION", i, 9, recipe_url);
									xlutil.setCellData("HYPERTENSION", i, 10, nutrientVal);



								}
								driver.navigate().back();
								recipeLinks=driver.findElements(By.xpath("//div[@class='rcc_recipecard']//div[3]/span/a"));
							}

					}//end of recipes loop

					catch(IndexOutOfBoundsException e) {
						System.out.println("Index Out of Bound Exception");
						driver.navigate().back();
						driver.findElements(By.xpath("//div[@class='rcc_recipecard']//div[3]/span/a"));
						continue;
					}   


				}//end of recipelinkssize loop
				alphabets = driver.findElements(By.xpath("//table[@id = 'ctl00_cntleftpanel_mnuAlphabets']//td[position()>=3 and position()<last()]//a"));


				total_recipes = total_recipes + recipeLinksSize;
				System.out.println("Total Recipes:"+total_recipes);  
				System.out.println("Eliminated Recipes:"+eliminated_recipe_count);
				System.out.println("Total Better Ingredients Recipes: "+goodRecipes_count);
				timer_end = Instant.now();
				System.out.println("Time taken to execute in Minutes: "+ Duration.between(timer_start,timer_end).toMinutes());;
			}//end of alphabet loop
		}	//end of if alphabet count loop 
		}catch(Exception e) {
			
		}
	}



}