/**
 * Common functions is used to compare between expected value and fact value.
 * Includes Util JavaScript, JQuery, TraceBack ... classes.
 *
 * @author HaNT
 */
package com.itson.myaccount;

import com.itson.servicedesigncenter.Config;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Common {

  public static class Constant {
    
    public static boolean config_doDebug = false;
    public static boolean config_doSetNickname = true;
    public static boolean config_doVerifyMessage = false;
    public static boolean config_doWaitBufferAfterPurchasing = true;
    public static boolean config_doWaitSelenium = true; //Click fast the UGDG Voice/Text/Data Li element, The Selenium miss one step.
    
    // CONFIG FOR TICKETS OR TEMP ISSUES
    // TICKETS FOR UI
    public static String feature_SAAS_15855_CustomizeTheWording = "SAAS-15855: My Account : Enable Sapphire ( and other tenants ) to customize the wording";
    
    // TICKETS FOR COMMON FUNCTIONS
    public static String closed = ": CLOSED";
    public static boolean issue_BrokenMainTab = false;
    public static boolean issue_PMS_13835 = true;
    public static boolean issue_SAAS_11186 = true;
    public static String issue_SAAS_11186_Title = "SAAS-15445: MyAccount: Can not see the purchased plan in My Plans and Subscription List.";
    public static boolean issue_Config_BasePlanName = true;
    public static boolean issue_PurchasingIsUpdatedSlowly = true;
    public static boolean issue_SAAS_8805_Ignored = true;
    public static boolean issue_PhoneFormat_Ignored = false;
    public static boolean issue_PMS_13999 = true;
    public static String issue_SAAS_11280_Title = "SAAS-11280: My account: Can't upgrade/downgrade a base plan";
    public static boolean issue_Spinner_SpecialOffer = true;
    public static String issue_Spinner_SpecialOffer_PMS_14004 = "PMS-14004: MyAccount > Special Offers: Spinner is not displayed when the web is loading";
    public static String issue_SAAS_10874_Title = "SAAS-10874: Not matched base plan name" + closed;
    public static String issue_manageAPlan_DeviceUsing2 = "SAAS-11012: MyAccount: The new joined device should NOT be shared the previous special offers automatically" + closed;
    public static String issue_SAAS_10917_Title = "SAAS-10917: My account: Can't renew / Cancel plan with some plan" + closed;
    public static String issue_SAAS_10878_Title = "SAAS-10878: MyAccount: Without drop down list to choose action Assign or Share" + closed;
    public static String issue_WithoutGiftOption_Title = "MyAccount: Without Gift option";
    public static String issue_SAAS_10876_Title = "SAAS-10876: Wrong center title during adjusting plan" + closed;
    public static String issue_SAAS_9007_Title = "SAAS-9007: Wrong phone number and nickname format" + closed;
    public static String issue_OP_2843_Title = "OP-2843: The cart/skuwithallowance returns NumberFormatException" + closed;
    public static String issue_OP_3035_Title = "OP-3035: Getting error code 500 when retrieving products" + closed;
    public static String issue_SAAS_11320_Title = "SAAS-11320: MyAccount: Spinner is stopped (not displayed) but the content is not ready." + closed;
    public static String issue_SAAS_9397_Title = "SAAS-9397: My Account: Add loading icon for My Account screens";
    public static String issue_UnsuccessfullyPromotedBasePlan_Title = "Base plan is promoted unsuccessfully";
    public static String issue_CurrencyFormat_Title = "MyAccount: Currency format should be unique.";
    public static String issue_Card_CantAdd_Title = "OPS-3929: secure load balancer is down on QA env: Can not add card." + closed;
    public static String issue_Card_Message_SoFast = "Success message of adding/editing/removing card is so fast. Or there is No message.";
    public static String issue_CreateAccount_AccountBalance_Zero = "Can NOT add Account Balance during creating account.";
    public static String issue_SAAS_10550_AccountOverviewBlankWhenChangeNotMatchedPassword = "SAAS-10550: My Account: After change password with not matched new password, Account Overview screen is blank, section is lost." + closed;
    public static String issue_SAAS_8805_ChangeEmailAndApplyToUserName = "SAAS-8805: My account: when I change email address as a customer, it has to get changed in username as well" + closed;
    public static String issue_SAAS_12514_InvalidDeviceAndAllowancesInDeviceUsingThisPlan = "SAAS-12514: My Account: The app shows invalid device + allowances in 'DEVICES USING THIS PLAN' section" + closed;
    public static String issue_SAAS_14259 = "SAAS-14259: My account: Some errors for Discount - Coupon code feature";
    
    // TICKET FOR PURCHASE FLOW
    public static String issue_PurchaseFlow_ThePriceIsChangedWithNoReason = "The price is changed with no reason";
    
    // TICKET FOR BUNDLE
    public static boolean feature_Purchase_Complete_Description_Has_AssignShareGiftInfo = false;
    public static String issue_Bundle_SAAS_10600 = "SAAS-10600: MyAccount: As an operator, I can create bundles consisting of multiple plans" + closed;
    public static String issue_Bundle_SAAS_11313_BundleInvalidAllwance = "SAAS-11313: My Account: The bundle shows invalid allowance of bundle & plan" + closed;
    public static String issue_Bundle_OP_3788 = "OP-3788: Plan s price and monthly cost are showing incorrect when purchasing a basic bundle.";
    public static String issue_Bundle_SAAS_11487 = "SAAS-11487: My Account: Some issues in bundle history: Manage Plans > Bundles > Bundle Details";
    public static String issue_Bundle_SAAS_12978 = "SAAS-12978: My Account: Some UI issues on view detail bundles, view detail plan in bundle" + closed;
    public static String issue_Bundle_SAAS_11318 = "SAAS-11318: My Account: The bundle is showing incorrect usage, cycle bar of bundle & plan";
    
    
    // CONFIG FOR TENANT/PARTNER
    public static String tenant_Sapphire = "sapphire";
    public static String partner_Sapphire = "sapphire-sapphire";
    public static String tenant_Zact = "zact";
    public static final String partnerId_Zact_Mobile = "d878bc1e-9bb8-4a0e-b224-a4ba0d7dfcec";
    public static final String partnerId_Zact_Virgin = "a178339b-f446-46d7-a50c-91eb12c32496";
    public static final String partnerId_Zact_Evizi = "ecf8f8c3-33c9-45a0-8713-b17ddc27d8fa";
    public static final String partnerId_Zact_QAAuto = "369334f3-2edd-43b7-8141-f6b757a65078";
    public static final String partnerId_Zact_Sapphire = "ecf8f8c3-33c9-45a0-8713-b17ddc27d8fa";
    public static final String partnerId_Zact_Mvp = "e14412e4-1a32-46e0-84d9-caeb614d6e7f";
    
    public static final String family = "Family";
    public static final String custom = "Custom";
    
    public static final String basePlan = "Base"; // Use for the data file plan.xlsx
    public static final String specialOffer = "Special"; // Use for the data file plan.xlsx
    public static final String baseBundle = "BaseBundle"; // Use for the data file plan.xlsx
    public static final String compoundBundle = "CompoundBundle"; // Use for the data file plan.xlsx
    
    public static final String active = "Active";
    public static final String detach = "Detach";

    public static int maxDisplayedTotalPages = 5;
    
    public static int maxDevices = 5;
    
    public static boolean accountControlStatusDefaultOn = true;
    
    public static String replacedString = "@_@";
    
    public static final String shareablePinata = "PINATA";
    public static final String shareable = "Shareable";
    public static final String shareableAuto = "SHARE_AUTOMATICALLY";
    public static final String assignable = "Assignable";
    public static final String reassignable = "Reassignable";
    public static final String single = "Single";
    public static final String bundle = "Bundle";
    public static final String voice = "Voice";
    public static final String mins = "Mins";
    public static final String minsLower = "mins";
    public static final String minLower = "min";
    public static final String seconds = "Seconds";
    public static final String message = "Text";
    public static final String text = "Text";
    public static final String texts = "Texts";
    public static final String textsLower = "texts";
    public static final String data = "Data";
    public static final String kb = "KB";
    public static final String mb = "MB";
    public static final String gb = "GB";
    public static final String bytes = "B";
    public static final String days = "Days";
    public static final String day = "day";
    public static final String weeks = "Weeks";
    public static final String months = "Months";
    public static final String monthsAligned = "Months_Aligned";
    public static final String limited = "Limited";
    public static final String unlimited = "Unlimited";
    public static final String recurring = "Recurring";
    public static final String onetime = "Onetime";
    
    public static final String switchOff = "switch-off";
    public static final String switchOn = "switch-on";
    public static final String switchOn_NoLimit = "no limit";
    public static final String switchOn_NoLimit_Uppercase = "NO LIMIT";
    
    public static final String usageDisplay_Summary = "UD_SHOW_SUMMARY";
    public static final String usageDisplay_Unit = "UD_SHOW_UNIT_USAGE_ONLY";
    public static final String usageDisplay_Cycle = "UD_SHOW_CYCLE_USAGE_ONLY";
    public static final String usageDisplay_Both = "UD_SHOW_BOTH";
    public static final String usageDisplay_Default = "DEFAULT";
    public static final String unit = "Unit";
    public static final String cycle = "Cycle";
    public static final String cycleDram_MonthlyAligned = "MONTHS_ALIGNED";
    public static final int cycle_Week = 7;
    
    public static final String shareLink = "// SHARE";
    public static final String manageLink = "// MANAGE";
    
    public static final String sDes = " S.Des";
    public static final String lDes = " L.Des";
    
    // DATA FILE
    public static final String pathData = System.getProperty("user.dir")+"\\";
    public static final String fileDataPlan = "plan.xlsx";
    
    // ICON
    public static final String iconDataUrl = "frontend/images/myaccount/icon_data.png";
    public static final String iconVoiceUrl = "frontend/images/myaccount/icon_voice.png";
    public static final String iconMessageUrl = "frontend/images/myaccount/icon_messaging.png";
    
    public static final String iconPhoneUrl = "frontend/images/myaccount/phones/individual_devices_2.png";
    
    // CONSTANT FOR PLAN
    public static final int allowanceValueMax = 100;
    public static final int allowanceValueNotShareNotAssign_DeviceNotUsing = 0;
    public static final int allowanceValueNotShareNotAssign_ChangeAllowance_Off = -1; // Currently, when shareType != Pinata, the plan still is ON and shares to new device with 0%. STB said: Do NOT care about it currently.
    
    // EXCEL PLAN
    public static final String notAvailable = "N/A";
    public static final String endOfColumn = "ENDOFCOL";
    public static final String endOfFile = "ENDOFFILE";
    
    // EXCEL > BASE PLAN
    public static final int oneMin = 60; // Seconds
    public static final int oneMB = 1024*1024; // Bytes
    public static final int oneGB = oneMB*1024; // Bytes
    public static final int oneText = 1; // Texts
    public static final int limitedUnit50 = 50;
    public static final int limitedUnit100 = 100;
    public static final int limitedUnit200 = 200;
    public static final int limitedUnit400 = 400;
    public static final int limitedUnit600 = 600;
    
    public static final double lineFee1 = 3.00;
    public static final double lineFee2 = 2.97;
    public static final double activationFee = 3.00;
    
    // DISPLAY
    public static final int nameLength_SpecialOffer_ManagePlans = 43;
    public static final int nameLength_CompoundBundle_ManagePlans = 70;
    
    // Unlimited Cycle
    public static final int unlimitedCycle = 180000; // 6000 * 30
    
    // PURCHASE HISTORY
    public static final String purchaseSource_Web = "My Account";
    public static final String purchaseBy_Web = "Web User";
    public static final String purchaseSource_Device = "Device";
    public static final String purchaseBy_Device = "Individual Subscriber";
    
    // CREDIT CARD
    public static final String cardTypeVisa = "Visa";
    public static final String cardTypeMaster = "Master";
    public static final String cardTypeAmericanExpress = "AmericanExpress";
    public static final String cardNameVisa = "Visa Card Name";
    public static final String cardNumberVisa = "4111111111111111";
    public static final String cardCvvVisa = "111";
    public static final String cardExpMonthVisa = "12";
    public static final String cardExpYearVisa = "2020";
    public static final String cardNameMaster = "Master Card Name";
    public static final String cardNumberMaster = "5555555555554444";
    public static final String cardCvvMaster = "444";
    public static final String cardExpMonthMaster = "12";
    public static final String cardExpYearMaster = "2020";
    public static final String cardNameAmericanExpress = "AmericanExpress Card Name";
    public static final String cardNumberAmericanExpress = "5555555555554444";
    public static final String cardCvvAmericanExpress = "444";
    public static final String cardExpMonthAmericanExpress = "12";
    public static final String cardExpYearAmericanExpress = "2020";
    public static final String cardAddLine1NoTaxed = "3 Lagoon Drive";
    public static final String cardAddLine2NoTaxed = "";
    public static final String cardAddCityNoTaxed = "Redwood";
    public static final String cardAddStateNoTaxed = "CA";
    public static final String cardAddZipcodeNoTaxed = "94065";
    public static final String cardAddLine1Taxed = "2710 N Whipple St";
    public static final String cardAddLine2Taxed = "";
    public static final String cardAddCityTaxed = "Chicago";
    public static final String cardAddStateTaxed = "IL";
    public static final String cardAddZipcodeTaxed = "60647";
    public static final String cardAutopay = "Autopay";
    public static final String cardDefault = "Default";
    
    // PAYMENT GATEWAY
    public static final String paymentGatewayBrainTree = "BrainTree";
    public static final String paymentGatewayPayFort = "PayFort";
    public static final String paymentGatewayItson = "Itson";
    
    // PAYMENT OPTIONS
    public static double topupPinVal = 15;
    public static double topupCardValDefault = 200;
    
    // DISCOUNT COUPON CODE
    public static String discountType_Amount = "Amount";
    public static String discountType_Percent = "Percent";
    public static double discountValue_Amount = 2;
    public static double discountValue_Percent = 20;
    public static String discountType = "";
  }

  public static class Function {

    private static ArrayList<String> classNamesHasOneAccount = new ArrayList<String>();
    private static ArrayList<String> dependedTestcases = new ArrayList<String>();
    
    private static void setClassNamesHasOneAccount() { // HaNT Temp: I comment some test suites to test all test suites with multi accounts (each account for each testcase).
      classNamesHasOneAccount.add("com.itson.myaccount.CreditCardTestSuite");
    }
    
    public static boolean hasOneAccount(String className) {
      if (classNamesHasOneAccount.isEmpty())
        setClassNamesHasOneAccount();
      
      if (classNamesHasOneAccount.contains(className))
        return true; 
      else
        return false;
    }
    
    private static void setDependedTestCases() {
      dependedTestcases.add("AddNewCard_Master_NoTaxed_NoAutopay_NoDefault");
      dependedTestcases.add("EditCard_Visa_NoTaxed_Autopay_NoDefault_StillDefault");
      dependedTestcases.add("EditCard_Master_NoTaxed_Autopay_Default");
      dependedTestcases.add("RemoveCard_Master_NoTaxed_Autopay_Default");
      dependedTestcases.add("RemoveCard_Visa_NoTaxed_NoAutopay_Default");
      dependedTestcases.add("RemoveCard_Visa_NoTaxed_Autopay_Default");
    }
    
    public static boolean isDependedTestCase(String testcase) {
      if (dependedTestcases.isEmpty())
        setDependedTestCases();
      
      if (dependedTestcases.contains(testcase))
        return true; 
      else
        return false;
    }
    
    /**
     * @description Format the phone number into US phone number style.
     * @param phoneNumber
     * @return String formatedPhoneNumber
     * @author HaNT
     */
    public static String formatPhoneNumber(String phoneNumber) {
      String formatedPhoneNumber = "";
      formatedPhoneNumber += "(" + phoneNumber.substring(0, 3) + ") ";
      formatedPhoneNumber += phoneNumber.substring(3, 6) + "-";
      formatedPhoneNumber += phoneNumber.substring(6, phoneNumber.length());
      return formatedPhoneNumber; // HaNT Temp for SAAS-9007: Wrong phone number format. Real code is formatedPhoneNumber;
//      return phoneNumber; // HaNT Temp for SAAS-9007: Wrong phone number format. Real code is formatedPhoneNumber;
    }

    /**
     * @description Remove format of phone number.
     * @param formatedPhoneNumber
     * @return String phoneNumber
     * @author HaNT
     */
    public static String iggnoreFormatOfPhoneNumber(String formatedPhoneNumber) {
      return formatedPhoneNumber.replace("(", "").replace(")", "").replace("-", "").replace(" ", "");
    }
    
    public static JSONArray removeJSONObjectFromJSONArray(JSONArray jsonArray, int index) {
      JSONArray returnedJSONArray = new JSONArray();
      for (int i = 0; i < jsonArray.length(); i++) {
        if (i != index) {
          try {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            returnedJSONArray.put(jsonObject);
          } catch (JSONException ex) {
            Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
      return returnedJSONArray;
    }

    public static JSONArray unsetNullJSONObject(JSONArray jsonArray) {
      JSONArray jsonArrayUnNull = new JSONArray();
        if (jsonArray != null) {
          for (int i = 0; i < jsonArray.length(); i++) {
            try {
              JSONObject jsonObject = jsonArray.getJSONObject(i);
              if (jsonObject != null) {
                jsonArrayUnNull.put(jsonObject);
              }
            } catch (JSONException ex) {
              Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
      return jsonArrayUnNull;
    }
    
    public static String getTestCaseName() {
      StackTraceElement[] stack = Thread.currentThread().getStackTrace();
      String testCaseName = "";
      for (int i = 0; i < stack.length; i++) {
        if (stack[i].getClassName().contains("TestSuite")) {
          testCaseName = stack[i].getMethodName();
          break;
        }
      }
      return testCaseName;
    }

    public static String getIconUrl(String planType) {
      String iconUrl = "";
      switch (planType) {
        case Common.Constant.voice:
          iconUrl = Common.Constant.iconVoiceUrl;
          break;
        case Common.Constant.message:
          iconUrl = Common.Constant.iconMessageUrl;
          break;
        case Common.Constant.data:
          iconUrl = Common.Constant.iconDataUrl;
          break;
      }
      return iconUrl;
    }
    
    public static String capitalize(String aString) {
      return aString.substring(0,1).toUpperCase() + aString.substring(1);
    }
    
    public static String shorter(String text, int charsLimit) {
      String newText;
      // Check if length is larger than the character limit
      if (text.length() > charsLimit) {
        // If so, cut the string at the character limit
        newText = text.substring(0, charsLimit);
        // Trim off white space
//        newText = newText.trim();
        // Add at end of text ...
        return newText + "...";
      } // If not just return the text as is
      else {
        return text;
      }
    }
    
//    // ADJUST BASE PLAN
//    private static JSONObject planInfos1 = new JSONObject();
//    
//    static {
//      try {
//        // VOICE
//        planInfos.put(Constant.voice + Constant.limitedUnit50, "1.50" + Constant.replacedString);
//        planInfos.put(Constant.voice + Constant.limitedUnit100, "4.00" + Constant.replacedString);
//        planInfos.put(Constant.voice + Constant.limitedUnit200, "5.00" + Constant.replacedString);
//        planInfos.put(Constant.voice + Constant.limitedUnit400, "6.00" + Constant.replacedString);
//        planInfos.put(Constant.voice + Constant.limitedUnit600, "7.00" + Constant.replacedString);
//        
//        // MESSAGE
//        planInfos.put(Constant.message + Constant.limitedUnit20, "3.00" + Constant.replacedString);
//        planInfos.put(Constant.message + Constant.limitedUnit25, "4.00" + Constant.replacedString);
//        planInfos.put(Constant.message + Constant.limitedUnit30, "5.00" + Constant.replacedString);
//        planInfos.put(Constant.message + Constant.limitedUnit35, "6.00" + Constant.replacedString);
//        planInfos.put(Constant.message + Constant.limitedUnit40, "7.00" + Constant.replacedString);
////        planInfos.put(Constant.message + Constant.limitedUnit45, "8.00" + Constant.replacedString);
//        planInfos.put(Constant.message + Constant.limitedUnit50, "9.00" + Constant.replacedString);
//        
//        // DATA
//        planInfos.put(Constant.data + Constant.limitedUnit20, "2.00" + Constant.replacedString);
//        planInfos.put(Constant.data + Constant.limitedUnit25, "4.00" + Constant.replacedString);
//        planInfos.put(Constant.data + Constant.limitedUnit30, "5.00" + Constant.replacedString);
//        planInfos.put(Constant.data + Constant.limitedUnit35, "6.00" + Constant.replacedString);
//        planInfos.put(Constant.data + Constant.limitedUnit40, "7.00" + Constant.replacedString);
//        planInfos.put(Constant.data + Constant.limitedUnit45, "8.00" + Constant.replacedString);
//        planInfos.put(Constant.data + Constant.limitedUnit50, "9.00" + Constant.replacedString);
//      } catch (JSONException ex) {
//        Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex);
//      }
//    }
    
//    public static double getPrice(String planType, int limitedUnit) {
//      try {
//        String planInfoString = planInfos.getString(planType + limitedUnit);
//        String[] planInfoArray = planInfoString.split(Constant.replacedString);
//        return Util.CustomNumber.parseDouble(planInfoArray[0]);
//      } catch (JSONException ex) {
//        Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex);
//      }
//      return 0.00;
//    }
    
    public static boolean doResetNickname(String testSuiteName) {
      boolean doReset = true;
      if (testSuiteName != null) {
        switch (testSuiteName) {
          case "CreditCardTestSuite":
          case "AccountOverviewTestSuite":
          case "MyAccountUITestSuite":
            doReset = false;
            break;
        }
      }
      return doReset;
    }
    
    public static String randomPin() {
        DateFormat df = new SimpleDateFormat("yyMMddHHmm");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }

    public static boolean isTicketClosed(String ticketTitle) {
      return Util.containsIgnoreCase(ticketTitle, Common.Constant.closed);
    }
    
    public static double getPlanCostAfterDiscounting(double cost) {
      if (!Common.Constant.discountType.isEmpty()) {
        // issue_SAAS_14259
        if (Common.Constant.discountType.equals(Common.Constant.discountType_Amount))
          return (cost - Common.Constant.discountValue_Amount);
        return (cost - (cost*Common.Constant.discountValue_Percent)/100);
      }
      return cost;
    }
  }
}
