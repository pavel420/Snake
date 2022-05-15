package com.example.snake2;

public class Point {
    public final int x, y;
    public PointType type;
    public TypeApple typeApple;
    public Point(int x, int y){
        this.x=x;
        this.y=y;
        this.type=PointType.EMPTY;
        this.typeApple=TypeApple.NOAPPLE;
    }

    public TypeApple getTypeApple() {
        return typeApple;
    }
}
