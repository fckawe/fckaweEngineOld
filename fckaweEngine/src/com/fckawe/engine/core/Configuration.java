package com.fckawe.engine.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Configuration {
	
	private String gameName;
	private int displayWidth = 1024;
	private int displayHeight = 768;
	private AspectRatio aspectRatio = AspectRatio.W4H3;
	private short screenScale = 1;
	private Locale locale = Locale.getDefault();
	
	public String getGameName() {
		return gameName;
	}
	
	public int getDisplayWidth() {
		return displayWidth;
	}
	
	public int getDisplayHeight() {
		return displayHeight;
	}
	
	public short getScreenScale() {
		return screenScale;
	}

	private enum AspectRatio {
		W4H3("4:3");
		private final String code;

		AspectRatio(final String code) {
			this.code = code;
		}

		private static AspectRatio getByCode(final String code) {
			AspectRatio aspectRatio = null;
			AspectRatio[] ars = AspectRatio.values();
			for (AspectRatio ar : ars) {
				if (code.equals(ar.code)) {
					aspectRatio = ar;
					break;
				}
			}
			return aspectRatio;
		}

		public String getCode() {
			return code;
		}
	};

	public Configuration() {
		Loader loader = new Loader();
		loader.load();
	}

	private class Loader extends DefaultHandler {

		private String elementValue;
		private boolean inElement;

		private boolean displayHeightCalculated;

		private Logger logger;
		
		private void load() {
			logger = Session.getSession().getConfigLogger();
			
			logger.info("Loading configuration...");
			InputStream inStream = Session.class
					.getResourceAsStream("/configuration.xml");
			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				parser.parse(inStream, this);
			} catch (FileNotFoundException e) {
				logger.error("The configuration file does not exist!", e);
			} catch (ParserConfigurationException e) {
				logger.error("Could not parse configuration file!", e);
			} catch (SAXException e) {
				logger.error("Could not parse configuration file!", e);
			} catch (IOException e) {
				logger.error("Could not load configuration file!", e);
			}
		}

		@Override
		public void characters(final char[] cBuf, final int offset,
				final int len) throws SAXException {
			if (inElement) {
				String str = new String(cBuf, offset, len);
				elementValue += str;
			}
		}

		@Override
		public void startElement(final String uri, final String localName,
				final String qName, final Attributes attributes)
				throws SAXException {
			XmlElement element = XmlElement.getByName(qName);
			if (element != null) {
				switch (element) {
				case GAME:
					takeGameAttributes(attributes);
					break;
				case DISPLAY:
					takeDisplayAttributes(attributes);
					break;
				case LOCALIZATION:
					takeLocalizationAttributes(attributes);
					break;
				default:
					inElement = true;
					break;
				}
			}
		}
		
		private void takeGameAttributes(final Attributes attributes) {
			for (int i = 0; i < attributes.getLength(); i++) {
				String qName = attributes.getQName(i);
				XmlAttribute attr = XmlAttribute.getByName(qName);
				switch (attr) {
				case NAME:
					gameName = attributes.getValue(qName);
					logger.info(" Game: " + gameName);
					break;
				}
			}
		}

		private void takeDisplayAttributes(final Attributes attributes) {
			for (int i = 0; i < attributes.getLength(); i++) {
				String qName = attributes.getQName(i);
				XmlAttribute attr = XmlAttribute.getByName(qName);
				switch (attr) {
				case WIDTH:
					displayWidth = getIntAttribute(attributes, qName);
					logger.info(" Display width: " + displayWidth + "px");
					getHeightByWidthAndAspectRatio();
					break;
				case ASPECT_RATIO:
					String str = attributes.getValue(qName);
					aspectRatio = AspectRatio.getByCode(str);
					logger.info(" Aspect ratio: " + aspectRatio.getCode());
					getHeightByWidthAndAspectRatio();
					break;
				case SCALE:
					screenScale = getShortAttribute(attributes, qName);
					logger.info(" Screen scale: " + screenScale);
					break;
				}
			}
		}

		private void takeLocalizationAttributes(final Attributes attributes) {
			for (int i = 0; i < attributes.getLength(); i++) {
				String qName = attributes.getQName(i);
				XmlAttribute attr = XmlAttribute.getByName(qName);
				switch (attr) {
				case LOCALE:
					String loc = attributes.getValue(qName);
					if (loc.equals("DEFAULT")) {
						locale = Locale.getDefault();
						logger.info(" Locale: " + locale.toString());
					} else {
						String[] parts = loc.indexOf('-') >= 0 ? loc
								.split("-") : loc.split("_");
						String language = parts[0];
						boolean languageFound = false;
						String country = parts[1];
						boolean countryFound = false;
						for (Locale l : Locale.getAvailableLocales()) {
							if (!languageFound
									&& l.getLanguage().equals(language)) {
								languageFound = true;
							}
							if (!countryFound && l.getCountry().equals(country)) {
								countryFound = true;
							}
							if (languageFound && countryFound) {
								break;
							}
						}
						if (!languageFound) {
							logger.error(
									" Language '{}' for locale not found!",
									language);
						}
						if (!countryFound) {
							logger.warn(
									" Country '{}' for locale not found!",
									country);
						}
						locale = new Locale(parts[0], parts[1]);
					}
					break;
				}
			}
		}

		private void getHeightByWidthAndAspectRatio() {
			if (!displayHeightCalculated && displayWidth > 0
					&& aspectRatio != null) {
				switch (aspectRatio) {
				case W4H3:
					displayHeight = displayWidth * 3 / 4;
					break;
				default:
					logger.error("Unexpected aspect ratio: " + aspectRatio);
				}
				logger.info(" Display height: " + displayHeight + "px");
				displayHeightCalculated = true;
			}
		}
		
		private int getIntAttribute(final Attributes attributes,
				final String qName) {
			String str = attributes.getValue(qName);
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException e) {
				return 0;
			}
		}

		private short getShortAttribute(final Attributes attributes,
				final String qName) {
			String str = attributes.getValue(qName);
			try {
				return Short.parseShort(str);
			} catch (NumberFormatException e) {
				return 0;
			}
		}

		@Override
		public void endElement(final String uri, final String localName,
				final String qName) throws SAXException {
			XmlElement element = XmlElement.getByName(qName);

			if (element != null) {
				// nothing to do, yet
			}

			elementValue = "";
			inElement = false;
		}

	}

	private enum XmlElement {
		GAME("game"), DISPLAY("display"), LOCALIZATION("localization");

		private String name;

		private XmlElement(final String name) {
			this.name = name;
		}

		private static XmlElement getByName(final String name) {
			XmlElement[] elements = XmlElement.values();
			for (XmlElement element : elements) {
				if (name.equals(element.name)) {
					return element;
				}
			}
			return null;
		}
	}

	private enum XmlAttribute {
		NAME("name"), WIDTH("width"), ASPECT_RATIO("aspectRatio"),
			SCALE("scale"), LOCALE("locale");

		private String name;

		private XmlAttribute(final String name) {
			this.name = name;
		}

		private static XmlAttribute getByName(final String name) {
			XmlAttribute[] attrs = XmlAttribute.values();
			for (XmlAttribute attr : attrs) {
				if (name.equals(attr.name)) {
					return attr;
				}
			}
			return null;
		}
	}

}
