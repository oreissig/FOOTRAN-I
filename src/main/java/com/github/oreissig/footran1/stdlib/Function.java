package com.github.oreissig.footran1.stdlib;

import com.github.oreissig.footran1.checks.Type;

public interface Function {
    String getName();
    Type getParameterType();
    boolean isValidParameterCount(int paramCount);
}
