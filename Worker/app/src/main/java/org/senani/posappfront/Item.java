package org.senani.posappfront;

public class Item {
    private String c;
    private float l;
    private String n;
    private float p;
    private float q;

    public Item() {
    }

    public Item(String c, float l, String n, float p, float q) {
        this.c = c;
        this.l = l;
        this.n = n;
        this.p = p;
        this.q = q;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public float getQ() {
        return q;
    }

    public void setQ(float q) {
        this.q = q;
    }

    public float getL() {
        return l;
    }

    public void setL(float l) {
        this.l = l;
    }


    public float getP() {
        return p;
    }

    public void setP(float p) {
        this.p = p;
    }
}

