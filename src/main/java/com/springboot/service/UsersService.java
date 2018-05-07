package com.springboot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.entities.User;
import com.springboot.entities.custom.CustomUser;
import com.springboot.repository.custom.UsersRepository;

@Service
public class UsersService {
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private UsersRepository usersRepository;
	
	
	public List<CustomUser> retrieveUsers() {
		List data = usersRepository.retrieveUsers(em);
		List<CustomUser> cUsers = new ArrayList<CustomUser>();
		List<Integer> track = new ArrayList<Integer>();
		int index = 0;
		
		
		int length = data.size();
		for(int i=0; i<length; i++) {
			CustomUser cUser = new CustomUser();
			
			Object[] row = (Object[]) data.get(i);
			index = track.indexOf(row[0]);
			if(index != -1) {
				cUsers.get(index).setTraining(row[4].toString());
				cUsers.get(index).setRole(row[5].toString());
			} else {
				cUser.setId((int)row[0]);
				cUser.setName(row[1].toString());
				cUser.setPosition(row[2].toString());
				cUser.setEmail(row[3].toString());
				if(row[4] != null)
					cUser.setTraining(row[4].toString());
				if(row[5] != null)
					cUser.setRole(row[5].toString());
				
				cUsers.add(cUser);
				track.add((Integer)row[0]);
			}
		}	
		
		return cUsers;
	}

	public List<User> retrievePendingUsers() {
		List<User> users = usersRepository.retrievePendingUsers(em);
		
		return users;
	}
	
	public void removeUser(String id) {
		try {
			int uid = Integer.parseInt(id);
			usersRepository.removeUser(em, uid);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public void approveUser(String id) {
		try {
			int uid = Integer.parseInt(id);
			usersRepository.approveUser(em, uid);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void declineUser(String id) {
		try {
			int uid = Integer.parseInt(id);
			usersRepository.declineUser(em, uid);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}