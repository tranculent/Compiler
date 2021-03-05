import java.io.* ;

// @Author: Yanko Mirov (37513192)

public class SyntaxAnalyser extends AbstractSyntaxAnalyser {
    public SyntaxAnalyser(String fileName) {
        System.out.println("Analyzing syntax of " + fileName);
        LexicalAnalyser la = new LexicalAnalyser("Program Folder/" + fileName);
    }

    @Override
    public void _statementPart_() throws IOException, CompilationException {
        if (nextToken.symbol == Token.beginSymbol)
            acceptTerminal(Token.beginSymbol);
        else
            myGenerate.reportError(nextToken, "...");
        
        statementList();

        if (nextToken.symbol == Token.endSymbol)
            acceptTerminal(Token.endSymbol);
        else
            myGenerate.reportError(nextToken, "...");
            
        myGenerate.reportSucess(nextToken, "//");
    }

    @Override
    public void acceptTerminal(int symbol) throws IOException, CompilationException {
        if (nextToken.symbol == symbol) {
            System.out.println(nextToken.text);
            nextToken = lex.getNextToken();
        }
        else {
            myGenerate.reportError(Token.symbol, "Expected " + Token.getName(Token.symbol) + " but received " + nextToken.text);
        }
    }

    private void statementList() throws IOException, CompilationException { // TODO
        statement();

        while (nextToken.symbol == Token.semicolonSymbol) {
            acceptTerminal(nextToken.symbol);
            statement();
        }
        
        myGenerate.reportSucess(Token.eofSymbol, "//");
    }

    private void statement() throws IOException, CompilationException {
        switch (nextToken) {
            case Token.identifierSymbol:
                assignmentStatement();
                break;
            case Token.ifSymbol
                ifStatement();
                break;
            case Token.whileSymbol:
                whileStatement();
                break;
            case Token.callSymbol:
                procedureStatement();
                break;
            case Token.doSymbol:
                untilStatement();
                break;
            case Token.forSymbol:
                forStatement();
                break;
            default:
                myGenerate.reportError(nextToken, "...");
        }
        myGenerate.reportSucess(Token.eofSymbol, "//");
    }

    private void assignmentStatement() throws IOException, CompilationException {
        if (nextToken.symbol == Token.identifier)
            acceptTerminal(Token.identifier);
        else
            myGenerate.reportError(nextToken, "...");
        
        if (nextToken.symbol == Token.becomesSymbol)
            acceptTerminal(Token.becomesSymbol);
        else
            myGenerate.reportError(nextToken, "...");
            
        switch (nextToken) { // TODO
            case ...:
                expression();
                break;
            case Token.identifier:
                acceptTerminal(Token.stringConstant);
                break;
            default:
                myGenerate.reportError(nextToken, "...");
        }
        myGenerate.reportSucess(Token.eofSymbol, "//");
    }

    private void ifStatement() throws IOException, CompilationException {
        if (nextToken.symbol == Token.ifSymbol)
            acceptTerminal(Token.ifSymbol);
        else
            myGenerate.reportError(nextToken, "...");

        condition();

        if (nextToken.symbol == Token.thenSymbol)
            acceptTerminal(Token.thenSymbol);
        else
            myGenerate.reportError(nextToken, "...");

        statementList();

        switch (nextToken) { 
            case Token.endSymbol
                acceptTerminal(Token.endSymbol);
                if (nextToken.symbol == Token.ifSymbol)
                    acceptTerminal(Token.ifSymbol);
                else
                    myGenerate.reportError(nextToken, "...");
                break;
            case Token.elseSymbol:
                acceptTerminal(Token.elseSymbol);

                statementList();

                if (nextToken.symbol == Token.endSymbol)
                    acceptTerminal(Token.endSymbol);
                else
                    myGenerate.reportError(nextToken, "...");
                
                if (nextToken.symbol == Token.ifSymbol)
                    acceptTerminal(Token.ifSymbol);
                else
                    myGenerate.reportError(nextToken, "...");
                break;
            default:
                myGenerate.reportError(nextToken, "...");
        }
        myGenerate.reportSucess(Token.eofSymbol, "//");
    }

    private void whileStatement() throws IOException, CompilationException {
        if (nextToken.symbol == Token.whileSymbol)
            acceptTerminal(Token.whileSymbol);
        else
            myGenerate.reportError(nextToken, "...");

        condition();

        if (nextToken.symbol == Token.loopSymbol)
            acceptTerminal(Token.loopSymbol);
        else
            myGenerate.reportError(nextToken, "...");

        statementList();

        if (nextToken.symbol == Token.endSymbol)
            acceptTerminal(Token.endSymbol);
        else
            myGenerate.reportError(nextToken, "...");

        if (nextToken.symbol == Token.loopSymbol)
            acceptTerminal(Token.loopSymbol);
        else
            myGenerate.reportError(nextToken, "...");

        myGenerate.reportSucess(Token.eofSymbol, "//");
    }

    private void procedureStatement() throws IOException, CompilationException {
        if (nextToken.symbol == Token.callSymbol)
            acceptTerminal(Token.callSymbol);
        else
            myGenerate.reportError(nextToken, "...");

        if (nextToken.symbol == Token.identifier)
            acceptTerminal(Token.identifier);
        else
            myGenerate.reportError(nextToken, "...");
        
        if (nextToken.symbol == Token.leftParenthesis)
            acceptTerminal(Token.leftParenthesis);
        else
            myGenerate.reportError(nextToken, "...");

        argumentList();

        if (nextToken.symbol == Token.rightParenthesis)
            acceptTerminal(Token.rightParenthesis);
        else
            myGenerate.reportError(nextToken, "...");

        myGenerate.reportSucess(Token.eofSymbol, "//");
    }

