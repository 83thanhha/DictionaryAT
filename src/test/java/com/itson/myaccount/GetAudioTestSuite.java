/*
 /**My Account testSuite WIP
 *
 * @author gurtejphangureh
 */
package com.itson.myaccount;

import java.text.ParseException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetAudioTestSuite extends AbstractMyAccountTestSuite {
    
  private JSONArray plansExcel;
  private String oxfordInputOutputPath = "_OXFORD" + Util.separator;
  private String book = System.getProperty("book");
  private String unitBegin = System.getProperty("unitBegin");
  private String unitEnd = System.getProperty("unitEnd");
  private String bookFolder = "Book"+book+"-Audio";
    
  @BeforeMethod // Use when setup this testsuite will run with multi account
  public void methodSetUpGetAudio() throws JSONException {
  }

  @Test(groups = {"MyAccount", "Zact", "Sprint"}, priority = 1)
  public void getWordInfoMultiSheets() throws JSONException, ParseException  {
      int unitBeginInt = Integer.parseInt(unitBegin);
      int unitEndInt = Integer.parseInt(unitEnd);
      for (int i = unitBeginInt; i <= unitEndInt; i ++) {
          getWordInfoEachSheet(String.format("%02d", i));
      }
  }
  
  public void getWordInfoEachSheet(String unit) throws JSONException, ParseException {
    browser.logAction("GET INFO FOR UNIT: " + unit);
    Reporter.log("");
    Reporter.log("================================================================");
    Reporter.log("");
    plansExcel = Util.Reader.readExcelHasFirstRowIsKeyToJSONArray("Vocabulary-"+book+".xlsx", unit, oxfordInputOutputPath);
    
    for (int i = 0; i < plansExcel.length(); i ++) {
      browser.logAction("Get row: " + (i+2));
      String word = plansExcel.getJSONObject(i).getString("Word").trim();
      String brEPronounce = plansExcel.getJSONObject(i).getString("BrEPronounce");
      String amEPronounce = plansExcel.getJSONObject(i).getString("AmEPronounce");
      String brEAudioLink = plansExcel.getJSONObject(i).getString("BrEAudioLink");
      String amEAudioLink = plansExcel.getJSONObject(i).getString("AmEAudioLink");
      
//      if (util.isStringEmpty(brEPronounce) || util.isStringEmpty(amEPronounce) || util.isStringEmpty(brEAudioLink) || util.isStringEmpty(amEAudioLink)) {
        browser.logAction("Get Pronounciation and Link of row " + (i+2) + " with word: " + word);
        WebElement searchFld = browser.findElement(By.id("q"));
        browser.sendKeys(searchFld, word, true);
        WebElement searchBtn = browser.findElement(By.id("search-btn"));
        browser.click(searchBtn);
        browser.waitForPageLoaded();
        
        if (browser.findElement(By.tagName("body")).getText().contains("No exact match found")) {
            browser.logAction("The row " + (i+2) + " with word '" + word + "' has 'No exact match found', so next.");
            Reporter.log("");
            continue;
        }

        try {
            /** PRONOUNCE **/
            browser.logAction("Get BrE and AmE Pronunciation:");
            List<WebElement> pronounces = browser.findElements(By.cssSelector(".top-container .pron-g .phon"));
            if (pronounces.size() > 0) {
                if (pronounces.get(0) != null) {
                    WebElement brEPronounceEle = pronounces.get(0);
                    brEPronounce = brEPronounceEle.getText();
                    browser.logAction("BrE pronunciation of " + word + " is: " + brEPronounce);
                }
                else 
                    browser.logAction("Can not get BrE Pronunciation!!!");

                if (pronounces.get(1) != null) {
                    WebElement amEPronounceEle = pronounces.get(1);
                    amEPronounce = amEPronounceEle.getText();
                    browser.logAction("AmE pronunciation of " + word + " is: " + amEPronounce);
                }
                else 
                    browser.logAction("Can not get AmE Pronunciation!!!");
            }
            else 
                browser.logAction("Can not get BrE and AmE Pronunciation!!!");
            /*~ PRONOUNCE ~*/

            /** LINK **/
            browser.logAction("Get BrE and AmE Sound Link:");
            WebElement brEAudioEle = browser.findElement(By.cssSelector(".sound.audio_play_button.pron-uk.icon-audio"));
            brEAudioLink = brEAudioEle.getAttribute("data-src-mp3").toString();
            browser.logAction("BrE link of " + word + " is: " + brEAudioLink);

            WebElement amEAudioEle = browser.findElement(By.cssSelector(".sound.audio_play_button.pron-us.icon-audio"));
            amEAudioLink = amEAudioEle.getAttribute("data-src-mp3").toString();
            browser.logAction("AmE link of " + word + " is: " + amEAudioLink);
            /*~ LINK ~*/
            
            /** IDIOM, PHRASAL VERB **/
            String phrasalVerbs = "";
            String idioms = "";
            try {
                List<WebElement> extraSections = browser.findElements(By.cssSelector(".accordion>dt"));
                for (int j = 0; (extraSections.size() > 0 && j < extraSections.size()); j++) {
                    String tempSectionText = extraSections.get(j).getText();
                    extraSections.get(j).click();
                    switch (tempSectionText) {
                        case "Phrasal verbs":
                            phrasalVerbs = getExtraSectionData(j).replace("phrasal verb", "");
                            break;
                        case "Idioms":
                            idioms = getExtraSectionData(j);
                            break;
                    }
                }
            }
            catch (Exception ex1) {
                browser.logAction("CAN NOT GET IDIOMS OR PHRASAL VERBS FOR THE WORD: " + word.toUpperCase() + "!!! With the exception is: " + ex1.toString());
            }
            /*~ IDIOM, PHRASAL VERB ~*/
            
            /** MAIN DESCRIPTION **/
            String wordTopSection = browser.findElementByXPath("//div[@class='webtop-g']").getText();
            String type = "";
            if (hasType(wordTopSection))
                type = browser.findElementByXPath("//div[@class='webtop-g']/span[3]").getText();
            List<WebElement> descriptions = browser.findElements(By.cssSelector(".cf,.def"));
            String mainDes = "";
            for (int j = 0; (descriptions.size() > 0 && j < descriptions.size() && j < 10); j++) {
                String temDes = descriptions.get(j).getText();
                if (descriptions.get(j).getAttribute("class").equalsIgnoreCase("cf"))
                    temDes = temDes.toUpperCase();
                mainDes += ", " + temDes;
            }
            mainDes = getShortType(type) + mainDes.substring(2);
              browser.logAction("Type of word " + word + ": " + getShortType(type));
            /*~ MAIN DESCRIPTION ~*/
            
            /** ANOTHER DESCRIPTION **/
            String anotherDes = "";
            try {
                browser.click(By.cssSelector(".accordion>dt")); // Open the "All matches" section again (after open the Idioms or Phrasal Verbs sections)
                browser.click(By.cssSelector(".list-col>li>a"));
                browser.waitForPageLoaded();
                
                String wordTopSectionAnother = browser.findElementByXPath("//div[@class='webtop-g']").getText();
                String typeAnother = "";
                if (hasType(wordTopSectionAnother))
                    typeAnother = browser.findElementByXPath("//div[@class='webtop-g']/span[3]").getText();
                List<WebElement> descriptionsAnother = browser.findElements(By.cssSelector(".cf,.def"));
                for (int j = 0; (descriptionsAnother.size() > 0 && j < descriptionsAnother.size() && j < 10); j++) {
                    String temDes = descriptionsAnother.get(j).getText();
                    if (descriptionsAnother.get(j).getAttribute("class").equalsIgnoreCase("cf"))
                        temDes = temDes.toUpperCase();
                    anotherDes += ", " + temDes;
                }
                if (anotherDes.length() >= 2)
                    anotherDes = getShortType(typeAnother) + anotherDes.substring(2);
                browser.logAction("Another Type of word " + word + ": " + getShortType(typeAnother));
            }
            catch (Exception ex1) {
                browser.logAction("CAN NOT GET ANOTHER DESCRIPTION FOR THE WORD: " + word.toUpperCase() + "!!! With the exception is: " + ex1.toString());
            }
            /*~ ANOTHER DESCRIPTION ~*/
            
            // SUMMARIZE:
            browser.logAction(brEPronounce + "^_^" + amEPronounce + "^_^" + idioms + "^_^" + phrasalVerbs + "^_^" + mainDes + "^_^" + anotherDes + "^_^" + " " + "^_^" + " " + "^_^" + brEAudioLink + "^_^" + amEAudioLink);
            Reporter.log(brEPronounce + "^_^" + amEPronounce + "^_^" + idioms + "^_^" + phrasalVerbs + "^_^" + mainDes + "^_^" + anotherDes + "^_^" + " " + "^_^" + " " + "^_^" + brEAudioLink + "^_^" + amEAudioLink);
        }
        catch (Exception ex) {
            browser.logAction("CAN NOT GET INFO FOR THE WORD: " + word.toUpperCase() + "!!! With the exception is: " + ex.toString());
            Reporter.log("");
        }
//      }
//      else {
//          browser.logAction("The row " + (i+2) + " with word '" + word + "' has full data, so next.");
//          Reporter.log("");
//          continue;
//      }
    }
  }
  
  @Test(groups = {"MyAccount", "Zact", "Sprint"}, priority = 1)
  public void getAudioFileMultiSheets() throws JSONException, ParseException  {
      int unitBeginInt = Integer.parseInt(unitBegin);
      int unitEndInt = Integer.parseInt(unitEnd);
      for (int i = unitBeginInt; i <= unitEndInt; i ++) {
          getAudioFileEachSheet(String.format("%02d", i));
      }
  }
  
  public void getAudioFileEachSheet(String unit) throws JSONException, ParseException  {
    browser.logAction("GET AUDIO FILE FOR UNIT: " + unit);
    Reporter.log("");
    String unitFolder = "Book"+book+"-Unit"+unit;
    String bookUnitPath = bookFolder + Util.separator + unitFolder + Util.separator;
    plansExcel = Util.Reader.readExcelHasFirstRowIsKeyToJSONArray("Vocabulary-"+book+".xlsx", unit, oxfordInputOutputPath);
    
    for (int i = 0; i < plansExcel.length(); i ++) {
      browser.logAction("Get row: " + (i+2));
      String word = plansExcel.getJSONObject(i).getString("Word").trim();
      String brEAudioLink = plansExcel.getJSONObject(i).getString("BrEAudioLink");
      String amEAudioLink = plansExcel.getJSONObject(i).getString("AmEAudioLink");
      
      if (!util.isStringEmpty(brEAudioLink)) {
        browser.logAction("Get audio of row " + (i+2) + " with word: " + word);
        download.downloadFile(brEAudioLink, word + "_1_br.mp3", oxfordInputOutputPath + bookUnitPath);
      }
      else {
          browser.logAction("The row " + (i+2) + " with word '" + word + "' has NO LINK in British English, so next.");
          Reporter.log("The row " + (i+2) + " with word '" + word + "' has NO LINK in British English, so next.");
          continue;
      }
      
      if (!util.isStringEmpty(amEAudioLink)) {
        browser.logAction("Get audio of row " + (i+2) + " with word: " + word);
        download.downloadFile(amEAudioLink, word + "_2_am.mp3", oxfordInputOutputPath + bookUnitPath);
      }
      else {
          browser.logAction("The row " + (i+2) + " with word '" + word + "' has NO LINK in American English, so next.");
          Reporter.log("The row " + (i+2) + " with word '" + word + "' has NO LINK in American English, so next.");
          continue;
      }
    }
  }
  
  public String getShortType(String type) {
      if (type.equalsIgnoreCase("noun")) 
          return "N: ";
      if (type.equalsIgnoreCase("verb"))
          return "V: ";
      if (type.equalsIgnoreCase("adjective"))
          return "Adj: ";
      if (type.equalsIgnoreCase("adverb"))
          return "Adv: ";
      if (type.equalsIgnoreCase("preposition"))
          return "Pre: ";
      if (type.equalsIgnoreCase("phrasal verb"))
          return "PhV: ";
      if (type.trim().isEmpty())
          return "";
      return "" + capitalize(type) + ": ";
  }
  
  public boolean hasType(String text) {
      text = text.toLowerCase();
      if (text.contains("noun") || text.contains("verb") || text.contains("adjective") || text.contains("adverb") || text.contains("preposition") || text.contains("phrasal verb"))
          return true;
      return false;
  }
  
  public String capitalize(String str) {
      return str.substring(0, 1).toUpperCase()+str.substring(1);
  }
  
  public String getExtraSectionData(int sectionIndex) {
      WebElement section = browser.findElement(By.xpath("//dl[@class='accordion ui-grad']/dd["+ (sectionIndex+1) +"]"));
      String extraSectionData = "";
      String sectionText = section.getText();
      if (section.getText().contains("See more")) {
          section.findElement(By.cssSelector("a.see-more")).click();
      }
      List<WebElement> rows = section.findElements(By.cssSelector("li"));
      if (rows != null) {
          for (int i = 0; i < rows.size(); i ++) {
              extraSectionData += ", " + rows.get(i).getText();
          }
      }
      extraSectionData = extraSectionData.substring(2);
      return extraSectionData;
  }
}
