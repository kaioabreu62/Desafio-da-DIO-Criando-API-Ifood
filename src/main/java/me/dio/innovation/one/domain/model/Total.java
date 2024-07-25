package me.dio.innovation.one.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_total")
public class Total {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_total")
    private Long id;

    @Column(precision = 13, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(name = "service_fee", precision = 13, scale = 2, nullable = false)
    private BigDecimal serviceFee;

    @Column(name = "delivery_fee", precision = 13, scale = 2, nullable = false)
    private BigDecimal deliveryFee;

    @Column(name = "discount_restaurant", precision = 13, scale = 2, nullable = false)
    private BigDecimal discountRestaurant;

    @Column(name = "incentives_ifood", precision = 13, scale = 2, nullable = false)
    private BigDecimal incentivesIfood;

    @Column(name = "final_total", precision = 13, scale = 2, nullable = false)
    private BigDecimal finalTotal;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id_payment")
    private Payment payment;


    @PrePersist
    @PreUpdate
    private void calculateFinalTotal() {
        this.finalTotal = this.subtotal
                .add(this.serviceFee)
                .add(this.deliveryFee)
                .subtract(this.discountRestaurant)
                .subtract(this.incentivesIfood);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getDiscountRestaurant() {
        return discountRestaurant;
    }

    public void setDiscountRestaurant(BigDecimal discountRestaurant) {
        this.discountRestaurant = discountRestaurant;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getIncentivesIfood() {
        return incentivesIfood;
    }

    public void setIncentivesIfood(BigDecimal incentivesIfood) {
        this.incentivesIfood = incentivesIfood;
    }

    public BigDecimal getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(BigDecimal finalTotal) {
        this.finalTotal = finalTotal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
