package org.jmf.job;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.databind.DeserializationFeature;
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
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		// Read properties from file
		Integer numberBids = Integer.valueOf(FileService.getProperty(propFileName, "number_bids"));
		Integer numberAsks = Integer.valueOf(FileService.getProperty(propFileName, "number_asks"));
		Integer delay = Integer.valueOf(FileService.getProperty(propFileName, "delay"));
		String url = FileService.getProperty(propFileName, "url");

		if (url == null || numberAsks == null || numberAsks == null || delay == null) return;

		while(true) {
			try {
				// Connect to web service and get order book
				BookVO book = mapper.readValue(new URL(url), BookVO.class);
				// Write new line in bid and ask csv files
				addLine("bids.csv", book.getTimestamp(), Arrays.copyOfRange(book.getBids(), 0, numberBids));
				addLine("asks.csv", book.getTimestamp(), Arrays.copyOfRange(book.getAsks(), 0, numberAsks));
				Thread.sleep(delay);
			} catch (IOException | InterruptedException exc) {
				exc.printStackTrace();
			}
		}
	}

	/**
	 * Add new line to ask/bid file
	 * 
	 * @param fileType
	 * @param timestamp
	 * @param pairs
	 * @throws IOException
	 */
	private static void addLine(String fileType, Long timestamp, Double[][] pairs) throws IOException {

        Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		StringBuilder sb = new StringBuilder();

		String fileName = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-"
				+ cal.get(Calendar.DAY_OF_MONTH) +  "_" + fileType;

		// Compose line
		sb.append(String.valueOf(timestamp));
		for (Double[] pair : pairs) {
			sb.append(",").append(String.valueOf(pair[0])).append(",").append(String.valueOf(pair[1]));
		}

		FileService.addLine(fileName, sb.toString());
	}


}
