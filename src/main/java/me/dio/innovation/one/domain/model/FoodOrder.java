package me.dio.innovation.one.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tb_food_order")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_food_order")
    private Long id;

    @Column(length = 60, nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "food_order_ingredients", joinColumns = @JoinColumn(name = "food_order_id"))
    private List<String> ingredients;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "food_order_additional_items", joinColumns = @JoinColumn(name = "food_order_id"))
    private List<String> additional_items;

    @Column(length = 25, nullable = false)
    private String cup_size;

    @Column(length = 25, nullable = false)
    private String size;

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 13, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(precision = 13, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_cart_id", referencedColumnName = "id_shopping_cart")
    private ShoppingCart shoppingCart;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getAdditional_items() {
        return additional_items;
    }

    public void setAdditional_items(List<String> additional_items) {
        this.additional_items = additional_items;
    }

    public String getCup_size() {
        return cup_size;
    }

    public void setCup_size(String cup_size) {
        this.cup_size = cup_size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}
