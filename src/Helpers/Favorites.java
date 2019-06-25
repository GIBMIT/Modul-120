package Helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Favorites {


    //Creates xml file with own structure
    public void createFavoritefile(String ebdid){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Favorites");
            doc.appendChild(rootElement);

            Element edbid = doc.createElement("EDB-ID");
            edbid.appendChild(doc.createTextNode(ebdid));
            rootElement.appendChild(edbid);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("lib\\favorites.properties"));

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (Exception e) {
           System.out.println("Error creating xml! " + e);
        }
    }

    //Adds a new 'favorite' node to the favorite file
    public void addFavoriteNode(String ebdid){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("lib\\favorites.properties");
            Node rootElement = doc.getFirstChild();
            Element newfav = doc.createElement("EBD-ID");
            newfav.appendChild(doc.createTextNode(ebdid));
            rootElement.appendChild(newfav);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("lib\\favorites.properties"));

            transformer.transform(source, result);

            System.out.println("File saved!");
            System.out.println("ID: " + ebdid);

        }
        catch(Exception e){
            System.out.println("Error overwriting xml! " + e);
        }
    }

    //Removes 'favorite' node if it matches the provided ebd-id
    public void removeFavoriteNode(String ebdid){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("lib\\favorites.properties");

            Node rootElement = doc.getFirstChild();
            NodeList favs = rootElement.getChildNodes();
            for (int i = 0; i < favs.getLength(); i++) {
                String favnode = favs.item(i).getTextContent();
                if(favnode.equals(ebdid)){
                    favs.item(i).getParentNode().removeChild(favs.item(i));
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("lib\\favorites.properties"));

            transformer.transform(source, result);

            System.out.println("File saved!");
            System.out.println("ID: " + ebdid);

        }
        catch(Exception e){
            System.out.println("Error overwriting xml! " + e);
        }
    }

    //Returns all favorites
    public List<String> getAllFavoriteNodes(){
        List<String> favorites = new ArrayList<>();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("lib\\favorites.properties");

            Node rootElement = doc.getFirstChild();
            NodeList favs = rootElement.getChildNodes();
            for (int i = 0; i < favs.getLength(); i++) {
                favorites.add(favs.item(i).getTextContent());
            }
        }
        catch(Exception e){
            System.out.println("Error reading xml!" + e);
        }
        return favorites;
    }

    //Checks if provided id is in favorite list
    public boolean isFavorite(String ebdid){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("lib\\favorites.properties");

            Node rootElement = doc.getFirstChild();
            NodeList favs = rootElement.getChildNodes();
            for (int i = 0; i < favs.getLength(); i++) {
                if(favs.item(i).getTextContent().equals(ebdid)){
                    return true;
                }
            }
        }
        catch(Exception e){
            System.out.println("Error reading xml!" + e);
        }
        return false;
    }

}
