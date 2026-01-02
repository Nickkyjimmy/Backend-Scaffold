package com.rmit.auth_service.auth.external.dto;

public class BetaTopicRegistry {
  static private final String prefix = "beta.";
  
  public static class Topic {
    public static final String TEST_REQUEST_REPLY_REQ = prefix + "test_request_reply_req";
    public static final String TEST_REQUEST_REPLY_RES = prefix + "test_request_reply_res";
    public static final String LIST_REQ = prefix + "list_req";
    public static final String LIST_RES = prefix + "list_res";
  }
}

