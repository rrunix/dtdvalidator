package com.runix.xdvalidator.dtd.groups;

import java.util.StringTokenizer;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.dtd.ValidationContext;

public class ChildElementDef extends ElementDef {

	private String pattern;

	private String rawPattern;

	public ChildElementDef(String name, String pattern) {
		super(name);
		this.pattern = adapt(pattern);
		this.rawPattern = pattern;
	}

	public void validateThis(ValidationContext context)
			throws ValidationException {
		String xml = getChildsAsString();
		String childs = getChildsAsString();
		if (!xml.matches(this.pattern)) {
			throw new ValidationException("on element " + this.getName()
					+ " childrens don't match with the def [" + rawPattern
					+ "] current is [" + childs+"]");
		}
	}

	public String getChildsAsString() {
		StringBuilder builder = new StringBuilder();
		for (ElementDef child : this.childs) {
			builder.append(child.getName() + ",");
		}
		return builder.toString();
	}

	private static String adapt(String name) {
		String temp = name.replace(" ", "");
		temp = temp.replaceAll(",|\\+|\\*|\\)|\\(|\\[|\\]|\\|", " ");
		StringTokenizer tokenizer = new StringTokenizer(temp);
		String adapted = name.replace(" ", "");
		adapted = name.replace(",", " ");
		while (tokenizer.hasMoreElements()) {
			String actual = tokenizer.nextToken();
			adapted = adapted.replaceAll("([^a-zA-Z0-9-_])" + actual
					+ "([^a-zA-Z0-9-_])", "$1(" + actual + ",)$2");
		}
		adapted = adapted.replaceAll(" ", "");
		return "("+adapted+")";
	}
}
