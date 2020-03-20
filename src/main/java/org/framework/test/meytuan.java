package org.framework.test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class meytuan {
    class Node{
        public Node(int a,int b){
            this.i  =a;
            this.j = b;
        }
       public int i;
       public int j;
    }
    private Queue<Node> queue = new LinkedList<>();
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int[][] bu = new int[2][n];
        for(int i =0; i < 2 ; i++){
            for(int j = 0;j < n;j++){
                String a = input.next();
                if(a.equals(".")){
                    bu[i][j] = 1;
                }else{
                    bu[i][j]= 0;
                }
            }
        }
    }

    private int getNum(int[][] arr,int n,int i,int j){
        if(i > -1 && i < 2 && j > -1 && j < n && arr[i][j] == 1){
            //当前节点入队
            queue.add(new Node(i,j));
            int num = 1;
           num += getNum(arr,n,i,j+1);
           num += getNum(arr,n,i-1,j+1);
           num += getNum(arr,n,i+1,j+1);
        }
        return -1;
    }


}
