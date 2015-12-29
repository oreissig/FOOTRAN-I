package com.github.oreissig.footran1.stdlib;

import static com.github.oreissig.footran1.checks.Type.FIXED;
import static com.github.oreissig.footran1.checks.Type.FLOAT;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import com.github.oreissig.footran1.checks.Type;

public class BuiltInFunctionLibrary implements FunctionLibrary {
    private static final Map<String,Function> funcs = new HashMap<>();

    static {
        define("ABSF",   FLOAT, 1);
        define("XABSF",  FIXED, 1);
        
        define("INTF",   FIXED, 1);
        define("XINTF",  FIXED, 1);
        
        define("MODF",   FIXED, 1);
        define("XMODF",  FIXED, 1);
        
        define("MAX0F",  FIXED, 1);
        define("MAX1F",  FIXED, 1);
        define("XMAX0F", FIXED, 1);
        define("XMAX1F", FIXED, 1);
        
        define("MIN0F",  FIXED, 1);
        define("MIN1F",  FIXED, 1);
        define("XMIN0F", FIXED, 1);
        define("XMIN1F", FIXED, 1);
        
        // added according to http://www.softwarepreservation.org/projects/FORTRAN/manual/Addenda_to_FORTRAN_Prog_Ref_Manual.pdf
        define("FLOATF", FIXED, 1);
        define("XFIXF",  FLOAT, 1);
        define("SIGNF",  FLOAT, 1);
        define("XSIGNF", FIXED, 1);
        
        // added according to http://archive.computerhistory.org/resources/text/Fortran/102679277.05.01.acc.pdf#4
        define("LOGF",   FLOAT, 1);
        define("SINF",   FLOAT, 1);
        define("COSF",   FLOAT, 1);
        define("EXPF",   FLOAT, 1);
        define("SQRTF",  FLOAT, 1);
        define("ATANF",  FLOAT, 1);
        define("TANHF",  FLOAT, 1);
        define("XRANDF", FIXED, 1);
        define("CERF",   FLOAT, 1);
        define("LGAMF",  FLOAT, 1);
    }
    
    private static void define(String name, Type paramType, int paramCount) {
        define(name, paramType, c -> c == paramCount);
    }
    
    private static void define(String name, Type paramType, Predicate<Integer> paramCount) {
        funcs.put(name, new FunctionImpl(name, paramType, paramCount));
    }
    
    @Override
    public Function getFunction(String name) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private static class FunctionImpl implements Function {
        
        private final String name;
        private final Type paramType;
        private final Predicate<Integer> paramCount;
        
        public FunctionImpl(String name, Type paramType, Predicate<Integer> paramCount) {
            this.name = name;
            this.paramType = paramType;
            this.paramCount = paramCount;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public Type getParameterType() {
            return paramType;
        }
        
        @Override
        public boolean isValidParameterCount(int count) {
            return paramCount.test(count);
        }
    }
}
