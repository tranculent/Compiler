// @Author: Yanko Mirov (37513192)
public class Generate extends AbstractGenerate {
    public Generate() {
        System.out.println("Creating a generate class..");
    }

    public void reportError(Token token, String explanatoryMessage) throws CompilationException {
        System.out.println(explanatoryMessage);
    }
    
}