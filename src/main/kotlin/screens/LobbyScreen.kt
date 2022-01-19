import factories.LoginItems
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import controller.AuctionController
import controller.LobbyController
import kotlinx.coroutines.*
import model.AuctionData
import navigation.NavController
import navigation.Screen


@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun LobbyScreen(navController: NavController, lobbyController: LobbyController) {

    val allAuctions : State<List<AuctionData>> = lobbyController.allAuctions.collectAsState()
    if(LoginItems.isLoggedIn) {
        currentAuctions(navController, allAuctions.value, fetchSpecificAuction = {AuctionController().joinAuction(it)} )
    } else {
        Column(modifier = Modifier.fillMaxHeight(1f)
            .fillMaxWidth(1f)
            .padding(40.dp),horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                Text(text = "You're Not logged In!", fontSize = 30.sp)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun currentAuctions(navController: NavController, auctionsState: List<AuctionData>, fetchSpecificAuction: (auctionURI: String) -> Unit){

    Column(
        modifier = Modifier.fillMaxHeight(1f).fillMaxWidth(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Column(modifier = Modifier.width(200.dp)) {
                Card(modifier = Modifier.padding(4.dp).height(80.dp)) {
                    Column {
                        Row {
                            Text("Hej " + LoginItems.userName + "!")
                        }
                        Row {
                            Text("Logged In: " + LoginItems.isLoggedIn)
                        }
                        Row {
                            Text("Balance: " + LoginItems.money + " $")
                        }
                    }
                }

            }
            Column(modifier = Modifier.width(600.dp)) {
                Row {
                    Card(modifier = Modifier.padding(4.dp).height(40.dp).weight(0.5F)) {
                        Text(
                            text = "Title",
                            style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        )
                    }
                    Card(modifier = Modifier.padding(4.dp).height(40.dp).weight(0.2F)) {
                        Text(
                            text = "Price",
                            style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        )
                    }
                    Card(modifier = Modifier.padding(4.dp).height(40.dp).weight(0.2F)) {
                        Text(
                            text = "Time Remaining",
                            style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        )
                    }
                }
                LazyVerticalGrid(
                    cells = GridCells.Fixed(1),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(auctionsState) { auction ->
                        Row(modifier = Modifier.clickable {
                            //println(auction.auctionURI)
                            fetchSpecificAuction(auction.auctionId.toString())
                            GlobalScope.launch(Dispatchers.IO) {
                                delay(200L)
                                navController.navigate(Screen.AuctionScreen.name)

                            }
                        }) {
                            Card(modifier = Modifier.padding(4.dp).height(40.dp).weight(0.5F)) {
                                Text(
                                    text = auction.auctionTitle,
                                )
                            }
                            Card(modifier = Modifier.padding(4.dp).height(40.dp).weight(0.2F)) {
                                Text(
                                    text = auction.auctionPrice + " $",
                                    style = TextStyle(textAlign = TextAlign.Center),
                                )
                            }
                            Card(modifier = Modifier.padding(4.dp).height(40.dp).weight(0.2F)) {
                                Text(
                                    text = auction.auctionEndTime + " min",
                                    style = TextStyle(textAlign = TextAlign.Center),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}