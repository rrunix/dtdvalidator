package com.runix.xdvalidator.dtd;

import java.util.HashMap;
import java.util.Map;

public class ValidationContext {

	private Map<String, String> pairs;

	public ValidationContext() {
		this.pairs = new HashMap<String, String>();
	}
	
	public void addId(String key, String value) {
		this.pairs.put(key,  value);
	}
	
	public String getId(String key) {
		return this.pairs.get(key);
	}
}
