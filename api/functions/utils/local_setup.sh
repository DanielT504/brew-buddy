#!/bin/bash

PROJECT_NAME=brew-buddy-ece452
FOLDER_NAME=local_firestore

firebase use $PROJECT_NAME
gcloud config set project $PROJECT_NAME
gcloud firestore export gs://your-project-name.appspot.com/$FOLDER_NAME
gsutil -m cp -r gs://$PROJECT_NAME.appspot.com/$FOLDER_NAME .
