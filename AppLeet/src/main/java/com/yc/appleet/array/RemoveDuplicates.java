package com.yc.appleet.array;

import com.yc.toolutils.AppLogUtils;

import java.util.Arrays;

public class RemoveDuplicates {

    //给定一个排序数组，你需要在原地删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
    //不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
    public static int removeDuplicates(int[] num) {
        if (num==null || num.length==0){
            return 0;
        }
        int currentV = num[0];
        int count = 0;
        for(int i = 1 ; i < num.length; i++){
            if(num[i] != currentV){
                count++;
                currentV = num[i];
                num[count] = currentV;
            }
        }
        AppLogUtils.d("数据数组数量" + count + "  " + Arrays.toString(num));
        return count+1;
    }




}
