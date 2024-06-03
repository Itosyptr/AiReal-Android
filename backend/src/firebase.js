const Firestore = require('@google-cloud/firestore');

const firestore = new Firestore({
  projectId: 'capstone-aireal',
  databaseId: 'aireal',
});

module.exports = { firestore };