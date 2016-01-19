package com.marklogic.spring.batch.geonames;

import com.marklogic.client.io.DOMHandle;
import com.marklogic.spring.batch.geonames.data.Geoname;

import javax.xml.parsers.DocumentBuilder;

import org.springframework.batch.item.ItemProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GeonamesItemProcessor implements ItemProcessor<Geoname, Document> {
	
	private final String GEO = "http://geonames.org";

	@Override
	public Document process(Geoname item) throws Exception {
		DOMHandle handle = new DOMHandle();
		DocumentBuilder builder = handle.getFactory().newDocumentBuilder();
		Document doc = builder.newDocument();
		
		//Set document URI
		doc.setDocumentURI("http://geonames.org/geoname/" + item.getId());
		
		Element name = doc.createElementNS(GEO,  "name");
		name.appendChild(doc.createTextNode(item.getName()));
		
		Element id = doc.createElementNS(GEO, "id");
		id.appendChild(doc.createTextNode(item.getId()));
		
		Element asciiName = doc.createElementNS(GEO, "ascii-name");
		asciiName.appendChild(doc.createTextNode(item.getAsciiName()));
		
		Element altNames = doc.createElementNS(GEO, "alternate-names");
		for (String altName : item.getAlternateNames()) {
			Element elName = doc.createElementNS(GEO, "name");
			elName.appendChild(doc.createTextNode(altName));
			altNames.appendChild(elName);			
		}
		
		//Create root element and children
		Element root = doc.createElementNS("http://geonames.org", "geoname");
		doc.appendChild(root);
		root.appendChild(id);
		root.appendChild(name);
		root.appendChild(asciiName);
		root.appendChild(altNames);
		
		return doc;
	}

}