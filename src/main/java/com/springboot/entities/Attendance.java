package com.springboot.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;


/**
 * The persistent class for the attendance database table.
 * 
 */
@Entity
@NamedQuery(name="Attendance.findAll", query="SELECT a FROM Attendance a")
public class Attendance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.DATE)
	private Date date;

	private Time timeIn;

	private Time timeOut;

	//bi-directional many-to-one association to TrainingPlan
	@ManyToOne
	@JoinColumn(name="trainingPlanID")
	private TrainingPlan trainingPlan;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="userID")
	private User user;

	public Attendance() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getTimeIn() {
		return this.timeIn;
	}

	public void setTimeIn(Time timeIn) {
		this.timeIn = timeIn;
	}

	public Time getTimeOut() {
		return this.timeOut;
	}

	public void setTimeOut(Time timeOut) {
		this.timeOut = timeOut;
	}

	public TrainingPlan getTrainingPlan() {
		return this.trainingPlan;
	}

	public void setTrainingPlan(TrainingPlan trainingPlan) {
		this.trainingPlan = trainingPlan;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}