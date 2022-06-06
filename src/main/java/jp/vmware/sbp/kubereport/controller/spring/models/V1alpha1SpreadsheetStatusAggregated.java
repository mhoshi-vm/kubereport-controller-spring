/*
 * Kubernetes
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v1.21.1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package jp.vmware.sbp.kubereport.controller.spring.models;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * Aggregated is a boolean to tell the status of aggregator
 */
@ApiModel(description = "Aggregated is a boolean to tell the status of aggregator")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-06-06T03:12:04.488Z[Etc/UTC]")
public class V1alpha1SpreadsheetStatusAggregated {
  public static final String SERIALIZED_NAME_ERROR = "error";
  @SerializedName(SERIALIZED_NAME_ERROR)
  private String error;

  public static final String SERIALIZED_NAME_STARTED_AT = "startedAt";
  @SerializedName(SERIALIZED_NAME_STARTED_AT)
  private String startedAt;

  public static final String SERIALIZED_NAME_SUCCESS = "success";
  @SerializedName(SERIALIZED_NAME_SUCCESS)
  private String success;

  public static final String SERIALIZED_NAME_UPDATE_AT = "updateAt";
  @SerializedName(SERIALIZED_NAME_UPDATE_AT)
  private String updateAt;


  public V1alpha1SpreadsheetStatusAggregated error(String error) {
    
    this.error = error;
    return this;
  }

   /**
   * Get error
   * @return error
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getError() {
    return error;
  }


  public void setError(String error) {
    this.error = error;
  }


  public V1alpha1SpreadsheetStatusAggregated startedAt(String startedAt) {
    
    this.startedAt = startedAt;
    return this;
  }

   /**
   * Get startedAt
   * @return startedAt
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getStartedAt() {
    return startedAt;
  }


  public void setStartedAt(String startedAt) {
    this.startedAt = startedAt;
  }


  public V1alpha1SpreadsheetStatusAggregated success(String success) {
    
    this.success = success;
    return this;
  }

   /**
   * Get success
   * @return success
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSuccess() {
    return success;
  }


  public void setSuccess(String success) {
    this.success = success;
  }


  public V1alpha1SpreadsheetStatusAggregated updateAt(String updateAt) {
    
    this.updateAt = updateAt;
    return this;
  }

   /**
   * Get updateAt
   * @return updateAt
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getUpdateAt() {
    return updateAt;
  }


  public void setUpdateAt(String updateAt) {
    this.updateAt = updateAt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    V1alpha1SpreadsheetStatusAggregated v1alpha1SpreadsheetStatusAggregated = (V1alpha1SpreadsheetStatusAggregated) o;
    return Objects.equals(this.error, v1alpha1SpreadsheetStatusAggregated.error) &&
        Objects.equals(this.startedAt, v1alpha1SpreadsheetStatusAggregated.startedAt) &&
        Objects.equals(this.success, v1alpha1SpreadsheetStatusAggregated.success) &&
        Objects.equals(this.updateAt, v1alpha1SpreadsheetStatusAggregated.updateAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(error, startedAt, success, updateAt);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class V1alpha1SpreadsheetStatusAggregated {\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    updateAt: ").append(toIndentedString(updateAt)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

