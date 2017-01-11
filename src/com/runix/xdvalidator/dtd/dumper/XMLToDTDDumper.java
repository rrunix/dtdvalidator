package com.runix.xdvalidator.dtd.dumper;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.dtd.DTDContext;
import com.runix.xdvalidator.dtd.ValidationContext;
import com.runix.xdvalidator.dtd.groups.ElementDef;
import com.runix.xdvalidator.xml.groups.Attribute;
import com.runix.xdvalidator.xml.groups.Element;

public class XMLToDTDDumper {

	public void build(Element root, DTDContext context) throws ValidationException {
		ElementDef def = makeDump(root, context);
		ValidationContext vContext = new ValidationContext();
		def.preValidation(vContext);
		def.validate(vContext);
	}
	
	private ElementDef makeDump(Element root, DTDContext context) throws ValidationException {
		ElementDef def_root = context.getRoot();
		if(!def_root.getName().equals(root.getElementName())) {
			throw new ValidationException("The root of the xml must be "+def_root.getName());
		}
		return makeDumpHelper(root, def_root, context);
	}
	
	private ElementDef makeDumpHelper(Element root, ElementDef actual,  DTDContext context) throws ValidationException{
		//Dumping attributes
		for(Attribute attribute : root.getAttributes()) {
			actual.setAttributeValue(attribute.getName(), attribute.getValue());
		}
		//Dumping elements
		for(Element element : root.getChilds()) {
			ElementDef newElement = context.mockElement(element.getElementName());
			if(element.wasClosedOnDef()) {
				newElement.markAsClosedOnDef();
			}
			actual.addChild(newElement);
			makeDumpHelper(element, newElement, context);
		}
		return actual;
	}
}
