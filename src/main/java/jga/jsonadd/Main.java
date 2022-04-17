package jga.jsonadd;

import jga.jsonadd.antlr.JSONLexer;
import jga.jsonadd.antlr.JSONParser;
import jga.jsonadd.jastadd.JsonProgram;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
// import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        if(args.length == 0) {
            System.err.printf("usage: %s <JSON file>", Main.class.getName());
            System.err.println();
            return;
        }
        // create a CharStream that reads from standard input
        String fileName = args[0];
        CharStream cs = CharStreams.fromFileName(fileName);
        parseAndPrint(cs);
    }

    public static void parseAndPrint(CharStream cs) {
        JSONParser parser = getParser(cs); // begin parsing at init rule
        JSONParser.JsonContext parseTree = parser.json(); // begin parsing at init rule
        System.out.println(parseTree.toStringTree(parser)); // print LISP-style tree

        analyzeAndPrintResult(parseTree);
    }

    /**
     * Creates a JastAdd AST representation of the CST parseTree.
     * Runs JastAdd analysis over the AST to print the number of
     * string values in the input JSON.
     * @param parseTree
     */
    private static void analyzeAndPrintResult(JSONParser.JsonContext parseTree) {
        JsonProgram program = createAST(parseTree);
        int numStrings = program.numStringValues();
        System.out.println("num of string values in input JSON: " + numStrings);
    }

    private static JsonProgram createAST(JSONParser.JsonContext parseTree) {
        return (JsonProgram) parseTree.accept(new ASTFactory());
    }

    public static JSONParser getParser(CharStream cs)  {
        JSONLexer lexer = new JSONLexer(cs);

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        return new JSONParser(tokens);
    }
}
