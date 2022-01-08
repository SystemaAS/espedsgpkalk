/**
 * 
 */
package no.systema.fraktkalkulator.mapper.jsonjackson;


import org.slf4j.*;

//application library
import no.systema.fraktkalkulator.model.jsonjackson.dropdownlist.JsonFraktKalkulatorDropDownContainer;

import no.systema.fraktkalkulator.model.jsonjackson.dropdownlist.JsonFraktKalkulatorIncotermsRecord;
import no.systema.fraktkalkulator.model.jsonjackson.dropdownlist.JsonFraktKalkulatorOppdragTypeRecord;
import no.systema.fraktkalkulator.model.jsonjackson.dropdownlist.JsonFraktKalkulatorProductRecord;
import no.systema.fraktkalkulator.model.jsonjackson.dropdownlist.JsonFraktKalkulatorTjenesteTypeRecord;
import no.systema.main.mapper.jsonjackson.general.ObjectMapperAbstractGrandFather;

import java.util.*;

/**
 * 
 * @author oscardelatorre
 * @date Mar 9, 2015
 * 
 * 
 */
public class FraktKalkulatorDropDownMapper extends ObjectMapperAbstractGrandFather {
	private static final Logger logger = LoggerFactory.getLogger(FraktKalkulatorDropDownMapper.class.getName());
	/**
	 * 
	 * @param utfPayload
	 * @return
	 * @throws Exception
	 */
	public JsonFraktKalkulatorDropDownContainer getContainer(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		JsonFraktKalkulatorDropDownContainer container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonFraktKalkulatorDropDownContainer.class); 
		
		//DEBUG
		/*
		Collection<JsonFraktKalkulatorTjenesteTypeRecord> fields = container.getPricecalclist_tjen();
		for(JsonFraktKalkulatorTjenesteTypeRecord record : fields){
			logger.info("Mapper!!!!:" + record.getWsavd2());
		}
		*/
		
		return container;
		
	}
	
}
