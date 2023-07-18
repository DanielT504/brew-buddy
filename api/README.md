# firebase api using firebase functions

## index.js
This is the file where you put the actual endpoints that will be called by the app.

## running locally

**NOTE**: i don't know how to use two firebase apps in one app, so right now the setup between the two is kind of confusing. all the setup right now is working on a SINGLE firebase app. this will be fixed soon. at the moment only claudiatest/claudiatest and test12/test12 logins work without having to do anything extra.

### app setup

In assets/env there is a field called "DEBUG". switching it between true/false will either set it to use the local instance (which you can run below). if it is false it will access the production instances of the firebase app `brewbuddy-ece452`

You will need access to the Firebase app to run it locally. When you run it locally, the Firestore DB will be *empty*; you can try running the `clone_prod_db` script if you want to download a local version of the Firestore DB that's live (make sure you're in the `functions` folder to run it).

If you downloaded the Firestore DB and wanted to run your function against that, use the following:

```firebase emulators:start --import ./local_firestore --only functions,firebase```

If you just want to run without importing the local DB, just use

```firebase emulators:start --only functions,firebase```

If you want to run only functions locally (meaning all the authentication, database stuff is being accessed on prod), use:

```firebase emulators:start --only functions```

## deploying changes

Use the command to push functions:

```firebase deploy --only functions```

