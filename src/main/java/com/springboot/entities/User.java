package com.springboot.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String email;

	@Column(name = "is_admin")
	private int isAdmin;

	private String name;

	private String password;

	private String status;

	// bi-directional many-to-one association to Attendance
	@OneToMany(mappedBy = "user")
	private List<Attendance> attendances;

	// bi-directional many-to-one association to Position
	@ManyToOne
	@JoinColumn(name = "position")
	private Position position;

	// bi-directional many-to-one association to UserEvent
	@OneToMany(mappedBy = "user")
	private List<UserEvent> userEvents;

	private transient int userEventID;

	public User() {
		this(0);
	}

	public User(int id) {
		this.id = id;
		
		attendances = new ArrayList<Attendance>();
		userEvents = new ArrayList<UserEvent>();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIsAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Attendance> getAttendances() {
		return this.attendances;
	}

	public Attendance getAttendanceByTrainingAndDate(int trainingId, Date date) {
		Attendance attendance = null;
		for (Attendance attend : attendances) {
			if (attend.getTrainingPlan().getId() == trainingId && attend.getDate().equals(date)) {
				attendance = attend;
				break;
			}
		}

		return attendance;
	}

	public void setAttendances(List<Attendance> attendances) {
		this.attendances = attendances;
	}

	public Attendance addAttendance(Attendance attendance) {
		getAttendances().add(attendance);
		attendance.setUser(this);

		return attendance;
	}

	public Attendance removeAttendance(Attendance attendance) {
		getAttendances().remove(attendance);
		attendance.setUser(null);

		return attendance;
	}

	public Position getPosition() {
		return this.position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public List<UserEvent> getUserEvents() {
		return this.userEvents;
	}

	public void setUserEvents(List<UserEvent> userEvents) {
		this.userEvents = userEvents;
	}

	public UserEvent addUserEvent(UserEvent userEvent) {
		getUserEvents().add(userEvent);
		userEvent.setUser(this);

		return userEvent;
	}

	public UserEvent removeUserEvent(UserEvent userEvent) {
		getUserEvents().remove(userEvent);
		userEvent.setUser(null);

		return userEvent;
	}

	public int getUserEventID() {
		return userEventID;
	}

	public void setUserEventID(int userEventID) {
		this.userEventID = userEventID;
	}

}