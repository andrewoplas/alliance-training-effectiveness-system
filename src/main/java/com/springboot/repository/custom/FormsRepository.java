package com.springboot.repository.custom;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.NonUniqueResultException;
import org.springframework.stereotype.Repository;

import com.springboot.entities.Form;
import com.springboot.entities.FormAnswer;
import com.springboot.entities.FormAssignment;
import com.springboot.entities.FormOption;
import com.springboot.entities.FormQuestion;
import com.springboot.entities.SaAssignment;
import com.springboot.entities.SaCategory;


@Repository
@Transactional
public class FormsRepository {
	
	public void deleteSkillsAssessment(EntityManager em, Collection<Integer> categoryIDS) {
		String sql = "DELETE FROM SaCategory WHERE id NOT IN (:categoryIDS)";
		Query query = em.createQuery(sql.toString());
		query.setParameter("categoryIDS", categoryIDS);
		query.executeUpdate();
	}
	
	public SaCategory retrieveCategory(EntityManager em, int id) {
		SaCategory category = null;
		String stringQuery = "FROM SaCategory WHERE id = :id";
		Query query = em.createQuery(stringQuery.toString());
		query.setParameter("id", id);
		query.setMaxResults(1);
		
		try {
			category = (SaCategory) query.getSingleResult();
		} catch (NonUniqueResultException ex) {
			ex.printStackTrace();
			category = null;
		} catch (NoResultException ex) {
			ex.printStackTrace();
			category = null;
		}
		
		return category;
	}
	
	public SaCategory insertCategory(EntityManager em, SaCategory category) {
		em.persist(category);
		em.flush();
		
		return category;
	}
	
	public void updateCategory(EntityManager em, SaCategory category) {
		String sql = "UPDATE SaCategory SET description = :description, row_order = :row_order, parentID = :parentID WHERE id = :id";
		Query query = em.createQuery(sql);
		query.setParameter("description", category.getDescription());
		query.setParameter("row_order", category.getRowOrder());
		query.setParameter("parentID", category.getSaCategory() == null? null : category.getSaCategory().getId());
		query.setParameter("id", category.getId());
		query.executeUpdate();
	}
	
	public List<SaCategory> retrieveParentSkillsAssessment(EntityManager em) {
		List<SaCategory> parents = null;
		StringBuilder sql = new StringBuilder("FROM SaCategory WHERE parentID = NULL ORDER BY row_order ASC");
		Query query = em.createQuery(sql.toString());
		parents = query.getResultList();		

		return parents;
	}
	
	public FormQuestion insertQuestion(EntityManager em, FormQuestion question) {
		em.persist(question);
		em.flush();
		
		return question;
	}
	

	public FormQuestion updateQuestion(EntityManager em, FormQuestion question) {
		em.merge(question);
		
		return question;
	}
	
	public void deleteQuestions(EntityManager em, int formID, Collection<Integer> questionIDS) {
		String sql = "DELETE FROM FormQuestion WHERE formID = :formID AND id NOT IN (:questionIDS)";
		Query query = em.createQuery(sql.toString());
		query.setParameter("questionIDS", questionIDS);
		query.setParameter("formID", formID);
		query.executeUpdate();
	}
	
	public void mergeOptions(EntityManager em, FormOption option) {
		em.merge(option);
	}
	
	public void deleteOption(EntityManager em, int questionID, Collection<Integer> optionIDS) {
		String sql = "DELETE FROM FormOption WHERE questionID = :questionID AND id NOT IN (:optionIDS)";
		Query query = em.createQuery(sql.toString());
		query.setParameter("optionIDS", optionIDS);
		query.setParameter("questionID", questionID);
		query.executeUpdate();
	}

	public Form retrieveForm(EntityManager em, int formID) {
		Form form = null;
		String stringQuery = "FROM Form WHERE id = :id";
		Query query = em.createQuery(stringQuery.toString());
		query.setParameter("id", formID);
		query.setMaxResults(1);
		
		try {
			form = (Form)query.getSingleResult();
		} catch (NonUniqueResultException ex) {
			ex.printStackTrace();
			form = null;
		} catch (NoResultException ex) {
			ex.printStackTrace();
			form = null;
		}
		
		return form;
	}
	
	public boolean containsFormAssignment(EntityManager em, int formID, int userID) {
		// Check if user exist through checking email
		String stringQuery = "FROM FormAssignment WHERE formID = :formID AND userEventID = :userEventID";
		Query query = em.createQuery(stringQuery.toString());
		query.setParameter("formID", formID);
		query.setParameter("userEventID", userID);
		
		return !query.getResultList().isEmpty();
	}
	
	public void insertFormAssignment(EntityManager em, FormAssignment assignment) {
		em.persist(assignment);
	}
	
	public SaAssignment retrieveAssignment(EntityManager em, int assignmentID) {
		SaAssignment assignment = em.find(SaAssignment.class, assignmentID);
		return assignment;
	}

	public FormAnswer retrieveFormAnswer(EntityManager em, int formAnswerID) {
		FormAnswer formAnswer = null;
		String stringQuery = "FROM FormAnswer WHERE id = :id";
		Query query = em.createQuery(stringQuery.toString());
		query.setParameter("id", formAnswerID);
		query.setMaxResults(1);
		
		
		try {
			formAnswer = (FormAnswer)query.getSingleResult();
		} catch (NonUniqueResultException ex) {
			ex.printStackTrace();
			formAnswer = null;
		} catch (NoResultException ex) {
			ex.printStackTrace();
			formAnswer = null;
		}
		
		return formAnswer;
	}

	public FormAssignment retrieveFormAssignment(EntityManager em, int formAssignmentID) {
		FormAssignment formAssignment = null;
		String stringQuery = "FROM FormAssignment WHERE id = :id";
		Query query = em.createQuery(stringQuery.toString());
		query.setParameter("id", formAssignmentID);
		query.setMaxResults(1);
		
		try {
			formAssignment = (FormAssignment)query.getSingleResult();
		} catch (NonUniqueResultException ex) {
			ex.printStackTrace();
			formAssignment = null;
		} catch (NoResultException ex) {
			ex.printStackTrace();
			formAssignment = null;
		}
		
		return formAssignment;
	}


}
