var express = require('express');
var bodyParser = require('body-parser');
var app = express();
var indexRoute = require('./routes/index');
var voteRoute = require('./routes/vote');


app.engine('jade', require('jade').__express);
app.set('view engine','jade');
app.set('views', __dirname + '/views');

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: false }));
// parse application/json
app.use(bodyParser.json());

app.get('/', indexRoute.getIndex);

app.post('/vote', voteRoute.vote);

app.get('/beer', indexRoute.listBeer);

var server = app.listen(3000, function () {

	var host = server.address().address;
	var port = server.address().port;

	console.log('Example app listening at http://%s:%s', host, port);
});