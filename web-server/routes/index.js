var beerModel = require('./../models/beer');

exports.getIndex = function(req,res){
	res.render('index',{});
};

exports.listBeer = function(req,res){
	beerModel.getAllBeer(function (err, result){
		res.json(result);
	});
};