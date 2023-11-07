package com.sunyi.algo.base;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@EqualsAndHashCode
public class Position implements Cloneable {
    private long x;
    private long y;
    private String value;
    private String layerType;
    private long timestamp;

    public Position(long x, long y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public Object clone() {
        Position position = null;
        try {
            position = (Position) super.clone();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return position;
    }
}
