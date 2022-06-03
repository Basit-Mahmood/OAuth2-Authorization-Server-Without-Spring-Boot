package pk.training.basit.jpa.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;

import pk.training.basit.jpa.audit.AuditDeletedDate;


@Entity
@Table(
	uniqueConstraints = {
        @UniqueConstraint(
    	    name = "UserPrincipal_Username", 
    	    columnNames = "Username"
        )
    }
)
public class UserPrincipal implements UserDetails, CredentialsContainer, Cloneable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "UserId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	private String username;
	
	@Basic(fetch = FetchType.LAZY)
    @Column(name = "HashedPassword")
    private String hashedPassword;
	
	private boolean enabled;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private boolean accountNonLocked;
	
	// @CollectionTable in UserPrincipal maps UserAuthority to the UserPrincipalAuthority table.
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
    	name = "UserPrincipalAuthority", 
    	joinColumns = {
            @JoinColumn(
            	name = "UserId", 
            	referencedColumnName = "UserId"
            )
       }
    )
    private Set<UserAuthority> authorities = new HashSet<>();
    
    @Embedded
    private AuditDeletedDate audit = new AuditDeletedDate();
   
    public UserPrincipal() {
		
	}

    public UserPrincipal(Long id, String username, String password, Set<UserAuthority> authorities) {
		this(id, username, password, true, true, true, true, authorities, null);
	}
    
	public UserPrincipal(Long id, String username, String password, boolean enabled, boolean accountNonExpired, 
			boolean credentialsNonExpired, boolean accountNonLocked, Set<UserAuthority> authorities, AuditDeletedDate audit) {
		this.id = id;
		this.username = username;
		this.hashedPassword = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = Collections.unmodifiableSet(authorities);
		this.audit = audit;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public void setHashedPassword(String password) {
        this.hashedPassword = password;
    }
    
    @Transient
    @Override
    public String getPassword() {
        return this.getHashedPassword() == null ? null : this.getHashedPassword();
    }

    @Override
    public void eraseCredentials() {
        this.hashedPassword = null;
    }
	
    @Override
    public Set<UserAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<UserAuthority> authorities) {
        this.authorities = authorities;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public AuditDeletedDate getAudit() {
		return audit;
	}

	public void setAudit(AuditDeletedDate audit) {
		this.audit = audit;
	}

	@Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public boolean equals(Object other) {
    	return other instanceof UserPrincipal && ((UserPrincipal)other).id == this.id;
    }

    @Override
    protected UserPrincipal clone() {
    	try {
            return (UserPrincipal)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // not possible
        }
    }

    @Override
    public String toString() {
        return this.username;
    }
    
}
