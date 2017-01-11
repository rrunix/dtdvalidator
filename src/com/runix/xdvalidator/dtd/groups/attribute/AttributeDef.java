package com.runix.xdvalidator.dtd.groups.attribute;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.dtd.ValidationContext;

public class AttributeDef implements Cloneable {

	protected AttributeValueType type;

	protected String value;

	protected String defaultValue;

	protected String name;

	public AttributeDef(AttributeValueType type, String defaultValue,
			String name) {
		this.type = type;
		this.name = name;
		this.defaultValue = defaultValue;
		if (this.defaultValue != null) {
			this.defaultValue = this.defaultValue.trim();
			this.defaultValue = this.defaultValue.substring(1,
					this.defaultValue.length() - 1);
		}
		if (type == AttributeValueType.DEFAULT) {
			this.value = this.defaultValue;
		}
	}

	public void validate(ValidationContext context) throws ValidationException {
		if (type == AttributeValueType.REQUIRED) {
			if (value == null) {
				throw new ValidationException("Attribute " + name
						+ " is required\n");
			}
		} else if (type == AttributeValueType.FIXED) {
			if (value == null) {
				throw new ValidationException("Attribute " + name
						+ " value is fixed should be " + defaultValue + "\n");
			} else if (!value.equals(defaultValue)) {
				throw new ValidationException("Attribute " + name
						+ " value is fixed should be " + defaultValue + "\n");
			}
		}
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public AttributeDef makeClone() {
		try {
			return (AttributeDef) this.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public void preValidate(ValidationContext context)
			throws ValidationException {

	}
}
