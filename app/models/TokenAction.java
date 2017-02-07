package models;

// Generated Jul 4, 2015 5:56:59 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * TokenAction generated by hbm2java
 */
@Entity
@Table(name = "token_action", uniqueConstraints = @UniqueConstraint(columnNames = "token"))
public class TokenAction implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private User user;
	private String token;
	private String type;
	private Date createdOn;
	private Date expiresOn;

	public TokenAction() {
	}

	public TokenAction(Date createdOn, Date expiresOn) {
		this.createdOn = createdOn;
		this.expiresOn = expiresOn;
	}

	public TokenAction(User user, String token, String type, Date createdOn,
			Date expiresOn) {
		this.user = user;
		this.token = token;
		this.type = type;
		this.createdOn = createdOn;
		this.expiresOn = expiresOn;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target_user_id")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "token", unique = true)
	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(name = "type", columnDefinition="enum('EMAIL_VERIFICATION','PASSWORD_RESET')")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false, length = 19)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expires_on", nullable = false, length = 19)
	public Date getExpiresOn() {
		return this.expiresOn;
	}

	public void setExpiresOn(Date expiresOn) {
		this.expiresOn = expiresOn;
	}
	
	@Transient
	public boolean isValid() {
		return this.expiresOn.after(new Date());
	}

}
