package com.runix.xdvalidator.dtd.groups.attribute;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.dtd.ValidationContext;

public class IDAttributeDef extends AttributeDef {

	public IDAttributeDef(AttributeValueType type, String defaultValue,
			String name) {
		super(type, defaultValue, name);
	}

	public void preValidate(ValidationContext context)
			throws ValidationException {
		super.preValidate(context);
		if (this.value != null) {
			context.addId(this.value.trim(), this.value);
		}
	}
}
