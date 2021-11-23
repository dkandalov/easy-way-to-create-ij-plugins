import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import liveplugin.registerInspection
import liveplugin.registerIntention
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.expressionVisitor

// depends-on-plugin org.jetbrains.kotlin

registerIntention(object : PsiElementBaseIntentionAction() {
    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement) =
        element.containingFile.fileType.defaultExtension == "kt" && element.text.contains("hello")

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        element.replace(KtPsiFactory(project).createLiteralStringTemplateEntry("hello world!"))
    }

    override fun startInWriteAction() = true
    override fun getText() = "Foo"
    override fun getFamilyName() = "Foo"
})

registerInspection(object : AbstractKotlinInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return expressionVisitor { expression: KtExpression ->
            if (expression is KtStringTemplateExpression && expression.text == "\"hello\"") {
                holder.registerProblem(expression, "Found \"hello\"", HelloWorldQuickFix())
            }
        }
    }
    override fun getGroupDisplayName() = "Foo"
    override fun getDisplayName() = "Fooooo"
    override fun getShortName() = "Foo"
    override fun isEnabledByDefault() = true
})

inner class HelloWorldQuickFix: LocalQuickFix {
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val stringLiteral = KtPsiFactory(descriptor.psiElement).createExpression("\"Hello World\"")
        descriptor.psiElement.replace(stringLiteral)
    }
    override fun getName() = "Foo fix"
    override fun getFamilyName() = "Foo"
}
