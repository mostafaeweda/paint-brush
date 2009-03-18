package tagroba;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XML_Trial {

	public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter out = factory.createXMLStreamWriter(new FileOutputStream("sossa.xml"));
		out.writeStartElement("Contact");
		out.writeStartElement("Name");
		out.writeCharacters("Mostafa");
		out.writeEndElement();
		out.writeStartElement("Age");
		out.writeCharacters("18");
		out.writeEndElement();
		out.writeStartElement("Year");
		out.writeCharacters("1990");
		out.writeEndElement();
		out.writeEndElement();
		out.close();
	}
}
