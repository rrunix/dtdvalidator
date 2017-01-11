package com.runix.xdvalidator.dtd.groups.attribute;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.dtd.ValidationContext;

public class AttributeIdRefDef extends AttributeDef {

	public AttributeIdRefDef(AttributeValueType type, String defaultValue,
			String name) {
		super(type, defaultValue, name);
	}

	public void validate(ValidationContext context) throws ValidationException {
		super.validate(context);
		if (this.value != null) {
			this.value = this.value.trim();
			if (context.getId(this.value) == null) {
				throw new ValidationException("Id : :" + this.value
						+ ": not found; required in " + this.name);
			}
		}
	}
}
