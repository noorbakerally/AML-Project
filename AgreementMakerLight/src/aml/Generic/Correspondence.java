package aml.Generic;

import java.math.BigDecimal;

public class Correspondence {
    public String iriFrom;
    public String iriTo;
    public BigDecimal confidence;
    public Correspondence.relation rel;

    public Correspondence(){

    }

    public Correspondence(String iriFrom, String iriTo, BigDecimal confidence, Correspondence.relation rel ){
        this.iriFrom = iriFrom;
        this.iriTo = iriTo;
        this.confidence = confidence;
        this.rel = rel;
    }

    public static enum relation {
        EQUIVALENT, SUBCLASS,SUPERCLASS;
    }

}
