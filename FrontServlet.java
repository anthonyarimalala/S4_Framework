package etu2033.framework.servlet;

import etu2033.framework.Mapping;
import etu2033.framework.utils.Fonctions;

import java.util.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.ServletException;

public class FrontServlet extends HttpServlet{

    HashMap<String, Mapping> mappingUrls;

    public void init() throws ServletException {
        String packageName = getServletContext().getInitParameter("packageName");
        try{
            Fonctions.traitementMappingUrls(this.mappingUrls, packageName);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}