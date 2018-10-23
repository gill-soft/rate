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

import com.gillsoft.model.Exchange;
import com.gillsoft.model.Rate;
import com.gillsoft.model.RateLoader;

public class NbuRateLoader implements RateLoader {

	private static final String HOST = "http://bank.gov.ua/NBUStatService/v1/statdirectory/exchange";

	public String getName() {
		return "NbuRateLoader";
	}

	public List<Rate> loadRate(Date date) {
		List<Rate> rateList = new ArrayList<>();
		Exchange exchange = getExchange(date);
		if (exchange != null && exchange.getCurrency() != null) {
			exchange.getCurrency().stream()
					.forEach(rate -> rateList.add(new Rate(rate.getCc(), new BigDecimal(rate.getRate()))));
		}
		return rateList;
	}

	private Exchange getExchange(Date date) {
		try {
			HttpGet httpget = new HttpGet(
					new URIBuilder(HOST).setParameter("date", new SimpleDateFormat("yyyyMMdd").format(date)).build());
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(httpget);
			JAXBContext jaxbContext = JAXBContext.newInstance(Exchange.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return (Exchange) unmarshaller.unmarshal(response.getEntity().getContent());
		} catch (Exception e) {
			LogManager.getLogger(NbuRateLoader.class).error(e);
		}
		return null;
	}

}
