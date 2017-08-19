package cn.yyx.research.program.eclipse.jdtutil;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ITrackedNodePosition;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

public class ASTRewriteHelper {
	
	public static String GetRewriteContent(final ASTNode replace_happen, ASTRewrite rewriter, final ICompilationUnit type_declare_resource, final CompilationUnit type_declare) {
		String text= replace_happen.toString();
		try {
			// final ASTRewrite rewriter= ASTRewrite.create(bodyDeclaration.getAST());
			// ModifierRewrite.create(rewriter, bodyDeclaration).setVisibility(Modifier.PROTECTED, null);
			final ITrackedNodePosition position= rewriter.track(replace_happen);
			final IDocument document= new Document(type_declare_resource.getBuffer().getText(type_declare.getStartPosition(), type_declare.getLength()));
			rewriter.rewriteAST(document, type_declare_resource.getJavaProject().getOptions(true)).apply(document, TextEdit.UPDATE_REGIONS);
			text= document.get(position.getStartPosition(), position.getLength());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return text;
	}
	
}
