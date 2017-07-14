package cn.yyx.research.program.ir.bind;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public class BindingManager {
	
	public static boolean QualifiedBinding(IBinding ib)
	{
		if (ib != null && (ib instanceof ITypeBinding || ib instanceof IVariableBinding || ib instanceof IMethodBinding))
		{
			IJavaElement ije = ib.getJavaElement();
			if (ije != null) {
				return true;
			}
		}
		return false;
	}
	
}
