package lk.kj.kjshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@Builder
@NoArgsConstructor

public class User {
    private String uid;
    private String name;
    private String email;
    private String profilePicUrl;
}