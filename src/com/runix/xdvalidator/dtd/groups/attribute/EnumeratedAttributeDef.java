package com.runix.xdvalidator.dtd.groups.attribute;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.dtd.ValidationContext;

public class EnumeratedAttributeDef extends AttributeDef {

	private String enumeration;

	public EnumeratedAttributeDef(AttributeValueType type,
			String defaultValue, String name, String enumeration) {
		super(type, defaultValue, name);
		this.enumeration = enumeration;
	}

	public void validate(ValidationContext context) throws ValidationException {

	}
}
