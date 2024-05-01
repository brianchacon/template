package com.ich.template.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="UserSession")
public class UserSession {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "updated_at")
	private Date  updated_at = new Date();//Registrará la actualización del token (de la session)	
	@Column(name = "user")
	private int  user;
	@Column(name = "token")
	private String token;//que identifica la session actual del usuario	
	@Column(name = "revoked")
    public boolean revoked;//yo creo: que usa para inhabilitar una cuenta
//	private boolean  online;//para marcar que el usuairio esta online
	@Column(name = "sessionEnded")
	private boolean  sessionEnded;//para marcar que ha cerrado session (con esto si token esta en fecha, no podrá ser usado tras cerrrar sesión

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean isRevoked() {
		return revoked;
	}
	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}
	public boolean isSessionEnded() {
		return sessionEnded;
	}
	public void setSessionEnded(boolean sessionEnded) {
		this.sessionEnded = sessionEnded;
	}


//	  public boolean expired;//yo creo: que usa para marcar que el token/sesion expiró

//	  @ManyToOne(fetch = FetchType.LAZY)
//	  @JoinColumn(name = "user_id")
//	  public User user;
	
	
}
