package com.runix.xdvalidator.dtd;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.dtd.groups.ElementDef;
import com.runix.xdvalidator.dtd.groups.ElementDefBuilder;
import com.runix.xdvalidator.dtd.groups.PCElementDef;
import com.runix.xdvalidator.dtd.groups.attribute.AttributeDef;
import com.runix.xdvalidator.dtd.groups.attribute.AttributeDefBuilder;

public class DTDContext {

	private ElementDef root;

	private Map<String, ElementDef> childs;

	private AttributeDefBuilder builder;

	private ElementDefBuilder elementBuilder;

	public DTDContext() {
		this.childs = new HashMap<String, ElementDef>();
		this.builder = new AttributeDefBuilder();
		this.elementBuilder = new ElementDefBuilder();
	}

	public void addElement(String elementName, String type) {
		ElementDef validator = this.elementBuilder.build(elementName, type);
		if (root == null) {
			this.root = validator;
		} else {
			this.childs.put(elementName, validator);
		}
	}

	public void addAttribute(String elementName, String attributeName,
			String attributeType, String attributeValue) throws ValidationException {
		ElementDef validator = get(elementName);
		AttributeDef def = builder.build(attributeName, attributeType,
				attributeValue);
		if(def == null) {
			throw new ValidationException("Null argument for "+elementName+" "+attributeName+" "+attributeType+" "+attributeValue);
		}
		if(validator == null) {
			throw new ValidationException("The element "+elementName+" maybe it is declared after the attribute or missing");
		}
		validator.addAttribute(def);
	}

	public ElementDef mockElement(String name) {
		ElementDef base = get(name);
		return base.makeClone();
	}

	public ElementDef getRoot() {
		return this.root;
	}

	public ElementDef get(String name) {
		ElementDef def;
		if (name.equals("#PCDATA")) {
			def = new PCElementDef();
		} else if (root.getName().equals(name)) {
			def = root;
		} else {
			def = this.childs.get(name);
		}
		return def;
	}
}
