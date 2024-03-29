package com.springboot.repository.custom;

import java.sql.Time;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.springboot.entities.Attendance;
import com.springboot.entities.SaAssignment;
import com.springboot.entities.Schedule;
import com.springboot.entities.TrainingPlan;
import com.springboot.entities.User;
import com.springboot.entities.UserEvent;


@Repository
@Transactional
public class TrainingPlanRepository {
	
	public int insertTraining(EntityManager em, TrainingPlan trainingPlan) {
		em.persist(trainingPlan);
		em.flush();
		
		return trainingPlan.getId();
	}
	
	public void insertSchedule(EntityManager em, Schedule[] schedules) {
		for(int i=0; i<schedules.length; i++) {
			em.persist(schedules[i]);
		}
	}
	
	public void insertUserEvent(EntityManager em, UserEvent[] userEvents) {
		for(int i=0; i<userEvents.length; i++) {
			em.persist(userEvents[i]);
		}
	}
	
	public boolean editTraining(EntityManager em, TrainingPlan trainingPlan) {
		TrainingPlan training = em.find(TrainingPlan.class, trainingPlan.getId());
		
		try {
			training.setTitle(trainingPlan.getTitle());
			training.setDescription(trainingPlan.getDescription());
			training.setCourseOutline(trainingPlan.getCourseOutline());
			
			return true;
		} catch (Exception ex ) {
			return false;
		}
	}
	
	public void editSchedule(EntityManager em, Schedule[] schedules, int id) {
		String sql = "DELETE FROM Schedule WHERE trainingPlanID = :trainingPlanID";
		Query query = em.createQuery(sql);
		query.setParameter("trainingPlanID", id);
		query.executeUpdate();
		
		for(int i=0; i<schedules.length; i++) {
			em.persist(schedules[i]);
		}
	}
	
	public void editUserEvent(EntityManager em, int id, UserEvent[] userEvents, Collection<Integer> userIDS) {		
		String sql = "DELETE FROM UserEvent WHERE trainingPlanID = :trainingID AND userID NOT IN (:userIds)";
		Query query = em.createQuery(sql.toString());
		query.setParameter("trainingID", id);
		query.setParameter("userIds", userIDS);
		query.executeUpdate();
		
		
		for(int i=0; i<userEvents.length; i++) {
			sql = "UPDATE UserEvent SET role = :newRole, status = :newStatus WHERE trainingPlanID = :tid AND userID = :uid AND role <> :role";
			query = em.createQuery(sql);
			query.setParameter("tid", id);
			query.setParameter("uid", userEvents[i].getUser().getId());
			query.setParameter("newRole", userEvents[i].getRole());
			query.setParameter("role", userEvents[i].getRole());
			query.setParameter("newStatus", "pending");
			query.executeUpdate();
			
			sql = "FROM UserEvent WHERE trainingPlanID = :tid AND userID = :uid";
			query = em.createQuery(sql);
			query.setParameter("tid", id);
			query.setParameter("uid", userEvents[i].getUser().getId());
			
			if(query.getResultList().size() == 0) {
				em.persist(userEvents[i]);
			}			
		}
	}

	public List<TrainingPlan> retrieveTrainings(EntityManager em) {
		List<TrainingPlan> trainings = null;
		StringBuilder sql = new StringBuilder("FROM TrainingPlan");
		Query query = em.createQuery(sql.toString());
		trainings = query.getResultList();		

		return trainings;
	}
	
	public TrainingPlan retrieveTraining(EntityManager em, int id) {
		TrainingPlan training = null;
		StringBuilder sql = new StringBuilder("FROM TrainingPlan WHERE id = :id");
		Query query = em.createQuery(sql.toString());
		query.setParameter("id", id);
		
		training = query.getResultList().size() != 1? null : (TrainingPlan)query.getResultList().get(0);		

		return training;
	}
	

	public List<TrainingPlan> retrieveTraining(EntityManager em, int startMonth, int endMonth) {
		Session session = em.unwrap(Session.class);		
		StringBuilder stringQuery = new StringBuilder("SELECT p.* FROM training_plan p INNER JOIN schedule s ON p.id = s.trainingPlanID WHERE MONTH(s.date) BETWEEN :startMonth AND :endMonth GROUP BY s.trainingPlanID");
		SQLQuery query = session.createSQLQuery(stringQuery.toString());
		query.setParameter("startMonth", startMonth);
		query.setParameter("endMonth", endMonth);
		query.setResultTransformer(Transformers.aliasToBean(TrainingPlan.class));
				
		return query.list();
	}
	
	public List<TrainingPlan> retrieveTrainingByMonth(EntityManager em, int month) {
		Session session = em.unwrap(Session.class);		
		StringBuilder stringQuery = new StringBuilder("SELECT p.* FROM training_plan p INNER JOIN schedule s ON p.id = s.trainingPlanID WHERE MONTH(s.date) = :month GROUP BY s.trainingPlanID");
		SQLQuery query = session.createSQLQuery(stringQuery.toString());
		query.setParameter("month", month);
		query.setResultTransformer(Transformers.aliasToBean(TrainingPlan.class));
				
		return query.list();
	}
	
	public boolean removeTraining(EntityManager em, int id) {
		TrainingPlan training = em.find(TrainingPlan.class, id);
		
		em.remove(training);
		return true;
	}

