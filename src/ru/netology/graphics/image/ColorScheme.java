package ru.netology.graphics.image;

public class ColorScheme implements TextColorSchema {

    private final char[] gradient = {'#', '$', '@', '%', '*', '+', '-', '\''};
    @Override
    public char convert(int color) {
        return gradient[(int) Math.floor(color / 256.0 * gradient.length)];
    }
}