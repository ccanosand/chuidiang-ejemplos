package com.chuidiang.ejemplos.properties;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;



public class LoadFileProperties {

   public static void main(String[] args) {
      Properties p = new Properties();
      try {
         p.load(new FileReader("resources/properties/config.properties"));
         System.out.println("uno="+p.getProperty("uno"));
         
         p.setProperty("cuatro", "4");
         p.store(new FileWriter("target/out.properties"),"Some comment");
      } catch (Exception e){
         System.err.println("Error reading file "+e.getMessage());
      }

   }

}