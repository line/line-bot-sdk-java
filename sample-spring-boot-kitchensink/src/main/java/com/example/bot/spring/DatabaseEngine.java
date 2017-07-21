package com.example.bot.spring;

import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseEngine {
	String search(String text) throws Exception {
		String result = null;
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(KitchenSinkController.createUri(FILENAME));
   		    br = new BufferedReader(fr);
			String sCurrentLine;
			while (result != null && (sCurrentLine = br.readLine()) != null) {
				String[] parts = sCurrentLine.split(":");
				if (text.toLowerCase().equals(parts[0].toLowerCase())) {
					result = parts[1];
				}
			}
		} catch (IOException e) {
			log.info("IOException while reading file: {}", e.toString());
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				log.info("IOException while closing file: {}", ex.toString());
			}
		}
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
    }
	private final String FILENAME = "/static/database.txt";
}