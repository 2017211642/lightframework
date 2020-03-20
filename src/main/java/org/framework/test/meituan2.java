package org.framework.test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class meituan2 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int a,b;
        a = input.nextInt();
        b = input.nextInt();
        int[] arr = new int[a];
        for(int i =0 ;i < a;i++){
            int n = input.nextInt();
            arr[i] = n;
        }
        System.out.println(new meituan2().getNum(arr,b));
    }

    private int getNum(int[] arr,int b){
        Arrays.sort(arr);
        int num;
        do{
            num = getPublicNum(arr);
            for(int i = 0 ;i < arr.length;i++){
                if(arr[i] > b){
                    arr[i] = arr[i]&b;
                }else{
                    break;
                }
            }
        }while(arr[0] > b);
        return num;
    }

    private Integer getPublicNum(int[] arr){
        int maxIndex = 0;
        Map<Integer,Integer> map = new HashMap<>();
        for(int i =0 ; i < arr.length;i++){
            Integer b = map.get(arr[i]);
            if(b != null){
                b++;
            }else{
                b = 1;
            }
            map.put(arr[i],b);
            if(map.get(maxIndex) != null && b > map.get(maxIndex)){
                maxIndex = arr[i];
            }
        }

        return map.get(maxIndex);
    }

}
