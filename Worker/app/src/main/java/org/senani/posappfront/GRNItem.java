package org.senani.posappfront;

public class GRNItem {

    private String c;
    private float p;
    private float q;
    private String d;

    public GRNItem(String c, float p, float q, String d) {
        this.c = c;
        this.p = p;
        this.q = q;
        this.d = d;
    }

    public GRNItem() {

    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public float getP() {
        return p;
    }

    public void setP(float p) {
        this.p = p;
    }

    public float getQ() {
        return q;
    }

    public void setQ(float q) {
        this.q = q;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }
}
