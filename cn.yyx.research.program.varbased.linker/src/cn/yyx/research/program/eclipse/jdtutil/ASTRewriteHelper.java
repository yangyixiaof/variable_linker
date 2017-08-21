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
			ASTNode parent = replace_happen.getParent();
			final ITrackedNodePosition position_parent = rewriter.track(parent);
			final ITrackedNodePosition position = rewriter.track(replace_happen);
			final IDocument document= new Document(type_declare_resource.getBuffer().getText(type_declare.getStartPosition(), type_declare.getLength()));
			// int track_parent_start_before = position_parent.getStartPosition();
			int track_parent_length_before = position_parent.getLength();
			int position_length_before = position.getLength();
			rewriter.rewriteAST(document, type_declare_resource.getJavaProject().getOptions(true)).apply(document, TextEdit.UPDATE_REGIONS);
			int position_length_after = position.getLength();
			int origin_gap = position_length_after - position_length_before;
			// int track_parent_start_after = position_parent.getStartPosition();
 			int track_parent_length_after = position_parent.getLength();
			int replaced_gap = track_parent_length_after - track_parent_length_before;
			int track_start = position.getStartPosition();
			int track_length = position.getLength();
			text= document.get(track_start, track_length + replaced_gap - origin_gap);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return text;
	}
	
}
