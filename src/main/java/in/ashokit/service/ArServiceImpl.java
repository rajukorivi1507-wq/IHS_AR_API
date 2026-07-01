package in.ashokit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import in.ashokit.bindings.App;
import in.ashokit.constants.AppConstants;
import in.ashokit.entities.AppEntity;
import in.ashokit.entities.UserEntity;
import in.ashokit.exception.SsaWebException;
import in.ashokit.repositories.AppRepo;
import in.ashokit.repositories.UserRepo;

@Service
public class ArServiceImpl implements ArService {

	@Autowired
	private CitizenAppRepository appRepo;

	@Autowired
	private UserRepo userRepo;

	@Override
	public Integer createApplication(CitizenApp app) {
		 String endpoint = "https://ssa.web.app/{ssn}";

		// for both synchronous and asynchronous  call
		/* RestTemplate rt = new RestTemplate();
		ResponseEntity<String> resEntity=rt.getForEntity(endpointurl,String.class,app.getSsn());
		String stateName = resEntity.getBody(); */

		//use webflux dependency - only for synchronous call
		Webclient webclient = Webclient.create();
        String statename = webclient.get() // it represents GET request
		                   .uri(endpointurl,app.getSsn()) // It represents url to send req
		                    .retrieve() // to retrieve response
		                    .bodyToMono(String.class) // to specify response type
		                    .block(); // to make synchronous call

		
        if("New Jersey".equals(statename)){
			CitizenAppEntity entity=new CitizenAppEntity;
			BeanUtils.copyProperties(app,entity);

			entity.getStatename(statename);
			CitizenAppEntity save = appRepo.save(entity);

			return save.getAppId();
		}
		return 0;	
	}

	
