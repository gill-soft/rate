package com.gillsoft.api;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gillsoft.entity.CoupleRates;
import com.gillsoft.entity.Couples;
import com.gillsoft.entity.SystemCouples;
import com.gillsoft.entity.Systems;
import com.gillsoft.model.Rate;
import com.gillsoft.model.RequestError;
import com.gillsoft.service.RateLoaderService;
import com.gillsoft.service.impl.CoupleRatesServiceImpl;
import com.gillsoft.service.impl.CouplesServiceImpl;
import com.gillsoft.service.impl.SystemCouplesServiceImpl;
import com.gillsoft.service.impl.SystemsServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Configuration
@EnableScheduling
public class RateController {

	private static final Logger LOGGER = LogManager.getLogger(RateController.class);

	@Autowired
    private SystemsServiceImpl systemsService;

	@Autowired
    private SystemCouplesServiceImpl systemCouplesService;

	@Autowired
    private CouplesServiceImpl couplesService;

	@Autowired
    private CoupleRatesServiceImpl coupleRatesService;

	@GetMapping("/loaders")
	@ApiOperation("Get rate loaders")
	public ResponseEntity<List<String>> getAllLoaders() {
		List<String> loaders = new ArrayList<>();
		try {
			RateLoaderService rateLoaderService = RateLoaderService.getInstance();
			loaders = rateLoaderService.getLoaders();
		} catch (Exception e) {
			return new ResponseEntity<>(Arrays.asList(e.getMessage()), HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<>(loaders == null ? new ArrayList<>() : loaders, HttpStatus.OK);
	}

	@GetMapping("/systems")
	@ApiOperation("Get rate systems")
	public ResponseEntity<List<Systems>> getAll() {
		updateRateSchedule();
		return new ResponseEntity<>(systemsService.getAll(), HttpStatus.OK);
	}

	@PostMapping("/systems/add")
	@ApiOperation("Add new rate system")
	public ResponseEntity<Systems> addSystem(@RequestBody Systems system) {
		return new ResponseEntity<>(systemsService.save(system), HttpStatus.OK);
	}

	@DeleteMapping("/systems/{system_id}")
	@ApiOperation("Delete rate system by it's key (id)")
	public ResponseEntity<String> deleteSystem(@PathVariable("system_id") Integer systemId) {
		Systems system = systemsService.findOne(systemId);
		if (system != null) {
			systemsService.delete(system);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
	}

	@GetMapping("/{organization_id}/systems")
	@ApiOperation("Get all rate system couples")
	public ResponseEntity<Map<Integer, List<Couples>>> getSystemCouples(
			@PathVariable("organization_id") String organizationId) {
		return new ResponseEntity<>(systemCouplesService.getAllSystemCouples(organizationId),
				HttpStatus.OK);
	}

	@PostMapping("/systems/couples/add")
	@ApiOperation("Add rate system couple")
	public ResponseEntity<SystemCouples> addSystemCouples(@RequestBody SystemCouples systemCouples) {
		return new ResponseEntity<>(systemCouplesService.save(systemCouples), HttpStatus.OK);
	}

	@DeleteMapping("/{organization_id}/{couple_id}")
	@ApiOperation("Delete organization's rate system")
	public ResponseEntity<Map<Integer, List<Couples>>> deleteCouple(
			@PathVariable("organization_id") String organizationId, @PathVariable("couple_id") Integer coupleId) {
		systemCouplesService.deleteSystemCouple(organizationId, coupleId);
		return new ResponseEntity<>(systemCouplesService.getAllSystemCouples(organizationId),
				HttpStatus.OK);
	}

	@GetMapping("/{organization_id}/couples")
	@ApiOperation("Get all rate couples")
	public ResponseEntity<List<Couples>> getOrganizationCouples(
			@PathVariable("organization_id") String organizationId) {
		return new ResponseEntity<>(couplesService.getOrganizationCouples(organizationId),
				HttpStatus.OK);
	}

	@GetMapping("/{organization_id}/all")
	@ApiOperation("Get all organization info")
	public ResponseEntity<Map<String, List<Object>>> getOrganizationAll(
			@PathVariable("organization_id") String organizationId) {
		return new ResponseEntity<>(couplesService.getOrganizationAll(organizationId),
				HttpStatus.OK);
	}

	@PostMapping("/couples")
	@ApiOperation("Add new rate couple")
	public ResponseEntity<?> createCouples(@RequestBody Couples couples) {
		if (couples.getCurrencyFrom() == null || couples.getCurrencyTo() == null
				|| !couples.getCurrencyFrom().replaceAll("[A-Z]{3}", "").isEmpty()
				|| !couples.getCurrencyTo().replaceAll("[A-Z]{3}", "").isEmpty()) {
			return new ResponseEntity<>(new RequestError("Currency code error (ISO 4217) [USD, EUR, UAH...]"),
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(couplesService.save(couples), HttpStatus.OK);
	}

	@DeleteMapping("/couples/{couple_id}")
	@ApiOperation("Delete couple by it's key (id)")
	public ResponseEntity<?> deleteCouple(@PathVariable("couple_id") Integer coupleId) {
		couplesService.delete(coupleId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/rate/{couple_id}")
	@ApiOperation("Get rate for couple_id (currency_from/currency_to pair)")
	public ResponseEntity<List<CoupleRates>> getRate(@PathVariable("couple_id") Integer coupleId) {
		return new ResponseEntity<>(
				coupleRatesService.getRateCouple(coupleId, new Date()), HttpStatus.OK);
	}

	@GetMapping("/rates/{organization_id}")
	@ApiOperation("Get organization rates for current date")
	public ResponseEntity<Object> getOrganizationRates(@PathVariable("organization_id") String organizationId) {
		return new ResponseEntity<>(
				coupleRatesService.getAllCouplesRatesByOrganization(organizationId), HttpStatus.OK);
	}

	@GetMapping("/rate/{couple_id}/{date}")
	@ApiOperation("Get rate for couple_id (currency_from/currency_to pair) for date")
	public ResponseEntity<?> getRateForDate(@PathVariable("couple_id") Integer coupleId,
			@PathVariable("date") String date) {
		if (date == null || !date.replaceAll("\\d{4}-\\d{2}-\\d{2}", "").isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RequestError("Bad date format [yyyy-MM-dd]"));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date rateDate;
		try {
			rateDate = sdf.parse(date);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad date format [yyyy-MM-dd]");
		}
		return new ResponseEntity<>(
				coupleRatesService.getRateCouple(coupleId, rateDate), HttpStatus.OK);
	}

	@PostMapping("/rate")
	@ApiOperation("Set rate for currency_from/currency_to pair")
	public ResponseEntity<?> setRate(@RequestBody CoupleRates coupleRates) {
		try {
			coupleRatesService.setRate(coupleRates);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			return new ResponseEntity<>(new RequestError(e.getMessage()), HttpStatus.OK);
		}
	}

	@Scheduled(cron = "0 0 * * * ?")
	public void updateRateSchedule() {
		int hourOfDay = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
		systemsService.getAll().stream().filter(f -> f.getRunHour().equals(hourOfDay)).forEach(system -> {
			try {
				// получаем дату начала действия курса
				Date startDate = getRateStartDate(system.getDaysShift());
				// получаем пары валют для обновления курса
				List<Couples> systemCouples = systemCouplesService.getSystemCouples(system.getId());
				// если список не пуст - получаем текущие курсы
				if (systemCouples != null && !systemCouples.isEmpty()) {
					RateLoaderService rateLoaderService = RateLoaderService.getInstance();
					List<Rate> rates = rateLoaderService.loadRate(system.getPluginName(), startDate);
					if (rates != null && !rates.isEmpty()) {
						systemCouples.stream().forEach(couple -> {
							if (couple.getCurrencyTo().equals(system.getCurrency())) {
								rates.stream().filter(rate -> rate.getCurCode().equals(couple.getCurrencyFrom()))
										.forEach(rate -> systemCouplesService.updateRate(system.getId(),
												couple.getCurrencyFrom(), couple.getCurrencyTo(), rate.getRate(),
												startDate));
							} else if (couple.getCurrencyFrom().equals(system.getCurrency())) {
								rates.stream().filter(rate -> rate.getCurCode().equals(couple.getCurrencyFrom()))
										.forEach(rate -> systemCouplesService.updateRate(system.getId(),
												couple.getCurrencyFrom(), couple.getCurrencyTo(), BigDecimal.ONE.divide(rate.getRate()),
												startDate));
							} else {
								Stream<Rate> ratesFromTo = rates.stream()
										.filter(rate -> rate.getCurCode().equals(couple.getCurrencyFrom())
												|| rate.getCurCode().equals(couple.getCurrencyTo()));
								if (ratesFromTo.count() == 2) {
									BigDecimal rateFrom = BigDecimal.ONE;
									BigDecimal rateTo = BigDecimal.ONE;
									if (ratesFromTo.findFirst().get().getCurCode().equals(couple.getCurrencyFrom())) {
										rateFrom = ratesFromTo.findFirst().get().getRate();
										rateTo = ratesFromTo.skip(1).findFirst().get().getRate();
									} else {
										rateTo = ratesFromTo.findFirst().get().getRate();
										rateFrom = ratesFromTo.skip(1).findFirst().get().getRate();
									}
									systemCouplesService.updateRate(system.getId(), couple.getCurrencyFrom(),
											couple.getCurrencyTo(), rateFrom.divide(rateTo), startDate);
								}
							}
						});
					}
				}
			} catch (Exception e) {
				LOGGER.error("updateRateSchedule(): " + e.getMessage(), e);
			}
		});
	}

	private Date getRateStartDate(Integer dayShift) {
		Calendar startDate = GregorianCalendar.getInstance();
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MILLISECOND, 0);
		if (dayShift != null && dayShift != 0) {
			startDate.add(Calendar.DAY_OF_WEEK, dayShift);
		}
		return startDate.getTime();
	}

}
