package com.himanshu.stackoverflow.auxiliary;

import com.himanshu.stackoverflow.entity.Answer;

import java.util.List;

public class Sort {
    public static <T extends Answer> List<T> sort(List<T> list) {
        list.sort(new MyComparator());
        return list;
    }
}
