package org.framework.test;

import java.util.*;

public class BackPack {
    private List<List<Integer>> result = new LinkedList();
    public List<List<Integer>> permute(int[] nums) {
        get(nums,0,nums.length);
        return result;
    }

    private void get(int[] nums,int start,int len){
        if(start == len){
            List<Integer> list = create(nums);
            result.add(list);
        }else {
            for (int i = start; i < len; i++) {
                swap(nums, start, i);
                get(nums, i+1, len);
                //回溯
                swap(nums, start, i);
            }
        }
    }

    private void swap(int[] nums ,int i,int j){
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    private List<Integer> create(int[] nums){
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < nums.length;i++){
            list.add(nums[i]);
        }
        return list;
    }

    public static void main(String[] args) {
        int[] a = {1,2,3};
        System.out.println(new BackPack().permute(a));
    }

}
