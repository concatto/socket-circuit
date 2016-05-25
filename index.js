var express = require("express");
var net = require("net");
var http = require("http");
var app = express();
var server = http.Server(app);
var io = require("socket.io")(server);

app.set("views", "./views");
app.set("view engine", "pug");
app.use(express.static("public"));
app.get("/", function(req, res) {
  res.render("index");
});

io.on("connection", function(socket) {

});

server.listen(3000);

function toString(data) {
  return '' + data;
}

var externalServer = net.createServer((client) => {
  console.log("Cliente conectado!");
  client.on("data", (data) => {
    console.log("Recebido: " + data);
    io.emit("createBlock", {color: toString(data)});
  })
});

externalServer.listen(4000, () => {
  console.log("Servidor ouvindo.");
});
