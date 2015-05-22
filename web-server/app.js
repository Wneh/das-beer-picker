var express = require('express');
var app = express();
var indexRoute = require('./routes/index');


app.engine('jade', require('jade').__express);
app.set('view engine','jade');
app.set('views', __dirname + '/views');

app.get('/', indexRoute.getIndex);

app.get('/beer', indexRoute.listBeer);

var server = app.listen(3000, function () {

	var host = server.address().address;
	var port = server.address().port;

	console.log('Example app listening at http://%s:%s', host, port);
});