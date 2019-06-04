package Model.ResponsePOJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import Model.ResponsePOJO.UserResponsePOJO;

public class UserResponsePOJOlist {

    @SerializedName("data")
    private List<UserResponsePOJO> userResponsePOJOS;

    public UserResponsePOJOlist() {
    }

    public List<UserResponsePOJO> getUserResponsePOJOS() {
        return userResponsePOJOS;
    }

    public void setUserResponsePOJOS(List<UserResponsePOJO> userResponsePOJOS) {
        this.userResponsePOJOS = userResponsePOJOS;
    }
}
