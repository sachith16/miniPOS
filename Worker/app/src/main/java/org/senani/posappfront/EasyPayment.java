package org.senani.posappfront;

/**
 * Created by sachith on 8/25/18.
 */

public class EasyPayment {

    private String d;
    private String c;
    private String ci;
    private float p;
    private boolean ic;

    public EasyPayment(String d, String c, String ci, float p, boolean ic) {
        this.d = d;
        this.c = c;
        this.ci = ci;
        this.p = p;
        this.ic = ic;
    }

    public EasyPayment() {
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public float getP() {
        return p;
    }

    public void setP(float p) {
        this.p = p;
    }

    public boolean isIc() {
        return ic;
    }

    public void setIc(boolean ic) {
        this.ic = ic;
    }
}
