/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package House_creator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author hass-pc
 */
public class Lector {
    private Main main;
    Lector (Main main){
        this.main = main;
    }
    
    public void Leer()
    {
        //Ejemplo Lectura de txt normal
        File file = new File(main.NombreCasa.getText()+".txt");
        File s_file = new File(main.NombreCasa.getText()+"_sensores.xml");
        if (file.exists()) {
            BufferedReader br=null;
            try {
                br = new BufferedReader(new FileReader(file));
                String line="";
                while ((line = br.readLine()) != null) 
                {
                    main.Habitaciones.addElement(line); //esto cambia segun su implementacion
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (br!=null)
                try {
                    br.close(); // SIEMPRE, cerrar el archivo.
            } catch (IOException ex) {
                Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (s_file.exists()) {
            try {
                //Leer un archivo xml
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(s_file); // s_file seria la ruta del archivo
                doc.getDocumentElement().normalize();
                NodeList sensores = doc.getElementsByTagName("Sensor"); // vector con todas las aparciciones de la etiqueta "Sensor"
                for (int i = 0; i < sensores.getLength(); i++) {
                    Node sensor = sensores.item(i); // Se obtiene el primer sensor
                    String ss=""; //Los datos se juntan en un solo string seguido de ;, esto cambia en su implementación
                    if (sensor.getNodeType() == Node.ELEMENT_NODE) { //Se extraen los datos
                        Element ele = (Element)sensor;
                        ss+=ele.getAttribute("id")+";";
                        ss+=ele.getElementsByTagName("Ubicacion").item(0).getTextContent()+";";
                        ss+=ele.getElementsByTagName("Tipo").item(0).getTextContent()+";";
                        ss+=ele.getElementsByTagName("Fecha").item(0).getTextContent();
                    }
                    main.Sensores.addElement(ss); //esto cambia segun su implementacion
                }
                NodeList eventos = doc.getElementsByTagName("Evento");
                for (int i = 0; i < eventos.getLength(); i++) {
                    Node evento = eventos.item(i);
                    String ss=""; //Los datos se juntan en un solo string seguido de ;, esto cambia en su implementación
                    if (evento.getNodeType() == Node.ELEMENT_NODE) {
                        Element ele = (Element)evento;
                        ss+=ele.getAttribute("nombre")+";";
                        ss+=ele.getElementsByTagName("Tipo-Sensor").item(0).getTextContent()+";";
                        ss+=ele.getElementsByTagName("Valor").item(0).getTextContent()+";";
                        ss+=ele.getElementsByTagName("Unidad-de-Medida").item(0).getTextContent();
                    }
                    main.Eventos.addElement(ss); //esto cambia segun su implementacion
                }
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
