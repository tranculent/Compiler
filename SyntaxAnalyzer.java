// @Author: Yanko Mirov (37513192)

import java.io.* ;

public class SyntaxAnalyser extends AbstractSyntaxAnalyser {
    private final String SPACES = "          ";
    String fileName;
    String spaceIndents = "";
    public SyntaxAnalyser(String fileName) throws IOException {
        this.fileName = fileName;
        lex = new LexicalAnalyser(fileName);
    }

    @Override
    public void _statementPart_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<statement part>");
        System.out.print(spaceIndents += SPACES); // 5 spaces
        acceptTerminal(Token.beginSymbol);
        
        statementList();
        acceptTerminal(Token.endSymbol);
        myGenerate.finishNonterminal("<statement part>");
    }

    @Override
    public void acceptTerminal(int symbol) throws IOException, CompilationException {
        if (nextToken.symbol == symbol) {
            myGenerate.insertTerminal(nextToken);

            nextToken = lex.getNextToken();
        }
        else {
            Token token = new Token(nextToken.symbol, Token.getName(nextToken.symbol), nextToken.lineNumber);
            myGenerate.reportError(nextToken, "Expected token " + Token.getName(symbol) + " but received " + nextToken + " in file " + fileName + " on line " + nextToken.lineNumber);
        }
    }

    private void statementList() throws IOException, CompilationException {
        System.out.print(spaceIndents);
        myGenerate.commenceNonterminal("<statement list>");
        spaceIndents += SPACES;
        statement();

        while (nextToken.symbol == Token.semicolonSymbol) {
            acceptTerminal(nextToken.symbol);
            statement();
        }

        myGenerate.finishNonterminal("<statement list>");
    }

    private void statement() throws IOException, CompilationException {
        System.out.print(spaceIndents);
        myGenerate.commenceNonterminal("<statement>");
        spaceIndents += SPACES;

        switch (nextToken.symbol) {
            case Token.identifier:
                assignmentStatement();
                break;
            case Token.ifSymbol:
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
                myGenerate.reportError(nextToken, "Expected an assignment statement, an if statement, a while statement, a procedure statement, until statement, or a for loop.");
                return;
        }
        myGenerate.finishNonterminal("<statement>");
    }

    private void assignmentStatement() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<assignment statement>");
        acceptTerminal(Token.identifier);
        
        acceptTerminal(Token.becomesSymbol);
        
        if (nextToken.symbol != Token.stringConstant)
            expression();
        else
            acceptTerminal(Token.stringConstant);
        
        myGenerate.finishNonterminal("<assignment statement>");
    }

    private void ifStatement() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<if statement>");
        acceptTerminal(Token.ifSymbol);

        condition();

        acceptTerminal(Token.thenSymbol);

        statementList();

        switch (nextToken.symbol) { 
            case Token.endSymbol:
                acceptTerminal(Token.endSymbol);
                acceptTerminal(Token.ifSymbol);
                break;
            case Token.elseSymbol:
                acceptTerminal(Token.elseSymbol);
                statementList();
                acceptTerminal(Token.endSymbol);
                acceptTerminal(Token.ifSymbol);
                break;
            default:
                myGenerate.reportError(nextToken, "Expected an end or else symbol but received " + Token.getName(nextToken.symbol));
        }
        myGenerate.finishNonterminal("<if statement>");
    }

    private void whileStatement() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<while statement>");

        acceptTerminal(Token.whileSymbol);

        condition();

        acceptTerminal(Token.loopSymbol);

        statementList();

        acceptTerminal(Token.endSymbol);

        acceptTerminal(Token.loopSymbol);
        
        myGenerate.finishNonterminal("<while statement>");
    }

    private void procedureStatement() throws IOException, CompilationException {
        System.out.print(spaceIndents);
        myGenerate.commenceNonterminal("<procedure statement>");
        spaceIndents += SPACES;

        acceptTerminal(Token.callSymbol);

        acceptTerminal(Token.identifier);
        
        acceptTerminal(Token.leftParenthesis);

        argumentList();

        acceptTerminal(Token.rightParenthesis);

        myGenerate.commenceNonterminal("<procedure statement>");
    }

    private void untilStatement() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<until statement>");

        acceptTerminal(Token.doSymbol);
        
        statementList();

        acceptTerminal(Token.untilSymbol);
        
        condition();

        myGenerate.finishNonterminal("<until statement>");
    }

    private void forStatement() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<for statement>");

        acceptTerminal(nextToken.forSymbol);

        acceptTerminal(nextToken.leftParenthesis);

        assignmentStatement();

        acceptTerminal(Token.semicolonSymbol);
        
        condition();

        acceptTerminal(Token.semicolonSymbol);
        
        assignmentStatement();

        acceptTerminal(nextToken.rightParenthesis);

        acceptTerminal(Token.doSymbol);
        
        statementList();

        acceptTerminal(Token.endSymbol);

        acceptTerminal(Token.loopSymbol);

        myGenerate.finishNonterminal("<for statement>");
    }

    private void argumentList() throws IOException, CompilationException {
        System.out.print(spaceIndents);
        myGenerate.commenceNonterminal("<argument list>");
        spaceIndents += SPACES;

        acceptTerminal(Token.identifier);
            
        while (nextToken.symbol == Token.commaSymbol) {
            acceptTerminal(Token.commaSymbol);
            acceptTerminal(Token.identifier);
        }
        System.out.print(spaceIndents);
        myGenerate.finishNonterminal("<argument list>");
    }

    private void condition() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<condition>");

        acceptTerminal(Token.identifier);

        conditionalOperator();

        switch (nextToken.symbol) {
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
                myGenerate.reportError(nextToken,"Expected an identifier, a number constant or a string constant but received " + Token.getName(nextToken.symbol));
        }

        myGenerate.finishNonterminal("<condition>");
    }

    private void conditionalOperator() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<conditional operator>");
        switch (nextToken.symbol) { 
            case Token.greaterThanSymbol:
                acceptTerminal(Token.greaterThanSymbol);
                break;
            case Token.greaterEqualSymbol:
                acceptTerminal(Token.greaterEqualSymbol);
                break;
            case Token.equalSymbol:
                acceptTerminal(Token.equalSymbol);
                break;
            case Token.notEqualSymbol:
                acceptTerminal(Token.notEqualSymbol);
                break;
            case Token.lessThanSymbol:
                acceptTerminal(Token.lessThanSymbol);
                break;
            case Token.lessEqualSymbol:
                acceptTerminal(Token.lessEqualSymbol);
                break;
            default:
                myGenerate.reportError(nextToken, "Expected a >, >=, ==, /=, <, <= symbol but received " + Token.getName(nextToken.symbol));
        }
        myGenerate.finishNonterminal("<conditional operator>");
    }

    private void expression() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<expression>");
        term();

        while (nextToken.symbol == Token.plusSymbol || nextToken.symbol == Token.minusSymbol) {
            acceptTerminal(nextToken.symbol);
            term();
        }

        myGenerate.finishNonterminal("<expression>");
    }

    private void term() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<term>");
        factor();
 
        while (nextToken.symbol == Token.timesSymbol || nextToken.symbol == Token.divideSymbol) {
            acceptTerminal(nextToken.symbol);
            factor();
        } 

        myGenerate.finishNonterminal("<term>");
     }

    private void factor() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<factor>");

        switch (nextToken.symbol) {
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
                myGenerate.reportError(nextToken, "Expected an identifier, a number constant, or an expression but received " + Token.getName(nextToken.symbol));
        }
        myGenerate.finishNonterminal("<factor>");
    }
}
