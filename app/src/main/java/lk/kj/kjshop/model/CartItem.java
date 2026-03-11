package lk.kj.kjshop.model;


import com.google.firebase.firestore.Exclude;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor



@NoArgsConstructor



public class CartItem {

    @Getter(onMethod_ = {@Exclude})
    @Setter(onMethod_ = {@Exclude})
    private String documentId;
    private String productId;
    private int quantity;
    private List<Attribute> attribute;


    public CartItem(String productId, int quantity, List<Attribute> attribute) {
        this.productId = productId;
        this.quantity = quantity;
        this.attribute = attribute;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor

    public static class Attribute {

        private String name;
        private String value;
    }

}