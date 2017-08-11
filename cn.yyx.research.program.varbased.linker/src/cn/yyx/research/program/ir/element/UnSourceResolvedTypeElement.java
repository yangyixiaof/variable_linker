package cn.yyx.research.program.ir.element;

import java.io.InputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ICompletionRequestor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.IWorkingCopy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;

import cn.yyx.research.program.ir.meta.IRElementMeta;

@SuppressWarnings("deprecation")
public class UnSourceResolvedTypeElement extends VirtualDefinedElement implements IType {
	
	public UnSourceResolvedTypeElement(String represent) {
		super(represent);
	}
	
	@Override
	public String getElementName() {
		return IRElementMeta.UnresolvedTypeElement + "#" + represent;
	}
	
	@Override
	public String getHandleIdentifier() {
		return IRElementMeta.UnresolvedTypeElement + "#" + represent;
	}

	@Override
	public String[] getCategories() throws JavaModelException {
		
		return null;
	}

	@Override
	public IClassFile getClassFile() {
		
		return null;
	}

	@Override
	public ICompilationUnit getCompilationUnit() {
		
		return null;
	}

	@Override
	public IType getDeclaringType() {
		
		return null;
	}

	@Override
	public int getFlags() throws JavaModelException {
		
		return 0;
	}

	@Override
	public ISourceRange getJavadocRange() throws JavaModelException {
		
		return null;
	}

	@Override
	public int getOccurrenceCount() {
		
		return 0;
	}

	@Override
	public ITypeRoot getTypeRoot() {
		
		return null;
	}

	@Override
	public IType getType(String name, int occurrenceCount) {
		
		return null;
	}

	@Override
	public boolean isBinary() {
		
		return false;
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
	public ISourceRange getNameRange() throws JavaModelException {
		
		return null;
	}

	@Override
	public void copy(IJavaElement container, IJavaElement sibling, String rename, boolean replace,
			IProgressMonitor monitor) throws JavaModelException {
		
		
	}

	@Override
	public void delete(boolean force, IProgressMonitor monitor) throws JavaModelException {
		
		
	}

	@Override
	public void move(IJavaElement container, IJavaElement sibling, String rename, boolean replace,
			IProgressMonitor monitor) throws JavaModelException {
		
		
	}

	@Override
	public void rename(String name, boolean replace, IProgressMonitor monitor) throws JavaModelException {
		
		
	}

	@Override
	public IJavaElement[] getChildren() throws JavaModelException {
		
		return null;
	}

	@Override
	public boolean hasChildren() throws JavaModelException {
		
		return false;
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
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames,
			char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, ICompletionRequestor requestor)
			throws JavaModelException {
		
		
	}

	@Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames,
			char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, ICompletionRequestor requestor,
			WorkingCopyOwner owner) throws JavaModelException {
		
		
	}

