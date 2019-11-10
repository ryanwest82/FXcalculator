package com.rwest.fxcalculator;

import com.rwest.fxcalculator.config.ApplicationConfig;
import com.rwest.fxcalculator.domain.ConversionRequest;
import com.rwest.fxcalculator.exceptions.CurrencyNotFoundException;
import com.rwest.fxcalculator.exceptions.CurrencyRefreshException;
import com.rwest.fxcalculator.exceptions.InvalidInputException;
import com.rwest.fxcalculator.exceptions.RateNotFoundException;
import com.rwest.fxcalculator.exceptions.RateRefreshException;
import com.rwest.fxcalculator.service.InputParser;
import com.rwest.fxcalculator.service.InputParserImpl;
import com.rwest.fxcalculator.service.currency.CurrencyConverter;
import com.rwest.fxcalculator.service.currency.CurrencyConverterImpl;
import com.rwest.fxcalculator.service.currency.CurrencyLookup;
import com.rwest.fxcalculator.service.currency.CurrencyLookupImpl;
import com.rwest.fxcalculator.service.currency.CurrencyService;
import com.rwest.fxcalculator.service.currency.FileBasedCurrencyService;
import com.rwest.fxcalculator.service.rate.FileBasedRateService;
import com.rwest.fxcalculator.service.rate.RateLookup;
import com.rwest.fxcalculator.service.rate.RateLookupImpl;
import com.rwest.fxcalculator.service.rate.RateService;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application class. Starts up the application and handles console input.
 */
public class Application {

	private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {

    	RateLookup rateLookup = null;
    	CurrencyLookup currencyLookup = null;

    	try {
			ApplicationConfig.loadConfig();
			CurrencyService currencyService = new FileBasedCurrencyService();
			currencyLookup = new CurrencyLookupImpl(currencyService);
			RateService rateService = new FileBasedRateService(currencyLookup);
			rateLookup = new RateLookupImpl(rateService);
		} catch (CurrencyRefreshException | RateRefreshException | CurrencyNotFoundException | IOException e) {
    		LOGGER.log(Level.SEVERE, "Application startup failure", e);
			System.exit(1);
		}

		CurrencyConverter converter = new CurrencyConverterImpl(rateLookup);
    	InputParser parser = new InputParserImpl(currencyLookup);
	    Scanner scanner = new Scanner(System.in);

	    printWelcomeMessage();

	    boolean exit = false;

	    while(!exit) {
	        String input = scanner.nextLine();

	        switch(input) {
				case "QUIT":
					exit = true;
					break;
				case "SHOW CURRENCIES" :
					currencyLookup.getCurrencyList().forEach(System.out::println);
					break;
				case "SHOW RATES" :
					rateLookup.getRatesList().forEach(System.out::println);
					break;
				default:
					try {
						ConversionRequest spec = parser.parse(input);
						System.out.println(converter.convert(spec));

					} catch (RateNotFoundException | InvalidInputException | CurrencyNotFoundException e) {
						System.out.println(e.getMessage());
					}
					break;
			}
        }
    }

    private static void printWelcomeMessage() {
		System.out.println("Welcome to the FX calculator\n");
		System.out.println(InvalidInputException.FORMAT);
		System.out.println("Rates older than " + ApplicationConfig.getRefreshInterval()
				+ "ms will be refreshed before conversion takes place\n");
		System.out.println("Type \'SHOW CURRENCIES\' to view available currencies");
		System.out.println("Type \'SHOW RATES\' to view available rates");
		System.out.println("Type \'QUIT\' to exit\n");
	}
}
