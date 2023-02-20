package com.joizhang.naiverpc.remoting.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Command {

    /**
     * 请求头
     */
    protected Header header;

    /**
     * 请求体
     */
    private Object payload;

}