    private void untilStatement() throws IOException, CompilationException {
        if (nextToken.symbol == Token.doSymbol) 
            acceptTerminal(Token.doSymbol);
        else
            myGenerate.reportError(nextToken, "...");
        
        statementList();

        if (nextToken.symbol == Token.untilSymbol)
            acceptTerminal(Token.untilSymbol);
        else 
            myGenerate.reportError(nextToken, "...");
        
        condition();

        myGenerate.reportSucess(Token.eofSymbol, "//");
    }

    private void forStatement() throws IOException, CompilationException {
        if (nextToken.symbol == Token.forSymbol)
            acceptTerminal(nextToken.symbol);
        else 
            myGenerate.reportError(nextToken, "...");
        
        if (nextToken.symbol == Token.leftParenthesis) {
            acceptTerminal(nextToken.symbol);

            assignmentStatement();

            if (nextToken.symbol == Token.semicolonSymbol)
                acceptTerminal(Token.semicolonSymbol);
            else
                myGenerate.reportError(nextToken, "...");
            
            condition();

            if (nextToken.symbol == Token.semicolonSymbol)
                acceptTerminal(Token.semicolonSymbol);
            else
                myGenerate.reportError(nextToken, "...");
            
            assignmentStatement();

            if (nextToken.symbol == Token.rightParenthesis)
                acceptTerminal(nextToken.symbol);
            else
                myGenerate.reportError(nextToken, "...");

            if (nextToken.symbol == Token.doSymbol)
                acceptTerminal(Token.doSymbol);
            else
                myGenerate.reportError(nextToken, "...");
            
            statementList();

            if (nextToken.symbol == Token.endSymbol)
                acceptTerminal(Token.endSymbol);
            else
                myGenerate.reportError(nextToken, "...");
            
            if (nextToken.symbol == Token.loopSymbol)
                acceptTerminal(Token.loopSymbol);
            else
                myGenerate.reportError(nextToken, "...");
        }
        else 
            myGenerate.reportError(nextToken, "...");

        myGenerate.reportSucess(Token.eofSymbol, "//");
    }

    private void argumentList() throws IOException, CompilationException {
        System.out.println("312 BEGIN Argument List");

        if (nextToken.symbol == Token.identifier)
            acceptTerminal(Token.identifier);
        else
            myGenerate.reportError(nextToken, "...");
            
        while (nextToken.symbol == Token.commaSymbol) {
            acceptTerminal(nextToken.symbol);
            if (nextToken.symbol == Token.identifier)
                acceptTerminal(Token.identifier);
            else
                myGenerate.reportError(nextToken, "...");
        }
        
        myGenerate.reportSucess(Token.eofSymbol, "//");
    }

    private void condition() throws IOException, CompilationException {
        System.out.println("312 BEGIN Condition");

        if (nextToken.symbol == Token.identifier)
            acceptTerminal(Token.identifier);
        else
            myGenerate.reportError(nextToken, "...");

        conditionalOperator();

        switch (nextToken) {
            case Token.identifier:
                acceptTerminal(Token.identifier);
                break;
            case Token.numberConstant:
                acceptTerminal(Token.numberConstant);
                break;
            case Token.stringConstant:
                acceptTerminal(Token.stringConstant);
                break;
            default:
                myGenerate.reportError(nextToken, "...");
        }

        myGenerate.reportSucess(Token.eofSymbol, "//");
    }

    private void conditionalOperator() throws IOException, CompilationException {
        System.out.println("312 BEGIN Conditional Operator");
        switch (nextToken) { 
            case Token.greaterThanSymbol:
                acceptTerminal(Token.greaterThanSymbol);
                break;
            case Token.greaterEqualSymbol:
                acceptTerminal(Token.greaterEqualSymbol);
                break;
            case Token.equalSymbol:
                acceptTerminal(Token.equalSymbol);
                break;
            case ...: // TODO
                acceptTerminal(...);
                break;
            case Token.lessThanSymbol:
                acceptTerminal(Token.lessThanSymbol);
                break;
            case Token.lessEqualSymbol:
                acceptTerminal(Token.lessEqualSymbol);
                break;
            default:
                myGenerate.reportError(nextToken, "...");
        }
        myGenerate.reportSucess(Token.eofSymbol, "//");
    }

    private void expression() throws IOException, CompilationException {
        System.out.println("312 BEGIN Expression");
        term();

        while (nextToken.symbol == Token.plusSymbol || nextToken.symbol == Token.minusSymbol) {
            acceptTerminal(nextToken.symbol);
            term();
        }

        myGenerate.reportSucess(Token, "//");
    }

    private void term() throws IOException, CompilationException {
        System.out.println("312 BEGIN Term");
        factor(); // TODO
 
        while (nextToken.symbol == Token.timesSymbol || nextToken.symbol == Token.divideSymbol) {
            acceptTerminal(nextToken.symbol);
            factor();
        } 

        myGenerate.reportSucess(Token.eofSymbol, "//");
     }

    private void factor() throws IOException, CompilationException {
        System.out.println("312 BEGIN Factor");

        switch (nextToken) {
            case Token.identifier:
                acceptTerminal(Token.identifier);
                break;
            case Token.numberConstant:
                acceptTerminal(Token.numberConstant);
                break;
            case Token.leftParenthesis:
                acceptTerminal(Token.leftParenthesis);
                expression();
                acceptTerminal(Token.rightParenthesis);
                break;
            default:
                myGenerate.reportError(nextToken, "...");
        }
        myGenerate.reportSucess(Token.eofSymbol, "//");
    }
}
