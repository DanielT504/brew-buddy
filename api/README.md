# firebase api using firebase functions

## index.js
This is the file where you put the actual endpoints that will be called by the app

## running locally
You will need access to the Firebase app to run it locally. When you run it locally, the Firestore DB will be *empty*; you can try running the `local_setup` script if you want to download a local version of the Firestore DB that's live (make sure you're in the `functions` folder to run it).

If you downloaded the Firestore DB and wanted to run your function against that, use the following:

```firebase emulators:start --import ./local_firestore```

If you just want to run without importing the local DB, just use

```firebase emulators: start```

## deploying changes

Use the command to push functions:

```firebase deploy --only functions```