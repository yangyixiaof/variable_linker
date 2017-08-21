package cn.yyx.research.program.analysis.prepare;

import java.util.List;

import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import cn.yyx.research.program.eclipse.searchutil.EclipseSearchForICompilationUnits;

public class PreProcessHelper {

	public static void EliminateAllParameterizedTypeAndReformAssignment(IJavaProject java_project)
			throws JavaModelException {
		List<ICompilationUnit> units = EclipseSearchForICompilationUnits.SearchForAllICompilationUnits(java_project);
		// System.err.println("unit_size:" + units.size());
		for (final ICompilationUnit compilation_resource : units) {
			TextEdit edit1 = PreProcessCompilationUnitHelper.PreProcessTransformer(compilation_resource, java_project);
			compilation_resource.applyTextEdit(edit1, null);
			compilation_resource.reconcile(ICompilationUnit.NO_AST, false, compilation_resource.getOwner(), null);
			compilation_resource.save(null, false);

			String changed_class = PreProcessCompilationUnitHelper.PreProcessDeleter(compilation_resource,
					java_project);
			CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(null);
			TextEdit textEdit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, changed_class, 0,
					changed_class.length(), 0, null);
			IDocument doc = new Document(changed_class);
			try {
				textEdit.apply(doc);
				changed_class = doc.get();
			} catch (MalformedTreeException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}

			IBuffer ibuf = compilation_resource.getBuffer();
			ibuf.setContents(changed_class);
			compilation_resource.reconcile(ICompilationUnit.NO_AST, false, compilation_resource.getOwner(), null);
			compilation_resource.save(null, false);

			// CompilationUnit cu =
			// if (cu == null)
			// {
			// System.err.println("ModifiedCompilationUnit is null, something must be
			// wrong!");
			// System.exit(1);
			// }
			// testing
			// System.out.println("CompilationUnit:" + cu);
			// testing
			// System.out.println("ICompilationUnit:" + compilation_resource.getSource());
		}
		java_project.save(null, false);
	}

}
