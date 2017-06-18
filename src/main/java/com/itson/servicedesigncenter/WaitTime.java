package com.itson.servicedesigncenter;

/**
 * Handy enum that defines wait times, like extra-small, small, medium, large,
 * extra-large
 * 
 * @author ssingh
 */
public enum WaitTime {
  ExtraSmall(1), Small(10), Medium(65), Large(120), ExtraLarge(300);

  private final int seconds;

  private WaitTime(int seconds) {
    this.seconds = seconds;
  }

  public int toSeconds() {
    return seconds;
  }

  public int getMillis() {
    return seconds * 1000;
  }
}
