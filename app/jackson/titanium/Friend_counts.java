
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
    "requests",
    "friends"
})
public class Friend_counts {

    @JsonProperty("requests")
    private Integer requests;
    @JsonProperty("friends")
    private Integer friends;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The requests
     */
    @JsonProperty("requests")
    public Integer getRequests() {
        return requests;
    }

    /**
     * 
     * @param requests
     *     The requests
     */
    @JsonProperty("requests")
    public void setRequests(Integer requests) {
        this.requests = requests;
    }

    /**
     * 
     * @return
     *     The friends
     */
    @JsonProperty("friends")
    public Integer getFriends() {
        return friends;
    }

    /**
     * 
     * @param friends
     *     The friends
     */
    @JsonProperty("friends")
    public void setFriends(Integer friends) {
        this.friends = friends;
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
