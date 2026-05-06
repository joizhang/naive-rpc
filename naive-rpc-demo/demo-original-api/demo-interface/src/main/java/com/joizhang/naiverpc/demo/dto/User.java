package com.joizhang.naiverpc.demo.dto;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private String username;

    private byte gender;

    private short age;

    private int version;
}
