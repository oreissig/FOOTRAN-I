package com.github.oreissig.footran1.checks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import com.github.oreissig.footran1.FootranException;
import com.github.oreissig.footran1.parser.FootranBaseListener;
import com.github.oreissig.footran1.parser.FootranParser.ArithmeticFormulaContext;
import com.github.oreissig.footran1.parser.FootranParser.AssignContext;
import com.github.oreissig.footran1.parser.FootranParser.AssignedGotoContext;
import com.github.oreissig.footran1.parser.FootranParser.ComputedGotoContext;
import com.github.oreissig.footran1.parser.FootranParser.DoLoopContext;
import com.github.oreissig.footran1.parser.FootranParser.ExpressionContext;
import com.github.oreissig.footran1.parser.FootranParser.FunctionCallContext;
import com.github.oreissig.footran1.parser.FootranParser.LoopBoundaryContext;
import com.github.oreissig.footran1.parser.FootranParser.PowerContext;
import com.github.oreissig.footran1.parser.FootranParser.ProductContext;
import com.github.oreissig.footran1.parser.FootranParser.StatementContext;
import com.github.oreissig.footran1.parser.FootranParser.SubscriptContext;
import com.github.oreissig.footran1.parser.FootranParser.SubscriptExpressionContext;
import com.github.oreissig.footran1.parser.FootranParser.SumContext;
import com.github.oreissig.footran1.parser.FootranParser.UfixedConstContext;
import com.github.oreissig.footran1.parser.FootranParser.UfloatConstContext;
import com.github.oreissig.footran1.parser.FootranParser.UnaryExpressionContext;
import com.github.oreissig.footran1.parser.FootranParser.VariableContext;

public class TypeCheck extends FootranBaseListener {
    
    private final Map<ParserRuleContext, Type> types = new HashMap<>();
    
    public Type getType(ParserRuleContext ctx) {
        return types.get(ctx);
    }
    
    @Override
    public void exitFunctionCall(FunctionCallContext ctx) {
        // make sure all arguments are of the same type
        Type argsType = getAllSameType(ctx.expression(), ctx);
        
        // check expected argument type
        // TODO
        // check argument count
        // TODO
        
        // save function's return type
        char firstChar = ctx.function.getText().charAt(0);
        Type returnType = firstChar == 'X' ? Type.FIXED : Type.FLOAT;
        types.put(ctx, returnType);
    }
    
    @Override
    public void exitStatement(StatementContext ctx) {
        getOnlyChildType(ctx).ifPresent(t -> types.put(ctx, t));
    }
    
    @Override
    public void exitArithmeticFormula(ArithmeticFormulaContext ctx) {
        // expression() type is irrelevant, it will automatically be casted
        String name = ctx.VAR_ID() != null ? ctx.VAR_ID().getText()
                                           : ctx.FUNC_CANDIDATE().getText();
        types.put(ctx, getVariableType(name, ctx));
    }
    
    @Override
    public void exitAssignedGoto(AssignedGotoContext ctx) {
        if (getVariableType(ctx.variable()) != Type.FIXED)
            throw new TypeCheckException("Assigned GOTO needs a fixed point variable", ctx);
    }
    
    @Override
    public void exitAssign(AssignContext ctx) {
        if (getVariableType(ctx.variable()) != Type.FIXED)
            throw new TypeCheckException("ASSIGN needs a fixed point variable", ctx);
    }
    
    @Override
    public void exitComputedGoto(ComputedGotoContext ctx) {
        if (getVariableType(ctx.variable()) != Type.FIXED)
            throw new TypeCheckException("Computed GOTO needs a fixed point variable", ctx);
    }
    
    @Override
    public void exitDoLoop(DoLoopContext ctx) {
        types.put(ctx, getAllChildrenSameType(ctx));
    }
    
    @Override
    public void exitLoopBoundary(LoopBoundaryContext ctx) {
        Type t = getOnlyChildType(ctx).get();
        if (t != Type.FIXED)
            throw new TypeCheckException("DO loop boundary must be fixed point", ctx);
        types.put(ctx, t);
    }
    
    @Override
    public void exitVariable(VariableContext ctx) {
        types.put(ctx, getVariableType(ctx));
    }
    
    @Override
    public void exitSubscript(SubscriptContext ctx) {
        types.put(ctx, getVariableType(ctx));
    }
    
    @Override
    public void exitSubscriptExpression(SubscriptExpressionContext ctx) {
        Type t = getAllChildrenSameType(ctx);
        if (t != Type.FIXED)
            throw new TypeCheckException("subscripts must be fixed point", ctx);
        types.put(ctx, t);
    }
    
    @Override
    public void exitExpression(ExpressionContext ctx) {
        types.put(ctx, getAllChildrenSameType(ctx));
    }
    
    @Override
    public void exitSum(SumContext ctx) {
        types.put(ctx, getAllChildrenSameType(ctx));
    }
    
    @Override
    public void exitProduct(ProductContext ctx) {
        types.put(ctx, getAllChildrenSameType(ctx));
    }
    
    @Override
    public void exitPower(PowerContext ctx) {
        types.put(ctx, getAllChildrenSameType(ctx));
    }
    
    @Override
    public void exitUnaryExpression(UnaryExpressionContext ctx) {
        types.put(ctx, getOnlyChildType(ctx).get());
    }
    
    @Override
    public void exitUfixedConst(UfixedConstContext ctx) {
        types.put(ctx, Type.FIXED);
    }
    
    @Override
    public void exitUfloatConst(UfloatConstContext ctx) {
        types.put(ctx, Type.FLOAT);
    }
    
    private Optional<Type> getOnlyChildType(ParserRuleContext ctx) {
        return ctx.children.stream()
                           // get node types
                           .map(child -> types.get(child))
                           // return distinct result
                           .filter(t -> t != null)
                           .findFirst();
    }
    
    private Type getAllChildrenSameType(ParserRuleContext ctx) {
        return getAllSameType(ctx.children, ctx);
    }
    
    private Type getAllSameType(List<? extends ParseTree> nodes, ParserRuleContext location) {
        return nodes.stream()
                    // get node types
                    .map(child -> types.get(child))
                    .filter(t -> t != null)
                    // check they are all the same
                    .reduce((t1, t2) -> {
                        if (t1 != t2)
                            throw new TypeCheckException("Inconsistent types", location);
                        else
                            return t1;
                    })
                    .get();
    }
    
    private static Type getVariableType(ParserRuleContext ctx) {
        return getVariableType(ctx.getText(), ctx);
    }
    
    private static Type getVariableType(String varName, ParserRuleContext location) {
        char first = varName.charAt(0);
        return (first >= 'I' && first <= 'N') ? Type.FIXED : Type.FLOAT;
    }
    
    public static class TypeCheckException extends FootranException {
        public TypeCheckException(String msg, ParserRuleContext location) {
            super(msg, location);
        }
    }
}
