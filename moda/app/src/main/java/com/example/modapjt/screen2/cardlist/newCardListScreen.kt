import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.TopBarComponent

@Composable
fun newCardListScreen(navController: NavController, currentRoute: String) {
    Scaffold(
        topBar = { HeaderBar() },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(text = "Recommend Screen Page")
        }
    }
}