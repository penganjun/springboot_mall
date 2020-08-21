package com.ajpeng.mall.mmall.common;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class ImageCode implements Serializable {
    private BufferedImage image;
    private String sRand;
    private int i;

    public ImageCode(BufferedImage image, String sRand, int i) {
        this.image = image;
        this.sRand = sRand;
        this.i = i;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getsRand() {
        return sRand;
    }

    public void setsRand(String sRand) {
        this.sRand = sRand;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
