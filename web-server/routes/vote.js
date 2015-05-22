
exports.vote = function(req,res){
	console.log(req.body);
	res.json(req.body);
};