const { HttpsError } = require("firebase-functions/v2/https");

const getUserById = (userId, db) => {
  if (!userId) {
    throw new HttpsError("failed-precondition", "No user ID provided");
  }
  return db
    .collection("users")
    .doc(userId)
    .get()
    .then((doc) => {
      if (!doc) {
        throw new HttpsError(
          "failed-precondition",
          `No user with ID ${userId} found`
        );
      }
      return {
        id: doc.id,
        ...doc.data(),
        // username: doc.data().username,
        // email: doc.data().email,
        // bannerUrl: doc.data().bannerUrl,
        // avatarUrl: doc.data().avatarUrl,
        // pinnedRecipeIds: doc.data().pinnedRecipeIds,
        // recipeIds: doc.data().recipeIds,
      };
    });
};

exports.getUserById = getUserById;
