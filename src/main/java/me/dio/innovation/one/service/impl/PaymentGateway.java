package me.dio.innovation.one.service.impl;

import me.dio.innovation.one.domain.model.Payment;
import me.dio.innovation.one.domain.model.Total;
import org.springframework.stereotype.Component;

@Component
public class PaymentGateway {

    public Payment processPayment(String paymentMethod, Total total) {
        Payment payment = new Payment();
        payment.setPayment_method(paymentMethod);
        payment.setStatus("Pending");

        try {
            Thread.sleep(2000);
            payment.setStatus("Approved");
        } catch (InterruptedException e) {
            payment.setStatus("Declined");
        }

        payment.setTotal(total);
        return payment;
    }
}
