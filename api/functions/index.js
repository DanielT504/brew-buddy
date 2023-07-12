/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

// The Cloud Functions for Firebase SDK to create Cloud Functions and triggers.
const { logger } = require("firebase-functions");
const { onRequest } = require("firebase-functions/v2/https");
const { onDocumentCreated } = require("firebase-functions/v2/firestore");

// The Firebase Admin SDK to access Firestore.
const { initializeApp } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");
const { defineSecret } = require("firebase-functions/params");
const databasePassword = defineSecret("database");
initializeApp();
const db = getFirestore();

exports.testWorld = onRequest((request, response) => {
  logger.info(request);
  logger.info("Hello logs!", { structuredData: true });

  // const secretmanagerClient = new SecretManagerServiceClient();
  // const secretRequest = { name: "database" };

  // // Run request
  // const secretResponse = secretmanagerClient.getSecret(secretRequest);
  response.send("Hi!");
});

exports.getRecipeByAuthor = onRequest(
  { secrets: [databasePassword] },
  async ({ query }, response) => {
    // const pwd = databasePassword.value();
    // const db = new DB("developer", pwd);

    // const res = await db.getRecipeById();
    // logger.info(request);
    // logger.info("Hello logs!", { structuredData: true });
    var recipes = [];
    const { authorId, recipeId } = query;
    if (!id) {
      return [];
    }
    await db
      .collection("recipes")
      .get()
      .then((snapshot) => {
        snapshot.forEach((doc) => {
          const data = {
            id: doc.id,
            author: doc.data().author,
            ingredients: doc.data().ingredients,
          };
          recipes.push(data);
        });
      });
    // console.log(recipes);
    response.send(recipes);
  }
);

exports.getRecipeById = onRequest(
  { secrets: [databasePassword] },
  async ({ query }, response) => {
    // const pwd = databasePassword.value();
    // const db = new DB("developer", pwd);

    // const res = await db.getRecipeById();
    // logger.info(request);
    // logger.info("Hello logs!", { structuredData: true });
    var recipes = [];
    const id = { query };
    if (!id) {
      return [];
    }
    await db
      .collection("recipes")
      .get()
      .then((snapshot) => {
        snapshot.forEach((doc) => {
          const data = {
            id: doc.id,
            author: doc.data().author,
            ingredients: doc.data().ingredients,
          };
          recipes.push(data);
        });
      });
    // console.log(recipes);
    response.send(recipes);
  }
);
