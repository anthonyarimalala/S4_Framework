package etu2033.framework.utils;

import etu2033.framework.Mapping;
import java.util.*;
import java.net.URL;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;


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
                Method methode = obtenirMethode(object, method);

            // SPRINT-8
                Enumeration<String> paramNames_1 = request.getParameterNames();
                HashMap<String, Object[]> parametres = new HashMap<>();
                while (paramNames_1.hasMoreElements()) {
                    String paramName = paramNames_1.nextElement();
                    Object[] paramValues = request.getParameterValues(paramName);
                    parametres.put(paramName, paramValues);
                }
                ModelView retour = (ModelView) setMethodsParameters(parametres, methode, object);
                // ModelView retour = (ModelView) methode.invoke(object);


            // SPRINT-6 et SPRINT-7
                HashMap<String, Object> data = retour.getData();
                Enumeration<String> paramNames = request.getParameterNames();


            // sprint 7: recuperation donnée formulaire
                // raha tsy atao an'ito tonga de fafan'le data fotsiny le data anaty modelview
                while (paramNames.hasMoreElements()) {
                    String paramName = paramNames.nextElement();
                    Object paramValue = request.getParameter(paramName);

                    if (data.containsKey(paramName)) {
                        data.put(paramName, paramValue);
                        retour.addItem(paramName, paramValue);
                    }
                }
                

/////////////////////Eto apina an'ito ndray ModelView retour = (ModelView) methode.invoke(object); raha misy parametre ilay fonction  // 
            
            // sprint 6: envoyer des donnees recuperables dans jsp
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

    // SPRINT-8: manisy parametre am fonction
   public static Object setMethodsParameters(HashMap<String, Object[]> parametres, Method methode, Object objet) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Parameter[] parametresfonction = methode.getParameters();
        Object[] values = new Object[parametresfonction.length];

        // Création du tableau de noms de paramètres
        String[] parametersclasses = new String[parametresfonction.length];
        // Parcours des paramètres et récupération des noms
        for (int i = 0; i < parametresfonction.length; i++) {
            parametersclasses[i] = parametresfonction[i].getName();
        }
        
        for (Map.Entry<String, Object[]> entry : parametres.entrySet()) { //pour chaque clé / clé 
            String key = entry.getKey();
            for(int i = 0; i < parametersclasses.length; i++){
                if(parametersclasses[i].equals(key)){
                    Object[] params = parametres.get(key);
                    if(params.length == 1){
                        values[i] = convertToPrimitive(params[0], parametresfonction[i].getType());
                        i += 1;
                    }
                    else if(params.length > 1){
                        int[] array_object_to_set = new int[params.length];
                        int j = 0;
                        for(Object p : params){
                            System.out.println("p: "+p);
                            array_object_to_set[j] = Integer.parseInt((String) p);//Utile.convertToPrimitive(p, int.class);
                            j += 1;
                        }
                        values[i] = array_object_to_set;
                        i += 1;
                    }

                }
            }
        }
        Object retour = null;
        retour = methode.invoke(objet, values);
        return retour;
   }

    public static Method obtenirMethode(Object object, String nomMethode){
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if(method.getName().equals(nomMethode)){
                return method;
            }
        }
        return null;
    }

    private static Object convertToPrimitive(Object value, Class<?> type) {
        if (type.equals(byte.class)) {
            return Byte.valueOf(value.toString());
        } else if (type.equals(short.class)) {
            return Short.valueOf(value.toString());
        } else if (type.equals(int.class)) {
            return Integer.valueOf(value.toString());
        } else if (type.equals(long.class)) {
            return Long.valueOf(value.toString());
        } else if (type.equals(float.class)) {
            return Float.valueOf(value.toString());
        } else if (type.equals(double.class)) {
            return Double.valueOf(value.toString());
        } else if (type.equals(boolean.class)) {
            return Boolean.valueOf(value.toString());
        } else if (type.equals(char.class)) {
            return value.toString().charAt(0);
        } else {
            throw new IllegalArgumentException("Type non supporté : " + type.getName());
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