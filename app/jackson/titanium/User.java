
package jackson.titanium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "id",
    "first_name",
    "last_name",
    "created_at",
    "updated_at",
    "external_accounts",
    "confirmed_at",
    "email",
    "admin",
    "stats",
    "friend_counts"
})
public class User {

    @JsonProperty("id")
    private String id;
    @JsonProperty("first_name")
    private String first_name;
    @JsonProperty("last_name")
    private String last_name;
    @JsonProperty("created_at")
    private String created_at;
    @JsonProperty("updated_at")
    private String updated_at;
    @JsonProperty("external_accounts")
    private List<Object> external_accounts = new ArrayList<Object>();
    @JsonProperty("confirmed_at")
    private String confirmed_at;
    @JsonProperty("email")
    private String email;
    @JsonProperty("admin")
    private String admin;
    @JsonProperty("stats")
    private Stats stats;
    @JsonProperty("friend_counts")
    private Friend_counts friend_counts;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The first_name
     */
    @JsonProperty("first_name")
    public String getFirst_name() {
        return first_name;
    }

    /**
     * 
     * @param first_name
     *     The first_name
     */
    @JsonProperty("first_name")
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * 
     * @return
     *     The last_name
     */
    @JsonProperty("last_name")
    public String getLast_name() {
        return last_name;
    }

    /**
     * 
     * @param last_name
     *     The last_name
     */
    @JsonProperty("last_name")
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * 
     * @return
     *     The created_at
     */
    @JsonProperty("created_at")
    public String getCreated_at() {
        return created_at;
    }

    /**
     * 
     * @param created_at
     *     The created_at
     */
    @JsonProperty("created_at")
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /**
     * 
     * @return
     *     The updated_at
     */
    @JsonProperty("updated_at")
    public String getUpdated_at() {
        return updated_at;
    }

    /**
     * 
     * @param updated_at
     *     The updated_at
     */
    @JsonProperty("updated_at")
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    /**
     * 
     * @return
     *     The external_accounts
     */
    @JsonProperty("external_accounts")
    public List<Object> getExternal_accounts() {
        return external_accounts;
    }

    /**
     * 
     * @param external_accounts
     *     The external_accounts
     */
    @JsonProperty("external_accounts")
    public void setExternal_accounts(List<Object> external_accounts) {
        this.external_accounts = external_accounts;
    }

    /**
     * 
     * @return
     *     The confirmed_at
     */
    @JsonProperty("confirmed_at")
    public String getConfirmed_at() {
        return confirmed_at;
    }

    /**
     * 
     * @param confirmed_at
     *     The confirmed_at
     */
    @JsonProperty("confirmed_at")
    public void setConfirmed_at(String confirmed_at) {
        this.confirmed_at = confirmed_at;
    }

    /**
     * 
     * @return
     *     The email
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     *     The email
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return
     *     The admin
     */
    @JsonProperty("admin")
    public String getAdmin() {
        return admin;
    }

    /**
     * 
     * @param admin
     *     The admin
     */
    @JsonProperty("admin")
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    /**
     * 
     * @return
     *     The stats
     */
    @JsonProperty("stats")
    public Stats getStats() {
        return stats;
    }

    /**
     * 
     * @param stats
     *     The stats
     */
    @JsonProperty("stats")
    public void setStats(Stats stats) {
        this.stats = stats;
    }

    /**
     * 
     * @return
     *     The friend_counts
     */
    @JsonProperty("friend_counts")
    public Friend_counts getFriend_counts() {
        return friend_counts;
    }

    /**
     * 
     * @param friend_counts
     *     The friend_counts
     */
    @JsonProperty("friend_counts")
    public void setFriend_counts(Friend_counts friend_counts) {
        this.friend_counts = friend_counts;
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
