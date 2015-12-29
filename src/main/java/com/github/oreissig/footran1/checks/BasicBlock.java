package com.github.oreissig.footran1.checks;

import java.util.HashSet;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;

// TODO make pretty
public class BasicBlock {
    public BasicBlock previous;
    public BasicBlock next;
    
    public ParserRuleContext start;
    public Integer statementNumber;
    
    public final Set<BasicBlock> outbound = new HashSet<>();
    public final Set<BasicBlock> inbound = new HashSet<>();
}
