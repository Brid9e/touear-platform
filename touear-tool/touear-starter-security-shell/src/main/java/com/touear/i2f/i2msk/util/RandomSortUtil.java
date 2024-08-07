package com.touear.i2f.i2msk.util;

import java.util.Random;

public class RandomSortUtil {
	public static void changePosition(Object[] positions,Random random) {    
        for(int index=positions.length-1; index>=0; index--) {    
            //从0到index处之间随机取一个值，跟index处的元素交换    
            exchange(positions,random.nextInt(index+1), index);    
        } 
    } 
	private static void exchange(Object[] positions,int p1, int p2) {    
		Object temp = positions[p1];    
        positions[p1] = positions[p2];    
        positions[p2] = temp;  //更好位置  
    }    
}

