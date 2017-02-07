
package jackson.titanium;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "code",
    "status",
    "method_name",
    "session_id"
})
public class Meta {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("status")
    private String status;
    @JsonProperty("method_name")
    private String method_name;
    @JsonProperty("session_id")
    private String session_id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The code
     */
    @JsonProperty("code")
    public Integer getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    @JsonProperty("code")
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 
     * @return
     *     The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The method_name
     */
    @JsonProperty("method_name")
    public String getMethod_name() {
        return method_name;
    }

    /**
     * 
     * @param method_name
     *     The method_name
     */
    @JsonProperty("method_name")
    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    /**
     * 
     * @return
     *     The session_id
     */
    @JsonProperty("session_id")
    public String getSession_id() {
        return session_id;
    }

    /**
     * 
     * @param session_id
     *     The session_id
     */
    @JsonProperty("session_id")
    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
