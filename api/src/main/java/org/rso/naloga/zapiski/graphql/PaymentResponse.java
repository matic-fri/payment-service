package org.rso.naloga.zapiski.graphql;

import user.lib.Payment;

public class PaymentResponse {

    private Payment p;
    private boolean status;

    public PaymentResponse(Payment p, boolean c){
        this.p = p;
        this.status = c;
    }

}
