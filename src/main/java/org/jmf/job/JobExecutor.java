package org.jmf.job;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.jmf.file.FileService;
import org.jmf.vo.BookVO;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JobExecutor {

	private static final String propFileName = "config.properties";

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {

		ObjectMapper mapper = new ObjectMapper(); 
		Integer delay = 0;

		try {

			while(true) {

				try {
					// Read properties from file
					Integer numberBids = Integer.valueOf(FileService.getPropertyFromFile(propFileName, "number_bids"));
					Integer numberAsks = Integer.valueOf(FileService.getPropertyFromFile(propFileName, "number_asks"));
					delay = Integer.valueOf(FileService.getPropertyFromFile(propFileName, "delay"));					
					String url = FileService.getPropertyFromFile(propFileName, "url");
					
					// Connect to web service and get order book 
					BookVO book = mapper.readValue(new URL(url), BookVO.class);		
					
					// Write new line in bid and ask csv files
					addLine("bids.csv", book.getTimestamp(), Arrays.copyOfRange(book.getBids(), 0, numberBids));
					addLine("asks.csv", book.getTimestamp(), Arrays.copyOfRange(book.getAsks(), 0, numberAsks));	

				} catch (IOException ioe) {
					ioe.printStackTrace();
				}	

				Thread.sleep(delay);	
			}

		} catch (InterruptedException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * Add new line to ask/bid file
	 * 
	 * @param fileName
	 * @param timestamp
	 * @param pairs
	 * @throws IOException
	 */
	private static void addLine(String fileName, Long timestamp, Double[][] pairs) throws IOException {

		//LocalDate localDate = (new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		// int year  = localDate.getYear();
		// int month = localDate.getMonthValue();
		// int day   = localDate.getDayOfMonth();

		StringBuilder sb = new StringBuilder();		

		sb.append(String.valueOf(timestamp));

		for (Double[] pair : pairs) {
			sb.append(",").append(String.valueOf(pair[0])).append(",").append(String.valueOf(pair[1]));
		}

		FileService.addLine(year + "-" + month + "-" + day +  "_" + fileName, sb.toString());	
	}


}
