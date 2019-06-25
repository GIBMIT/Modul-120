package Helpers;

import Models.EdbEntry;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;
import java.sql.*;

public class CSV {


    //Parses query to properties and types
	public List[] parseQuery(String rawquery, boolean includeCaseSensitive, boolean excludeCaseSensitive){

        List[] out = new List[4];
	    //Splits at whitespace and merges with safe query
        String trimedquery = rawquery.trim();
        List<String> query = safifyquery(splitatwhitespace(trimedquery), autocompletequery(splitatwhitespace(trimedquery)));

        List<String> filtertypes = getfiltertypes(query);
        List<String> filterproperties = getfilterproperties(query);


        List<String> includefiltertypelist = new ArrayList<>();
        List<String> includefilterproperties = new ArrayList<>();
        List<String> excludefiltertypelist = new ArrayList<>();
        List<String> excludefilterproperties = new ArrayList<>();

        for (int i = 0; i < filterproperties.size(); i++) {
            if(!filtertypes.get(i).startsWith("-")) {
                if(!includeCaseSensitive) {
                    includefilterproperties.add(filterproperties.get(i).toLowerCase());
                }
                else {
                    includefilterproperties.add(filterproperties.get(i));
                }
                includefiltertypelist.add(filtertypes.get(i));
            }
            else{
                if(!excludeCaseSensitive) {
                    excludefilterproperties.add(filterproperties.get(i).toLowerCase());
                }
                else{
                    excludefilterproperties.add(filterproperties.get(i));
                }
                excludefiltertypelist.add(filtertypes.get(i).substring(1));
            }
        }



        out[0] = includefiltertypelist;
        out[1] = includefilterproperties;
        out[2] = excludefiltertypelist;
        out[3] = excludefilterproperties;

        return out;

    }



    //-----------------------Functions to make list safe---------------------------------------------//

    //Splits at whitespace (except when in quotes) and returns splited results in list
    private List<String> splitatwhitespace(String rawquery){

	    //Splits query @ space
        List<String> splitquery = new ArrayList<>();
        Matcher m1 = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(rawquery);
        while(m1.find()){
            splitquery.add(m1.group(1));
        }
        return splitquery;
    }

    //If a query property doesn't contain a type, it gets prepended with "notspecified"
    //NotSpecified gets handled as 'title' type
    private List<String> autocompletequery(List<String> splitquery){
        List<String> completedquery = new ArrayList<>();
        for (String queryelement:splitquery) {
            Matcher m4 = Pattern.compile("^((?!:).)*$").matcher(queryelement);
            while(m4.find()){
                completedquery.add("notspecified:" + m4.group(0) );
            }
        }
        return completedquery;
    }

    //Merges the safe query with the unsafe query, overwriting entries without filtertype
    private List<String> safifyquery(List<String> splitquery, List<String> completedquery){
        for(int i = 0; i < splitquery.size(); i++){
            for (String aCompletedquery : completedquery) {
                if (aCompletedquery.contains(splitquery.get(i))) {
                    splitquery.set(i, aCompletedquery);
                }
            }
        }
        return splitquery;
    }






    //-----------------------Functions to parse semantics-------------------------------------------//

    //Gets all filtertypes in a list(everything before ':')
    private List<String> getfiltertypes(List<String> splitquery){
        //Splits filtertype @ :
        List<String> filtertypes = new ArrayList<>();
        for (String queryelement:splitquery) {
            Matcher m2 = Pattern.compile("^[^:]+").matcher(queryelement);
            while(m2.find()){
                filtertypes.add(m2.group(0));
            }
        }
        return filtertypes;
    }

    //Gets all filterproperties in a list (everything after ':')
    private List<String> getfilterproperties(List<String> splitquery){
        List<String> filterproperties = new ArrayList<>();
        for (String queryelement:splitquery) {
            Matcher m3 = Pattern.compile(":(.*)").matcher(queryelement);
            while(m3.find()){
                filterproperties.add(m3.group(0).replace(":",""));
            }
        }
        return filterproperties;
    }






