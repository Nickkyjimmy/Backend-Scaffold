package com.rmit.route.route.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestKafkaResponse {
  private String message;
  private String fromService;
  private String receivedFrom;
  private Long timestamp;
  private Boolean success;
}

