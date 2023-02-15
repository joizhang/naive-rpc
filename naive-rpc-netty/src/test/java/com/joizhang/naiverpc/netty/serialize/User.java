package com.joizhang.naiverpc.netty.serialize;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class User implements Serializable {

    private String username;

    private int age;

}