    //Builds SQL Queries based on wheter tight or loose comparison is used
    public String buildQuery(List<String>[] query, boolean isLoose){

        List<String> filtertypes = query[0];
        List<String> filterproperties = query[1];


        List<String> idl = new ArrayList<>();
        List<String> pll = new ArrayList<>();
        List<String> tpl = new ArrayList<>();
        List<String> til = new ArrayList<>();

        for(int i = 0; i < filtertypes.size();i++ ){
            String type = filtertypes.get(i);
            switch(type){
                case "id":
                    idl.add(filterproperties.get(i));
                    break;

                case "platform":
                    pll.add(filterproperties.get(i));
                    break;

                case "type":
                    tpl.add(filterproperties.get(i));
                    break;

                case "title":
                    til.add(filterproperties.get(i));
                    break;

                case "notspecified" :
                    til.add(filterproperties.get(i));
                    break;

                default: break;
            }


        }

        StringBuilder idstring = new StringBuilder();
        StringBuilder platformstring = new StringBuilder();
        StringBuilder typestring = new StringBuilder();
        StringBuilder titlestring = new StringBuilder();

        if(!isLoose) {
            if (idl.size() > 0) {
                idstring = new StringBuilder("id IN (");
                for (String id : idl) {
                    if (id.contains("\"")) {
                        id = id.replace("\"", "");
                    }
                    idstring.append("'").append(id).append("',");
                }
                idstring = new StringBuilder(idstring.substring(0, idstring.length() - 1));
                idstring.append(") ");
            } else {
                idstring = new StringBuilder();
            }

            if (pll.size() > 0) {
                platformstring = new StringBuilder("platform IN (");
                for (String platform : pll) {
                    if (platform.contains("\"")) {
                        platform = platform.replace("\"", "");
                    }
                    platformstring.append("'").append(platform).append("',");
                }
                platformstring = new StringBuilder(platformstring.substring(0, platformstring.length() - 1));
                platformstring.append(") ");
            } else {
                platformstring = new StringBuilder();
            }

            if (tpl.size() > 0) {
                typestring = new StringBuilder("type IN (");
                for (String type : tpl) {
                    if (type.contains("\"")) {
                        type = type.replace("\"", "");
                    }
                    typestring.append("'").append(type).append("',");
                }
                typestring = new StringBuilder(typestring.substring(0, typestring.length() - 1));
                typestring.append(") ");
            } else {
                typestring = new StringBuilder();
            }

            if (til.size() > 0) {
                titlestring = new StringBuilder("description IN (");
                for (String title : til) {
                    if (title.contains("\"")) {
                        title = title.replace("\"", "");
                    }
                    titlestring.append("'").append(title).append("',");
                }
                titlestring = new StringBuilder(titlestring.substring(0, titlestring.length() - 1));
                titlestring.append(") ");
            } else {
                titlestring = new StringBuilder();
            }

        }

        else{
            if(idl.size() > 0) {
                for (int i = 0; i < idl.size(); i++) {
                    String id = idl.get(i);
                    if (id.contains("\"")) {
                        id = id.replace("\"", "");
                    }
                    if (i < 1) {
                        idstring.append(" id LIKE \'%").append(id).append("%\'");
                    } else {
                        idstring.append(" OR id LIKE \'%").append(id).append("%\'");
                    }
                }
            }
            else{
                idstring = new StringBuilder();
            }

            if(pll.size() > 0) {
                for (int i = 0; i < pll.size(); i++) {
                    String platform = pll.get(i);
                    if (platform.contains("\"")) {
                        platform = platform.replace("\"", "");
                    }
                    if (i < 1) {
                        platformstring.append(" platform LIKE \'%").append(platform).append("%\'");
                    } else {
                        platformstring.append(" OR platform LIKE \'%").append(platform).append("%\'");
                    }
                }
            }
            else{
                platformstring = new StringBuilder();
            }


            if(tpl.size() > 0) {
                for (int i = 0; i < tpl.size(); i++) {
                    String type = tpl.get(i);
                    if (type.contains("\"")) {
                        type = type.replace("\"", "");
                    }
                    if (i < 1) {
                        typestring.append(" type LIKE \'%").append(type).append("%\'");
                    } else {
                        typestring.append(" OR type LIKE \'%").append(type).append("%\'");
                    }
                }
            }
            else{
                typestring = new StringBuilder();
            }


            if(til.size() > 0) {
                for (int i = 0; i < til.size(); i++) {
                    String title = til.get(i);
                    if (title.contains("\"")) {
                        title = title.replace("\"", "");
                    }
                    if (i < 1) {
                        titlestring.append(" description LIKE \'%").append(title).append("%\'");
                    } else {
                        titlestring.append(" OR description LIKE \'%").append(title).append("%\'");
                    }
                }
            }
            else{
                titlestring = new StringBuilder();
            }
        }

        List<String> propertystrings = new ArrayList<>();
        propertystrings.add(idstring.toString());
        propertystrings.add(platformstring.toString());
        propertystrings.add(typestring.toString());
        propertystrings.add(titlestring.toString());





        StringBuilder qry = new StringBuilder("SELECT * FROM files_exploits");
        if(idstring.length()> 0){
            qry.append(" WHERE ").append(idstring);
            propertystrings.remove(0);
            for (String prop:propertystrings) {if(prop.length() > 0){
                qry.append(" AND ").append(prop);} }
        }
        else if(platformstring.length() > 0){
            qry.append(" WHERE ").append(platformstring);
            propertystrings.remove(1);
            for (String prop:propertystrings) {if(prop.length() > 0){
                qry.append(" AND ").append(prop);} }
        }
        else if(typestring.length() > 0){
            qry.append(" WHERE ").append(typestring);
            propertystrings.remove(2);
            for (String prop:propertystrings) {if(prop.length() > 0){
                qry.append(" AND ").append(prop);} }
        }
        else if(titlestring.length() > 0){
            qry.append(" WHERE ").append(titlestring);
            propertystrings.remove(3);
            for (String prop:propertystrings) {if(prop.length() > 0){
                qry.append(" AND ").append(prop);} }
        }
        return qry.toString();
    }

