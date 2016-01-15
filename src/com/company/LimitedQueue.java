package com.company;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by krist on 15-Jan-16.
 */
public class LimitedQueue implements Iterable
{
    private Object[] qArray;
    private int fiQ;
    private int liQ;
    private int length;

    public LimitedQueue(int maxLength){
        qArray = new Object[maxLength];
        fiQ = 0;
        liQ = 0;
        length = 0;
    }
    private int getNextIndex(int index){
        if(index+1 >= qArray.length){
            return 0;
        }
        return index+1;
    }
    private int getPreviousIndex(int index){
        if(index <= 0){
            return qArray.length-1;
        }
        return index-1;
    }

    public boolean EnQueue(Object o){
        if(length == qArray.length){
            return false;
        }
        liQ = getNextIndex(liQ);
        qArray[liQ] = o;
        length++;
        return true;
    }
    public Object DeQueue(){
        if(IsEmpty()){
            return null;
        }
        Object toReturn = qArray[liQ];
        liQ = getPreviousIndex(liQ);
        length--;
        return toReturn;
    }
    public boolean IsEmpty(){
        if(length == 0){
            return true;
        }
        return false;
    }
    public int Count(){
        return length;
    }



    @Override
    public Iterator iterator() {
        Iterator<Object> it = new Iterator<Object>() {
            private int currentIndex = fiQ;

            @Override
            public boolean hasNext() {
                if(getNextIndex(currentIndex) == fiQ || qArray[getNextIndex(currentIndex)] == null){
                    return false;
                }
                return true;
            }

            @Override
            public Object next() {
                return qArray[getNextIndex(currentIndex)];
            }
        };
        return it;
    }
}
