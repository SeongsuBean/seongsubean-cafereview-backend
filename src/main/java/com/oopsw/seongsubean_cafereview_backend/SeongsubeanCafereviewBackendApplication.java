package com.oopsw.seongsubean_cafereview_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SeongsubeanCafereviewBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(SeongsubeanCafereviewBackendApplication.class, args);
  }

}
