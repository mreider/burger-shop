package com.example.fulfillmentservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;
    private Boolean fulfilled;

    // Getters and Setters

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("fulfilled")
    public Boolean getFulfilled() {
        return fulfilled;
    }

    @JsonProperty("fulfilled")
    public void setFulfilled(Boolean fulfilled) {
        this.fulfilled = fulfilled;
    }
}