/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.data;

/**
 *
 * @author bhaduri
 */
public class DataSliceStartEnd<T> {
    private final T start;
    private final T end;

    public DataSliceStartEnd(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }
    
    
}
