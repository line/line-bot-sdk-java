package com.example.bot.spring;

import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseEngine {
	String search(String text) throws Exception {
		String result = null;
		BufferedReader br = null;
		FileReader fr = null;
		InputStreamReader isr = null;
		try {
			//fr = new FileReader(KitchenSinkController.createUri(dbFile));
//			fr = new FileReader(FILENAME);
//   		    br = new BufferedReader(fr);
			isr = new InputStreamReader(
                    this.getClass().getResourceAsStream(FILENAME));
			br = new BufferedReader(isr);
			String sCurrentLine;
			
			while (result != null && (sCurrentLine = br.readLine()) != null) {
				String[] parts = sCurrentLine.split(":");
				log.info("Pair {} + {}", parts[0], parts[1]);
				if (text.toLowerCase().equals(parts[0].toLowerCase())) {
					result = parts[1];
					log.info("Result Found");
				}
			}
		} catch (IOException e) {
			log.info("IOException while reading file: {}", e.toString());
		} finally {
			try {
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
			} catch (IOException ex) {
				log.info("IOException while closing file: {}", ex.toString());
			}
		}
		if (result != null)
			return result;
		log.info("Result Not Found");
		throw new Exception("NOT FOUND");
    }
	
	DatabaseEngine() {
		 //Resource resource = resourceLoader.getResource("classpath:" + FILENAME);
		 
	}

	private final String FILENAME = "/static/database.txt";

}