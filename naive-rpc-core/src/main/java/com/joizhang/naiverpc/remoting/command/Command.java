package com.joizhang.naiverpc.remoting.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Command {

    protected Header header;

    private Object payload;

}
