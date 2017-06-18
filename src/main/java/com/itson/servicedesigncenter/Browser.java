/**
 * Browser common functions and setup used for the test Created by harryjackson
 *
 * @author Gurtej
 */
package com.itson.servicedesigncenter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.SkipException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.regex.Pattern;

import com.google.common.base.Function;

public class Browser {

  private static final By SPINNER_XPATH = By.xpath("//div[@class='dataTables_processing']");
  private static final Logger LOGGER = LogManager.getFormatterLogger(Browser.class);
  public static final String SCREENSHOT_FOLDER = "screenshots";
  private static final String ALLOWED_CHARS;

  static {
    StringBuilder temp = new StringBuilder(128);
    for (char c = 'a'; c <= 'z'; c++) {
      temp.append(c);
    }
    for (char c = 'A'; c <= 'Z'; c++) {
      temp.append(c);
    }
    for (char c = '0'; c <= '9'; c++) {
      temp.append(c);
    }
    temp.append(" ");
    ALLOWED_CHARS = temp.toString();
  }
  private long start;
  private long stop;
  private long duration;
  private final RemoteWebDriver driver;
  private Object page;
  private String browser_type;
  private final String platform;
  private final String version;
  private final String seleniumHubUrl;
  private final String site;
  private final String seleniumHubSelector;
  private final Config conf;
  private String liveSelection;
  private String siteUrl;
  private String browser_version;
  private String browser_platform;
  private String browser_platform_version;
  private String adminPass;
  private String adminUser;
  private String testMDN;
  private String givenUrl;
  private int closed;

  private final int waitPollTimeMilli = 45;
  private final int implicitWaitTime = 1;
  private final int waitTimeTiny = WaitTime.ExtraSmall.toSeconds();
  private final int waitTimeSmall = WaitTime.Small.toSeconds();
  private final int waitTimeMedium = WaitTime.Medium.toSeconds();
  private final int waitTimeLarge = WaitTime.Large.toSeconds();
  private final int waitTimeSearch = WaitTime.Medium.toSeconds();

  protected Actions actions;

  private ActionLog actionLog;
  private String testCaseName = "";
  private boolean useErrorThrowingExplicitWait;

  private Browser(Builder b) {
    this.browser_type = b.browser_type;
    this.platform = b.platform;
    this.version = b.version;
    this.site = b.site;
    this.seleniumHubSelector = b.seleniumHub;
    this.useErrorThrowingExplicitWait = b.useErrorThrowingExplicitWait;
    actionLog = ActionLog.INSTANCE;

    String envFile = System.getProperty("envFile");
    if (envFile == null) {
      System.out.println("envFile null Setting Default zactMobile QA");
      envFile = "../testng/env/qaZactMobile.json";
    }
    if (envFile.isEmpty()) {
      throw new IllegalStateException("Invalid -DenvFile=[" + envFile
              + " ] specified on command line\n");
    }
    String configFile = envFile;
    logAction(configFile);
    this.conf = new Config(configFile);
    this.browser_type = conf.getBrowserType();
    this.browser_platform = conf.getBrowserPlatform();
    this.browser_platform_version = conf.getBrowserPlatformVersion();
    this.browser_version = conf.getBrowserVersion();
    this.seleniumHubUrl = hubSelector(conf.getHub());
    this.testMDN = conf.getMDN();

    DesiredCapabilities capability = new DesiredCapabilities();
    browserTitle(browser_type, version, platform);
    capability.setBrowserName(browser_type);
    capability.setCapability("os", browser_platform);
    capability.setCapability("os_version", browser_platform_version);
    capability.setCapability("version", browser_version);
    capability.setCapability("nativeEvents", false);
    if (seleniumHubUrl.contains("browserstack")) {
      String testname = conf.getTestName();
      capability.setCapability("browserstack.local", "true");
      capability.setCapability("browserstack.video", "true");
      capability.setCapability("browserstack.localIdentifier", testname);
      capability.setCapability("browserstack.debug", "true");
      if (!browser_type.equals("internet explorer")) {
        capability.setCapability("acceptSslCerts", "true");
      }
    }
    if (browser_type.equals("safari")) {
      SafariOptions safariOptions = new SafariOptions();
      capability.setCapability(SafariOptions.CAPABILITY, safariOptions);
    }
    URL url = null;
    try {
      url = new URL(seleniumHubUrl);
    }
    catch (MalformedURLException e) {
      LOGGER.error("Bad hub url " + seleniumHubUrl, e);
      throw new IllegalArgumentException("Bad seleniumHub Url:" + seleniumHubUrl);
    }
    driver = new RemoteWebDriver(url, capability);
    driver.manage()
            .timeouts()
            .implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
    driver.manage().window().setPosition(new Point(0, 0));
    driver.manage().window().setSize(new Dimension(1308, 1000));
    driver.manage().deleteAllCookies();
    actions = new Actions(driver);
  }

  Browser() {
    // To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void getSite(String url) {
    givenUrl = url;
    logAction("Site Url: " + givenUrl);
    driver.get(givenUrl);
  }

