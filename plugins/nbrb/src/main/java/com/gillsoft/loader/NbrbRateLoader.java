package com.gillsoft.loader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gillsoft.model.NbrbRate;
import com.gillsoft.model.Rate;
import com.gillsoft.model.RateLoader;

public class NbrbRateLoader implements RateLoader {

	/*
	 * http://www.nbrb.by/APIHelp/ExRates
	 * http://www.nbrb.by/API/ExRates/Rates?onDate=2018-10-8&Periodicity=0
	 */
	private static final String HOST = "http://www.nbrb.by/API/ExRates/Rates";

	public String getName() {
		return "NbrbRateLoader";
	}

	public List<Rate> loadRate(Date date) {
		List<Rate> rateList = new ArrayList<>();
		List<NbrbRate> nbrbRateList = getExchange(date);
		if (nbrbRateList != null) {
			nbrbRateList.stream().forEach(rate -> rateList.add(new Rate(rate.getCurAbbreviation(), rate.getRate())));
		}
		return rateList;
	}

	private List<NbrbRate> getExchange(Date date) {
		try {
			HttpGet httpget = new HttpGet(
					new URIBuilder(HOST).setParameter("onDate", new SimpleDateFormat("yyyy-M-d").format(date))
							.setParameter("Periodicity", "0").build());
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(httpget);
			ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
	        return Arrays.asList((NbrbRate[]) objectMapper.readerFor(NbrbRate[].class).readValue(response.getEntity().getContent()));
		} catch (Exception e) {
			LogManager.getLogger(NbrbRateLoader.class).error(e);
		}
		return null;
	}

}
