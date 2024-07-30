package com.himanshu.stackoverflow.auxiliary;

import com.himanshu.stackoverflow.entity.Answer;

import java.util.Comparator;

public class MyComparator implements Comparator<Answer> {
    public int compare(Answer o1, Answer o2) {
        return o1.getVotes() == o2.getVotes() ? 0 : o2.getVotes() - o1.getVotes();
    }
}
