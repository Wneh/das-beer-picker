var beerModel = require('./../models/beer');

exports.getIndex = function(req,res){
	beerModel.getAllBeer(function (err, result){
		console.log(result);
		res.render('index',{beers:result});
	});
};

exports.listBeer = function(req,res){
	beerModel.getAllBeer(function (err, result){
		res.json(result);
	});
};