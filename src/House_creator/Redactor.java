/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package House_creator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author hass-pc
 */
public class Redactor {
    private Main main;
    Redactor (Main main){
        this.main = main;
    }
    
    public void Asignar() {
        try {
            File file = new File(main.NombreCasa.getText()+".txt");
            boolean p = file.createNewFile();
            if (!p)
             p = (JOptionPane.showConfirmDialog(main, "Desea Sobreescribir el archivo?") == JOptionPane.YES_OPTION);
            if (p) {
                PrintWriter writer = new PrintWriter(new FileWriter(file));
                for (int i = 0; i < main.Habitaciones.size(); i++) {
                    String data = main.Habitaciones.get(i);
                    writer.println(data);
                }
                writer.close();
            }
            
            File s_file = new File(main.NombreCasa.getText()+"_sensores.xml");
            p = file.createNewFile();
            if (!p)
             p = (JOptionPane.showConfirmDialog(main, "Desea Sobreescribir el archivo?") == JOptionPane.YES_OPTION);
            if (p) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                doc.setXmlStandalone(true);
                Element rootElement = doc.createElement("Sensores-y-Eventos");
                doc.appendChild(rootElement);
                Element sensores = doc.createElement("Sensores");
                sensores.setAttribute("Size", main.Sensores.size()+"");
                for (int i = 0; i < main.Sensores.size(); i++) {
                    Element sensor = doc.createElement("Sensor");
                    String data[] = main.Sensores.get(i).split(";");
                    sensor.setAttribute("id", data[0]);
                    Element s1 = doc.createElement("Ubicacion");
                    s1.setTextContent(data[1]);
                    sensor.appendChild(s1);
                    s1 = doc.createElement("Tipo");
                    s1.setTextContent(data[2]);
                    sensor.appendChild(s1);
                    s1 = doc.createElement("Fecha");
                    s1.setTextContent(data[3]);
                    sensor.appendChild(s1);
                    sensores.appendChild(sensor);
                }
                rootElement.appendChild(sensores);
                Element eventos = doc.createElement("Eventos");
                eventos.setAttribute("Size", main.Eventos.size()+"");
                for (int i = 0; i < main.Eventos.size(); i++) {
                    Element evento = doc.createElement("Evento");
                    String data[] = main.Eventos.get(i).split(";");
                    evento.setAttribute("nombre", data[0]);
                    Element s1 = doc.createElement("Tipo-Sensor");
                    s1.setTextContent(data[1]);
                    evento.appendChild(s1);
                    s1 = doc.createElement("Valor");
                    s1.setTextContent(data[2]);
                    evento.appendChild(s1);
                    s1 = doc.createElement("Unidad-de-Medida");
                    s1.setTextContent(data[3]);
                    evento.appendChild(s1);
                    eventos.appendChild(evento);
                }
                rootElement.appendChild(eventos);
                
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result =  new StreamResult(s_file);
                transformer.transform(source, result);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Redactor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Redactor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Redactor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
