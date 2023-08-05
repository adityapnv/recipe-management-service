package com.abnamro.recipemanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The type Swagger constants.
 */
@Component
@ConfigurationProperties("swagger.api")
public class SwaggerConstants {

  private String title;
  private String description;
  private String version;
  private Contact contact;
  private String basePackage;
  private String apiType;
  private boolean defaultResponseMessage;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public String getBasePackage() {
    return basePackage;
  }

  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }

  public String getApiType() {
    return apiType;
  }

  public void setApiType(String apiType) {
    this.apiType = apiType;
  }

  public boolean isDefaultResponseMessage() {
    return defaultResponseMessage;
  }

  public void setDefaultResponseMessage(boolean defaultResponseMessage) {
    this.defaultResponseMessage = defaultResponseMessage;
  }
}
