package com.runix.xdvalidator.dtd.groups.attribute;

public class AttributeDefBuilder {

	public AttributeDef build(String attributeName, String attributeType,
			String attributeValue) {
		String defaultValue = null;
		AttributeValueType type;
		attributeValue = attributeValue.trim();
		if (attributeValue.startsWith("#REQUIRED")) {
			type = AttributeValueType.REQUIRED;
		} else if (attributeValue.startsWith("#IMPLIED")) {
			type = AttributeValueType.IMPLIED;
		} else if (attributeValue.startsWith("#FIXED")) {
			type = AttributeValueType.FIXED;
			defaultValue = attributeValue
					.substring(attributeValue.indexOf(" "));
		} else {
			defaultValue = attributeValue;
			type = AttributeValueType.DEFAULT;
		}
		attributeType = attributeType.trim();
		if (attributeType.equals("CDATA")) {
			return new CDATAAttribute(type, defaultValue, attributeName);
		} else if (attributeType.startsWith("(")) {
			return new EnumeratedAttributeDef(type, defaultValue,
					attributeName, attributeType);
		} else if (attributeType.equals("ID")) {
			return new IDAttributeDef(type, defaultValue, attributeName);
		} else if (attributeType.equals("IDREF")) {
			return new AttributeIdRefDef(type, defaultValue, attributeName);
		} else if (attributeType.equals("IDREFS")) {
			return new AttributeIdRefsDef(type, defaultValue, attributeName);
		} else {
			return null;
		}

	}

}
