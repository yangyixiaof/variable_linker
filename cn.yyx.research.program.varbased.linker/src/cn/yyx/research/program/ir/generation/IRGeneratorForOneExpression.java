package cn.yyx.research.program.ir.generation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import cn.yyx.research.program.ir.IRConstantMeta;
import cn.yyx.research.program.ir.ast.ASTSearch;
import cn.yyx.research.program.ir.element.ConstantUniqueElement;
import cn.yyx.research.program.ir.element.UnSourceResolvedTypeElement;
import cn.yyx.research.program.ir.storage.node.IRJavaElement;

public class IRGeneratorForOneExpression extends ASTVisitor {
	
	List<IRJavaElement> elements = new LinkedList<IRJavaElement>();
	
	@Override
	public boolean visit(NumberLiteral node) {
		String content = node.toString();
		IRJavaElement irje = new IRJavaElement(content, new ConstantUniqueElement(IRConstantMeta.NumberConstant + "$" + content));
		elements.add(irje);
		return super.visit(node);
	}

	@Override
	public boolean visit(NullLiteral node) {
		String content = node.toString();
		IRJavaElement irje = new IRJavaElement(content, new ConstantUniqueElement(IRConstantMeta.NullConstant + "$" + content));
		elements.add(irje);
		return super.visit(node);
	}

	@Override
	public boolean visit(CharacterLiteral node) {
		String content = node.toString();
		IRJavaElement irje = new IRJavaElement(content, new ConstantUniqueElement(IRConstantMeta.CharConstant + "$" + content));
		elements.add(irje);
		return super.visit(node);
	}

	@Override
	public boolean visit(BooleanLiteral node) {
		String content = node.toString();
		IRJavaElement irje = new IRJavaElement(content, new ConstantUniqueElement(IRConstantMeta.BooleanConstant + "$" + content));
		elements.add(irje);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(StringLiteral node) {
		String content = node.toString();
		IRJavaElement irje = new IRJavaElement(content, new ConstantUniqueElement(IRConstantMeta.StringConstant + "$" + content));
		elements.add(irje);
		return super.visit(node);
	}
	
	protected void TreatSuperClassElement(ASTNode node) {
		ASTNode temp_node = ASTSearch.FindMostCloseAbstractTypeDeclaration(node);
		if (temp_node instanceof TypeDeclaration) {
			boolean source_kind = false;
			TypeDeclaration td = (TypeDeclaration) temp_node;
			Type tp = td.getSuperclassType();
			ITypeBinding itb = tp.resolveBinding();
			IType it = null;
			if (itb != null) {
				IJavaElement ijele = itb.getJavaElement();
				if (ijele != null && ijele instanceof IType) {
					it = (IType) ijele;
					if (!it.isBinary()) {
						source_kind = true;
					}
				}
			}
			if (!source_kind) {
				if (itb == null) {
					String content = tp.toString();
					HandleIJavaElement(content, new UnSourceResolvedTypeElement(content));
				} else {
					String content = itb.getQualifiedName();
					HandleIJavaElement(content, new UnSourceResolvedTypeElement(content));
				}
			} else {
				HandleIJavaElement(it.getFullyQualifiedName(), it);
			}
		}
	}

	protected void HandleIJavaElement(String content, IJavaElement ije) {
		IRJavaElement irje = new IRJavaElement(content, ije);
		elements.add(irje);
	}
	
}
