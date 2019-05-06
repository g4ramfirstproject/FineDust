package com.example.ju.finedust.Item;
import java.util.List;

/**
 * Awesome Pojo Generator
 * */
public class TM{
    private List<Documents> documents;
    private Meta meta;
  public void setDocuments(List<Documents> documents){
   this.documents=documents;
  }
  public List<Documents> getDocuments(){
   return documents;
  }
  public void setMeta(Meta meta){
   this.meta=meta;
  }
  public Meta getMeta(){
   return meta;
  }

  public String getX(){
      return documents.get(0).getX();
  }
  public String getY(){
      return documents.get(0).getY();
  }
}