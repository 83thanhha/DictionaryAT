/*
 * Create fixtures form API
 *  @author gurtejphangureh
 *
 */
package com.itson.restcalls;

import com.itson.servicedesigncenter.Config;


public abstract class AbstractRestCalls {
  protected Config config = new Config();

  public Config getConfig() {
    return config;
  }
}
