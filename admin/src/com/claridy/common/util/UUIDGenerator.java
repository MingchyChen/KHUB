package com.claridy.common.util;

import java.util.UUID;

public class UUIDGenerator { 
    public UUIDGenerator() { 
    } 
    
    /** 
     * 獲得一個UUID 
     * @return String UUID 
     */ 
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", ""); 
    } 
    
    /** 
     * 獲得指定數量的UUID 
     * @param number int 需要獲得的UUID數量
     * @return String[] UUID陣列
     */ 
    public static String[] getUUID(int number){ 
        if(number < 1){ 
            return null; 
        } 
        String[] UUIDs = new String[number]; 
        for(int i=number-1;i>=0;i--){ 
        	UUIDs[i] = getUUID(); 
        } 
        return UUIDs; 
    } 
    
    public static void main(String[] args){ 
        String[] ss = getUUID(10); 
        for(int i=0;i<ss.length;i++){ 
            System.out.println(ss[i]); 
        } 
    } 
}  
