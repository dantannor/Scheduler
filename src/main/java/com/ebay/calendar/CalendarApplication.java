package com.ebay.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CalendarApplication {

	public static void main(String[] args) {

		SpringApplication.run(CalendarApplication.class, args);
	}

}
