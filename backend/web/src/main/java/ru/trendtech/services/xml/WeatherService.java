package ru.trendtech.services.xml;

import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 04.02.2015.
 */

@Service
public class WeatherService {

    private String resultTemperature = "";

    public String getTemperatureNow() {
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {

                boolean fact = false;
                boolean temperature = false;


                public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
                    ///LOGGER.info("Start Element :" + qName);
                    if (qName.equalsIgnoreCase("fact")) {
                        fact = true;
                    }
                    if (qName.equalsIgnoreCase("temperature")) {
                        temperature = true;
                    }
                }

                public void endElement(String uri, String localName, String qName) throws SAXException {
                    //LOGGER.info("End Element :" + qName);
                }



                public void characters(char ch[], int start, int length) throws SAXException {

                    if (temperature) {
                        if(fact){
                            //LOGGER.info("Temperature Novosib : " + new String(ch, start, length));
                            resultTemperature = new String(ch, start, length);
                            temperature = false;
                            fact = false;
                              return;
                        }
                    }
                }
            };
            saxParser.parse("http://export.yandex.ru/weather-ng/forecasts/29634.xml", handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultTemperature;
    }
}
