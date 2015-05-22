var beerModel = require('./../models/beer');

/*
 * Renders the index page with the beer loaded
 * from the database
 */
exports.getIndex = function(req,res){
	beerModel.getAllBeer(function (err, result){
		res.render('index',{beers:result});
	});
};

/*
 * Just get all the beer from the database and
 * send it back as json. This is mainly for testing
 */
exports.listBeer = function(req,res){
	beerModel.getAllBeer(function (err, result){
		res.json(result);
	});
};