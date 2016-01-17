package com.github.oreissig.footran1.parser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.TokenStream;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.binder.ScopedBindingBuilder;

/**
 * This module binds all the generic stuff you need to get started with ANTLR.
 * To use it, complete the object graph by providing bindings for your generated {@link Lexer} and
 * {@link Parser} classes like using {@link AbstractSpecificParserModule}.
 */
public final class AntlrModule extends AbstractModule {
    
    public static Module withFile(String filePath) throws IOException {
        return withCharStream(new ANTLRFileStream(filePath));
    }
    
    public static Module withString(String sourceCode) {
        return withCharStream(new ANTLRInputStream(sourceCode));
    }
    
    public static Module withCharStream(CharStream input) {
        return new AntlrModule(input);
    }
    
    private final CharStream input;
    
    private AntlrModule(CharStream input) {
        this.input = input;
    }
    
    @Override
    protected void configure() {
        bind(CharStream.class).toInstance(input);
        bind(TokenSource.class).to(Lexer.class);
        try {
            bind(TokenStream.class).toConstructor(CommonTokenStream.class.getConstructor(TokenSource.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find constructor org.antlr.v4.runtime.CommonTokenStream.<init>(org.antlr.v4.runtime.TokenSource)", e);
        }
    }
    
    /**
     * Implement and load this Module to satisfy all bindings required for
     * {@link AntlrModule}. Provide the required bindings using
     * {@link #bindLexer(Class)} and {@link #bindParser(Class)}.
     */
    public static abstract class SpecificAntlrModule extends AbstractModule {
        /**
         * Binds your generated ANTLR lexer.
         * 
         * @param lexerClass the generated lexer implementation
         * @return {@link ScopedBindingBuilder} for the implementation's binding
         */
        protected <T extends Lexer> ScopedBindingBuilder bindLexer(Class<T> lexerClass) {
            return doBind(Lexer.class, lexerClass);
        }
        
        /**
         * Binds your generated ANTLR parser.
         * 
         * @param parserClass the generated parser implementation
         * @return {@link ScopedBindingBuilder} for the implementation's binding
         */
        protected <T extends Parser> ScopedBindingBuilder bindParser(Class<T> parserClass) {
            return doBind(Parser.class, parserClass);
        }
        
        private <G,S extends G> ScopedBindingBuilder doBind(Class<G> generic, Class<S> specific) {
            bind(generic).to(specific);
            return bind(specific).toConstructor(onlyConstructor(specific));
        }
        
        @SuppressWarnings("unchecked")
        private static <T> Constructor<T> onlyConstructor(Class<T> clazz) {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            if (constructors.length != 1)
                throw new IllegalArgumentException(
                        "More than one constructor for " + clazz.getName() + ": " + Arrays.asList(constructors));
            return (Constructor<T>) constructors[0];
        }
    }
}
