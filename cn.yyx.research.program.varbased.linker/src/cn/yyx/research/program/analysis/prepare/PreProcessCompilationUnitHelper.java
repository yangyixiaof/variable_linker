package cn.yyx.research.program.analysis.prepare;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

import cn.yyx.research.program.eclipse.jdtutil.JDTParser;

public class PreProcessCompilationUnitHelper {

	// public static TextEdit EntirePreProcessCompilationUnit(CompilationUnit cu,
	// JDTParser parser)
	// {
	// return EntirePreProcessCompilationUnit(cu);
	// try {
	// edits.apply(doc);
	// } catch (MalformedTreeException e) {
	// e.printStackTrace();
	// } catch (BadLocationException e) {
	// e.printStackTrace();
	// }
	// CompilationUnit modified_cu = parser.ParseJavaFile(doc);
	// return modified_cu;
	// }

	public static TextEdit PreProcessTransformer(ICompilationUnit icu) { // , IJavaProject java_project
		// CreateJDTParserWithJavaProject(java_project).
		CompilationUnit cu = JDTParser.ParseICompilationUnit(icu);
		IDocument doc = new Document(cu.toString());
		cu.recordModifications();
		final ASTRewrite rewrite = ASTRewrite.create(cu.getAST());
		// cu.accept(new ParameterizedTypeEliminator(rewrite));
		cu.accept(new AssignmentTransformer(rewrite));
		// cu.accept(new CommentRemover(rewrite));
		TextEdit edits = rewrite.rewriteAST(doc, icu.getJavaProject().getOptions(true));
		return edits;
	}
	
	public static String PreProcessDeleter(ICompilationUnit icu) { // , IJavaProject java_project
		// CreateJDTParserWithJavaProject(java_project).
		CompilationUnit cu = JDTParser.ParseICompilationUnit(icu);
		cu.recordModifications();
		@SuppressWarnings("unchecked")
		List<Comment> comments = cu.getCommentList();
		if (comments != null) {
			for (Comment comment : comments) {
				comment.accept(new CommentRemover());
			}
		}
		return cu.toString();
	}
	
}
