package com.runix.xdvalidator.dtd.groups.attribute;

import java.util.StringTokenizer;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.dtd.ValidationContext;

public class AttributeIdRefsDef extends AttributeDef {

	public AttributeIdRefsDef(AttributeValueType type, String defaultValue,
			String name) {
		super(type, defaultValue, name);
	}

	public void validate(ValidationContext context) throws ValidationException {
		super.validate(context);
		if (this.value != null) {
			StringTokenizer tokenizer = new StringTokenizer(this.value);
			while (tokenizer.hasMoreElements()) {
				String actual = tokenizer.nextToken();
				if (context.getId(actual) == null) {
					throw new ValidationException("Id : " + actual + "other refs "+this.value
							+ " not found; required in " + this.name);
				}
			}
		}
	}
}