  public WebElement waitForClickableElement(By b) {
    WebDriverWait wait = getDriverWait(waitTimeMedium);
    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(b));
    return element;
  }

  private WebDriverWait getDriverWait(long timeOutSeconds) {
    return new WebDriverWait(driver, timeOutSeconds, 100L);
  }

  public Boolean textToBePresentInElementValue(By by, String str) {
    WebDriverWait wait = getDriverWait(waitTimeLarge + 2);
    log(Level.INFO, "Checking for: " + str);
    Boolean b = wait.until(ExpectedConditions.textToBePresentInElementLocated(by,
            str));
    return b;
  }

  public WebElement waitForClickableElement(WebElement ele) {
    return waitForClickableElement(ele, WaitTime.Medium);
  }

  public WebElement waitForClickableElement(final WebElement ele,
          WaitTime waitTime) {
    return waitForClickableElement(ele, waitTime.toSeconds());
  }

  public WebElement waitForClickableElement(final WebElement ele,
          int waitTimeInSecs) {
    WebDriverWait wait = getDriverWait(waitTimeInSecs);
    return wait.until(new ExpectedCondition<WebElement>() {

      private final ExpectedCondition<WebElement> condition = ExpectedConditions.elementToBeClickable(ele);

      @Override
      public WebElement apply(WebDriver input) {
        return condition.apply(input);
      }
    });
  }

  public WebElement waitForVisibilityOfElement(By by) {
    return waitForVisibilityOfElement(by, waitTimeMedium);
  }

  public WebElement waitForVisibilityOfElement(By by, int waitTime) {
    WebDriverWait wait = getDriverWait(waitTime);
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    return element;
  }

  public void waitForSpinnerToVanish() {
    waitForElementToDisappear(SPINNER_XPATH);
  }

  public void waitForElementToDisappear(final By by) {
    waitForElementToDisappear(by, WaitTime.Medium);
  }

  public void waitForElementToDisappear(final By by, WaitTime waitTime) {
    logAction("Waiting for element " + by + " to disappear.");
    WebDriverWait wait = getDriverWait(waitTime.toSeconds());
    wait.until(new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply(WebDriver input) {
        try {
          WebElement x = driver.findElement(by);
          if (x.isDisplayed()) {
            String visible = x.getCssValue("visibility");
            if (StringUtils.isNotBlank(visible)) {
              return "hidden".equals(visible);
            }
            return Boolean.FALSE;
          }
          return Boolean.TRUE;
        }
        catch (NoSuchElementException | ElementNotVisibleException | StaleElementReferenceException e) {
          return Boolean.TRUE;
        }
      }
    });
  }

  public boolean isDisplayed(final WebElement element, WaitTime waitTime) {
    WebDriverWait wait = getDriverWait(waitTime.toSeconds());
    return wait.until(new ExpectedCondition<AtomicBoolean>() {

      @Override
      public AtomicBoolean apply(WebDriver input) {
        return new AtomicBoolean(isDisplayed(element));
      }

      private boolean isDisplayed(final WebElement element) {
        try {
          if (element.isDisplayed() && element.isEnabled()) {
            String visible = element.getCssValue("visibility");
            if (StringUtils.isNotBlank(visible)) {
              return !"hidden".equals(visible);
            }
            return Boolean.TRUE;
          }
          return Boolean.FALSE;
        }
        catch (NoSuchElementException | ElementNotVisibleException | StaleElementReferenceException e) {
          return Boolean.FALSE;
        }
      }
    }).get();
  }

  public void waitForURLToChange(final String old, WaitTime waitTime) {
    waitForURLToChange(old, null, waitTime);
  }

  public void waitForURLToChange(final String old, String newURLPattern,
          WaitTime waitTime) {
    logAction("Waiting for URL to change, current URL = " + old);
    WebDriverWait wait = getDriverWait(waitTime.toSeconds());
    final Pattern pattern = newURLPattern != null ? Pattern.compile(newURLPattern)
            : null;
    wait.until(new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply(WebDriver input) {
        String newValue = driver.getCurrentUrl();
        try {
          if (!old.equalsIgnoreCase(newValue) || pattern != null) {
            if (pattern != null) {
              newValue = URLDecoder.decode(newValue,
                      StandardCharsets.UTF_8.name());
              return pattern.matcher(newValue).matches();
            }
            return Boolean.TRUE;
          }
          return Boolean.FALSE;
        }
        catch (UnsupportedEncodingException e) {
          logAction("Bad url = " + newValue);
          return Boolean.FALSE;
        }
      }
    });
    logAction("URL changed to = " + getUrl());
  }

  public WebElement visibilityOfElementLocated(By by) {
    WebDriverWait wait = getDriverWait(waitTimeMedium);
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    return element;
  }

  public WebElement findElementByCssSelector(String s) {
    waitForClickableElement(By.cssSelector(s));
    return driver.findElementByCssSelector(s);
  }

  private WebElement findElementByTime(By by, Integer time) {
    WebDriverWait wait = getDriverWait(time);
    WebElement element = null;
    try {
      element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }
    catch (TimeoutException ex) {
      System.err.println("findElementByTime: unable to find element using: " + by.toString());
      throw ex;
    }
    return element;
  }

  public WebElement findElementFast(By by) {
    return findElementByTime(by, waitTimeTiny);
  }

  public WebElement findElement(By by) {
    return findElementByTime(by, waitTimeMedium);
  }

  public WebElement findElement(final WebElement start, final By by) {
    WebDriverWait wait = getDriverWait(waitTimeMedium);
    try {
      return wait.until(new ExpectedCondition<WebElement>() {
        @Override
        public WebElement apply(WebDriver driver) {
          WebElement x = start.findElement(by);
          x.getText();
          return x;
        }
      });
    }
    catch (TimeoutException ex) {
      throw ex;
    }
  }

  public WebElement findElementById(String s) {
    this.waitForId(s);
    return driver.findElementById(s);
  }

  public List<WebElement> findElements(final By by) {
    return findElements(by, false);
  }

  public List<WebElement> findElements(final By by, final boolean expectElements) {
    waitForPageLoaded();
    WebDriverWait wait = getDriverWait(waitTimeMedium);
    try {
      return wait.until(new ExpectedCondition<List<WebElement>>() {
        @Override
        public List<WebElement> apply(WebDriver driver) {
          List<WebElement> elements = driver.findElements(by);
          if (expectElements && elements.isEmpty()) {
            return null;
          }
          for (WebElement element : elements) {
            try {
              element.getText();
            }
            catch (StaleElementReferenceException | ElementNotVisibleException | NoSuchElementException e) {
              return null;
            }
          }
          return elements;
        }

        @Override
        public String toString() {
          return "visibility of all elements located by " + by;
        }
      });
    }
    catch (TimeoutException ex) {
      System.err.println("findElements: unable to find element using: " + by.toString());
      throw ex;
    }
  }

  public WebElement findElementByXPath(String s) {
    return driver.findElementByXPath(s);
  }

  public boolean isElementExisted(By by) {
    if (findElements(by).size() != 0) {
      return true;
    }
    return false;
  }

  public void waitForPageLoaded() {
    ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver driver) {
        return ((JavascriptExecutor) driver).executeScript("return document.readyState")
                .equals("complete");
      }
    };
    WebDriverWait wait = getDriverWait(waitTimeMedium);
    try {
      wait.until(expectation);
    }
    catch (Exception error) {
      throw new IllegalStateException("Timeout waiting for Page Load Request to complete.");
    }
  }

  public void waitForText(By by, String text, int waitTime) {
    WebDriverWait wait = getDriverWait(waitTime);
    wait.until(ExpectedConditions.textToBePresentInElementLocated(by, text));
  }

  public void waitForText(By by, String text) {
    waitForText(by, text, waitTimeMedium);
  }

  public WebElement waitForId(String id) {
    WebDriverWait wait = getDriverWait(waitTimeMedium);
    WebElement element = null;
    try {
      waitForPageLoaded();
      element = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }
    catch (TimeoutException ex) {
      ex.printStackTrace();
      hardExit("TimeoutException Failure", -1);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      hardExit("Unknown Exception:", -1);
    }
    return element;
  }

  public RemoteWebDriver getDriver() {
    return driver;
  }

  public String getUrl() {
    return driver.getCurrentUrl();
  }

  public Keyboard getKeyboard() {
    return driver.getKeyboard();
  }

  public Cookie getCookieNamed(String cookieName) {
    return driver.manage().getCookieNamed(cookieName);
  }

  public Browser get(String pageId) {
    logAction("Get Url: " + pageId);
    driver.get(pageId);
    return this;
  }

  public String getPageTitle() {
    return driver.getTitle();
  }

  public Browser clickAndWaitTillURLChanged(WebElement ele) {
    return clickAndWaitTillURLChanged(ele, null);
  }

  public Browser clickAndWaitTillURLChanged(WebElement ele, String newURLPattern) {
    String old = getUrl();
    click(ele);
    waitForURLToChange(old, newURLPattern, WaitTime.Medium);
    return this;
  }

  public Browser click(WebElement ele, boolean waitForURLToChange) {
    ele = waitForClickableElement(ele);
    logAction("Clicking on element " + ele.getText());
    String old = getUrl();
    ele.click();
    waitForURLToChange(old, WaitTime.Medium);
    return this;
  }

  public Browser click(WebElement ele) {
    return click(ele, WaitTime.Medium.toSeconds());
  }

  public Browser click(final WebElement ele, int waitTimeInSecs) {
    getDriverWait(waitTimeInSecs).until(new ExpectedCondition<Boolean>() {

      private final ExpectedCondition<WebElement> condition = ExpectedConditions.elementToBeClickable(ele);

      @Override
      public Boolean apply(WebDriver input) {
        WebElement x = condition.apply(input);
        if (x != null) {
          logAction("Clicking on element " + x.getText());
          x.click();
          return Boolean.TRUE;
        }
        return Boolean.FALSE;
      }
    });
    return this;
  }

  public void click(WebElement ele, WaitTime waitTime) {
    click(ele, waitTime.toSeconds());
  }

  public void click(By by) {
    click(waitForClickableElement(by));
  }

  public void javaSciptClick(WebElement ele) {
    executeScript("arguments[0].click();", ele);
  }

  public void checkRadioBtnSelected(WebElement ele) {
    logAction("checkRadioBtnSelected Checking Radio Btn Selected ");
    waitForClickableElement(ele);
    logAction("checkRadioBtnSelected Checking Radio Btn Selected " + ele.getText());
    String getCheckAttribute = ele.getAttribute("checked");
    compareText(getCheckAttribute, "true");
  }

  public Browser selectRadio(final WebElement element) {
    getDriverWait(waitTimeMedium).until(new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply(WebDriver input) {
        if (element.isDisplayed()) {
          element.click();
          return element.isSelected();
        }
        return Boolean.FALSE;
      }
    });
    return this;
  }

  public Browser selectRadio(final List<WebElement> elements, final String label) {
    getDriverWait(waitTimeMedium).until(new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply(WebDriver input) {
        for (WebElement x : elements) {
          if (label.equalsIgnoreCase(x.getAttribute("value"))) {
            x.click();
            return x.isSelected();
          }
        }

        throw new IllegalStateException("There is no label " + label);
      }
    });
    return this;
  }

  public void hardExit(String msg, int i) {
    System.err.println(msg);
    close();
    SkipException sex = new SkipException("Hard Exit called, skipping tests from here");
    sex.printStackTrace();
    throw new IllegalStateException(sex);
  }

  public void addAction(String name, String action) {
    actionLog.add(new ActionPair(name, action));
  }

  public void log(Level level, String msg) {
    actionLog.add(new ActionPair(msg, "unknownAction"));
  }

  public void logAction(String msg) {
    if (testCaseName != null && !testCaseName.isEmpty()) {
      msg = testCaseName + ": " + msg;
    }

    LOGGER.info(msg);
    actionLog.add(new ActionPair(msg, "unknownAction"));
  }

  public long timer_restart() {
    return System.nanoTime();
  }

  public long timer_duration(long start) {
    stop = System.nanoTime();
    return (stop - start) / 1000000000;
  }

  public String getHTMLSource() {
    return driver.getPageSource();
  }

  public String adminUser() {
    return adminUser;
  }

  public String testMDN() {
    return testMDN;
  }

  public String adminPass() {
    return adminPass;
  }

  public static class Builder {

    private String browser_type = "";
    private String platform = "";
    private String version = "";
    private String seleniumHub = "";
    private String site = "";
    private String seleniumHubSelector = "";
    private boolean useErrorThrowingExplicitWait;

    public Builder() {
    }

    public Builder setBrowser_type(String browser_type) {
      this.browser_type = browser_type;
      return this;
    }

    public Builder setPlatform(String platform) {
      this.platform = platform;
      return this;
    }

    public Builder setVersion(String version) {
      this.version = version;
      return this;
    }

    public Builder setSeleniumHub(String seleniumHub) {
      this.seleniumHub = seleniumHub;
      return this;
    }

    public Builder setSite(String site) {
      this.site = site;
      return this;
    }

    public Builder setUseErrorThrowingExplicitWait(
            boolean useErrorThrowingExplicitWait) {
      this.useErrorThrowingExplicitWait = useErrorThrowingExplicitWait;
      return this;
    }

    public Browser build() {
      return new Browser(this);
    }
  }

  public long timer_s() {
    start = System.nanoTime();
    return start;
  }

  public void timer_e(String msg, long startTime) {
    long endTime = System.nanoTime();
    String duration = Integer.toString(Math.round((endTime - startTime) / 1000000));
    logAction(msg);
    logAction(duration + " milliseconds");
  }

  private Browser browserTitle(String browser, String version, String platform) {
    logAction("TEST " + browser + browser_version + browser_platform);
    return this;
  }

  public String randomName() {
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date today = Calendar.getInstance().getTime();
    String reportDate = df.format(today);
    String remove = reportDate.replaceAll("[/:a-zA-Z]", "");
    String removespace = remove.replaceAll("\\s", "");
    String allowedChars = "abcdefghijklmnopqrstuvwxyz";
    String temp = RandomStringUtils.random(7, allowedChars);
    String randomName = "webqeauto" + temp;
    Reporter.log("Created random name :" + randomName);
    return randomName;
  }

  public String randomText(int length) {
    StringBuffer buffer = new StringBuffer(length);
    while (buffer.length() < length) {
      if (buffer.length() > 0) {
        buffer.append(" ");
      }
      buffer.append(RandomStringUtils.random(Math.min(128,
              length - buffer.length()),
              ALLOWED_CHARS)
              .trim());
    }
    return buffer.toString();
  }

  public String uniqueName() {
    return "webqeauto" + String.valueOf(UUID.randomUUID())
            .replace("-", "")
            .toLowerCase();
  }

  public String randomPhoneNumber() {
    // area code
    String areaCode = null;
    String areaCodeSelector = "1234567";
    String areaCodePicked = RandomStringUtils.random(1, areaCodeSelector);

    switch (areaCodePicked) {
      case "1":
        areaCode = "408";
        break;
      case "2":
        areaCode = "650";
        break;
      case "3":
        areaCode = "510";
        break;
      case "4":
        areaCode = "620";
        break;
      case "5":
        areaCode = "316";
        break;
      case "6":
        areaCode = "415";
        break;
      case "7":
        areaCode = "310";
        break;
    }

    String allowedChars = "123456789";
    String temp = RandomStringUtils.random(7, allowedChars);
    String randomName = areaCode + temp;
    logAction("Random Phone Number :" + randomName);
    return randomName;
  }

  public Browser compareText(String live, String given) {
    logAction("Comparing Text  Live:  '" + live
            + "' must contain given :  '"
            + given
            + "'");
    live = live.toLowerCase();
    given = given.toLowerCase();

    if (!live.contains(given.trim())) {
      reporterLogger("******Fail  Live  Text: '" + live
              + "' Not Match Given: '"
              + given
              + "'");
      throw new RuntimeException();
    }
    return this;
  }

  public void refresh() {
    logAction("BROWSER REFRESHES!");
    driver.navigate().refresh();
  }

  public void back() {
    driver.navigate().back();
  }

  public void switchTo_frameIndex(int index) {
    driver.switchTo().frame(index);
  }

  public void switchTo_deafult() {
    driver.switchTo().defaultContent();
  }

  public String getWindowHandle() {
    String parentWindow = driver.getWindowHandle();
    return parentWindow;
  }

  public void switchTo_Window(String window) {
    driver.switchTo().window(window);
  }

  public void switchTo_NewestWindow() {
    String parentWindow = driver.getWindowHandle();
    Set<String> allWindows = driver.getWindowHandles();
    for (String curWindow : allWindows) {
      driver.switchTo().window(curWindow);
    }

  }

  public void switchTo_frameName(String frame) {
    switchToFrame(By.name(frame));
  }

  public void switchToFrame(By by) {
    driver.switchTo().defaultContent();
    driver.switchTo().frame(waitForVisibilityOfElement(by));
  }

  public String getCurrentUrl() {
    String url = driver.getCurrentUrl();
    return url;
  }

  public Browser verifyDropDownSelectorBy(String locator, String text,
          String type) {
    logAction("Checking DropDown");
    WebElement dropDownCheck1;
    if (type.equals("id")) {
      dropDownCheck1 = driver.findElement(By.id(locator));
    }
    else {
      dropDownCheck1 = driver.findElement(By.className(locator));
    }
    dropDownCheck1.click();
    Select selected1 = new Select(dropDownCheck1);
    String getText = selected1.getFirstSelectedOption().getText();
    compareText(getText, text);
    return this;
  }

  public void initElements(Object PageObject) {
    PageFactory.initElements(this.driver, PageObject);
  }

  public void close() {
    logAction("BROWSER CLOSE");
    closed++;
    if (closed == 1) {
      driver.manage().deleteAllCookies();
      driver.quit();
    }
  }

  public void quit() {
    logAction("BROWSER QUIT");
    driver.manage().deleteAllCookies();
    driver.quit();

  }

  public void deleteAllCookies() {
    driver.manage().deleteAllCookies();
  }

  public void executeScript(String s1, Object obj) {
    JavascriptExecutor jsx = (JavascriptExecutor) driver;
    jsx.executeScript(s1, obj); // scroll down to iAgreeEulaAcceptCheckbox
  }

  public void sendTextExecuteScript(String s1, WebElement elem) {
    JavascriptExecutor jsx = (JavascriptExecutor) driver;
    jsx.executeScript("arguments[0].value = '" + s1 + "'", elem);
  }

  public void executeScriptFireEvent(String s1) {
    JavascriptExecutor jsx = (JavascriptExecutor) driver;
    ((JavascriptExecutor) driver).executeScript("return document.getElementByssSelector(" + s1
            + ").blur()");
  }

  public void actionsSelection(WebElement ele, WebElement ele2) {
    Actions builder = new Actions(driver);
    builder.moveToElement(ele)
            .click(ele)
            .moveToElement(ele2)
            .click(ele2)
            .build()
            .perform();
    logAction("actionsSelection done");
  }

  public void actionsHover(WebElement ele) {
    Actions builder = new Actions(driver);
    builder.moveToElement(ele)
            .build()
            .perform();
    logAction("actionsSelection done");
  }

  public void actionsSendText(WebElement textfield, String text) {
    Actions builder = new Actions(driver);
    builder.moveToElement(textfield)
            .click(textfield)
            .sendKeys(textfield, text)
            .build()
            .perform();
    logAction("actionsSelection done");
  }

  public void actionsEnter() {
    Actions actions = new Actions(driver);
    actions.sendKeys(Keys.ENTER).perform();
    logAction("Pressing Enter");
  }

  public void actionsMoveToElement(WebElement ele) {
    Actions builder = new Actions(driver);
    builder.moveToElement(ele).build().perform();
    logAction("actionsMoveToElement done");
  }

  public void waitForDropDownValue(String locator) {
    waitForDropDownValue(By.cssSelector(locator));
  }

  public void waitForDropDownValue(By by) {
    logAction("Waiting For Dropdown Value");
    WebDriverWait wait = getDriverWait(waitTimeMedium);
    wait.until(ExpectedConditions.presenceOfElementLocated(by));
  }

  public void invisibilityOfElementLocated(By by) {
    ExpectedConditions.invisibilityOfElementLocated(by);
  }

  public void textToBePresentInElement(WebElement ele, String text) {
    ExpectedConditions.textToBePresentInElement(ele, text);
  }

  public void deleteCookieNamed(String cookie) {
    driver.manage().deleteCookieNamed(cookie);
  }

  public int cleanPrice(String livePrice) {
    String intValue1 = livePrice.replaceAll("[.$a-zA-Z]", "");
    intValue1 = intValue1.replace("\"", "\\\"");
    intValue1 = intValue1.replace(",", "");
    int value = Integer.parseInt(intValue1.trim());
    logAction(String.valueOf(value));
    return value;
  }

  public String randomCreateUser() {

    String allowedChars = "abcdefghijklmnopqrstuvwxyz";
    String temp = RandomStringUtils.random(3, allowedChars);
    String randomName = "automationcreate+" + temp + "@itsoninc.com";
    Reporter.log("Created random name :" + randomName);

    return randomName;
  }

  public Browser clearCookies() {
    driver.manage().deleteAllCookies();
    return this;
  }

  public Browser dropDownSelectorBy(String locator, String text, String type) {
    WebElement dropDownListBox23;
    if (type.equals("id")) {
      dropDownListBox23 = driver.findElement(By.id(locator));
    }
    else {
      dropDownListBox23 = driver.findElement(By.className(locator));
    }
    dropDownListBox23.click();
    Select clickThis23 = new Select(dropDownListBox23);
    logAction("Selecting Dropdown");
    clickThis23.selectByVisibleText(text);
    return this;
  }

  public void waitForTextToAppear(WebElement element, String textToAppear,
          int time) {
    logAction("waitForTextToAppear(" + element + ", " + textToAppear + ", " + time + "): START ...");
    WebDriverWait wait = new WebDriverWait(driver, time);
    wait.until(ExpectedConditions.textToBePresentInElement(element,
            textToAppear));
    logAction("waitForTextToAppear(" + element + ", " + textToAppear + ", " + time + "): ... END");
  }

  public void waitForValueToAppear(WebElement element, String value) {
    waitForValueToAppear(element, value, waitTimeMedium);
  }

  public void waitForValueToAppear(final WebElement element, final String value, int time) {
    logAction("waitForValueToAppear(" + element + ", " + value + ", " + time + "): START ...");
    WebDriverWait wait = new WebDriverWait(driver, time);
    wait.until(new Function<WebDriver, Boolean>() {
      public Boolean apply(WebDriver input) {
        try {
          return (element.getAttribute("value").equals(value));
        }
        catch (Exception e) {
        }
        return Boolean.FALSE;
      }
    });
    logAction("waitForValueToAppear(" + element + ", " + value + ", " + time + "): ... END");
  }

  public void waitForValueToAppear(final By by, final String value) {
    waitForValueToAppear(by, value, waitTimeMedium);
  }

  public void waitForValueToAppear(final By by, final String value, int time) {
    waitForValueToAppear(findElement(by), value, time);
  }

  public void waitForElementNotEmpty(final WebElement element, int time) {
    logAction("waitForElementNotEmpty(" + element + ", " + time + "): START ...");
    WebDriverWait wait = new WebDriverWait(driver, time);
    wait.until(new Function<WebDriver, Boolean>() {
      public Boolean apply(WebDriver input) {
        try {
          return (!element.getText().isEmpty());
        }
        catch (Exception e) {
        }
        return Boolean.FALSE;
      }
    });
    logAction("waitForElementNotEmpty(" + element + ", " + time + "): ... END: element.getText() is '" + element.getText() + "'.");
  }

  public void waitForElementNotEmpty(final WebElement element) {
    waitForElementNotEmpty(element, waitTimeMedium);
  }

  public void waitForElementNotEmpty(final By by, int time) {
    waitForElementNotEmpty(findElement(by), time);
  }

  public Browser sleep(int time) {
    try {
      logAction("ZZZzz Sleeping zzZZZZ for " + time);
      Thread.sleep(time);
    }
    catch (InterruptedException ex) {
      LOGGER.fatal("Sleep interrupted.", ex);
    }
    return this;
  }

  // used for stale elements for link textelements used for search result
  public boolean retryingLinkTextElement(String text) {
    boolean result = false;
    int attempts = 0;
    while (attempts < 3) {
      try {
        logAction("<<<<Clicking Stale element>>> " + text
                + " Attempts: "
                + attempts);
        findElement(By.linkText(text)).click();
        result = true;
        break;
      }
      catch (Exception e) {
      }
      attempts++;
    }
    return result;
  }

  public void takeScreenshot(String filename) throws IOException {
    File out = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    File target = new File("target");
    File base;
    if (new File(".").getCanonicalFile().getName().equals("target")) {
      base = new File(".");
    }
    else {
      base = target;
    }
    File file = new File(new File(base, SCREENSHOT_FOLDER), filename);
    File parent = file.getParentFile();
    if (!parent.exists() && !parent.mkdirs()) {
      throw new IOException("Could not create screenshots folder:" + parent);
    }
    FileUtils.copyFile(out, file);
  }

  public void moveMouse(WebElement element) {
    Actions actions = new Actions(driver);
    actions.moveToElement(element).build().perform();
  }

  public void sendKeys(final WebElement element, final String value,
          final boolean clearOldValue) {
    getDriverWait(waitTimeMedium).until(new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply(WebDriver input) {
        String elementid = element.getAttribute("id");
        if (elementid == null || elementid.isEmpty()) {
          elementid = element.getAttribute("name");
        }
        if (elementid != null && !elementid.isEmpty()) {
          logAction("Set New value '" + value
                  + "' to Element '"
                  + elementid
                  + "'.");
        }

        if (clearOldValue) {
          String elementOldValue = element.getAttribute("value");
          if (elementOldValue != null) {
            logAction("Clear Old value '" + elementOldValue + "'.");
          }
          element.clear();
        }
        element.sendKeys(value);
        return Boolean.TRUE;
      }
    });
  }

  public void sendKeys(final WebElement element, final String value,
          final boolean clearOldValue, final String expectedFinalValue) {
    getDriverWait(waitTimeMedium).until(new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply(WebDriver input) {
        element.click();
        if (clearOldValue) {
          element.clear();
        }
        element.sendKeys(value);
        String setValue = element.getAttribute("value");
        String setText = element.getText();
        if (!(expectedFinalValue.equals(setValue) || expectedFinalValue.equals(setText))) {
          LOGGER.warn("The set value (%s) text (%s) after send-keys doesn't match with expected value (%s) for element (%s) - retrying.",
                  setValue,
                  setText,
                  expectedFinalValue,
                  element);
          return Boolean.FALSE;
        }
        return Boolean.TRUE;
      }
    });
  }

  public Browser selectDropdown(final WebElement element, final String label) {
    getDriverWait(waitTimeMedium).until(new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply(WebDriver input) {
        Select select = new Select(element);
        select.selectByVisibleText(label);
        return Boolean.TRUE;
      }
    });
    return this;
  }

  public List<String> getAllSelectedDropdowns(final WebElement element) {
    return getDriverWait(waitTimeMedium).until(new ExpectedCondition<List<String>>() {

      @Override
      public List<String> apply(WebDriver input) {
        Select select = new Select(element);
        List<String> all = new ArrayList<>();
        for (WebElement x : select.getAllSelectedOptions()) {
          all.add(x.getText());
        }
        return all;
      }
    });
  }

  private String hubSelector(String selehub) {
    String hubUrl;

    switch (selehub.toLowerCase()) {

      case "localhub":
        hubUrl = "http://localhost:4444/wd/hub";
        break;
      default:
        hubUrl = selehub;
        break;
    }

    logAction("Hub Url: " + hubUrl);
    return hubUrl;
  }

  public Browser reportLinkLog(String jobName) {
    if (seleniumHubUrl.contains("ondemand.saucelabs.com")) {
      String sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
      logAction("SauceOnDemandSessionID=" + sessionId + " job-name=" + jobName);
      String videoURL = "Video: http://saucelabs.com/jobs/" + sessionId;
      Reporter.log(videoURL);
    }
    else if (seleniumHubUrl.contains("hub.browserstack.com")) {
//      BrowserStackApi browserStackApi = new BrowserStackApi();

      String sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
      logAction("SessionID=" + sessionId + " job-name=" + jobName);
      String videoURL = null;
      try {
//        videoURL = browserStackApi.browserStack(sessionId);
      }
      catch (Exception ex) {
        java.util.logging.Logger.getLogger(Browser.class.getName()).log(Level.SEVERE, null, ex);
      }
      Reporter.log(videoURL);
      Reporter.log(jobName);
    }
    else {
      logAction(jobName);
      Reporter.log(jobName);
    }
    return this;
  }

  public boolean retryingElement(WebElement element) { // used for stale
    // elements
    boolean result = false;
    int attempts = 0;
    while (attempts < 3) {
      try {
        logAction("<<<<Clicking Stale element>>> " + element.toString());
        element.click();
        result = true;
        break;
      }
      catch (Exception e) {
      }
      attempts++;
    }
    return result;
  }

  public boolean retryingElement(By by) { // used for stale elements
    boolean result = false;
    int attempts = 0;
    while (attempts < 5) {
      try {
        logAction("<<<<Clicking Stale element>>> " + by.toString() + " attempts :" + attempts);
        findElement(by).click();
        result = true;
        break;
      }
      catch (Exception e) {
      }
      attempts++;
    }
    return result;
  }

  public boolean retryingWaitElement(By by) { // used for stale elements
    boolean result = false;
    int attempts = 0;
    while (attempts < 3) {
      try {
        logAction("<<<<Waiting for Stale element>>> " + by.toString() + " attempts :" + attempts);
        waitForClickableElement(by);
        result = true;
        break;
      }
      catch (Exception e) {
      }
      attempts++;
    }
    return result;
  }

  public boolean retryingWaitElement(WebElement element) { // used for stale elements
    boolean result = false;
    int attempts = 0;
    while (attempts < 3) {
      try {
        logAction("<<<<Waiting for Stale element>>> " + element.getText() + " attempts :" + attempts);
        waitForClickableElement(element);
        result = true;
        break;
      }
      catch (Exception e) {
      }
      attempts++;
    }
    if (result = false) {
      throw new IllegalStateException("Timeout waiting for Element");
    }
    return result;
  }

  public void switchTo_frameIndex() {
    try {
      driver.switchTo().frame(0);
    }
    catch (Exception e) {
    }
  }

  public void waitForTextToAppear(WebElement element, String textToAppear) {
    waitForTextToAppear(element, textToAppear, 200);
  }

  public void waitForInvisibilityOfElement(By by) {
    getDriverWait(waitTimeMedium).until(ExpectedConditions.invisibilityOfElementLocated(by));
  }

  public Browser dropDownSelectorBy(WebElement menu, String text) {
    waitForClickableElement(menu).click();
    Select clickThis23 = new Select(menu);
    logAction("Selecting Dropdown");
    logAction("Selecting : " + text);
    clickThis23.selectByVisibleText(text);
    actionsEnter();
    // waitForInvisibilityOfElement(By.linkText(text));
    return this;
  }

  public Browser dropDownSelectorValueBy(WebElement menu, int index, String text) {
    menu.click();
    Select clickThis23 = new Select(menu);
    logAction("Selecting Dropdown: " + text);
    clickThis23.selectByIndex(index);
    actionsEnter();
    if (!text.isEmpty()) {
      waitForInvisibilityOfElement(By.linkText(text));
    }
    return this;
  }

  public Browser dropDownSelector(String menuId, int index, String text) {
    try {
      WebElement menu = findElement(By.id(menuId));
      dropDownSelectorValueBy(menu, index, text);
    }
    catch (org.openqa.selenium.ElementNotVisibleException ex) {
      logAction("Can not select the dropdown when it's hidden: " + ex.toString());
      logAction("Catch the exception by visibling this element before selecting:");
      setVisibleElement(menuId);
      WebElement menu = findElement(By.id(menuId));
      dropDownSelectorValueBy(menu, index, text);
    }
    return this;
  }

  public Browser setVisibleElement(String elementId) {
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    executor.executeScript("document.getElementById('" + elementId + "').style.display='block';");
    return this;
  }

  public void actionsArrowDown() {
    actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).perform();
    logAction("Pressing Enter");
  }

  public void scrollUp() {
    JavascriptExecutor jse = (JavascriptExecutor) driver;
    jse.executeScript("window.scrollBy(750,0)", "");
    logAction("Done Scolling ");
  }

  public void reporterLogger(String msg) {
    logAction(msg);
    Reporter.log(msg);
  }

  public void scrollDown() {
    JavascriptExecutor jse = (JavascriptExecutor) driver;
    jse.executeScript("window.scrollBy(0,250)", "");
    logAction("Done Scolling ");
  }

  public Browser verifyTextField(WebElement textField, String text) {
    waitForClickableElement(textField);
    logAction("Checking textField for : " + text);
    String live = textField.getAttribute("value");
    compareText(live, text);
    return this;
  }

  public Browser verifyDropDownSelected(WebElement menu, String text) {
    logAction("Checking DropDown for : " + text);
    staleMenu(menu);
    // String live =
    compareText(liveSelection, text);
    return this;
  }

  public boolean staleMenu(WebElement menu) { // was having issue on dropdown
    // menu verification
    boolean result = false;
    int attempts = 0;
    while (attempts < 5) {
      try {
        logAction("<<<<Looking For StaleMenu>>> attempts :" + attempts);
        menu.click();
        Select selected = new Select(menu);
        logAction("Geting 1st Selected Attempt: " + attempts);
        liveSelection = selected.getFirstSelectedOption().getText();
        result = true;
        break;
      }
      catch (Exception e) {
      }
      attempts++;
    }
    return result;
  }

  public String getLiveSelection(WebElement menu) {
    staleMenu(menu);
    return liveSelection;
  }

  public WebElement waitfortext(String id, String text) {
    WebDriverWait wait = getDriverWait(waitTimeMedium);
    WebElement ele = null;
    ele = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    return ele;
  }

  public boolean pollElementIsClickAble(By by) { // was having issue on dropdown
    // menu verification
    boolean result = false;
    int attempts = 0;
    while (attempts < 5) {
      try {
        logAction("<<<<Waiting for Element to be ClickAble>>>  attempts :" + attempts);
        waitForClickableElement(by);
        result = true;
        break;
      }
      catch (Exception e) {
      }
      attempts++;
    }
    return result;
  }

  public String randomNumber(int place) {
    String allowedChars = "123456789";
    String randomNumber = RandomStringUtils.random(place, allowedChars);
    return randomNumber;
  }

  public String getText(final WebElement element) {
    return getText(element, WaitTime.Medium);
  }

  public String getText(final WebElement element, WaitTime waitTime) {
    return getDriverWait(waitTime.toSeconds()).until(new ExpectedCondition<String>() {

      @Override
      public String apply(WebDriver input) {
        try {
          if (element.isDisplayed()) {
            return element.getText();
          }
        }
        catch (NoSuchElementException | ElementNotVisibleException e) {
        }
        return null;
      }
    });
  }

  public String getTextWhenElementVisible(final WebElement element) {
    final String marker = new String();
    String value = getDriverWait(waitTimeMedium).until(new ExpectedCondition<String>() {

      @Override
      public String apply(WebDriver input) {
        if (element.isDisplayed()) {
          return element.getText();
        }
        return marker;
      }
    });
    if (value == marker) {
      return null;
    }
    return value;
  }

  public String getTextNotRecursive(WebElement element) {
    String text = getText(element);
    logAction(text);
    for (WebElement child : element.findElements(By.xpath("./*"))) {
      logAction(child.getText());
      text = text.replaceFirst(child.getText(), "");
    }
    text = text.replace("\n", "");
    logAction("Get the text is: '" + text + "'");
    return text;
  }

  public String getValue(final WebElement element) {
    return getDriverWait(waitTimeMedium).until(new ExpectedCondition<String>() {

      @Override
      public String apply(WebDriver input) {
        if (element.isDisplayed()) {
          return element.getAttribute("value");
        }
        return null;
      }
    });
  }

  public void logOutReplacement() { // needed for bl which now has no logout btn
    logAction("*****logOutReplacement");
    driver.manage().deleteAllCookies();
    sleep(4000); // needed to avoid http status 500 error
    driver.get(givenUrl);
    waitForPageLoaded();
  }

  public void setTestCaseName(String testCaseName) {
    this.testCaseName = testCaseName;
  }

  public void uploadFile(WebElement element, File file, By inprogress) {
    if (!file.exists()) {
      throw new IllegalStateException(file.getAbsolutePath() + " doesn't exist.");
    }
    this.driver.setFileDetector(new LocalFileDetector());
    sendKeys(element, file.getAbsolutePath(), false);
    waitForInvisibilityOfElement(inprogress);
  }

  public boolean isSelected(WebElement element) {
    return element.isSelected();
  }

  public String createRandomNumber(int charNumber) {
    String allowedChars = "123456789";
    String temp = RandomStringUtils.random(charNumber, allowedChars);
    String randomName = temp;
    logAction("Random  Number :" + randomName);
    return randomName;
  }
}
