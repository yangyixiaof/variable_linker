package cn.yyx.research.program.ir.element;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;

public class VirtualMethodReturnElement extends VirtualDefinedElement implements ILocalVariable {

	public VirtualMethodReturnElement(String represent) {
		super(represent);
	}

	@Override
	public String getSource() throws JavaModelException {
		return null;
	}

	@Override
	public ISourceRange getSourceRange() throws JavaModelException {
		return null;
	}

	@Override
	public IAnnotation getAnnotation(String name) {
		return null;
	}

	@Override
	public IAnnotation[] getAnnotations() throws JavaModelException {
		return null;
	}

	@Override
	public ISourceRange getNameRange() {
		return null;
	}

	@Override
	public String getTypeSignature() {
		return null;
	}

	@Override
	public boolean isParameter() {
		return false;
	}

	@Override
	public int getFlags() {
		return 0;
	}

	@Override
	public IMember getDeclaringMember() {
		return null;
	}

	@Override
	public ITypeRoot getTypeRoot() {
		return null;
	}
	
}
