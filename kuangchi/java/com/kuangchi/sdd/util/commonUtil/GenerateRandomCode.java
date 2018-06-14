package com.kuangchi.sdd.util.commonUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GenerateRandomCode {

	
	/*public static String getRandomCode(){
		Random rand = new Random();
		String result = rand.nextInt(1000000)+"";
		if(result.length()!=6){
			return getRandomCode();
		}
		return result;
	}*/
	
	public static String getRandomCode(){
	    String[] beforeShuffle = new String[] {"0","1", "2", "3", "4", "5", "6", "7",
                "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z" };
        List<String> list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(3, 9);
        return result;
	}
	
	
	
}
