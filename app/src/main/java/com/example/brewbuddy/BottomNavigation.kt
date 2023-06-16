//package com.example.brewbuddy
//
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.composable
//import androidx.compose.material3.Text
//import androidx.compose.material3.Button
//
////@Composable
////fun BottomNavigation() {
////    val navController = rememberNavController()
////    NavHost(navController = navController, startDestination = "profile") {
////        composable("profile") { Profile(/*...*/) }
////        composable("friendslist") { FriendsList(/*...*/) }
////        /*...*/
////    }
////
////}
//@Composable
//fun MyAppNavHost(
//    modifier: Modifier = Modifier,
//    navController: NavHostController = rememberNavController(),
//    startDestination: String = "profile"
//) {
//    NavHost(
//        modifier = modifier,
//        navController = navController,
//        startDestination = startDestination
//    ) {
//        composable("profile") {
//            ProfileScreen(
//                onNavigateToFriends = { navController.navigate("friendsList") },
//                /*...*/
//            )
//        }
//    }
//}
//
//@Composable
//fun ProfileScreen(
//    onNavigateToFriends: () -> Unit,
//    /*...*/
//) {
//    /*...*/
//    Button(onClick = onNavigateToFriends) {
//        Text(text = "See friends list")
//    }
//
//
// }

