package models;

// Generated Jul 4, 2015 5:56:59 PM by Hibernate Tools 4.3.1

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import models.UserProfile;
import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;

/**
 * User generated by hbm2java
 */
@Entity
@Table(name = "user")
public class User implements java.io.Serializable, Subject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private UserProfile userProfile;
	private String email;
	private String name;
	private String firstName;
	private String lastName;
	private Date lastLogin;
	private Boolean active;
	private Boolean emailValidated;
	private Date createdOn;
	private Date updatedOn;
	private Set<SecurityRole> securityRoles = new HashSet<SecurityRole>(0);
	private Set<LinkedAccount> linkedAccounts = new HashSet<LinkedAccount>(0);
	private Set<TokenAction> tokenActions = new HashSet<TokenAction>(0);
	private Set<UserPermission> userPermissions = new HashSet<UserPermission>(0);

	public User() {
	}
	
	public User(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
	public User(UserProfile userProfile, String email, String name,
			String firstName, String lastName, Boolean active, Boolean emailValidated) {
		this.userProfile = userProfile;
		this.email = email;
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.active = active;
		this.emailValidated = emailValidated;
	}

	public User(UserProfile userProfile, String email, String name,
			String firstName, String lastName, Date lastLogin, Boolean active, Boolean emailValidated,
			Date createdOn, Date updatedOn, Set<SecurityRole> securityRoles,
			Set<LinkedAccount> linkedAccounts, Set<TokenAction> tokenActions,
			Set<UserPermission> userPermissions) {
		this.userProfile = userProfile;
		this.email = email;
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.lastLogin = lastLogin;
		this.active = active;
		this.emailValidated = emailValidated;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.securityRoles = securityRoles;
		this.linkedAccounts = linkedAccounts;
		this.tokenActions = tokenActions;
		this.userPermissions = userPermissions;
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
	@JoinColumn(name = "userprofile_id", nullable = true)
	public UserProfile getUserProfile() {
		return this.userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "first_name")
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "last_name")
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login", length = 19)
	public Date getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "email_validated")
	public Boolean getEmailValidated() {
		return this.emailValidated;
	}

	public void setEmailValidated(Boolean emailValidated) {
		this.emailValidated = emailValidated;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 19)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on", length = 19)
	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_has_security_role", joinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "security_role_id", nullable = false, updatable = false) })
	public Set<SecurityRole> getSecurityRoles() {
		return this.securityRoles;
	}

	public void setSecurityRoles(Set<SecurityRole> securityRoles) {
		this.securityRoles = securityRoles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<LinkedAccount> getLinkedAccounts() {
		return this.linkedAccounts;
	}

	public void setLinkedAccounts(Set<LinkedAccount> linkedAccounts) {
		this.linkedAccounts = linkedAccounts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<TokenAction> getTokenActions() {
		return this.tokenActions;
	}

	public void setTokenActions(Set<TokenAction> tokenActions) {
		this.tokenActions = tokenActions;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_has_user_permission", joinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "user_permission_id", nullable = false, updatable = false) })
	public Set<UserPermission> getUserPermissions() {
		return this.userPermissions;
	}

	public void setUserPermissions(Set<UserPermission> userPermissions) {
		this.userPermissions = userPermissions;
	}
	
	@Transient
	public Set<String> getProviders() {
		Set<String> providerKeys = new HashSet<String>(this.linkedAccounts.size());
		for (LinkedAccount acc : this.getLinkedAccounts()) {
			providerKeys.add(acc.getProviderKey());
		}
		return providerKeys;
	}

	@Override
	@Transient
	public String getIdentifier() {
		return Integer.toString(this.id);
	}

	@Override
	@Transient
	public List<? extends Permission> getPermissions() {
		return new ArrayList<UserPermission>(this.getUserPermissions());
	}

	@Override
	@Transient
	public List<? extends Role> getRoles() {
		return new ArrayList<SecurityRole>(this.getSecurityRoles());
	}
	
	@Transient
	public List<Integer> getRoleIds(){
		List<Integer> roles = new ArrayList<Integer>();
		
		for(SecurityRole role: this.getSecurityRoles())
		{
			roles.add(role.getId());
		}
		
		return roles;
	}

}