	public void insertAttendance(EntityManager em, Attendance attendance, Time absentTime) {
		String sql = "UPDATE Attendance SET timeIn = :timeIn WHERE trainingPlanID = :tid AND userID = :uid AND date = :date";
		Query query = em.createQuery(sql);
		query.setParameter("tid", attendance.getTrainingPlan().getId());
		query.setParameter("uid", attendance.getUser().getId());
		query.setParameter("date", attendance.getDate());
		query.setParameter("timeIn", attendance.getTimeIn());
		int result = query.executeUpdate();
		
		if(result <= 0) {
			em.persist(attendance);
		} else {
			// Remove attendance if time in was set
			sql = "UPDATE Attendance SET timeOut = :timeOut WHERE trainingPlanID = :tid AND userID = :uid AND date = :date AND timeOut = :timeOutData";
			query = em.createQuery(sql);
			query.setParameter("tid", attendance.getTrainingPlan().getId());
			query.setParameter("uid", attendance.getUser().getId());
			query.setParameter("date", attendance.getDate());
			query.setParameter("timeOut", null);
			query.setParameter("timeOutData", absentTime);
			query.executeUpdate();
		}
	}
	
	public boolean checkHasTimeIn(EntityManager em, Collection<Integer> ids, int trainingId, Date date, Time absent) {
		Query query;
		
		String sql = "FROM Attendance WHERE date = :date AND trainingPlanID = :tid AND userID IN(:uid) AND (timeIn <> :emptyTimeIn OR timeIn <> :absent)";
		query = em.createQuery(sql);
		query.setParameter("tid", trainingId);
		query.setParameter("uid", ids);
		query.setParameter("date", date);
		query.setParameter("emptyTimeIn", null);
		query.setParameter("absent", absent);
		return query.getResultList().size() == ids.size();
	}
	
	public boolean checkIfBeforeTimeIn(EntityManager em, List<Integer> ids, int trainingId, Date date, Time timeOut) {
		Query query;
		
		String sql = "FROM Attendance WHERE date = :date AND trainingPlanID = :tid AND userID IN(:uid) AND timeIn < :timeOut";
		query = em.createQuery(sql);
		query.setParameter("tid", trainingId);
		query.setParameter("uid", ids);
		query.setParameter("date", date);
		query.setParameter("timeOut", timeOut);
		return query.getResultList().size() == ids.size();
	}
	
	public void insertTimeOutAttendance(EntityManager em, Attendance attendance) {
		String sql = "UPDATE Attendance SET timeOut = :timeOut WHERE trainingPlanID = :tid AND userID = :uid AND date = :date";
		Query query = em.createQuery(sql);
		query.setParameter("tid", attendance.getTrainingPlan().getId());
		query.setParameter("uid", attendance.getUser().getId());
		query.setParameter("date", attendance.getDate());
		query.setParameter("timeOut", attendance.getTimeOut());
		query.executeUpdate();
	}

	public void insertAbsentAttendance(EntityManager em, Attendance attendance) {
		String sql = "DELETE FROM Attendance WHERE trainingPlanID = :tid AND userID = :uid AND date = :date";
		Query query = em.createQuery(sql);
		query.setParameter("tid", attendance.getTrainingPlan().getId());
		query.setParameter("uid", attendance.getUser().getId());
		query.setParameter("date", attendance.getDate());
		query.executeUpdate();
		
		em.persist(attendance);
	}

	public void resetAttendance(EntityManager em, Attendance attendance) {
		Query query;
		String sql = "UPDATE Attendance SET timeIn = :timeIn, timeOut = :timeOut WHERE trainingPlanID = :tid AND userID = :uid AND date = :date";
		query = em.createQuery(sql);
		query.setParameter("tid", attendance.getTrainingPlan().getId());
		query.setParameter("uid", attendance.getUser().getId());
		query.setParameter("date", attendance.getDate());
		query.setParameter("timeIn", null);
		query.setParameter("timeOut", null);
		query.executeUpdate();
	}
	
	public List<UserEvent> retrieveUserEvent(EntityManager em, TrainingPlan training) {
		List<UserEvent> userEvents = null;
		StringBuilder sql = new StringBuilder("FROM UserEvent WHERE trainingPlanID = :id");
		Query query = em.createQuery(sql.toString());
		query.setParameter("id", training.getId());
		
		userEvents = query.getResultList();		

		return userEvents;
	}
	
	public List<UserEvent> retrieveUserEvent(EntityManager em, int userID) {
		List<UserEvent> userEvents = null;
		StringBuilder sql = new StringBuilder("FROM UserEvent WHERE userID = :id");
		Query query = em.createQuery(sql.toString());
		query.setParameter("id", userID);
		
		userEvents = query.getResultList();		

		return userEvents;
	}
	

	public List<UserEvent> retrieveUserEvent(EntityManager em) {
		List<UserEvent> userEvents = null;
		StringBuilder sql = new StringBuilder("FROM UserEvent");
		Query query = em.createQuery(sql.toString());
		
		userEvents = query.getResultList();		

		return userEvents;
	}

	public UserEvent retrieveUserEventById(EntityManager em, int id) {
		UserEvent userEvent = null;
		StringBuilder sql = new StringBuilder("FROM UserEvent WHERE id = :id");
		Query query = em.createQuery(sql.toString());
		query.setParameter("id", id);
		query.setMaxResults(1);
	
		try {
			userEvent = (UserEvent)query.getSingleResult();
		} catch (NonUniqueResultException ex) {
			ex.printStackTrace();
			userEvent = null;
		} catch (NoResultException ex) {
			ex.printStackTrace();
			userEvent = null;
		}

		return userEvent;
	}

}