    //Opens a new jdbc connection (using 3rd party library) and queries the csv based on my created sql query
    //Returns all results as EdbEntry objects
    public List<EdbEntry> search(String qry){


        List<EdbEntry> foundlist = new ArrayList<>();

        try {
            Class.forName("org.relique.jdbc.csv.CsvDriver");
        }
        catch(Exception e){
            System.out.println("No import possible!");
            return null;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:relique:csv:lib\\exploitdb");
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(qry);
            while(res.next()){
                EdbEntry en = new EdbEntry();
                en.setId(res.getString("id" ));
                en.setFile(res.getString("file"));
                en.setDate(res.getString("date"));
                en.setAuthor(res.getString("author"));
                en.setPort(res.getString("port"));
                en.setPlatform(res.getString("platform" ));
                en.setType(res.getString("type" ));
                en.setDescription(res.getString("description" ));
                foundlist.add(en);
            }


        }
        catch(Exception e){
            System.out.println("Could not connect to db/file" + e);
            return null;

        }


        //System.out.println(foundlist);
        return foundlist;


    }

    //Searches the csv by provided id
    public EdbEntry searchByID(String id){
        try {
            Class.forName("org.relique.jdbc.csv.CsvDriver");
        }
        catch(Exception e){
            System.out.println("No import possible!");
            return null;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:relique:csv:lib\\exploitdb");
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM files_exploits WHERE id = '" + id + "'");
            while(res.next()){
                EdbEntry en = new EdbEntry();
                en.setId(res.getString("id" ));
                en.setFile(res.getString("file"));
                en.setDate(res.getString("date"));
                en.setAuthor(res.getString("author"));
                en.setPort(res.getString("port"));
                en.setPlatform(res.getString("platform" ));
                en.setType(res.getString("type" ));
                en.setDescription(res.getString("description" ));
                return en;
            }
        }
        catch(Exception e){
            System.out.println("Could not connect to db/file" + e);
            return null;
        }
        return null;
    }


}
