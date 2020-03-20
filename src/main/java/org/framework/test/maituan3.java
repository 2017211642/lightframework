package org.framework.test;

import java.util.Scanner;

public class maituan3 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n,k ,l ,r;
        n = input.nextInt();//array size
        k = input.nextInt();//base
        l = input.nextInt();//low
        r = input.nextInt();//high
        int j=1,mul = 1;
        while(mul < r){
            mul = k * j;
            j++;
        }
        int num = 1;
        for(int i = 0 ;i < n; i++){System.out.println(n);
            num = num * j;
        }
        num = (int)(num % (1e9+7));
        System.out.println(num);
    }


}
