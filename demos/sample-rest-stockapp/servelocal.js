// Taken from: https://facebook.github.io/create-react-app/docs/deployment
const express = require('express');
const path = require('path');
const process = require('process');
const app = express();
let port = 3333

app.use(express.static(path.join(__dirname, 'build')));

app.get('/*', function(req, res) {
  res.sendFile(path.join(__dirname, 'build', 'index.html'));
});

const args = process.argv;
if (args.length > 2) {
    port = parseInt(args[2], 10);
}

console.log("Listening on: " + port);
app.listen(port);
