package cn.yyx.research.program.ir.util;

import org.eclipse.jdt.core.IMethod;

public class IMethodDotPermissionHelper {
	
	public static boolean GainPermissionToGenerateDot(IMethod im) {
		boolean permit = true;
		try {
			// System.out.println(im);
			// System.out.println(im.hasChildren());
//			IRForOneMethod ir = IRGeneratorForOneProject.GetInstance().FetchIMethodIR(im);
//			if (ir != null && !im.isConstructor() && !ir.IsHasElement()) {
//				permit = false;
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return permit;
	}
	
}
