/*
 * Create fixtures form API
 *  @author gurtejphangureh
 *
 */
package com.itson.restcalls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Usage extends AbstractRestCalls {

//  protected String savedPlanId;
//  protected String savedSubscriptionId;
//  protected String savedPlanOfferServicePoliciesServicePolicyId;
//  protected String savedPlanOfferServicePolicies;
//  protected String savedControlPoliciesId;
//  protected String savedControlfilterId;
//
//  //text
//  protected String textPlanId;
//  protected String textSubscriptionId;
//  protected String textPlanOfferServicePoliciesServicePolicyId;
//  protected String textPlanOfferServicePolicies;
//  protected String textControlPoliciesId;
//  protected String textControlfilterId;
//  //data
//  protected String dataPlanId;
//  protected String dataSubscriptionId;
//  protected String dataPlanOfferServicePoliciesServicePolicyId;
//  protected String dataPlanOfferServicePolicies;
//  protected String dataControlPoliciesId;
//  protected String dataControlfilterId;
//  //voice
//  protected String voicePlanId;
//  protected String voiceSubscriptionId;
//  protected String voicePlanOfferServicePolicies;
//  protected String voicePlanOfferServicePoliciesServicePolicyId;
//  protected String voiceControlPoliciesId;
//  protected String voiceControlfilterId;
//  
//  public void setUsage(int voice, int text, int data) throws Exception {
//    logAction("#### setUsage ####");
//    setUsagePrepare();
//    createVoiceUsage(voice);
//    createTextUsage(text);
//    createDataUsage(data);
//    //wait for device plan cycle to be complete
//    sleep(30000);
//  }
//  
//  private void setUsagePrepare() throws Exception {
//    sleep(60000);
//    getPlanSubscripton();
//    setChargingPolicy();
//    setServiceControlPolicyFilterId();
//    sleep(8000);
//  }
//  
//  public void setUsageVoice(int amount) throws Exception {
//    logAction("#### Set Usage _ VOICE ####");
//    setUsagePrepare();
//    createVoiceUsage(amount);
//    sleep(30000); // Wait for device plan cycle to be complete
//  }
//  
//  public void setUsageText(int amount) throws Exception {
//    logAction("#### Set Usage _ TEXT ####");
//    setUsagePrepare();
//    createTextUsage(amount);
//    sleep(30000); // Wait for device plan cycle to be complete
//  }
//  
//  public void setUsageData(int amount) throws Exception {
//    logAction("#### Set Usage _ DATA ####");
//    setUsagePrepare();
//    createDataUsage(amount);
//    sleep(30000); // Wait for device plan cycle to be complete
//  }
//
//  public void searchArray(String[] results, int givenplan) throws JSONException {
//    String plan = Integer.toString(givenplan);
//    boolean result = false;
//    logAction("Test " + results[0]);
//
//    int rowCount = 0;
//    int attempts = 1;
//    String rowName = "none";
//    while (!rowName.contains(plan)) {
//      try {
//        rowName = results[rowCount];
//        logAction("Got This " + rowName);
//        if (rowName.contains(plan)) {
//          logAction("Name Matched");
//          result = true;
//          break;
//        }
//        else {
//          rowCount++;
//        }
//      }
//      catch (Exception e) {
//        //rowCount = 1;
//        // break;
//      }
//      attempts++;
//    }
//    logAction("Got This " + rowCount);
//
//    logAction("Got This1 " + results[rowCount]);
//    logAction("Got This2 " + results[rowCount - 1]);  //111
//    logAction("voiceSubscriptionId " + results[rowCount - 4]);  //111
//
//    //cleaup save planid
//    String savedPlanId1 = results[rowCount - 1];
//    String[] planid = savedPlanId1.split("\"");
//    savedPlanId = planid[3];
//    savedSubscriptionId = results[rowCount - 4];
//    planid = savedSubscriptionId.split("\"");
//    savedSubscriptionId = planid[5];
//    logAction("##### savedSubscriptionId " + savedSubscriptionId);  //111
//  }
//
//  public void getPlanSubscripton() throws Exception {
//    logAction("#### GetPlanSubscripton ####");
//    String url = "http://subscriber-di01-service.qa.itsonsaas.net:8280/saas/services/1.0/account/" + saasAccountId + "/subscription";
//    String line = httpGet(url, uAccessToken);
//    logAction("httppost from getPlanSubscripton: " + url);
//    logAction("####PlanId: " + line);
//    String[] getplanid = line.split(",");
//    //voice
//    searchArray(getplanid, skuProducIdVoice);
//    voiceSubscriptionId = savedSubscriptionId;
//    voicePlanId = savedPlanId;
//    logAction("Saved VoicePlanId: " + voicePlanId);
//    logAction("Saved voiceSubscriptionId: " + voiceSubscriptionId);
//    //text
//    searchArray(getplanid, skuProducIdText);
//    textPlanId = savedPlanId;
//    textSubscriptionId = savedSubscriptionId;
//    logAction("Saved textPlanId: " + textPlanId);
//    logAction("Saved textSubscriptionId: " + textSubscriptionId);
//    //data
//    searchArray(getplanid, skuProducIdData);
//    dataPlanId = savedPlanId;
//    dataSubscriptionId = savedSubscriptionId;
//    logAction("Saved dataPlanId: " + dataPlanId);
//    logAction("Saved dataSubscriptionId: " + dataSubscriptionId);
//  }
//
//  public void setChargingPolicy() throws Exception {
//    logAction("#### setChargingPolicy ####");
//    //voice
//    getChargingPolicy(voicePlanId);
//    voicePlanOfferServicePolicies = savedPlanOfferServicePolicies;
//    voicePlanOfferServicePoliciesServicePolicyId = savedPlanOfferServicePoliciesServicePolicyId;
//    //text
//    getChargingPolicy(textPlanId);
//    textPlanOfferServicePolicies = savedPlanOfferServicePolicies;
//    textPlanOfferServicePoliciesServicePolicyId = savedPlanOfferServicePoliciesServicePolicyId;
//    //data
//    getChargingPolicy(dataPlanId);
//    dataPlanOfferServicePolicies = savedPlanOfferServicePolicies;
//    dataPlanOfferServicePoliciesServicePolicyId = savedPlanOfferServicePoliciesServicePolicyId;
//  }
//
//  public void getChargingPolicy(String givenPlanId) throws Exception {
//    logAction("#### GetChargingPolicy ####");
//    String url = "http://subscriber-di01-service.qa.itsonsaas.net:8280/saas/services/1.0/partner/" + partnerId + "/planoffer/" + givenPlanId;
//    String line = httpGet(url, oAccessToken);
//    logAction("httppost from getChargingPolicy: " + url);
//    logAction("getChargingPolicy: " + line);
//    String planOfferServicePolicies = getJsonKey(line, "planOfferServicePolicies"); //is in json in json
//    logAction("planOfferServicePolicies: " + planOfferServicePolicies);
//    planOfferServicePolicies = planOfferServicePolicies.substring(1, planOfferServicePolicies.length() - 1); //remove [
//    logAction("planOfferServicePolicies: " + planOfferServicePolicies);
//    savedPlanOfferServicePolicies = getJsonKey(planOfferServicePolicies, "id");
//    savedPlanOfferServicePoliciesServicePolicyId = getJsonKey(planOfferServicePolicies, "servicePolicyId");
//    logAction("Saved savedPlanOfferServicePolicies: " + savedPlanOfferServicePolicies);
//    logAction("Saved savedPlanOfferServicePoliciesServicePolicyId: " + savedPlanOfferServicePoliciesServicePolicyId);
//  }
//
//  public void setServiceControlPolicyFilterId() throws Exception {
//    logAction("#### setServiceControlPolicyFilterId ####");
//    getMutuhalAuth();
//    logAction("#### Voice setServiceControlPolicyFilterId ####");
//    // voice
//    getServiceControlPolicyFilterId(voicePlanOfferServicePoliciesServicePolicyId);
//    voiceControlPoliciesId = savedControlPoliciesId;
//    voiceControlfilterId = savedControlfilterId;
//    // text
//    logAction("#### Text setServiceControlPolicyFilterId ####");
//    getServiceControlTextPolicyFilterId(textPlanOfferServicePoliciesServicePolicyId);
//    textControlPoliciesId = savedControlPoliciesId;
//    textControlfilterId = savedControlfilterId;
//    //data
//    logAction("#### Data setServiceControlPolicyFilterId ####");
//    getServiceControlPolicyFilterId(dataPlanOfferServicePoliciesServicePolicyId);
//    dataControlPoliciesId = savedControlPoliciesId;
//    dataControlfilterId = savedControlfilterId;
//  }
//
//  public void getServiceControlPolicyFilterId(String givenPlanOfferServicePoliciesServicePolicyId) throws Exception {
//    logAction("#### getServiceControlPolicyFilterId ####");
//    String url = "http://app01.di01." + env + ".itsonsaas.com:8280/saas/services/1.0/subscriber/" + subscriberNetworkId + "/servicepolicies/" + givenPlanOfferServicePoliciesServicePolicyId;
//    logAction("httppost from getserviceControlPolicyFilterId: " + url);
//    String line = httpGet(url, mutualAccessToken);
//    logAction("getserviceControlPolicyFilterId: " + line);
//
//    String controlPolicies = getJsonKey(line, "controlPolicies"); //is in json in json
//    controlPolicies = controlPolicies.substring(1, controlPolicies.length() - 1); //remove [
//    controlPolicies = getJsonKey(controlPolicies, "id");
//    savedControlPoliciesId = controlPolicies;
//    logAction("controlPolicies: " + controlPolicies);
//    String filters = getJsonKey(line, "components"); //is in json in   "components":[  
//    filters = filters.substring(1, filters.length() - 1);
//    filters = getJsonKey(filters, "filters"); //is in json in json
//    filters = filters.substring(1, filters.length() - 1); //remove [
//    filters = getJsonKey(filters, "id");
//    savedControlfilterId = filters;
//    logAction("filters: " + filters);
//
//  }
//
//  public void getServiceControlTextPolicyFilterId(String givenPlanOfferServicePoliciesServicePolicyId) throws Exception {
//    logAction("#### getServiceControlTEXTPolicyFilterId ####");
//    String url = "http://app01.di01." + env + ".itsonsaas.com:8280/saas/services/1.0/subscriber/" + subscriberNetworkId + "/servicepolicies/" + givenPlanOfferServicePoliciesServicePolicyId;
//    //logAction("httppost from getserviceControlPolicyFilterId: " + url);
//    String line = httpGet(url, mutualAccessToken);
//    logAction("#####getserviceControlPolicyFilterId: " + line);
//    String controlPolicies = getJsonKey(line, "components"); //is in json in json
//    controlPolicies = controlPolicies.substring(1, controlPolicies.length() - 1); //remove [
//    logAction("#############Line" + controlPolicies);
//    controlPolicies = getJsonKey(controlPolicies, "controlPolicies"); //is in json in json
//    controlPolicies = controlPolicies.substring(1, controlPolicies.length() - 1); //remove [
//    controlPolicies = getJsonKey(controlPolicies, "id");
//    savedControlPoliciesId = controlPolicies;
//    logAction("controlPolicies: " + controlPolicies);
//    String filters = getJsonKey(line, "components"); //is in json in   "components":[  
//    filters = filters.substring(1, filters.length() - 1);
//    filters = getJsonKey(filters, "filters"); //is in json in json
//    filters = filters.substring(1, filters.length() - 1); //remove [
//    filters = getJsonKey(filters, "id");
//    savedControlfilterId = filters;
//    logAction("filters: " + filters);
//
//  }
//
//  public void createVoiceUsage(int usageAmount) throws JSONException, UnsupportedEncodingException, IOException {
//    HttpClient client = new DefaultHttpClient();
//    logAction("### Create Voice Usage### " + usageAmount);
//    String url = "http://app01.di01." + env + ".itsonsaas.com:8280/saas/services/1.0/subscriber/" + subscriberNetworkId + "/usage/report";
//
//    HttpPost httppost = new HttpPost(url);
//    logAction(url);
//    UUID uuid = UUID.randomUUID();
//    String randomUUIDString = uuid.toString();
//
//    // try {
//    httppost.addHeader("X-IO-ClientCert", mutualAccessToken);
//    httppost.addHeader("X-IO-Tenant-Id", tenantId);
//    httppost.addHeader("X-IO-Partner-Id", partnerId);
//    httppost.addHeader("Content-Type", "application/json");
//
//    //usageRecordNetworkDetails
//    JSONObject usageRecordNetworkDetails = new JSONObject();
//
//    JSONObject b = new JSONObject();
//
//    b.put("networkType", "Cellular");
//    b.put("networkTopology", "LTE");
//    b.put("networkSpeed", "ThreeG");
//    b.put("networkGroup", "home");
//    b.put("systemId", 2);
//    b.put("networkId", 100);
//
//    usageRecordNetworkDetails.put("usageRecordNetworkDetails", b);
//
//    //DeviceUsageReportAttributes
//    JSONObject a = new JSONObject();
//
//    a.put("filterId", voiceControlfilterId);
//    a.put("controlPolicyId", voiceControlPoliciesId);
//    a.put("billingCycleId", 1);
//    a.put("chargingPolicyId", voicePlanOfferServicePolicies);
//    a.put("subscriptionId", voiceSubscriptionId);
//
//    //// usageAmount
//    JSONObject c = new JSONObject();
//
//    c.put("DeviceUsageReportAttributes", a);
//    c.put("usageRecordNetworkDetails", b);
//    c.put("direction", "MO");
//    c.put("utcCallStartTime", System.currentTimeMillis());
//    c.put("callingParty", "12005552222");
//    c.put("usageAmount", usageAmount);
//
//    //voice json
//    JSONArray voice = new JSONArray();
//    voice.put(0, c);
//
//    JSONObject main = new JSONObject();
//    logAction("randomUUIDString: " + randomUUIDString);
//    main.put("subscriberId", subscriberNetworkId);
//    main.put("subscriberIdType", "SNID");
//    main.put("source", "Device");
//    main.put("reportId", randomUUIDString);
//    main.put("utcStartTime", System.currentTimeMillis());
//    main.put("utcEndTime", System.currentTimeMillis());
//    main.put("hardwareIdType", "DeviceId");
//    main.put("hardwareId", "DEVICE_HARDWARE_ID");
//    main.put("voice", voice);
//    logAction(httppost.toString());
//
//    StringEntity userEntity = new StringEntity(main.toString());
//    logAction("userEntity: " + main.toString());
//
//    httppost.setEntity(userEntity);
//    HttpResponse response = client.execute(httppost);
//    logAction("createVoiceUsage Call: " + httppost.toString());
//    logAction("createVoiceUsage");
//    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//    String line = "";
//    while ((line = rd.readLine()) != null) {
//      logAction(line);
//    }
//  }
//
//  public void createTextUsage(int usageAmount) throws JSONException, UnsupportedEncodingException, IOException {
//    HttpClient client = new DefaultHttpClient();
//    logAction("### Create Text Usage###");
//    String url = "http://app01.di01." + env + ".itsonsaas.com:8280/saas/services/1.0/subscriber/" + subscriberNetworkId + "/usage/report";
//
//    HttpPost httppost = new HttpPost(url);
//    logAction(url);
//    UUID uuid = UUID.randomUUID();
//    String randomUUIDString = uuid.toString();
//
//    // try {
//    httppost.addHeader("X-IO-ClientCert", mutualAccessToken);
//    httppost.addHeader("X-IO-Tenant-Id", tenantId);
//    httppost.addHeader("X-IO-Partner-Id", partnerId);
//    httppost.addHeader("Content-Type", "application/json");
//
//    //usageRecordNetworkDetails
//    JSONObject usageRecordNetworkDetails = new JSONObject();
//
//    JSONObject b = new JSONObject();
//
//    b.put("networkType", "Cellular");
//    b.put("networkTopology", "LTE");
//    b.put("networkSpeed", "ThreeG");
//    b.put("networkGroup", "home");
//    b.put("systemId", 2);
//    b.put("networkId", 100);
//
//    usageRecordNetworkDetails.put("usageRecordNetworkDetails", b);
//
//    //DeviceUsageReportAttributes
//    JSONObject a = new JSONObject();
//
//    a.put("filterId", textControlfilterId);
//    a.put("controlPolicyId", textControlPoliciesId);
//    a.put("billingCycleId", 1);
//    a.put("chargingPolicyId", textPlanOfferServicePolicies);
//    a.put("subscriptionId", textSubscriptionId);
//
//    //// usageAmount
//    JSONObject c = new JSONObject();
//
//    c.put("DeviceUsageReportAttributes", a);
//    c.put("usageRecordNetworkDetails", b);
//    c.put("direction", "MO");
//    c.put("remoteParty", "12005551234");
//    c.put("utcStartRange", System.currentTimeMillis());
//    c.put("messageCount", usageAmount);
//
//    //voice json
//    JSONArray text = new JSONArray();
//    text.put(0, c);
//
//    JSONObject main = new JSONObject();
//    logAction("randomUUIDString: " + randomUUIDString);
//    main.put("subscriberId", subscriberNetworkId);
//    main.put("subscriberIdType", "SNID");
//    main.put("source", "Device");
//    main.put("reportId", randomUUIDString);
//    main.put("utcStartTime", System.currentTimeMillis());
//    main.put("utcEndTime", System.currentTimeMillis());
//    main.put("hardwareIdType", "DeviceId");
//    main.put("hardwareId", "DEVICE_HARDWARE_ID");
//    main.put("text", text);
//    logAction(httppost.toString());
//
//    StringEntity userEntity = new StringEntity(main.toString());
//    logAction("userEntity: " + main.toString());
//
//    httppost.setEntity(userEntity);
//    HttpResponse response = client.execute(httppost);
//    logAction("createTextUsage Call: " + httppost.toString());
//    logAction("createTextUsage");
//    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//    String line = "";
//    while ((line = rd.readLine()) != null) {
//      logAction(line);
//    }
//  }
//
//  public void createDataUsage(int usageAmount) throws JSONException, UnsupportedEncodingException, IOException {
//    HttpClient client = new DefaultHttpClient();
//    logAction("### Create DATA Usage###");
//    String url = "http://app01.di01." + env + ".itsonsaas.com:8280/saas/services/1.0/subscriber/" + subscriberNetworkId + "/usage/report";
//
//    HttpPost httppost = new HttpPost(url);
//    logAction(url);
//    UUID uuid = UUID.randomUUID();
//    String randomUUIDString = uuid.toString();
//
//    // try {
//    httppost.addHeader("X-IO-ClientCert", mutualAccessToken);
//    httppost.addHeader("X-IO-Tenant-Id", tenantId);
//    httppost.addHeader("X-IO-Partner-Id", partnerId);
//    httppost.addHeader("Content-Type", "application/json");
//
//    //usageRecordNetworkDetails
//    JSONObject usageRecordNetworkDetails = new JSONObject();
//
//    JSONObject b = new JSONObject();
//    b.put("id", subscriberNetworkId);
//
//    b.put("networkType", "Cellular");
//    b.put("networkTopology", "LTE");
//    b.put("networkSpeed", "ThreeG");
//    b.put("networkGroup", "home");
//    b.put("systemId", 2);
//    b.put("networkId", 100);
//
//    usageRecordNetworkDetails.put("usageRecordNetworkDetails", b);
//
//    //DeviceUsageReportAttributes
//    JSONObject a = new JSONObject();
//    logAction("### Create DATA Usage###:   " + dataControlfilterId);
//
//    a.put("filterId", dataControlfilterId);
//    a.put("controlPolicyId", dataControlPoliciesId);
//    a.put("billingCycleId", 1);
//    a.put("chargingPolicyId", dataPlanOfferServicePolicies);
//    a.put("subscriptionId", dataSubscriptionId);
//
//    //// usageAmount
//    JSONObject c = new JSONObject();
//
//    c.put("DeviceUsageReportAttributes", a);
//    c.put("usageRecordNetworkDetails", b);
//    c.put("protocol", "TCP");
//    c.put("usageMO", usageAmount);
//    c.put("usageMT", usageAmount);
//    c.put("remoteIPAddress", "10.10.10.10");
//    c.put("remotePort", 80);
//    c.put("apn", "internet.sprint");
//    c.put("application", "facebook");
//    c.put("foreground", true);
//
//    //voice json
//    JSONArray data = new JSONArray();
//    data.put(0, c);
//
//    JSONObject main = new JSONObject();
//    logAction("randomUUIDString: " + randomUUIDString);
//    main.put("subscriberId", subscriberNetworkId);
//    main.put("subscriberIdType", "SNID");
//    main.put("source", "Device");
//    main.put("reportId", randomUUIDString);
//    main.put("utcStartTime", System.currentTimeMillis());
//    main.put("utcEndTime", System.currentTimeMillis());
//    main.put("hardwareIdType", "DeviceId");
//    main.put("hardwareId", "DEVICE_HARDWARE_ID");
//    main.put("data", data);
//    logAction(httppost.toString());
//
//    StringEntity userEntity = new StringEntity(main.toString());
//    logAction("userEntity: " + main.toString());
//
//    httppost.setEntity(userEntity);
//    HttpResponse response = client.execute(httppost);
//    logAction("createDataUsage Call: " + httppost.toString());
//    logAction("createDataUsage");
//    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//    String line = "";
//    while ((line = rd.readLine()) != null) {
//      logAction(line);
//    }
//  }
}