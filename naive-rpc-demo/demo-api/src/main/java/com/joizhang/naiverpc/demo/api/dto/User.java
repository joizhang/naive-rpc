package com.joizhang.naiverpc.demo.api.dto;

import lombok.*;

import java.io.Serializable;

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
