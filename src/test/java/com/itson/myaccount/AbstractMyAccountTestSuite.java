/**
 * This sets up the used for myaccount testsuite to start and end test.
 *
 * @author Gurtej Phangureh
 */
package com.itson.myaccount;

import com.itson.restcalls.Usage;
import com.itson.servicedesigncenter.Browser;
import com.itson.servicedesigncenter.Config;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractMyAccountTestSuite {

  protected Usage restCalls;
  
  protected Config config;
  protected Browser browser;
  protected Util util;
  protected Util.DownloadFile download;
  
  @BeforeClass
  protected void classSetUp() throws Exception {  
    System.setProperty("bType", "chrome");
    initialBrowser();
    initialUtilObjects();
  }

//  @BeforeMethod
//  protected void methodSetUp(Method method) throws Exception {
//  }

//  @AfterMethod
//  protected void methodCleanUp(Method method) throws Exception {
//  }

  @AfterClass
  protected void classCleanUp() throws IOException {
      browser.close();
  }
  
  private void initialBrowser() throws Exception {
    restCalls = new Usage();
    config = restCalls.getConfig();
    String myAccountHostUrl = config.getHostUrl("myaccount");
    this.browser = new Browser.Builder()
        .build();
    browser.getSite(myAccountHostUrl);
  }
  
  private void initialUtilObjects() {
    util = new Util(browser);
    download = util.new DownloadFile();
  }
}
