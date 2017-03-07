package org.javacore.base.colgoogle;

import com.google.common.collect.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tengw on 2017/3/6.
 */
public class ImmutableListTest {
    public static void main(String[] args){

//        List<Test01> list=new ArrayList<Test01>();
//        list.add(new Test01());
//        list.add(new Test01());
//
//        //List<Test01> immutableList=Collections.unmodifiableList(list);
//        //immutableList.add(new Test01());
//        //immutableList.get(0).setName("3232323");
//        //System.out.println(immutableList.get(0).getName());
//        List<Test01> immutableList= ImmutableList.copyOf(list);
//        list.get(0).setName("123456");
//        System.out.println(immutableList.get(0).getName());
//        System.out.println(list.get(0).getName());
//        immutableList.get(0).setName("3232323");
//        System.out.println(immutableList.get(0).getName());
//        System.out.println(list.get(0).getName());

//        Multiset<String> set = HashMultiset.create();
//        set.add("kafka0102");
//        set.add("kafka0102");
//        System.out.println(set.count("kafka0102"));//输出2
//        set.setCount("kafka0102", 5);
//        System.out.println(set.count("kafka0102"));//输出5


//        Multimap<String,String> map = HashMultimap.create();
//        map.put("kafka0102","1");
//        map.put("kafka0102","2");
//        System.out.println(map.get("kafka0102"));//输出[2, 1]


        BiMap<String,String> map = HashBiMap.create();
        map.put("kafka0102","1");
        System.out.println(map.get("kafka0102"));
        System.out.println(map.inverse().get("1"));
    }
}

 class Test01{
     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     private String name;
}
