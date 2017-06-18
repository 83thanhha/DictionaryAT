/**
 * Common functions is used to compare between expected value and fact value.
 * Includes Util JavaScript, JQuery, TraceBack ... classes.
 *
 * @author HaNT
 */
package com.itson.myaccount;

import com.google.common.base.Function;
import com.itson.restcalls.RestCalls;
import com.itson.restcalls.Usage;

import com.itson.servicedesigncenter.Browser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import javax.swing.text.StyleConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.jmx.Server;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;




import java.io.File ;
    import java.io.FileOutputStream ;
    import java.io.InputStream ;
    import java.util.Set ;

    import org.apache.http.HttpEntity ;
    import org.apache.http.HttpResponse ;
    import org.apache.http.client.CookieStore ;
    import org.apache.http.client.methods.HttpGet ;
    import org.apache.http.impl.client.BasicCookieStore ;
    import org.apache.http.impl.client.DefaultHttpClient ;
    import org.apache.http.impl.cookie.BasicClientCookie ;
    import org.openqa.selenium.By ;
    import org.openqa.selenium.Cookie ;
    import org.openqa.selenium.WebDriver ;
    import org.openqa.selenium.WebElement ;
    import org.openqa.selenium.firefox.FirefoxDriver ;


public class Util {

  protected Browser browser;
  protected RemoteWebDriver driver;
  protected JavascriptExecutor jsDriver;
  
  private String testCaseName = "";
  private static final Logger LOGGER = LogManager.getFormatterLogger(Browser.class);

  /**
   * ***************************************************************************
   * SLEEPING TIME
   * ***************************************************************************
   */
  public static final int sleepTimeDegree0 = 10000; // HaNT: I will set to 0 after the web's performance is better.
  public static final int sleepTimeDegree0_PreventDelay = 2000;
  public static final int sleepTimeDegree1 = 2000;
  public static final int sleepTimeDegree2 = 5000;
  public static final int sleepTimeDegree3 = 8000;
  public static final int sleepTimeDegree4 = 10000;
  public static final int sleepTimeDegree5 = 20000;
  public static final int sleepTimeDegree60 = 60000;

  public static final int waitTimeImplicit = 1;
  public static final int waitTimeTiny = 15;
  public static final int waitTimeSmall = 30;
  public static final int waitTimeMedium = 60;
  public static final int waitTimeLarge = 90;
  public static final int waitTimeHuge = 180;
  public static final int waitTimeMax = 240;
  public static final int waitTimeSearch = 37;
  
  public static final String equals = "equals";
  public static final String equalsIgnoreCase = "equalsIgnoreCase";
  public static final String contentEquals = "contentEquals";
  public static final String notEquals = "notEquals";
  public static final String contains = "contains";
  public static final String containsIgnoreCase = "containsIgnoreCase";
  public static final String notContainsIgnoreCase = "notContainsIgnoreCase";
  public static final String notContains = "notContains";
  public static final String startsWith = "startsWith";
  public static final String endsWith = "endsWith";
  public static final String isInteger = "isInteger";
  public static final String isDouble = "isDouble";
  public static final String isDisplayed = "isDisplayed";
  public static final String isNotDisplayed = "isNotDisplayed";
  public static final String isExisted = "isExisted";
  public static final String isNotExisted = "isNotExisted";
  public static final String isSelected = "isSelected";
  public static final String isNotSelected = "isNotSelected";
  public static final String same = "_same";
  
  public static final String getText = "getText()";
  public static final String getValue = "getAttribute('value')";
  
  public static final String willInvestigate = "WILL INVESTIGATE.";

  public boolean isStringEmpty(String aString) {
    if (aString.equals(Common.Constant.notAvailable) || aString.isEmpty())
        return true;
    return false;
  }

  public Util(Browser browser) {
    this.browser = browser;
    this.driver = this.browser.getDriver();
    this.jsDriver = (JavascriptExecutor) this.driver;
  }
  
