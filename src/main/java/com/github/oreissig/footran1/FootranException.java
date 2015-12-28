package com.github.oreissig.footran1;

import java.util.Arrays;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import com.github.oreissig.footran1.parser.FootranParser;

public class FootranException extends RuntimeException {
    public final ParserRuleContext location;
    
    public FootranException(String msg, ParserRuleContext location) {
        super(getMessageWithLocation(msg,location));
        this.location = location;
    }
    
    public FootranException(String msg, ParserRuleContext location, Throwable cause) {
        super(getMessageWithLocation(msg,location), cause);
        this.location = location;
    }
    
    private static String getMessageWithLocation(String msg, ParserRuleContext location) {
        Token t = location.getStart();
        String tree = location.toStringTree(Arrays.asList(FootranParser.ruleNames));
        return msg + "\n\tat " + t.getLine() + ":" + t.getCharPositionInLine() + " " + tree;
    }
}
