//setup express to handle routing and rendering
var express = require('express');
var app = express();
app.set('view engine', 'ejs');

//setup bodyparser to extra data from form posts
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true })); 

//setup static public directory (to link images from view pages)
app.use(express.static('public'))

// route to index page
app.get('/', function(req, res) {
    res.render('page/index');
});

// route to api call for checking a phone number
app.post('/api/check_phone_number', (req, res) => {
	var number = req.body.number
	if (isNaN(number)) {
		res.render('page/fail');
	} else if (number > 999999 && number < 10000000) {
		res.render('page/success');
	} else if (number > 999999999 && number < 10000000000) {
		res.render('page/success');
	} else { 
		res.render('page/fail');
	}
});

// route to tests page
app.get('/tests', function(req, res) {
	res.render('page/tests');
});

//start the web app
app.listen(8080, function () {
	console.log('App listening on port 8080!');
});

