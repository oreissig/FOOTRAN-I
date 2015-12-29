package com.github.oreissig.footran1.checks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.oreissig.footran1.FootranException;
import com.github.oreissig.footran1.parser.FootranBaseListener;
import com.github.oreissig.footran1.parser.FootranParser.CardContext;
import com.github.oreissig.footran1.parser.FootranParser.DoLoopContext;
import com.github.oreissig.footran1.parser.FootranParser.ProgramContext;
import com.github.oreissig.footran1.parser.FootranParser.StatementNumberContext;

public class BasicBlocks extends FootranBaseListener {
    
    private final List<BasicBlock> blocks = new ArrayList<>();
    private final Map<BasicBlock,Set<StatementNumberContext>> outbound = new HashMap<>();
    private final Map<StatementNumberContext,BasicBlock> jumpMap = new HashMap<>();
    private BasicBlock current = null;
    
    public List<BasicBlock> getBlocks() {
        return blocks;
    }
    
    public BasicBlock findJumpTarget(StatementNumberContext num) {
        return jumpMap.get(num);
    }
    
    @Override
    public void enterProgram(ProgramContext ctx) {
        if (!blocks.isEmpty())
            throw new IllegalStateException("You must not run this analysis twice");
        create(ctx);
    }
    
    @Override
    public void exitProgram(ProgramContext ctx) {
        // aggregate blocks
        Map<Integer,BasicBlock> blocksByNumber = blocks.stream()
               .filter(b -> b.statementNumber != null)
               .collect(Collectors.toMap(b -> b.statementNumber, b -> b));
        outbound.forEach((b,targets) -> {
            for (StatementNumberContext stmtNum : targets) {
                int number = parseStatementNumber(stmtNum.NUMBER());
                BasicBlock target = blocksByNumber.get(number);
                if (target == null)
                    throw new FootranException("unknown statement number: " + number, stmtNum);
                jumpMap.put(stmtNum, target);
                b.outbound.add(target);
                target.inbound.add(b);
            }
        });
    }
    
    @Override
    public void enterDoLoop(DoLoopContext ctx) {
        create(ctx);
    }
    
    @Override
    public void enterCard(CardContext ctx) {
        if (ctx.STMTNUM() != null) {
            create(ctx);
            int number = parseStatementNumber(ctx.STMTNUM());
            current.statementNumber = number;
        }
    }
    
    @Override
    public void enterStatementNumber(StatementNumberContext ctx) {
        outbound.get(current).add(ctx);
    }
    
    private void create(ParserRuleContext ctx) {
        BasicBlock b = new BasicBlock();
        b.start = ctx;
        if (current != null) {
            b.previous = current;
            current.next = b;
        }
        current = b;
        outbound.put(b, new HashSet<>());
        blocks.add(b);
    }
    
    private static int parseStatementNumber(TerminalNode node) {
        return Integer.parseInt(node.getText());
    }
}
