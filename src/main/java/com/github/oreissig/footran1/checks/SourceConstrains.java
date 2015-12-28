package com.github.oreissig.footran1.checks;

import java.util.Arrays;
import java.util.stream.Stream;

import org.antlr.v4.runtime.ParserRuleContext;

import com.github.oreissig.footran1.FootranException;
import com.github.oreissig.footran1.parser.FootranBaseListener;
import com.github.oreissig.footran1.parser.FootranParser.ArithmeticFormulaContext;
import com.github.oreissig.footran1.parser.FootranParser.AssignContext;
import com.github.oreissig.footran1.parser.FootranParser.AssignedGotoContext;
import com.github.oreissig.footran1.parser.FootranParser.ComputedGotoContext;
import com.github.oreissig.footran1.parser.FootranParser.FunctionCallContext;
import com.github.oreissig.footran1.parser.FootranParser.IfSenseLightContext;
import com.github.oreissig.footran1.parser.FootranParser.IfSenseSwitchContext;
import com.github.oreissig.footran1.parser.FootranParser.LoopBoundaryContext;
import com.github.oreissig.footran1.parser.FootranParser.PauseContext;
import com.github.oreissig.footran1.parser.FootranParser.SenseLightContext;
import com.github.oreissig.footran1.parser.FootranParser.StatementContext;
import com.github.oreissig.footran1.parser.FootranParser.StopContext;
import com.github.oreissig.footran1.parser.FootranParser.SubscriptContext;
import com.github.oreissig.footran1.parser.FootranParser.UfixedConstContext;
import com.github.oreissig.footran1.parser.FootranParser.UnaryExpressionContext;
import com.github.oreissig.footran1.parser.FootranParser.VariableContext;

public class SourceConstrains extends FootranBaseListener {
    
    @Override
    public void enterFunctionCall(FunctionCallContext ctx) {
        String name = ctx.function.getText();
        if (name.length() < 4 || name.length() > 7)
            throw new SourceConstrainViolation("Function names must have 4 to 7 characters", ctx);
    }
    
    @Override
    public void enterArithmeticFormula(ArithmeticFormulaContext ctx) {
        String name = ctx.VAR_ID() != null ? ctx.VAR_ID().getText()
                                           : ctx.FUNC_CANDIDATE().getText();
        checkVariable(name, ctx);
    }
    
    @Override
    public void enterAssignedGoto(AssignedGotoContext ctx) {
        checkVariable(ctx.variable());
    }
    
    @Override
    public void enterAssign(AssignContext ctx) {
        checkVariable(ctx.variable());
    }
    
    @Override
    public void enterComputedGoto(ComputedGotoContext ctx) {
        checkVariable(ctx.variable());
    }
    
    @Override
    public void enterSenseLight(SenseLightContext ctx) {
        int light = Integer.parseInt(ctx.light.getText());
        // allow 0 too
        if (light < 0 || light > 4)
            throw new SourceConstrainViolation("SENSE LIGHT must be 0-4", ctx.light);
    }
    
    @Override
    public void enterIfSenseLight(IfSenseLightContext ctx) {
        int light = Integer.parseInt(ctx.light.getText());
        // don't allow 0
        if (light < 1 || light > 4)
            throw new SourceConstrainViolation("SENSE LIGHT must be 1-4", ctx.light);
    }
    
    @Override
    public void enterIfSenseSwitch(IfSenseSwitchContext ctx) {
        int senseSwitch = Integer.parseInt(ctx.senseSwitch.getText());
        if (senseSwitch < 1 || senseSwitch > 6)
            throw new SourceConstrainViolation("SENSE SWITCH must be 1-6", ctx.senseSwitch);
    }
    
    @Override
    public void enterPause(PauseContext ctx) {
        checkOctal(ctx.consoleOutput);
    }
    
    @Override
    public void enterStop(StopContext ctx) {
        checkOctal(ctx.consoleOutput);
    }
    
    @Override
    public void enterVariable(VariableContext ctx) {
        checkVariable(ctx);
    }
    
    @Override
    public void enterSubscript(SubscriptContext ctx) {
        checkVariable(ctx);
    }
    
    @Override
    public void enterUnaryExpression(UnaryExpressionContext ctx) {
        checkOneChild(ctx);
    }
    
    @Override
    public void enterLoopBoundary(LoopBoundaryContext ctx) {
        checkOneChild(ctx);
    }
    
    @Override
    public void enterStatement(StatementContext ctx) {
        checkOneChild(ctx);
    }
    
    private static void checkVariable(ParserRuleContext ctx) {
        checkVariable(ctx.getText(), ctx);
    }
    
    private static void checkVariable(String varName, ParserRuleContext location) {
        if (varName.length() > 6)
            throw new SourceConstrainViolation("Variable names must have at most 6 characters", location);
    }
    
    private static void checkOctal(UfixedConstContext value) {
        if (value != null) {
            Stream<Character> chars = Arrays.stream(value.getText().split(""))
                                            .map(c -> c.charAt(0));
            if (!chars.allMatch(c -> c >= '0' && c <= '7')) {
                throw new SourceConstrainViolation("can only take octal outputs", value);
            }
        }
    }
    
    private static void checkOneChild(ParserRuleContext ctx) {
        long count = ctx.children.stream().filter(c -> c != null).count();
        if (count == 0)
            throw new SourceConstrainViolation("missing child", ctx);
        if (count > 1)
            throw new SourceConstrainViolation("expected just one child", ctx);
    }
    
    public static class SourceConstrainViolation extends FootranException {
        public SourceConstrainViolation(String msg, ParserRuleContext location) {
            super(msg, location);
        }
    }
}
