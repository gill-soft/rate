package com.gillsoft.loader;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;

import com.gillsoft.model.Rate;
import com.gillsoft.model.RateLoader;
import com.gillsoft.model.ValCurs;

public class CbrRateLoader implements RateLoader {

	/*
	 * http://www.cbr.ru/scripts/XML_daily.asp?date_req=08/10/2018
	 */
	private static final String HOST = "http://www.cbr.ru/scripts/XML_daily.asp";

	public String getName() {
		return "CbrRateLoader";
	}

	public List<Rate> loadRate(Date date) {
		List<Rate> rateList = new ArrayList<>();
		ValCurs valCurs = getExchange(date);
		if (valCurs != null && valCurs.getValute() != null) {
			valCurs.getValute().stream().forEach(valute -> rateList.add(
					new Rate(valute.getCharCode(), new BigDecimal(valute.getValue()).divide(valute.getNominal()))));
		}
		return rateList;
	}

	public static ValCurs getExchange(Date date) {
		try {
			HttpGet httpget = new HttpGet(new URIBuilder(HOST)
					.setParameter("date_req", new SimpleDateFormat("dd/MM/yyyy").format(date)).build());
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(httpget);
			JAXBContext jaxbContext = JAXBContext.newInstance(ValCurs.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return (ValCurs) unmarshaller.unmarshal(response.getEntity().getContent());
		} catch (Exception e) {
			LogManager.getLogger(CbrRateLoader.class).error(e);
		}
		return null;
	}

}
