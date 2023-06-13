package models;

import etu2033.annotation.url;
import etu2033.framework.servlet.ModelView;

public class Emp{
    String nom;
    String prenom;

    @url(value="emp-nomComplet")
    public ModelView getNomComplet(){
        ModelView modelView = new ModelView();
        modelView.setUrl("nomComplet.jsp");
        modelView.addItem("nom","Randriarimalala");
        modelView.addItem("prenom","Anthony");

        String nomComplet = this.nom +" "+ this.prenom;
        return modelView;
    }
}