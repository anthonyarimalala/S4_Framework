package etu2033.framework.servlet;

import java.util.HashMap;

public class ModelView{
    String url;
    HashMap<String,Object> data = new HashMap<>();


// FUNCTIONS
    public void addItem(String key,Object value){
        this.data.put(key, value);
    }

//  GET/SET
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public HashMap<String,Object> getData(){
        return this.data;
    }
    public void setData(HashMap<String,Object> data){
        this.data = data;
    }
}