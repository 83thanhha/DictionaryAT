package com.itson.servicedesigncenter;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUtils;
import org.apache.commons.collections.buffer.CircularFifoBuffer;


public enum ActionLog {
  INSTANCE;
  private static final Buffer actionBuffer = BufferUtils.synchronizedBuffer(new CircularFifoBuffer());

  public static void add(String name, String action) {
    actionBuffer.add(new ActionPair(name, action));
  }

  public void add(ActionPair actionPair) {
    actionBuffer.add(actionPair);
  }
}
