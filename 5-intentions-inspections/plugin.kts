import com.intellij.codeInsight.actions.OptimizeImportsProcessor
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiEnumConstant
import com.intellij.psi.util.parentOfType
import liveplugin.*
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.idea.intentions.ImportMemberIntention
import org.jetbrains.kotlin.nj2k.postProcessing.resolve
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtConstructor
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtEnumEntry
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.expressionVisitor
import org.jetbrains.kotlin.psi.psiUtil.referenceExpression

// depends-on-plugin org.jetbrains.kotlin

registerIntention(object : PsiElementBaseIntentionAction() {
    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement) =
        element.parentOfType<KtStringTemplateExpression>() != null && element.text.contains("OMG")

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val newText = element.text.replace("OMG", "🙀")
        val newElement = KtPsiFactory(element).createLiteralStringTemplateEntry(newText)
        element.replace(newElement)
    }

    override fun startInWriteAction() = true
    override fun getText() = "Replace with 🙀"
    override fun getFamilyName() = "Hello"
})

// -----------------------------------------

registerInspection(object : AbstractKotlinInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return expressionVisitor { expression: KtExpression ->
            if (expression is KtStringTemplateExpression && expression.text.contains("!!")) {
                holder.registerProblem(expression, "Found !!", MyQuickFix())
            }
        }
    }
    override fun getShortName() = "Usage of !!"
    override fun getDisplayName() = "Usage of !!"
    override fun getGroupDisplayName() = "Hello"
    override fun isEnabledByDefault() = true
})

inner class MyQuickFix: LocalQuickFix {
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = descriptor.psiElement
        val newText = element.text.replace("!!", "💥")
        val newElement = KtPsiFactory(element).createExpression(newText)
        element.replace(newElement)
    }
    override fun getName() = "Replace with 💥"
    override fun getFamilyName() = "Hello"
}

// -----------------------------------------

registerInspection(ImportNestedReferenceInspectionForKotlin())

class ImportNestedReferenceInspectionForKotlin : AbstractKotlinInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return expressionVisitor { expression ->
            findNestedReferenceIn(expression)?.let {
                holder.registerProblem(it, "Nested reference can be imported", ImportReferenceQuickFix())
            }
        }
    }
    override fun getDisplayName() = "Nested reference can be imported"
    override fun getShortName() = "ImportNestedReferenceInspectionForKotlin"
    override fun getGroupDisplayName() = "Live plugin"
    override fun isEnabledByDefault() = true

    private fun findNestedReferenceIn(expression: KtExpression): KtExpression? {
        if (expression !is KtDotQualifiedExpression) return null
        if (expression.parentOfType<KtImportDirective>() != null) return null

        val receiverRef = (expression.receiverExpression as? KtNameReferenceExpression) ?: return null
        val selectorRef = (expression.selectorExpression?.referenceExpression() as? KtNameReferenceExpression) ?: return null
        val left = receiverRef.resolve() ?: return null
        val right = selectorRef.resolve() ?: return null

        val isEnumRef = (left is KtClass && left.isEnum() && right is KtEnumEntry)
            || (left is PsiClass && right is PsiEnumConstant)
        val isClassRef = left is KtClass && (right is KtClass || right is KtConstructor<*>)
        val isObjectRef = left is KtClass && right is KtObjectDeclaration
        val isObjectValueRef = left is KtObjectDeclaration && right is KtProperty

        return if (isEnumRef || isClassRef || isObjectRef || isObjectValueRef) selectorRef else null
    }
}

class ImportReferenceQuickFix : LocalQuickFix {
    private val importMemberIntention = ImportMemberIntention()

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val ktExpression = descriptor.psiElement as? KtNameReferenceExpression ?: return
        val psiFile = ktExpression.containingFile
        importMemberIntention.applyTo(ktExpression, null)
        OptimizeImportsProcessor(project, psiFile).run()
    }
    override fun getName() = importMemberIntention.text
    override fun getFamilyName() = name
}