package no.systema.fraktkalkulator.validator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import no.systema.fraktkalkulator.controller.FraktKalkulatorController;
import no.systema.fraktkalkulator.model.jsonjackson.JsonFraktKalkulatorResultContainer;
import no.systema.fraktkalkulator.model.jsonjackson.JsonFraktKalkulatorUserContainer;
import no.systema.fraktkalkulator.service.FraktKalkulatorChildWindowsService;
import no.systema.fraktkalkulator.url.store.FraktKalkulatorUrlDataStore;
import no.systema.fraktkalkulator.util.FraktKalkulatorConstants;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.model.jsonjackson.general.postalcodes.JsonPostalCodesContainer;
import no.systema.main.model.jsonjackson.general.postalcodes.JsonPostalCodesRecord;
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.util.StringManager;

/**
 * 
 * @author oscardelatorre
 * @date Mar 12, 2015
 * 
 *
 */
public class FraktkalkulatorValidator implements Validator {
	private static final Logger logger = Logger.getLogger(FraktkalkulatorValidator.class.getName());
	private static final StringManager strMgr = new StringManager();
	private UrlCgiProxyService urlCgiProxyService;
	private FraktKalkulatorChildWindowsService fraktKalkulatorChildWindowsService;
	private SystemaWebUser appUser;
	/**
	 * 
	 */
	public boolean supports(Class clazz) {
		return JsonFraktKalkulatorUserContainer.class.isAssignableFrom(clazz); 
	}
	/**
	 * Special constructor
	 * @param service
	 * @param user
	 * @param fraktService
	 */
	public FraktkalkulatorValidator (UrlCgiProxyService service, SystemaWebUser user, FraktKalkulatorChildWindowsService fraktService){
		this.urlCgiProxyService = service;
		this.appUser = user;
		this.fraktKalkulatorChildWindowsService = fraktService;
	}
	
	/**
	 * @param obj
	 * @param errors
	 * 
	 */
	public void validate(Object obj, Errors errors) { 
		JsonFraktKalkulatorUserContainer record = (JsonFraktKalkulatorUserContainer)obj;
		//Check for Mandatory fields
		if(record!=null){
			if( "".equals(record.getFra()) || "".equals(record.getFralk()) || "".equals(record.getTil()) ||"".equals(record.getTillk())){
				errors.rejectValue("fra", "systema.fraktkalkulator.main.form.search.notValidMandatoryFields");
			}
			
			//check validity of Postal no.
			if(strMgr.isNotNull(record.getFra()) && strMgr.isNotNull(record.getFralk())){
				if(!this.isValidPostalNo(record, "FROM")){
					errors.rejectValue("fra", "systema.fraktkalkulator.main.form.search.invalid.postal.code.from");
				}
			}
			//check validity of Postal no.
			if(strMgr.isNotNull(record.getTil()) && strMgr.isNotNull(record.getTillk())){
				if(!this.isValidPostalNo(record, null)){
					errors.rejectValue("til", "systema.fraktkalkulator.main.form.search.invalid.postal.code.to");
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param record
	 * @param from
	 * @return
	 */
	private boolean isValidPostalNo(JsonFraktKalkulatorUserContainer record, String from){
		boolean retval = false;
		
		String BASE_URL = FraktKalkulatorUrlDataStore.FRAKTKALKULATOR_FETCH_POSTAL_CODES_URL;
		StringBuffer urlRequestParamsKeys = new StringBuffer();
		urlRequestParamsKeys.append("user=" + appUser.getUser());
		if(strMgr.isNotNull(from)){
			urlRequestParamsKeys.append("&soklk=" + record.getFralk() + "&sokkod=" + record.getFra());
		}else{
			urlRequestParamsKeys.append("&soklk=" + record.getTillk() + "&sokkod=" + record.getTil());
		}
		logger.info("URL: " + BASE_URL);
		logger.info("PARAMS: " + urlRequestParamsKeys);
		logger.info(Calendar.getInstance().getTime() +  " CGI-start timestamp");
		String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParamsKeys.toString());
		//Debug -->
    	logger.info(jsonPayload);
		logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    
		if(jsonPayload!=null){
    		JsonPostalCodesContainer container = this.fraktKalkulatorChildWindowsService.getPostalCodesContainer(jsonPayload);
    		if(container!=null){
    			if(container.getPostnrlist()!=null && container.getPostnrlist().size()>0){
    				logger.info("Postnr finnes!");
    				retval = true;
    			}
    		}
		}
		return retval;
	}
	
}
