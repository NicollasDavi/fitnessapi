// ShareExerciseUtils.kt
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.fitnessapi.Database.ExerciseEntity
import android.net.Uri

// Função de compartilhamento genérico (incluindo E-mail)
fun shareExercise(context: Context, exercise: ExerciseEntity) {
    val textToShare = "Confira este exercício: ${exercise.name}"

    // Criando a Intent para compartilhamento genérico
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, textToShare) // O texto que será compartilhado
        type = "text/plain" // Tipo de conteúdo
    }

    // Tentando abrir o seletor de aplicativos para escolher o método de compartilhamento
    val chooserIntent = Intent.createChooser(shareIntent, "Compartilhar via")

    // Verifica se há aplicativos que possam lidar com o compartilhamento
    if (chooserIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(chooserIntent) // Abre o seletor de aplicativos
    } else {
        // Caso não haja nenhum aplicativo para compartilhar, mostra uma mensagem de erro
        Toast.makeText(context, "Nenhum aplicativo encontrado para compartilhar.", Toast.LENGTH_SHORT).show()
    }
}