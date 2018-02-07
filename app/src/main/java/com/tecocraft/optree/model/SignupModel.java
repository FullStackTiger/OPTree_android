package com.tecocraft.optree.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupModel {

@SerializedName("success")
@Expose
private boolean success;
@SerializedName("_id")
@Expose
private String id;
@SerializedName("message")
@Expose
private String message;
@SerializedName("token")
@Expose
private String token;

public boolean isSuccess() {
return success;
}

public void setSuccess(boolean success) {
this.success = success;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public String getToken() {
return token;
}

public void setToken(String token) {
this.token = token;
}

}