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
  private String book = "01";
  private String unit = "01";
  private String bookFolder = "Book"+book+"-Audio";
  private String unitFolder = "Book"+book+"-Unit"+unit;
  private String bookUnitPath = bookFolder + Util.separator + unitFolder + Util.separator;
    
  @BeforeMethod // Use when setup this testsuite will run with multi account
  public void methodSetUpGetAudio() throws JSONException {
  }

  @Test(groups = {"MyAccount", "Zact", "Sprint"}, priority = 1)
  public void getAudioLink() throws JSONException, ParseException  {
    plansExcel = Util.Reader.readExcelHasFirstRowIsKeyToJSONArray("vocabulary-"+book+".xlsx", unit, oxfordInputOutputPath);
    
    for (int i = 0; i < plansExcel.length(); i ++) {
      browser.logAction("Get row: " + (i+2));
      String word = plansExcel.getJSONObject(i).getString("Word").trim();
      String brEPronounce = plansExcel.getJSONObject(i).getString("BrEPronounce");
      String amEPronounce = plansExcel.getJSONObject(i).getString("AmEPronounce");
      String description = plansExcel.getJSONObject(i).getString("Description");
      String brEAudioLink = plansExcel.getJSONObject(i).getString("BrEAudioLink");
      String amEAudioLink = plansExcel.getJSONObject(i).getString("AmEAudioLink");
      boolean passed = plansExcel.getJSONObject(i).getBoolean("Passed");
      
      if (util.isStringEmpty(brEPronounce) || util.isStringEmpty(amEPronounce) || util.isStringEmpty(brEAudioLink) || util.isStringEmpty(amEAudioLink)) {
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

        /** PRONOUNCE **/
        browser.logAction("Get BrE and AmE Pronunciation:");
        List<WebElement> pronounces = browser.findElements(By.cssSelector(".top-container .pron-g .phon"));
        if (pronounces.size() > 0) {
            if (pronounces.get(0) != null) {
                WebElement brEPronounceEle = pronounces.get(0);
                brEPronounce = brEPronounceEle.getText();
                browser.logAction("BrE link of " + word + " is: " + brEPronounce);
            }
            else 
                browser.logAction("Can not get BrE Pronunciation!!!");

            if (pronounces.get(1) != null) {
                WebElement amEPronounceEle = pronounces.get(1);
                amEPronounce = amEPronounceEle.getText();
                browser.logAction("AmE link of " + word + " is: " + amEPronounce);
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

        Reporter.log(brEPronounce + ", " + amEPronounce + ", " + brEAudioLink + ", " + amEAudioLink);
        /*~ LINK ~*/
      }
      else {
          browser.logAction("The row " + (i+2) + " with word '" + word + "' has full data, so next.");
          Reporter.log("");
          continue;
      }
    }
  }
  
  @Test(groups = {"MyAccount", "Zact", "Sprint"}, priority = 1)
  public void getAudioFile() throws JSONException, ParseException  {
    plansExcel = Util.Reader.readExcelHasFirstRowIsKeyToJSONArray("vocabulary-"+book+".xlsx", unit, oxfordInputOutputPath);
    
    for (int i = 0; i < plansExcel.length(); i ++) {
      browser.logAction("Get row: " + (i+2));
      String word = plansExcel.getJSONObject(i).getString("Word").trim();
      String brEPronounce = plansExcel.getJSONObject(i).getString("BrEPronounce");
      String amEPronounce = plansExcel.getJSONObject(i).getString("AmEPronounce");
      String description = plansExcel.getJSONObject(i).getString("Description");
      String brEAudioLink = plansExcel.getJSONObject(i).getString("BrEAudioLink");
      String amEAudioLink = plansExcel.getJSONObject(i).getString("AmEAudioLink");
      boolean passed = plansExcel.getJSONObject(i).getBoolean("Passed");
      
      if (!util.isStringEmpty(brEAudioLink)) {
        browser.logAction("Get audio of row " + (i+2) + " with word: " + word);
        download.downloadFile(brEAudioLink, word + "_br.mp3", oxfordInputOutputPath + bookUnitPath);
      }
      else {
          browser.logAction("The row " + (i+2) + " with word '" + word + "' has NO LINK in British English, so next.");
          Reporter.log("The row " + (i+2) + " with word '" + word + "' has NO LINK in British English, so next.");
          continue;
      }
      
      if (!util.isStringEmpty(amEAudioLink)) {
        browser.logAction("Get audio of row " + (i+2) + " with word: " + word);
        download.downloadFile(amEAudioLink, word + "_am.mp3", oxfordInputOutputPath + bookUnitPath);
      }
      else {
          browser.logAction("The row " + (i+2) + " with word '" + word + "' has NO LINK in American English, so next.");
          Reporter.log("The row " + (i+2) + " with word '" + word + "' has NO LINK in American English, so next.");
          continue;
      }
    }
  }
}
