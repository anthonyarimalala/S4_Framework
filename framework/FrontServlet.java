package etu2033.framework.servlet;

import etu2033.framework.Mapping;
import etu2033.framework.utils.Fonctions;
import etu2033.annotation.url;

import jakarta.servlet.RequestDispatcher;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.io.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.ServletException;

public class FrontServlet extends HttpServlet{

    HashMap<String, Mapping> mappingUrls;
    String urll = "";

    public void init() throws ServletException {
        
        try {
            String packageName = getServletContext().getInitParameter("packageName");
            
            // sprint3
            mappingUrls = Fonctions.mameno_HashMap(mappingUrls, packageName);
        
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        Fonctions.recup_et_dispatch(mappingUrls, request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
        String stringUri = request.getRequestURI();
        PrintWriter out = response.getWriter();
        String packageName = getServletContext().getInitParameter("packageName");
        URL root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "//")); 
        out.println("<br>");  
        out.println("Front Servlet");
        out.println("<br>");  
        out.println("URL: " + stringUri);  
        out.println("<br>"); 
        out.println("Root: " + root);
        out.println("<br>"); 
        out.println("URLL: " + urll);
        out.println("<br>"); 
        for(String key : mappingUrls.keySet()){
            Mapping mapping = mappingUrls.get(key);
            out.println("Cle: " + key + ", ClassName: "+ mapping.getClassName() + ", Mapping: " + mapping.getMethod());
            out.println("<br>"); 
        }
        out.println("Vita");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }


}

