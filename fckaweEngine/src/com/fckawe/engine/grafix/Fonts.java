package com.fckawe.engine.grafix;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fckawe.engine.core.MainApplication;
import com.fckawe.engine.core.Session;

public class Fonts {

	public static final String DEFAULT_FONT = "DEFAULT";

	private final Map<String, Font> fonts = new HashMap<String, Font>();
	
	public void loadFonts() {
		new Loader(fonts);
	}

	public Font getFont(final String fontName) {
		Font font;
		if (fontName == null) {
			font = fonts.get(DEFAULT_FONT);
		} else {
			font = fonts.get(fontName.toUpperCase());
		}
		if (font == null) {
			Session.getSession().getGrafixLogger().error(
					"Font with name {} does not exist (or was not loaded successfully)!",
							fontName.toUpperCase());
			font = fonts.get(DEFAULT_FONT);
			fonts.put(fontName.toUpperCase(), font);
		}
		return font;
	}

	private static class Loader extends DefaultHandler {

		private final Map<String, Font> fonts;

		private String fontName;
		private short fontWidth;
		private short fontHeight;
		private short fontLetterSpacing;
		private short fontLineSpacing;
		private String fontFile;
		private List<String> fontChars;

		// private String elementValue;
		// private boolean inElement;

		private Loader(final Map<String, Font> fonts) throws RuntimeException {
			Session.getSession().getGrafixLogger().info("Loading fonts...");
			this.fonts = fonts;
			fonts.clear();
			String fontConfigFile = "/fonts/fonts.xml";
			try {
				InputStream inStream = MainApplication.class
						.getResourceAsStream(fontConfigFile);
				if (inStream == null) {
					throw new FileNotFoundException(fontConfigFile);
				}
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				parser.parse(inStream, this);
			} catch (FileNotFoundException e) {
				Session.getSession().getGrafixLogger().error("The font configuration file does not exist!", e);
			} catch (ParserConfigurationException e) {
				Session.getSession().getGrafixLogger().error("Could not parse fonts configuration file!", e);
			} catch (SAXException e) {
				Session.getSession().getGrafixLogger().error("Could not parse fonts configuration file!", e);
			} catch (IOException e) {
				Session.getSession().getGrafixLogger().error("Could not load fonts configuration file!", e);
			}
		}

		@Override
		public void characters(final char[] cBuf, final int offset,
				final int len) throws SAXException {
			// if (inElement) {
				// String str = new String(cBuf, offset, len);
				// elementValue += str;
			// }
		}

		@Override
		public void startElement(final String uri, final String localName,
				final String qName, final Attributes attributes)
				throws SAXException {
			XmlElement element = XmlElement.getByName(qName);
			if (element != null) {
				switch (element) {
				case FONTS:
					fonts.clear();
					break;
				case FONT:
					takeFontAttributes(attributes);
					fontChars = new ArrayList<String>();
					break;
				case BITMAP:
					takeBitmapAttributes(attributes);
					break;
				case LINE:
					takeLineAttributes(attributes);
					break;
				default:
					// inElement = true;
					break;
				}
			}
		}

		private void takeFontAttributes(final Attributes attributes) {
			fontName = null;
			fontWidth = 0;
			fontHeight = 0;
			fontLetterSpacing = Font.DEFAULT_LETTER_SPACING;
			fontLineSpacing = Font.DEFAULT_LINE_SPACING;
			for (int i = 0; i < attributes.getLength(); i++) {
				String qName = attributes.getQName(i);
				XmlAttribute attr = XmlAttribute.getByName(qName);
				switch (attr) {
				case NAME:
					fontName = attributes.getValue(qName).toUpperCase();
					break;
				case WIDTH:
					fontWidth = getShortAttribute(attributes, qName);
					break;
				case HEIGHT:
					fontHeight = getShortAttribute(attributes, qName);
					break;
				case LETTER_SPACING:
					fontLetterSpacing = getShortAttribute(attributes, qName);
					break;
				case LINE_SPACING:
					fontLineSpacing = getShortAttribute(attributes, qName);
					break;
				default:
					// nothing to do
					break;
				}
			}
		}

		private void takeBitmapAttributes(final Attributes attributes) {
			for (int i = 0; i < attributes.getLength(); i++) {
				String qName = attributes.getQName(i);
				XmlAttribute attr = XmlAttribute.getByName(qName);
				switch (attr) {
				case FILE:
					fontFile = attributes.getValue(qName);
					break;
				default:
					// nothing to do
					break;
				}
			}
		}

		private void takeLineAttributes(final Attributes attributes) {
			for (int i = 0; i < attributes.getLength(); i++) {
				String qName = attributes.getQName(i);
				XmlAttribute attr = XmlAttribute.getByName(qName);
				switch (attr) {
				case CHARS:
					String chars = attributes.getValue(qName);
					fontChars.add(chars);
					break;
				default:
					// nothing to do
					break;
				}
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
				switch (element) {
				case FONT:
					if (fontName == null || fontName.trim().length() == 0) {
						Session.getSession().getGrafixLogger().error(" No font name given!");
					} else if (fontWidth <= 0) {
						Session.getSession().getGrafixLogger().error(" Font {}: width must not be zero!", fontName);
					} else if (fontHeight <= 0) {
						Session.getSession().getGrafixLogger().error(" Font {}: height must not be zero!", fontName);
					} else if (fontChars.isEmpty()) {
						Session.getSession().getGrafixLogger().error(" Font {}: no chars specified!", fontName);
					} else {
						String[] chars = fontChars.toArray(new String[0]);
						Bitmap[][] bitmapData = Session.getSession().getBitmapsLogic().cut("/fonts/"
								+ fontFile, fontWidth, fontHeight);
						Font font = new Font(bitmapData, chars, fontWidth,
								fontHeight, fontLetterSpacing, fontLineSpacing);
						fonts.put(fontName, font);
						Session.getSession().getGrafixLogger().info(
								" Loaded font {} ({}x{})",
								new String[] { fontName,
										String.valueOf(fontWidth),
										String.valueOf(fontHeight) });
					}
					break;
				default:
					// nothing to do
					break;
				}
			}

			// elementValue = "";
			// inElement = false;
		}

		private enum XmlElement {
			FONTS("fonts"), FONT("font"), BITMAP("bitmap"), LINE("line");

			private String name;

			private XmlElement(final String name) {
				this.name = name;
			}

			private static XmlElement getByName(final String name) {
				XmlElement elementReturn = null;
				XmlElement[] elements = XmlElement.values();
				for (XmlElement element : elements) {
					if (name.equals(element.name)) {
						elementReturn = element;
						break;
					}
				}
				return elementReturn;
			}
		}

		private enum XmlAttribute {
			NAME("name"), FILE("file"), WIDTH("width"), HEIGHT("height"), LETTER_SPACING(
					"letterSpacing"), LINE_SPACING("lineSpacing"), CHARS(
					"chars");

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

}