  public void analyzeLog() {
    LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
    for (LogEntry entry : logEntries) {
      browser.logAction(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
      //do something useful with the data
    }
  }

  public WebDriverWait getDriverWait(long timeOutSeconds) {
    return new WebDriverWait(driver, timeOutSeconds, 100L);
  }
  
  /**
   * Get the selector of an element
   * @param element
   * @elementText: [[RemoteWebDriver: firefox on WINDOWS (8482522b-5d38-4005-9499-bada6beff635)] -> css selector: .plan-block .plan-block:not(.special-block)]
   * @return { css selector: .plan-block .plan-block:not(.special-block) }
   */
  public String extractElementSelector(WebElement element) {
    String elementSelector = element.toString();
    browser.logAction("Element selector (Full): {"+ elementSelector +"}");
    String flag = ")] -> ";
    int elementSelectorIndex = elementSelector.indexOf(flag) + flag.length();
    elementSelector = elementSelector.substring(elementSelectorIndex);
    elementSelector = elementSelector.replace("]}", "");
    return "{" + elementSelector + "}";
  }
  
  /**
   * Log to Console AND Report
   * @param logAction 
   */
  public void logConsoleAndReport(String logAction) {
    logConsoleAndReport(logAction, true);
  }
  /**
   * Log to Console (and Report?)
   * @param logAction
   * @param doReport 
   */
  public void logConsoleAndReport(String logAction, boolean doReport) {
    browser.logAction(logAction);
    if (doReport) {
      if (!logAction.equalsIgnoreCase(Util.willInvestigate))
        Reporter.log("");
      Reporter.log(logAction);
    }
  }
  
  public static boolean containsIgnoreCase(String stringFull, String stringSub) {
    return stringFull.toLowerCase().contains(stringSub.toLowerCase());
  }

  public boolean isElementVisibilityAfterWaiting(By by, String ticket, int waitTime) {
    try {
      browser.waitForVisibilityOfElement(by, waitTime);
      return true;
    } catch (ElementNotVisibleException ex) {
    }
    return false;
  }
  
//  public void makesureAjaxProcessed(String ) {
//    LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
//    for (LogEntry entry : logEntries) {
//      System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
//      //do something useful with the data
//    }
//  }
  
  public static class Reader {
    private final static String separator = System.getProperty("file.separator");
    private final static String directory = System.getProperty("user.dir") + separator + ".." + separator;

    public static JSONArray readExcelHasFirstRowIsKeyToJSONArray(String fileName, String sheetName, String... subFolders) {
        String subFolder = "";
        if (subFolders.length > 0)
            subFolder = subFolders[0];
      try {
        JSONArray sheetJSONArray = new JSONArray();
        ArrayList<String> keys = new ArrayList<String>();
        FileInputStream file = new FileInputStream(new File(directory + subFolder + separator + fileName));
        
        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheet(sheetName);
       
        
        //Iterate through each rows one by one
        Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
        int rowIndex = 0;
        while (rowIterator.hasNext()) {
          JSONObject rowJSONObject = new JSONObject();
          org.apache.poi.ss.usermodel.Row row = rowIterator.next();
          Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.iterator();

          int cellIndex = 0;
          while (cellIterator.hasNext()) {
            org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
            String cellValue = cell.getStringCellValue().trim();
            
            if (cellValue.equals(Common.Constant.endOfFile))
              return sheetJSONArray;
            
            if (cellValue.equals(Common.Constant.endOfColumn))
              break;

            if (rowIndex == 0) {
              if (cellValue.isEmpty())
                cellValue = Common.Constant.notAvailable + cellIndex;
              keys.add(cellValue);
            } else {
              if (cellValue.isEmpty())
                cellValue = Common.Constant.notAvailable;
              rowJSONObject.put(keys.get(cellIndex), cellValue);
            }

            cellIndex++;
          }
          
          if (rowIndex > 0)
            sheetJSONArray.put(rowJSONObject);

          rowIndex++;
        }
        file.close();
        return sheetJSONArray;
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }

    public Map<String, String> readExcelHasFirstColumnIsKeyToMap(String fileName, String sheetName) {
      try {
        Map<String, String> excelMap = new HashMap<String, String>();
        String key = "";
        String value = "";
        FileInputStream file = new FileInputStream(new File(directory + fileName));
        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheet(sheetName);
        System.out.println("Reading sheet: " + sheetName);
        //Iterate through each rows one by one
        Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
          org.apache.poi.ss.usermodel.Row row = rowIterator.next();
          key = row.getCell(0).getStringCellValue();
          value = row.getCell(1).getStringCellValue().trim();
          excelMap.put(key, value);
          System.out.println("Put to Map: " + excelMap.get(key));
        }
        file.close();
        return excelMap;
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }

    public ArrayList<String> readFromExcel(String fileName, String sheetName, int column) {

      ArrayList<String> list = new ArrayList<String>();
      try {
        FileInputStream file = new FileInputStream(new File(directory + fileName));
        // Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        // Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheet(sheetName);
        System.out.println("\nReading sheet: " + sheetName + "- Column: " + column);

        // Iterate through each rows one by one			
        Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
          org.apache.poi.ss.usermodel.Row row = rowIterator.next();
          if (row.getCell(column) != null) {
            String value = row.getCell(column).toString();
            System.out.print(value + ", ");
            list.add(value);
          }
        }
        file.close();
        return list;
      } catch (Exception e) {
        System.out.println("\n cell error: " + e.getMessage());
        return null;
      }
    }
  }
  
  
    public class DownloadFile {

        public void downloadFile(String downloadUrl, String fileNameWithExtension, String... subFolders) {
            String subFolder = "";
            if (subFolders.length > 0)
                subFolder = subFolders[0];

            try {
                CookieStore cookieStore = seleniumCookiesToCookieStore();
                DefaultHttpClient httpClient = new DefaultHttpClient();
                httpClient.setCookieStore(cookieStore);

                HttpGet httpGet = new HttpGet(downloadUrl);
                browser.logAction("Downloading file: " + downloadUrl);
                HttpResponse response = httpClient.execute(httpGet);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    File outputFile = new File(folderPath + subFolder + fileNameWithExtension);
                    if (!outputFile.exists()) {
                        InputStream inputStream = entity.getContent();
                        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        while ((read = inputStream.read(bytes)) != -1) {
                            fileOutputStream.write(bytes, 0, read);
                        }
                        fileOutputStream.close();
                        browser.logAction("DOWNLOADED FILE" + outputFile.length() + " bytes. " + entity.getContentType() + " to " + outputFile.getPath());
                    }
                    else {
                        browser.logAction("FILE " + outputFile.getName() + " IS ALREADY AVAILABLE");
                        Reporter.log("FILE " + outputFile.getName() + " IS ALREADY AVAILABLE");
                    }
                } else {
                    browser.logAction("FAILED DOWNLOADING!");
                }
            }
            catch (Exception ex) {
                browser.logAction("CAN NOT DOWNLOAD FILE " + downloadUrl + " BY: " + ex.toString());
                Reporter.log("CAN NOT DOWNLOAD FILE " + downloadUrl + " BY: " + ex.toString());
            }
        }

        private CookieStore seleniumCookiesToCookieStore() {

            Set<Cookie> seleniumCookies = driver.manage().getCookies();
            CookieStore cookieStore = new BasicCookieStore();

            for (Cookie seleniumCookie : seleniumCookies) {
                BasicClientCookie basicClientCookie
                        = new BasicClientCookie(seleniumCookie.getName(), seleniumCookie.getValue());
                basicClientCookie.setDomain(seleniumCookie.getDomain());
                basicClientCookie.setExpiryDate(seleniumCookie.getExpiry());
                basicClientCookie.setPath(seleniumCookie.getPath());
                cookieStore.addCookie(basicClientCookie);
            }

            return cookieStore;
        }
    }

  
  public static String separator = System.getProperty("file.separator");
  private static String folderPath = System.getProperty("user.dir") + separator + ".." + separator;
}
