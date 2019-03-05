package org.senani.posapp;

public class Payment {
    private String d;
    private float p;
    private String r;

    public Payment(String d, float p, String r) {
        this.d = d;
        this.p = p;
        this.r = r;
    }

    public Payment() {
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public float getP() {
        return p;
    }

    public void setP(float p) {
        this.p = p;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }
}
