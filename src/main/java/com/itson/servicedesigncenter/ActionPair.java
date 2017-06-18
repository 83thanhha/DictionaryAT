package com.itson.servicedesigncenter;

/**
 * Created by harryjackson on 8/19/14.
 */
public class ActionPair {
  private String name;
  private String action;
  public ActionPair(String name, String action) {
    this.name = name;
    this.action = action;
  }
  public String toString() {
    return name + ":" + action;
  }
}
