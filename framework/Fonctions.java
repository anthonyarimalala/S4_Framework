package etu2033.framework.utils;

import etu2033.framework.Mapping;
import java.util.*;
import java.net.URL;

import java.io.*;
import java.lang.reflect.Method;

import etu2033.framework.servlet.ModelView;
import etu2033.annotation.url;
import jakarta.servlet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;


public class Fonctions{

    // SPRINT 5
    // recuperation valeur de retour et dispatcher
    public static void recup_et_dispatch(HashMap<String, Mapping> mappingUrls,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out =response.getWriter();

        // sprint5
        String stringUri = request.getRequestURI();
        String[] arrayPath = stringUri.split("/");
        String cle = arrayPath[arrayPath.length - 1];

        try{
            Mapping mapping = new Mapping();
            for(String key : mappingUrls.keySet()){
                if(key.equals(cle)){
                    mapping = mappingUrls.get(key);
                    break;
                }
            }
            if(!mapping.getClassName().equals("default")){
                // out.println("tsy null tsony ilay mapping");
                String nomMethode = cle;
                String nomDeClasse = (String) mapping.getClassName();
                java.lang.Class cl = java.lang.Class.forName(nomDeClasse);
                Object object = cl.newInstance();
                String method = (String) mapping.getMethod();
                Method methode = object.getClass().getDeclaredMethod(method);
                ModelView retour = (ModelView) methode.invoke(object);

            // sprint 6
                // sprint 6: maka object amle modelView
                HashMap<String, Object> data = retour.getData();
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    String key = entry.getKey();
                    Object valeurObjet = entry.getValue();

                    request.setAttribute(key, convertToObjectPrimitive(valeurObjet));
                }

                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/"+retour.getUrl());
                requestDispatcher.forward(request, response);

            }else{
                // out.println("Null ilay mapping ehh");
            }

        } catch(Exception e){
            e.printStackTrace(out);
        }

    }
    public static Object convertToObjectPrimitive(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        } else if (obj instanceof Double) {
            return ((Double) obj).doubleValue();
        } else if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        } else if (obj instanceof Date) {
            Date sqlDate = (Date) obj;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sqlDate);
            return calendar.getTime();
        }
        
        // Si le type de l'objet n'est pas pris en charge, vous pouvez retourner null ou une valeur par défaut.
        return null;
    }

    // SPRINT 3
    public static HashMap<String,Mapping> mameno_HashMap(HashMap<String,Mapping> mappingUrls, String packageName) throws Exception{
        try {
        mappingUrls = new HashMap<String, Mapping>();
        URL root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "//")); 
            for (File file : new File(root.getFile().replaceAll("%20", " ")).listFiles()) {
                if (file.getName().contains(".class")) {
                    String className = file.getName().replaceAll(".class$", "");
                    Class<?> cls = Class.forName(packageName + "." + className);
                    for (Method method : cls.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(url.class)) {
                            mappingUrls.put(method.getAnnotation(url.class).value(), new Mapping(cls.getName(), method.getName()));
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
        return mappingUrls;
    }

    
}