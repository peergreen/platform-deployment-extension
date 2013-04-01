package com.peergreen.deployment.configadmin.jonas.processor.parser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.EventReaderDelegate;

/**
 * User: guillaume
 * Date: 27/03/13
 * Time: 22:25
 */
public class ScopedXMLEventReader extends EventReaderDelegate {

    private int depth = 0;

    public ScopedXMLEventReader(XMLEventReader reader) {
        super(reader);
    }

    @Override
    public XMLEvent nextEvent() throws XMLStreamException {
        XMLEvent event = super.nextEvent();
        switch (event.getEventType()) {
            case XMLEvent.START_ELEMENT:
                depth++;
                break;
            case XMLEvent.END_ELEMENT:
                depth--;
                break;
        }

        return event;
    }

    @Override
    public boolean hasNext() {
        if (depth == -1) {
            return false;
        }
        return super.hasNext();
    }

    @Override
    public XMLEvent nextTag() throws XMLStreamException {
        throw new XMLStreamException("nextTag() cannot be used with scoped XMLEventReader");
    }

    public void decrease() {
        depth--;
    }
}
