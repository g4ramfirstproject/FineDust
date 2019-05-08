package com.example.ju.finedust;
import com.example.ju.finedust.Item.list;

import java.util.List;

/**
 * Awesome Pojo Generator
 * */
public class NationWideFineDustData{
  private List<list> list;
  private Integer totalCount;
  public void setList(List<com.example.ju.finedust.Item.list> list){
   this.list=list;
  }
  public List<list> getList(){
   return list;
  }
  public void setTotalCount(Integer totalCount){
   this.totalCount=totalCount;
  }
  public Integer getTotalCount(){
   return totalCount;
  }
}