package com.runix.xdvalidator.dtd;

import java.util.StringTokenizer;

import javax.xml.bind.ValidationException;

public class DTDBuilder {

	private String dtd;

	private DTDContext context;

	public DTDBuilder(String dtd) throws ValidationException {
		this.dtd = dtd;
		this.dtd = this.dtd.replace("\n", " ");
		this.dtd = this.dtd.replace("\t", " ");
		this.dtd = this.dtd.replace("  ", " ");
		if (this.dtd.indexOf("[") != -1) {
			this.dtd = this.dtd.substring(this.dtd.indexOf("[") + 1,
					this.dtd.lastIndexOf("]"));
		} else {
			if(this.dtd.indexOf("]>") != -1) {
				throw new ValidationException("The dtd is malformed, it it's closed with ]> but no open with DOCTYPE name [");
			}
		}
		this.context = new DTDContext();
	}

	public void build() throws ValidationException {
		StringTokenizer tokenizer = new StringTokenizer(this.dtd, "<//>");
		while (tokenizer.hasMoreElements()) {
			String nextElement = tokenizer.nextToken().trim();
			if (nextElement.length() > 0) {
				if (nextElement.startsWith("!ELEMENT")) {
					this.buildElement(nextElement);
				} else if (nextElement.startsWith("!ATTLIST")) {
					this.buildAttribute(nextElement);
				} else {
					throw new ValidationException("On dtd; line " + nextElement
							+ " is not supported");
				}
			}
		}
	}

	private void buildElement(String token) throws ValidationException {
		StringTokenizer tokenizer = new StringTokenizer(token);
		tokenizer.nextToken();
		String name = tokenizer.nextToken();
		String type = tokenizer.nextToken();
		if (type.startsWith("(")) {
			while (tokenizer.hasMoreTokens()) {
				type += tokenizer.nextToken();
			}
		}
		this.context.addElement(name, type);
		if (tokenizer.hasMoreElements()) {
			throw new ValidationException("The line " + token
					+ " is not well-formed");
		}
	}

	private void checkNewElementAndThrow(int index, String[] array, String msg)
			throws ValidationException {
		if (array.length <= index) {
			throw new ValidationException(msg);
		}
	}

	private void buildAttribute(String token) throws ValidationException {
		String[] split = token.split("\\s+");
		String elementTarget = split[1];
		checkNewElementAndThrow(
				1,
				split,
				"Attribute format must be <!ATTLIST element-name attribute-name attribute-type attribute-value>");
		int i = 2;
		while (i < split.length) {
			String name = split[i];
			String attributeType;
			String attributeValue;
			i++;
			checkNewElementAndThrow(i, split, "Missing attribute type; target="
					+ elementTarget + " attribute " + name);
			attributeType = split[i];
			i++;
			if (attributeType.startsWith("(") && !attributeType.endsWith(")")) {
				while (i < split.length) {
					attributeType = attributeType += split[i];
					i++;
					if (attributeType.endsWith(")")) {
						break;
					}
				}
			}
			checkNewElementAndThrow(i, split,
					"Missing attribute value; target=" + elementTarget
							+ " attribute=" + name + " attributeType="
							+ attributeType);
			attributeValue = split[i];
			i++;
			if (attributeValue.startsWith("#FIXED")) {
				checkNewElementAndThrow(i, split,
						"Missing value for #FIXED; target=" + elementTarget
								+ " attribute " + name);
				attributeValue += " " + split[i];
				i++;
			}
			this.context.addAttribute(elementTarget, name, attributeType,
					attributeValue);
		}
	}

	public String getDtd() {
		return dtd;
	}

	public DTDContext getContext() {
		return context;
	}
}
