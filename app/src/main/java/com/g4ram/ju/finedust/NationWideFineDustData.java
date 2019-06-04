package com.g4ram.ju.finedust;
import com.g4ram.ju.finedust.Item.list;

import java.util.List;

/**
 * Awesome Pojo Generator
 * */
public class NationWideFineDustData{
  private List<com.g4ram.ju.finedust.Item.list> list;
  private Integer totalCount;
  public void setList(List<com.g4ram.ju.finedust.Item.list> list){
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