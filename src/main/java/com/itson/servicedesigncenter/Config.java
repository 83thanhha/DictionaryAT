package com.itson.servicedesigncenter;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Config {

  private HashMap map;
  private String hostUrl;
  private String hostUrlSdc;
  private String hostUrlBl;
  private String hostUrlMyAccount;

  private String configFile;
  private URL hub;
  private String browser_type;
  private String browser_version;
  private String browser_platform;
  private String browser_platform_version;
  private String testMDN;
  private String cookieName;

  private String tenantId;
  private String envId;
  private String partnerId;
  private String grant_type;
  private String username;
  private String password;
  private String adminName;
  private String adminPassword;
  private String client_id;
  private String client_secret;
  private String oAuthUrl;
  private String adminUrl;
  private String adapterUrl;

  private String blUsername;
  private String blPassword;
  private String sandbox;
  private String verify_username;
  private String verify_password;
  private String partnerName;

  public Config() {
    System.setProperty("envFile", "../testng/env/qaZactMobile.json"); //qaZactQaAuto
    System.setProperty("hub", "http://localhost:4444/wd/hub");

    String envFile = System.getProperty("envFile");
    System.out.println(envFile);
    if (envFile == null) {
      System.out.println("envFile == null Setting Default to IT Zact Mobile");
      envFile = "../testng/env/itSprint.json";
    }
    if (envFile.isEmpty()) {
      System.out.println("empty");

      throw new IllegalStateException("Invalid -DenvFile=[" + envFile
              + " ] specified on command line\n");
    }
    this.init(envFile);
  }

  public Config(String filename) {
    this.init(filename);
  }

  private void init(String filename) {
    configFile = filename;
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(configFile));
    }
    catch (FileNotFoundException e) {
      String pwd = "Unable to find file" + filename + " from " + " working directory = " + System.getProperty("user.dir");
      e.printStackTrace();
      throw new IllegalArgumentException(pwd);
    }
    Gson gson = new Gson();
    map = new HashMap<String, String>();
    map = (HashMap<String, String>) gson.fromJson(br, map.getClass());
    //  this.hostUrlSdc = (String) map.get("hostUrlSdc");
    this.testMDN = (String) map.get("testMDN");
    this.tenantId = (String) map.get("tenantId");
    this.envId = (String) map.get("envId");
    this.partnerId = (String) map.get("partnerId");
    this.grant_type = (String) map.get("browser_platform");
    this.username = (String) map.get("username");
    this.password = (String) map.get("password");
    this.adminName = (String) map.get("adminuser");
    this.adminPassword = (String) map.get("adminpassword");
    this.client_id = (String) map.get("client_id");
    this.client_secret = (String) map.get("client_secret");

    this.blUsername = (String) map.get("blUsername");
    this.blPassword = (String) map.get("blPassword");
    this.sandbox = (String) map.get("sandbox");
    this.partnerName = (String) map.get("partnerName");

    this.hostUrlBl = "https://zact-operator-di01-service." + envId + ".itsonsaas.net:443/op-admin/?siteId=" + partnerId;
    this.hostUrlMyAccount = "http://www.oxfordlearnersdictionaries.com/definition/english/love_1?q=love";
    this.hostUrlSdc = "https://" + tenantId + "-portal-di01." + envId + ".itsonsaas.net/";
    this.hostUrl = "http://www.oxfordlearnersdictionaries.com/definition/english/love_1?q=love";
    cookieName = null;//(String) map.get("cookieName");
  }

  private static void checkString(String name, String arg) {
    String err = "Bad argument in Config file: " + name;
    if (arg == null) {
      throw new NullPointerException(err + " is NULL");
    }
    if (arg.isEmpty()) {
      throw new NullPointerException(err + " is EMPTY");
    }
  }

  public String getHostUrl(String site) {
    site = site.toLowerCase();
    String checkUrl = System.getProperty("hostUrl");
    if (checkUrl == null || checkUrl.equals("")) {
      switch (site) {
        case "bl":
          hostUrl = hostUrlBl;
          break;
        case "broadleaf":
          hostUrl = hostUrlBl;
          break;
        case "myaccount":
          hostUrl = hostUrlMyAccount;
          break;
        default:
          hostUrl = hostUrlSdc;//default value
          break;
      }

    }
    else {
      hostUrl = checkUrl;
    }
    return hostUrl;
  }

  public String getConfigFile() {
    return configFile;
  }

  public String toString() {
    String str = new StringBuilder()
            .append("configFile:" + configFile + "\n")
            .append("hostUrl:" + hostUrl + "\n")
            .toString();
    return str;
  }

  public String getMDN() {
    return testMDN;
  }

  public String getCookieName() {
    if (cookieName == null) {
      return "PHPSESSID";
    }
    return cookieName;
  }

  public String getTenantID() {
    return tenantId;
  }

  public String getEnvId() {
    return envId;
  }

  public String getPartnerId() {
    return partnerId;
  }

  public String getGrant_type() {
    return grant_type;
  }

  public String getClientSecret() {
    return client_secret;
  }

  public String getClientID() {
    return client_id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getAdminUsername() {
    return adminName;
  }

  public String getAdminPassword() {
    return adminPassword;
  }

  public String getoAuthUrl() {
    oAuthUrl = "http://subscriber-di01-service." + envId + ".itsonsaas.net:8280/saas/services/1.0/oauth/login?grant_type=password&username=" + username + "&password="
            + password + "&client_secret=" + client_secret + "&client_id=" + client_id;

    return oAuthUrl;
  }

  public String getAdminUrl() {
    adminUrl = "http://subscriber-di01-service." + envId + ".itsonsaas.net:8280/saas/services/1.0/partner/" + partnerId + "/";
    return adminUrl;
  }

  public String getAdapterUrl() {
    adapterUrl = "http://subscriber-di01-service." + envId + ".itsonsaas.net:8280/saas/services/1.0/partner/" + partnerId + "/subscriber/provision";
    return adapterUrl;
  }

  public String getHostBlUrl() {
    return hostUrlBl;
  }

// parmters come from command line if not called or left blank deafult value is given
  public String getHub() {
    String hubUrl = System.getProperty("hub");
    if (hubUrl == null || hubUrl.equals("")) {
      hubUrl = "qahub";//default value
    }
    return hubUrl;
  }

  public String getBrowserPlatform() {
    browser_platform = System.getProperty("bPlatform");
    if (browser_platform == null || browser_platform.equals("")) {
      browser_platform = "ANY";//default value
    }
    return browser_platform;
  }

  public String getBrowserPlatformVersion() {
    browser_platform_version = System.getProperty("bPlatformVersion");
    if (browser_platform_version == null || browser_platform.equals("")) {
      browser_platform_version = "ANY";//default value
    }
    return browser_platform_version;
  }

  public String getBrowserType() {
    browser_type = System.getProperty("bType");
    if (browser_type == null || browser_type.equals("")) {
      browser_type = "firefox"; //default value
    }
    if (browser_type.equals("ie")) { //needed for saucelabs
      browser_type = "internet explorer";
    }
    return browser_type;
  }

  public String getBrowserVersion() {
    browser_version = System.getProperty("bVersion");
    if (browser_version == null || browser_version.equals("")) {
      browser_version = "ANY";//default value
    }
    return browser_version;
  }

  public String getTestName() {
    String testName = System.getProperty("testName");
    if (testName == null) {
      testName = "webauto1";
    }
    return testName;
  }

  public String getBlUsername() {
    return blUsername;
  }

  public String getBlPassword() {
    return blPassword;
  }

  public String getSandbox() {
    return sandbox;
  }

  public String getPartnerName() {
    return partnerName;
  }

  public String getVerifyUsername() {
    verify_username = "verify";
    return verify_username;
  }

  public String getVerifyPassword() {
    verify_password = "SOASTA650";
    return verify_password;
  }

}
