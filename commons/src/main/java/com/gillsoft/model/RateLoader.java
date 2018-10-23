package com.gillsoft.model;

import java.util.Date;
import java.util.List;

public interface RateLoader {

	public String getName();

	public List<Rate> loadRate(Date date);

}
