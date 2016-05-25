package univali.lia.sc.lang;

import java.util.function.BiConsumer;

public class Semantico implements Constants
{	
	private BiConsumer<Integer, Token> actionConsumer;
	
    public void executeAction(int action, Token token)	throws SemanticError
    {
    	System.out.println("Ação #"+action+", Token: "+token);
        if (actionConsumer != null) {
        	actionConsumer.accept(action, token);
        }
    }
    
    public void setActionConsumer(BiConsumer<Integer, Token> actionConsumer) {
		this.actionConsumer = actionConsumer;
	}
}
