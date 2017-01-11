package com.runix.xdvalidator.dtd.groups;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.dtd.ValidationContext;
import com.runix.xdvalidator.dtd.groups.attribute.AttributeDef;

public class ElementDef implements Cloneable {

	protected List<ElementDef> childs;

	protected List<AttributeDef> attributes;

	protected String name;

	protected boolean closeOnDef;

	public ElementDef(String name) {
		this.name = name;
		this.childs = new ArrayList<ElementDef>();
		this.attributes = new ArrayList<AttributeDef>();
		this.closeOnDef = false;
	}

	public String getName() {
		return this.name;
	}

	public void addAttribute(AttributeDef def) {
		this.attributes.add(def);
	}

	public void setAttributeValue(String attributeName, String value) {
		AttributeDef def = this.getAttribute(attributeName);
		if (def == null) {
			System.out.println("ATTS are");
			for(AttributeDef att : this.attributes) {
				System.out.println(att.getName());
			}
			throw new IllegalArgumentException("Attribute not found "+attributeName+" on "+this.getName());
		}
		def.setValue(value);
	}
	
	public void addChild(ElementDef def) {
		this.childs.add(def);
	}
	

	public void markAsClosedOnDef() {
		this.closeOnDef = true;
	}

	public void preValidation(ValidationContext context)
			throws ValidationException {
		for (ElementDef eDef : this.childs) {
			eDef.preValidation(context);
		}

		for (AttributeDef aDef : this.attributes) {
			aDef.preValidate(context);
		}
		this.preValidateThis(context);
	}

	public void preValidateThis(ValidationContext context)
			throws ValidationException {

	}

	public void validate(ValidationContext context) throws ValidationException {
		for (ElementDef eDef : this.childs) {
			eDef.validate(context);
		}

		for (AttributeDef aDef : this.attributes) {
			aDef.validate(context);
		}
		this.validateThis(context);
	}

	public ElementDef makeClone() {
		try {
			ElementDef clone =  (ElementDef) clone();
			List<AttributeDef> clones = new ArrayList<AttributeDef>();
			for(AttributeDef def : this.attributes) {
				clones.add(def.makeClone());
			}
			clone.attributes = clones;
			clone.childs = new ArrayList<ElementDef>();
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public void validateThis(ValidationContext context)
			throws ValidationException {

	}

	public AttributeDef getAttribute(String name) {
		for (AttributeDef def : this.attributes) {
			if (def.getName().equals(name)) {
				return def;
			}
		}
		return null;
	}
	
	public String toStringObject() {
		return "ref-> "+super.toString();
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[ childs = {");
		for(ElementDef def : this.childs) {
			builder.append(def.toString()+"\n");
		}
		builder.append("}");
		builder.append("attributes {");
		for(AttributeDef def : this.attributes) {
			builder.append(def.toString()+",");
		}
		builder.append("}\n");
		builder.append("my id="+super.toString()+"]");
		return builder.toString();
	}
}
