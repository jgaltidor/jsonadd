package jga.jsonadd;

import jga.jsonadd.antlr.JSONBaseVisitor;
import jga.jsonadd.antlr.JSONParser.ArrayContext;
import jga.jsonadd.antlr.JSONParser.JsonContext;
import jga.jsonadd.antlr.JSONParser.ObjectContext;
import jga.jsonadd.antlr.JSONParser.PairContext;
import jga.jsonadd.antlr.JSONParser.ValueContext;
import jga.jsonadd.jastadd.*;

public class ASTFactory extends JSONBaseVisitor<ASTNode<ASTNode>>
{
	@Override
	public JsonProgram visitJson(JsonContext ctx) {
		this.visitChildren(ctx);
		// This super call will cause the correct
		// visit method to be called on the right
		// alternative.
		Json json = (Json) super.visitJson(ctx);
		JsonProgram p = new JsonProgram();
		p.setJson(json);
		return p;
	}

	@Override
	public JArray visitArray(ArrayContext ctx) {
		JArray arr = new JArray();
		if(ctx.value() != null) {
			for(ValueContext vctx : ctx.value()) {
				Value v = (Value) vctx.accept(this);
				arr.addValue(v);
			}	
		}
		return arr;
	}

	@Override
	public Pair visitPair(PairContext ctx) {
		String name = ctx.STRING().getText();
		Value value = (Value) ctx.value().accept(this);
		Pair p = new Pair();
		p.setName(name);
		p.setValue(value);
		return p;
	}

	@Override
	public JObject visitObject(ObjectContext ctx) {
		JObject obj = new JObject();
		if(ctx.pair() != null) {
			for(PairContext pctx : ctx.pair()) {
				Pair p = (Pair) pctx.accept(this);
				obj.addPair(p);
			}	
		}
		return obj;
	}
	
	@Override
	public Value visitValue(ValueContext ctx) {
		if(ctx.STRING() != null) {
			JString str = new JString();
			str.setVal(ctx.STRING().getText());
			return str;
		}
		else if(ctx.NUMBER() != null) {
			JNumber num = new JNumber();
			num.setVal(
			  Integer.parseInt(ctx.NUMBER().getText()));
			return num;
		}
		else if(ctx.getText().equals("true")) {
			JBoolean bval = new JBoolean();
			bval.setVal(true);
			return bval;
		}
		else if(ctx.getText().equals("false")) {
			JBoolean bval = new JBoolean();
			bval.setVal(true);
			return bval;
		}
		else if(ctx.getText().equals("null")) {
			return new JNull();
		}
		else return (Value) super.visitValue(ctx);
	}
}

