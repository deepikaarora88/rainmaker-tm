package org.egov.tm.service;


import org.egov.tm.model.ActionInfo;
import org.egov.tm.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionService {
	@Autowired
	private ActionRepository actionRepository;
	
	public ActionInfo save(ActionInfo action) {
		// TODO Auto-generated method stub
		return actionRepository.save(action);
	}
}
