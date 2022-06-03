CREATE TABLE IF NOT EXISTS UserPrincipal (
    UserId BIGINT NOT NULL IDENTITY PRIMARY KEY,
  	Username VARCHAR(36) NOT NULL,
  	HashedPassword VARCHAR(128) NOT NULL,
  	AccountNonExpired BOOLEAN NOT NULL,
  	AccountNonLocked BOOLEAN NOT NULL,
  	CredentialsNonExpired BOOLEAN NOT NULL,
  	Enabled BOOLEAN NOT NULL,
  	CreatedDate TIMESTAMP NOT NULL,
  	CreatedBy BIGINT DEFAULT 0 NOT NULL,
  	UpdatedDate TIMESTAMP,
  	UpdatedBy BIGINT,
  	DeletedDate TIMESTAMP,
  	CONSTRAINT UC_UserPrincipal_Username UNIQUE (Username)
);

CREATE TABLE IF NOT EXISTS UserPrincipalAuthority (
    UserId BIGINT NOT NULL,
  	Authority VARCHAR(100) NOT NULL,
  	CONSTRAINT UC_UserPrincipalAuthority_User_Authority UNIQUE (UserId, Authority),
  	CONSTRAINT FK_UserPrincipalAuthority_UserId FOREIGN KEY (UserId) 
	REFERENCES UserPrincipal (UserId) ON DELETE CASCADE
)
