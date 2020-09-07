package com.example.covidnews.SearchEngine;

public class KMP{
    //计算next数组
    public static int[] CountNext(String _target){
        int size = _target.length();
        int []next = new int[size];
        char[] target = _target.toCharArray();
        next[0] = -1;
        for(int i = 1; i <= size - 1; i ++) {
            //得到前一位的最长前缀后缀子串长度
            int j = next[i - 1];
            //如果当前的字符与最长前缀后缀子串的后一位不匹配，需要继续找前面的最长前缀后缀子串
            while((target[j+1] != target[i]) && (j>=0))
                j = next[j];
            if(target[j+1] == target[i])
                next[i] = j + 1;
            else
                next[i] = -1;
        }
        return next;
    }

    //匹配两个字符串
    public static boolean Match(String _text, String _target){
        //记录最终匹配的个数
        int count = 0;
        //使用KMP算法来匹配字符串
        String StringText = _text.toUpperCase();
        String StringTarget = _target.toUpperCase();
        //计算next数组
        int[] next = CountNext(StringTarget);
        int i = 0;
        int j = 0;
        int TextSize = _text.length();
        int TargetSize = _target.length();
        //转换成字符数组的形式
        char[] text = StringText.toCharArray();
        char[] target = StringTarget.toCharArray();
        //开始比较
        while(i < TextSize){
            //当还没比较完的时候
            if(text[i] == target[j]){
                //如果字符串符合，i和j都相加
                i ++;
                j ++;
                //如果j等于目标字符串长度，说明匹配成功
                if(j == TargetSize) {
                    return true;
//                    j = next[j - 1] + 1;
                }
            }
            else{
                //如果不匹配，则需要寻找next数组
                if(j == 0)
                    i ++;
                else
                    j = next[j - 1] + 1;
            }
        }
        return false;
    }
}