package cn.yyx.research.program.ir.bind;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public class BindingManager {
	
	public static boolean SourceResolvedBinding(IBinding ib)
	{
		boolean is_source_Resolved = false;
		if (ib != null && (ib instanceof ITypeBinding || ib instanceof IVariableBinding || ib instanceof IMethodBinding))
		{
			IJavaElement ije = ib.getJavaElement();
			if (ije != null) {
				if (ije instanceof IMethod) {
					IMethod im = (IMethod)ije;
					is_source_Resolved = IMemberIsSource(im, im.getDeclaringType());
//					if (!im.getDeclaringType().isBinary()) {
//						is_source_Resolved = true;
//					}
				}
				if (ije instanceof ILocalVariable) {
					ILocalVariable ilv = (ILocalVariable)ije;
					is_source_Resolved = IMemberIsSource(ilv, ilv.getDeclaringMember());
//					if (!ilv.getDeclaringMember().isBinary()) {
//						is_source_Resolved = true;
//					}
				}
				if (ije instanceof IField) {
					IField ifd = (IField)ije;
					is_source_Resolved = IMemberIsSource(ifd, ifd.getDeclaringType());
//					if (!ifd.getDeclaringType().isBinary()) {
//						is_source_Resolved = true;
//					}
				}
				if (ije instanceof IType) {
					IType it = (IType)ije;
					is_source_Resolved = !it.isBinary();
//					IMemberIsSource(it, it.getDeclaringType());
//					if (!it.getDeclaringType().isBinary()) {
//						is_source_Resolved = true;
//					}
				}
			}
		}
		return is_source_Resolved;
	}
	
	private static boolean IMemberIsSource(IJavaElement ije, IMember imember) {
		if (imember != null) {
			if (!imember.isBinary()) {
				return true;
			}
		} else {
			// testing. strange print,
			// System.err.println("Declare member null:" + ije);
		}
		return false;
	}
	
}
