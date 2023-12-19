package com.recipe.testCases;
	import java.time.Duration;
import java.time.Instant;
	import java.util.List;
	import java.util.ArrayList;
	import org.openqa.selenium.By;
	import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.recipe.testBase.BaseClass;
import com.recipe.utilities.ExcelUtility;

public class TC_004_PCOS extends BaseClass{
	   	static int flag, allergyFlag = 0;
		private com.recipe.utilities.ExcelUtility excel;
		int eliminated_recipe_count =  0;
		int goodRecipes_count=0;
		Instant timer_start, timer_end;
		String allergy_name;
		
		
		public String recipeID;
		public String Recipe_name;
		public String ingredients;
		public String prep_method;
		public String PrepTime;
		public String CookTime;
		public String recipe_url;
		public String nutrient;
		public String recipeCategory;
		public String targetmorbidity;
		public String foodCategory;
				
		 @Test
		public  void pcos()  throws Exception
		{

			 try {
			   ExcelUtility  xlutil = new ExcelUtility(".\\TestData\\Team7_Scraping_Spatulas_ScrapedRecipes.xlsx");
			 ArrayList<String> EliminateList = excel.readDataFromSheet(0, 6);
			    ArrayList<String> toAddList = excel.readDataFromSheet(0, 7);
     		ArrayList<String> allergyList = excel.readDataFromSheet(1, 0);

			
				    xlutil.setCellData("PCOS", 0, 0, "RecipeID");
					xlutil.setCellData("PCOS", 0, 1, "RecipeName");
					xlutil.setCellData("PCOS", 0, 2, "RecipeCategory");
					xlutil.setCellData("PCOS", 0, 3, "FoodCategory");
					xlutil.setCellData("PCOS", 0, 4, "Preparation Time");
					xlutil.setCellData("PCOS", 0, 5, "Cooking Time");
					xlutil.setCellData("PCOS", 0, 6, "Ingredients");
					xlutil.setCellData("PCOS", 0, 7, "RecipeMethod");
					xlutil.setCellData("PCOS", 0, 8, "Targetted morbidity");
					xlutil.setCellData("PCOS", 0, 9, "Recipe URL");				
					xlutil.setCellData("PCOS", 0, 10, "Nutrients");
					xlutil.setCellData("PCOS", 0, 11, "Allergy Info");
					xlutil.setCellData("TOADD_RECIPES", 0, 0, "Recipe_Name");
								
			timer_start = Instant.now();
			int eliminated_recipe_count =  0;
			int total_recipes = 0;
			int i=0;
			
			//Click on Recipes A to Z
			WebElement recipes=driver.findElement(By.xpath("//div[@id = 'toplinks']/a[5]"));
			recipes.click();
			//commonmethods.clickRecipesAtoZ();
			logger.info("clicked on Recipe A to Z");
			        
			List<WebElement> getAlphabates=driver.findElements(By.xpath("//table[@id='ctl00_cntleftpanel_mnuAlphabets']//td[position()>=3 and position()<last()]//a"));
	        int alphabetCount=getAlphabates.size();
	        System.out.println("total alphabetes: "+alphabetCount);
	        if (alphabetCount > 0)
	        {
	        	for(int a=0;a<alphabetCount;a++)
	            {
	        		WebElement active_alphabet = driver.findElement(By.xpath("//table[@id='ctl00_cntleftpanel_mnuAlphabets']//td[position()=last()]//a[text()='" + getAlphabates.get(a).getText() + "']"));
	                System.out.println("Active alphabet: " + active_alphabet.getText());
	                active_alphabet.click();
	                
	                List <WebElement> pages = driver.findElements(By.xpath("//div[@style='text-align:right;padding-bottom:15px;'][1]/a"));
	        		
	            	int pageCount =pages.size();
	            	System.out.println("page count is: " +pageCount);
	            	String total_pages_to_scrape = pages.get(pageCount-1).getText();
	                int total_pages = (Integer.parseInt(total_pages_to_scrape));
	        		System.out.println("Total pages to Scrape: "+total_pages);
	        //Traverse through pages
	        
	        for(int p=1; p<=total_pages;p++) 
	        {
	        	        	
	        WebElement active_page=driver.findElement(By.xpath("//div[@style='text-align:right;padding-bottom:15px;'][1]/a[text()='"+p+"']"));
	        System.out.println("active page: "+active_page.getText());        
	        active_page.click();
	       
	       List <WebElement> recipeLinks=driver.findElements(By.xpath("//div[@class='rcc_recipecard']//div[3]/span/a"));
	       int recipeLinksSize=recipeLinks.size();
	      
	       System.out.println("Total Recipe cards in Page "+p+":"+recipeLinksSize);

	       try {

	       //Traverse through recipes in each page
	        for_loop:
	        	if(recipeLinksSize>0) {
	        for(int j=0;j<recipeLinksSize; j++ )
	        {         
	        	 System.out.println("Loop index j: " + j);
	        	JavascriptExecutor js = (JavascriptExecutor) driver;
	            js.executeScript("window.scrollBy(0,350)", "");
	                        
	            String Recipe_name = recipeLinks.get(j).getText();
	           
	            recipeLinks.get(j).click();
	            String recipe_url = driver.getCurrentUrl();
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
	            	 outer1:
	 	            	for (String betterIngrediants : toAddList) 
	 	            	{
	 	    				if (ingredients.toLowerCase().contains(betterIngrediants.trim())) 
	 	    				{
	 	    					goodRecipes_count++;
	 	    					System.out.println("GOOD TO HAVE RECIPE: " + Recipe_name);
	 	    					xlutil.setCellData("TOADD_RECIPES", i, 0,Recipe_name);
	 	    					break outer1;
	 	    				}
	 	    			}
	             	
	 	            	outer2:
	 	            		 for(int k=0;k<allergyList.size();k++)
	 	                     {
	 	                   	  String s1 = allergyList.get(k).toLowerCase();
	 	                   	  String s2 = ingredients.toLowerCase();
	 	                   	  if(s2.contains(s1))
	 	                   	  {	                
	 	                   		  allergyFlag = 1;
	 	                   		  allergy_name = s1;
	 	                   		  System.out.println("ALLERGY TYPE: "+allergy_name+"            RECIPE NAME:       " + Recipe_name);
	 	                   		xlutil.setCellData("PCOS", i, 11, allergy_name);
	 	                   		  
	 	                   		  break outer2;
	 	                         }
	 	                     }
	            System.out.println("********************************************************");
	            System.out.println(j+" : Recipe name: " +Recipe_name);
	             System.out.println(recipe_url);
	        	String recipeID= recipe_url.replaceAll("[^0-9]", "").toString();
	        	System.out.println("Recipe ID: "+ recipeID);
	        	System.out.println("****Ingredients****");
	        	
	        	System.out.println(ingredients);
	        	System.out.println("------------------------");
	        	String PrepTime="",CookTime="",nutrient="";
	        	try {
	        		WebElement prep_time = driver.findElement(By.xpath("//time[@itemprop = 'prepTime']"));
	        	  PrepTime=prep_time.getText().toString();
	        	        		
	        	System.out.println("PreparationTime: "+PrepTime);
	        	
	        	WebElement cook_time=driver.findElement(By.xpath("//time[@itemprop = 'cookTime']"));
	        	 CookTime = cook_time.getText().toString();
	        	       	
	        	System.out.println("CookingTime: "+CookTime);
	        	
	        	}	catch(NoSuchElementException e)
	        	{
	        		 System.err.println("Element not found: //time[@itemprop = 'prepTime']");
	        		 System.err.println("Element not found: //time[@itemprop = 'cookTime']");
	        	}
	        	WebElement method = driver.findElement(By.xpath("//div[@id='recipe_small_steps']"));
				System.out.println("----------Recipe Method: ----------------------------------------------------------------------");
				String prep_method =method.getText().toString();
			
				System.out.println(prep_method);
				

				try {
				WebElement nutrients = driver.findElement(By.xpath("//table[@id='rcpnutrients']"));
				
				 nutrient = nutrients.getText();
			
				System.out.println("----------nutrients:------------------------------------ " + nutrient);
				}catch(NoSuchElementException e)
				{
					 System.err.println("Element not found: //table[@id='rcpnutrients']");
				}
				  
	    
	        	foodCategory=getFoodCategory().toString();
	        	System.out.println("FoodCategory: "+foodCategory);
	      
	        	recipeCategory=getRecipeCategory().toString();
	        	System.out.println("RecipeCategory: "+recipeCategory);
	        	
	        	targetmorbidity=SetPCOSMorbidity().toString();
	        	System.out.println("TargettedMorbidity: "+targetmorbidity);
	            
	            
	            xlutil.setCellData("PCOS", i, 0, recipeID);
				xlutil.setCellData("PCOS", i, 1, Recipe_name);
				xlutil.setCellData("PCOS", i, 2, recipeCategory);
				xlutil.setCellData("PCOS", i, 3, foodCategory);
				xlutil.setCellData("PCOS", i, 4, PrepTime);
				xlutil.setCellData("PCOS", i, 5, CookTime);
				xlutil.setCellData("PCOS", i, 6, ingredients);
				xlutil.setCellData("PCOS", i, 7, prep_method);
				xlutil.setCellData("PCOS", i, 8, targetmorbidity);
				xlutil.setCellData("PCOS", i, 9, recipe_url);
		    	xlutil.setCellData("PCOS", i, 10, nutrient);
		    
	            }
	        	 driver.navigate().back();
	        	 recipeLinks=driver.findElements(By.xpath("//div[@class='rcc_recipecard']//div[3]/span/a"));
	        }//end of recipes loop
	        	}//end of recipeSize if loop

	        }catch(IndexOutOfBoundsException e) {
				System.out.println("Index Out of Bound Exception");
				driver.navigate().back();
				recipeLinks=driver.findElements(By.xpath("//div[@class='rcc_recipecard']//div[3]/span/a"));
				continue;
		}

	        total_recipes = total_recipes + recipeLinksSize;
	        System.out.println("Total Recipes:"+total_recipes);  
	        System.out.println("Eliminated Recipes:"+eliminated_recipe_count);
	        timer_end = Instant.now();
	        System.out.println("Time taken to execute in Minutes: "+ Duration.between(timer_start,timer_end).toMinutes());;

	        }//end of pages loop
	        getAlphabates=driver.findElements(By.xpath("//table[@id='ctl00_cntleftpanel_mnuAlphabets']//td[position()>=3 and position()<last()]//a"));
	            }//end of alphabet count loop
	        }//end of if alphabet count
	     				

		}catch(Exception e) 
				{}		

		}

		}

				