	@Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames,
			char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, CompletionRequestor requestor)
			throws JavaModelException {
		
		
	}

	@Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames,
			char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, CompletionRequestor requestor,
			IProgressMonitor monitor) throws JavaModelException {
		
		
	}

	@Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames,
			char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, CompletionRequestor requestor,
			WorkingCopyOwner owner) throws JavaModelException {
		
		
	}

	@Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames,
			char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, CompletionRequestor requestor,
			WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaModelException {
		
		
	}

	@Override
	public IField createField(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor)
			throws JavaModelException {
		return null;
	}

	@Override
	public IInitializer createInitializer(String contents, IJavaElement sibling, IProgressMonitor monitor)
			throws JavaModelException {
		return null;
	}

	@Override
	public IMethod createMethod(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor)
			throws JavaModelException {
		return null;
	}

	@Override
	public IType createType(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor)
			throws JavaModelException {
		
		return null;
	}

	@Override
	public IMethod[] findMethods(IMethod method) {
		
		return null;
	}

	@Override
	public IJavaElement[] getChildrenForCategory(String category) throws JavaModelException {
		
		return null;
	}

	@Override
	public IField getField(String name) {
		
		return null;
	}

	@Override
	public IField[] getFields() throws JavaModelException {
		
		return null;
	}

	@Override
	public String getFullyQualifiedName() {
		
		return null;
	}

	@Override
	public String getFullyQualifiedName(char enclosingTypeSeparator) {
		
		return null;
	}

	@Override
	public String getFullyQualifiedParameterizedName() throws JavaModelException {
		
		return null;
	}

	@Override
	public IInitializer getInitializer(int occurrenceCount) {
		
		return null;
	}

	@Override
	public IInitializer[] getInitializers() throws JavaModelException {
		
		return null;
	}

	@Override
	public String getKey() {
		
		return null;
	}

	@Override
	public IMethod getMethod(String name, String[] parameterTypeSignatures) {
		
		return null;
	}

	@Override
	public IMethod[] getMethods() throws JavaModelException {
		
		return null;
	}

	@Override
	public IPackageFragment getPackageFragment() {
		
		return null;
	}

	@Override
	public String getSuperclassName() throws JavaModelException {
		
		return null;
	}

	@Override
	public String getSuperclassTypeSignature() throws JavaModelException {
		
		return null;
	}

	@Override
	public String[] getSuperInterfaceTypeSignatures() throws JavaModelException {
		
		return null;
	}

	@Override
	public String[] getSuperInterfaceNames() throws JavaModelException {
		
		return null;
	}

	@Override
	public String[] getTypeParameterSignatures() throws JavaModelException {
		
		return null;
	}

	@Override
	public ITypeParameter[] getTypeParameters() throws JavaModelException {
		
		return null;
	}

	@Override
	public IType getType(String name) {
		
		return null;
	}

	@Override
	public ITypeParameter getTypeParameter(String name) {
		
		return null;
	}

	@Override
	public String getTypeQualifiedName() {
		
		return null;
	}

	@Override
	public String getTypeQualifiedName(char enclosingTypeSeparator) {
		
		return null;
	}

	@Override
	public IType[] getTypes() throws JavaModelException {
		
		return null;
	}

	@Override
	public boolean isAnonymous() throws JavaModelException {
		
		return false;
	}

	@Override
	public boolean isClass() throws JavaModelException {
		
		return false;
	}

	@Override
	public boolean isEnum() throws JavaModelException {
		
		return false;
	}

	@Override
	public boolean isInterface() throws JavaModelException {
		
		return false;
	}

	@Override
	public boolean isAnnotation() throws JavaModelException {
		
		return false;
	}

	@Override
	public boolean isLocal() throws JavaModelException {
		
		return false;
	}

	@Override
	public boolean isMember() throws JavaModelException {
		
		return false;
	}

	@Override
	public boolean isResolved() {
		
		return false;
	}

	@Override
	public ITypeHierarchy loadTypeHierachy(InputStream input, IProgressMonitor monitor) throws JavaModelException {
		
		return null;
	}

	@Override
	public ITypeHierarchy newSupertypeHierarchy(IProgressMonitor monitor) throws JavaModelException {
		
		return null;
	}

	@Override
	public ITypeHierarchy newSupertypeHierarchy(ICompilationUnit[] workingCopies, IProgressMonitor monitor)
			throws JavaModelException {
		
		return null;
	}

	@Override
	public ITypeHierarchy newSupertypeHierarchy(IWorkingCopy[] workingCopies, IProgressMonitor monitor)
			throws JavaModelException {
		
		return null;
	}

	@Override
	public ITypeHierarchy newSupertypeHierarchy(WorkingCopyOwner owner, IProgressMonitor monitor)
			throws JavaModelException {
		
		return null;
	}

	@Override
	public ITypeHierarchy newTypeHierarchy(IJavaProject project, IProgressMonitor monitor) throws JavaModelException {
		
		return null;
	}

	@Override
	public ITypeHierarchy newTypeHierarchy(IJavaProject project, WorkingCopyOwner owner, IProgressMonitor monitor)
			throws JavaModelException {
		
		return null;
	}

	@Override
	public ITypeHierarchy newTypeHierarchy(IProgressMonitor monitor) throws JavaModelException {
		
		return null;
	}

	@Override
	public ITypeHierarchy newTypeHierarchy(ICompilationUnit[] workingCopies, IProgressMonitor monitor)
			throws JavaModelException {
		
		return null;
	}

	@Override
	public ITypeHierarchy newTypeHierarchy(IWorkingCopy[] workingCopies, IProgressMonitor monitor)
			throws JavaModelException {
		
		return null;
	}

	@Override
	public ITypeHierarchy newTypeHierarchy(WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaModelException {
		return null;
	}

	@Override
	public String[][] resolveType(String typeName) throws JavaModelException {
		return null;
	}

	@Override
	public String[][] resolveType(String typeName, WorkingCopyOwner owner) throws JavaModelException {
		return null;
	}

	@Override
	public boolean isLambda() {
		return false;
	}
	
}
