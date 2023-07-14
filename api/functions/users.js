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
      };
    });
};

exports.getUserById = getUserById;
