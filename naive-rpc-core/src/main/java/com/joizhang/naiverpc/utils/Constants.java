package com.joizhang.naiverpc.utils;

public interface Constants {

    int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

}
