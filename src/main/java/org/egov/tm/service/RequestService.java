package org.egov.tm.service;


import org.egov.tm.model.Services;
import org.egov.tm.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
	@Autowired
	private RequestRepository requestRepository;
	
	public Services save(Services service) {
		// TODO Auto-generated method stub
		return requestRepository.save(service);
	}
}
