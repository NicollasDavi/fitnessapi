package com.example.fitnessapi.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fitnessapi.network.Exercise

@Composable
fun ExerciseDetails(exercise: Exercise) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(
                BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Nome do Exercício
            Text(
                text = exercise.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // Exibição de Imagem (se necessário)
            // Se você tiver uma URL para imagem ou ícone do exercício, pode colocar aqui
            // Caso contrário, podemos omitir a imagem ou exibir uma imagem genérica
            val imageUrl = "" // Substitua com a URL da imagem se necessário
            if (imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Imagem de exercício ${exercise.name}",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp)) // Borda arredondada na imagem
                )
            } else {
                Text(text = "Imagem não disponível", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Descrição do exercício
            Text(
                text = "Descrição: ${exercise.description}",
                modifier = Modifier.padding(vertical = 2.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White // Cor de texto branco para contraste
            )

            // Categoria do exercício
            Text(
                text = "Categoria: ${exercise.category}",
                modifier = Modifier.padding(vertical = 2.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Se tiver mais informações ou variáveis para mostrar, como séries, repetições, etc.
            // Isso depende dos dados que você tem sobre os exercícios
            // Exemplo:
            Text(
                text = "Dica: Faça de 3 a 5 séries com 12 repetições para melhores resultados.",
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
